package standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductModel;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModel;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModelNew;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

public class MyOrderProductListAdapter extends RecyclerView.Adapter<MyOrderProductListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<MyOrderListModelNew> addProductListingModels;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static MyOrderProductListAdapter.ClickListener clickListener;
    String imageUrl;

    public MyOrderProductListAdapter(){

    }


    public MyOrderProductListAdapter(Context context, ArrayList<MyOrderListModelNew> addProductListingModels) {
        this.mContext = context;
        this.addProductListingModels = addProductListingModels;
        this.flag = flag;
    }

    @NonNull
    @Override
    public MyOrderProductListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyOrderProductListAdapter.MyViewHolder holder, final int position) {

        final MyOrderListModelNew addProductListingModel = addProductListingModels.get(position);
        pos = position;
        viewholder = holder;


        holder.status.setText(addProductListingModel.getStatus());
        if(addProductListingModel.getPaymentModeStatus()!=null){
            holder.payment_status.setText(addProductListingModel.getPaymentModeStatus());
        }
        else {
            holder.payment_status.setText("N/A");
        }
        String priceVal = String.format("%.2f", Double.valueOf(addProductListingModel.getGrandTotal()));
        holder.price.setText(priceVal);
        if(addProductListingModel.getCustomer_firstname() == null){
            holder.cust_name.setText("");
        }
        else {
            holder.cust_name.setText(addProductListingModel.getCustomer_firstname()+" "+addProductListingModel.getCustomer_lastname());

        }
        holder.date.setText(addProductListingModel.getCreated_at());
        if(addProductListingModel.getIncrement_id().equalsIgnoreCase("null")){
            holder.order_id.setText("");
        }
        else {
            holder.order_id.setText(addProductListingModel.getIncrement_id());
        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });


    }
    @Override
    public int getItemCount() {
        return addProductListingModels.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView status;
        TextView payment_status;
        TextView price;
        TextView date;
        TextView cust_name;
        TextView order_id;

        public MyViewHolder(View itemView) {
            super(itemView);
            status =  itemView.findViewById(R.id.my_order_list_status);
            payment_status =  itemView.findViewById(R.id.my_order_list_paymentstatus);
            date  =  itemView.findViewById(R.id.my_order_list_date);
            price  =  itemView.findViewById(R.id.my_order_list_price);
            cust_name = itemView.findViewById(R.id.my_order_list_name);
            order_id = itemView.findViewById(R.id.my_order_list_orderid);
        }
    }
    public MyOrderListModelNew getWordAtPosition(int position) {
        return addProductListingModels.get(position);
    }

    public void setOnItemClickListener(MyOrderProductListAdapter.ClickListener clickListener) {
        MyOrderProductListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
