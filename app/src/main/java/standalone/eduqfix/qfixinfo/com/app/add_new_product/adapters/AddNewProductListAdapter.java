package standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductModel;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.AddProductListingModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Product;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

public class AddNewProductListAdapter extends RecyclerView.Adapter<AddNewProductListAdapter.MyViewHolder> implements Filterable {

    Context mContext;
    List<AddNewProductModel> addProductListingModels;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static AddNewProductListAdapter.ClickListener clickListener;
    String imageUrl;

    public static ArrayList<String> getProductIds() {
        return productIds;
    }

    public static void setProductIds(ArrayList<String> productIds) {
        AddNewProductListAdapter.productIds = productIds;
    }

    private List<AddNewProductModel> catList;
    public static ArrayList<String> productIds = new ArrayList<>();


    public AddNewProductListAdapter(){
    }


    public AddNewProductListAdapter(Context context, List<AddNewProductModel> addProductListingModels) {
        this.mContext = context;
        this.addProductListingModels = addProductListingModels;
        this.catList = addProductListingModels;
        this.flag = flag;
    }

    @NonNull
    @Override
    public AddNewProductListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_product_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final AddNewProductListAdapter.MyViewHolder holder, final int position) {

        final AddNewProductModel addProductListingModel = addProductListingModels.get(position);
        pos = position;
        viewholder = holder;

        sharedPreferences = mContext.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);

        holder.add_image.setVisibility(View.GONE);
        imageUrl = addProductListingModel.getThumbnail();
        Glide.with(mContext).load(imageUrl).error(R.drawable.download).into(holder.imageView);


        holder.productname.setText(addProductListingModel.getProductName());
        holder.sku.setText(addProductListingModel.getSku());
        try {
            if (addProductListingModel.getProductPrice().equalsIgnoreCase("null")) {
                holder.price.setText("0");
            } else if (addProductListingModel.getProductPrice().equalsIgnoreCase("0")) {
                holder.price.setText("0");
            } else {
//                StringTokenizer tokens = new StringTokenizer(addProductListingModel.getProductPrice(), ".");
//                String first = tokens.nextToken();// this will contain "Fruit"
//                String second = tokens.nextToken();
//                Log.d("....", first);
//                Log.d("....", second);
                holder.price.setText(addProductListingModel.getProductPrice());
            }
        }catch (Exception e){

        }

        holder.add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });

//
        holder.product_visible.setChecked(addProductListingModels.get(position).isSelected());
        holder.product_visible.setTag(position);

        holder.product_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) holder.product_visible.getTag();
                if (addProductListingModels.get(pos).isSelected()) {
                    addProductListingModels.get(pos).setSelected(false);
                    productIds.remove(addProductListingModel.getProductId());
                } else {
                    addProductListingModels.get(pos).setSelected(true);
                    productIds.add(addProductListingModel.getProductId());
                }
            }
        });

        if(addProductListingModel.getVisibilityStatus() == 1){
            holder.visible_txt.setText("Approved");
            holder.visible_txt.setTextColor(Color.parseColor("#008000"));
        }
        if(addProductListingModel.getVisibilityStatus() == 2){
            holder.visible_txt.setText("Disapproved");
            holder.visible_txt.setTextColor(Color.parseColor("#d70505"));
        }

    }
    @Override
    public int getItemCount() {
        return addProductListingModels.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView productname;
        TextView sku;
        TextView price;
        Button add_image;
        CheckBox product_visible;
        TextView visible_txt;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.add_productImage);
            productname =  itemView.findViewById(R.id.add_product_productName);
            sku  =  itemView.findViewById(R.id.add_product_skuname);
            price  =  itemView.findViewById(R.id.add_product_rupees);
            add_image = itemView.findViewById(R.id.add_image_btn);
            product_visible = itemView.findViewById(R.id.product_visible);
            visible_txt = itemView.findViewById(R.id.visible_txt);
        }
    }
    public AddNewProductModel getWordAtPosition(int position) {
        return addProductListingModels.get(position);
    }

    public void setOnItemClickListener(AddNewProductListAdapter.ClickListener clickListener) {
        AddNewProductListAdapter.clickListener = clickListener;
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
                    addProductListingModels = catList;
                } else {
                    List<AddNewProductModel> filteredList = new ArrayList<>();
                    for (AddNewProductModel row : catList) {
                        if (row.getProductName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                        if (row.getSku().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    addProductListingModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = addProductListingModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                addProductListingModels = (ArrayList<AddNewProductModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
