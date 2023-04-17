package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.AddNewProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.OutofStcokModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

public class OutofStockAdapter extends RecyclerView.Adapter<OutofStockAdapter.MyViewHolder> implements Filterable {

    Context mContext;
    List<OutofStcokModel> outofStcokModels;
    OutofStockAdapter.MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    String quantity;


    private static OutofStockAdapter.ClickListener clickListener;
    public static ArrayList<String> outOfStockArray = new ArrayList<>();
    public static java.util.HashMap<Integer,String> OutofStockHashMap=new HashMap<Integer,String>();

    public static ArrayList<String> getOutOfStockArray() {
        return outOfStockArray;
    }

    public static void setOutOfStockArray(ArrayList<String> outOfStockArray) {
        OutofStockAdapter.outOfStockArray = outOfStockArray;
    }

    private List<OutofStcokModel> catList;

    public OutofStockAdapter() {

    }

    public OutofStockAdapter(Context context, List<OutofStcokModel> outofStcokModels,String quantity) {
        this.mContext = context;
        this.outofStcokModels = outofStcokModels;
        this.quantity = quantity;
        this.catList = outofStcokModels;
        this.flag = flag;
    }

    @NonNull
    @Override
    public OutofStockAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.out_of_stock_list, parent, false);
        OutofStockAdapter.MyViewHolder viewHolder = new OutofStockAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OutofStockAdapter.MyViewHolder holder, final int position) {

        final OutofStcokModel outofStcokModel = outofStcokModels.get(position);
        pos = position;
        viewholder = holder;


        sharedPreferences = mContext.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);

        String imageUrl = outofStcokModel.getThumbnail();
        Glide.with(mContext).load(imageUrl).error(R.drawable.download).into(holder.imageView);

        holder.outofstockcheckbox.setChecked(outofStcokModels.get(position).isSelected());
        holder.outofstockcheckbox.setTag(position);
        holder.productname.setText(outofStcokModel.getProductName());
        holder.quantity.setText("100");
        holder.sku.setText(outofStcokModel.getSku());
        try {
            if (outofStcokModel.getPrice().equalsIgnoreCase("null")) {
                holder.price.setText("0");
            } else if (outofStcokModel.getPrice().equalsIgnoreCase("0")) {
                holder.price.setText("0");
            } else {
                String productPrice = String.valueOf(outofStcokModel.getPrice()) ;
                final double price = productPrice != null ?  Math.round(Double.parseDouble(productPrice) * 100)/100.00:0.0;
                holder.price.setText(String.valueOf(price));
            }


            }catch (Exception e){

            }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });

        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value=s.toString();
                if(isNumeric(value)){
                    if(Integer.parseInt(value)>100){
                        holder.quantity.setText(String.valueOf(100));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        holder.outofstockcheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quantity = holder.quantity.getText().toString();
                Integer pos = (Integer) holder.outofstockcheckbox.getTag();
                if (outofStcokModels.get(pos).isSelected()) {
                    outofStcokModels.get(pos).setSelected(false);
                    OutofStockHashMap.remove(position);
                } else {
                    outofStcokModels.get(pos).setSelected(true);
                    OutofStockHashMap.put(position,outofStcokModels.get(position).getSku()+":"+quantity);
                }
            }
        });


    }
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(.\\d+)?");
    }

    @Override
    public int getItemCount() {
        return outofStcokModels.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView productname;
        TextView sku;
        TextView price;
        CheckBox outofstockcheckbox;
        EditText quantity;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.outofstockproductimage);
            productname =  itemView.findViewById(R.id.outofstockproductname);
            sku  =  itemView.findViewById(R.id.outofstockproductsku);
            price  =  itemView.findViewById(R.id.outofstockproductprice);
            outofstockcheckbox = itemView.findViewById(R.id.outofstockcheckbox);
            quantity = itemView.findViewById(R.id.out_of_stock_quantity);
        }
    }
    public OutofStcokModel getWordAtPosition(int position) {
        return outofStcokModels.get(position);
    }

    public void setOnItemClickListener(OutofStockAdapter.ClickListener clickListener) {
        OutofStockAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    outofStcokModels = catList;
                } else {
                    List<OutofStcokModel> filteredList = new ArrayList<>();
                    for (OutofStcokModel row : catList) {
                        if (row.getProductName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                        if (row.getSku().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    outofStcokModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = outofStcokModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                outofStcokModels = (ArrayList<OutofStcokModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
