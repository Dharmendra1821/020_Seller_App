package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModelNew;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Categories;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SellerCustomerNewModel;

public class MySellerCustomerNewAdapter extends RecyclerView.Adapter<MySellerCustomerNewAdapter.MyViewHolder> implements Filterable {

    Context mContext;
    ArrayList<SellerCustomerNewModel> sellerCustomerNewModels;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static MySellerCustomerNewAdapter.ClickListener clickListener;
    String imageUrl;
    private ArrayList<SellerCustomerNewModel> catList;

    public MySellerCustomerNewAdapter(){

    }


    public MySellerCustomerNewAdapter(Context context, ArrayList<SellerCustomerNewModel> sellerCustomerNewModels) {
        this.mContext = context;
        this.sellerCustomerNewModels = sellerCustomerNewModels;
        this.flag = flag;
        this.catList = sellerCustomerNewModels;
    }

    @NonNull
    @Override
    public MySellerCustomerNewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_customer_new_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MySellerCustomerNewAdapter.MyViewHolder holder, final int position) {

        final SellerCustomerNewModel sellerCustomerNewModel = sellerCustomerNewModels.get(position);
        pos = position;
        viewholder = holder;

        holder.name.setText(sellerCustomerNewModel.getCustomerName());
        holder.email.setText(sellerCustomerNewModel.getCustomerEmail());
        holder.mobile.setText(sellerCustomerNewModel.getCustomerMobile());
        holder.status.setText(sellerCustomerNewModel.getStatus());
        holder.addon.setText(sellerCustomerNewModel.getAddOnDate());



//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickListener.onItemClick(view, position);
//            }
//        });


    }
    @Override
    public int getItemCount() {
        return sellerCustomerNewModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    sellerCustomerNewModels = catList;
                } else {
                    ArrayList<SellerCustomerNewModel> filteredList = new ArrayList<>();
                    for (SellerCustomerNewModel row : catList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    sellerCustomerNewModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = sellerCustomerNewModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                sellerCustomerNewModels = (ArrayList<SellerCustomerNewModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        TextView mobile;
        TextView status;
        TextView addon;

        public MyViewHolder(View itemView) {
            super(itemView);
            status =  itemView.findViewById(R.id.seller_cust_status);
            name =  itemView.findViewById(R.id.seller_cust_name);
            email  =  itemView.findViewById(R.id.seller_cust_email);
            mobile  =  itemView.findViewById(R.id.seller_cust_mobile);
            addon = itemView.findViewById(R.id.seller_cust_addon);
        }
    }
    public SellerCustomerNewModel getWordAtPosition(int position) {
        return sellerCustomerNewModels.get(position);
    }

    public void setOnItemClickListener(MySellerCustomerNewAdapter.ClickListener clickListener) {
        MySellerCustomerNewAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
