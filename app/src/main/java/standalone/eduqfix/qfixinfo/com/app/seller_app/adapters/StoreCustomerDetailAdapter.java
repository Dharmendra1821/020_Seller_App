package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.database.DeliveryDBManager;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.DetailCollection;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCreditDetailModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCustomerListModel;

public class StoreCustomerDetailAdapter extends RecyclerView.Adapter<StoreCustomerDetailAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<DetailCollection> storeCreditDetailModels;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static StoreCustomerDetailAdapter.ClickListener clickListener;
    String imageUrl;
    DeliveryDBManager deliveryDBManager;
    ArrayList<String> deliveryValue;
    String customerName;

    public StoreCustomerDetailAdapter(){

    }


    public StoreCustomerDetailAdapter(Context context, ArrayList<DetailCollection> storeCreditDetailModels,String customerName) {
        this.mContext = context;
        this.storeCreditDetailModels = storeCreditDetailModels;
        this.customerName = customerName;

    }

    @NonNull
    @Override
    public StoreCustomerDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_customer_detail_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final StoreCustomerDetailAdapter.MyViewHolder holder, final int position) {

        final DetailCollection storeCreditDetailModel = storeCreditDetailModels.get(position);
        pos = position;
        viewholder = holder;

        holder.name.setText(customerName);
        holder.action.setText(storeCreditDetailModel.getAction_data().replaceAll("[^a-zA-Z0-9]",""));
        holder.date.setText(storeCreditDetailModel.getCreated_at());
        holder.amount.setText(mContext.getString(R.string.Rs)+" "+storeCreditDetailModel.getDifference());
        if(storeCreditDetailModel.getIs_deduct().equalsIgnoreCase("1")){
           holder.amount.setTextColor(Color.parseColor("#FF0000"));
        }
        else {
            holder.amount.setTextColor(Color.parseColor("#3bce33"));
        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickListener.onItemClick(view, position);
//            }
//        });


    }
    @Override
    public int getItemCount() {
        return storeCreditDetailModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView action;
        TextView name;
        TextView date;
        TextView amount;


        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.store_credit_customer);
            action = itemView.findViewById(R.id.store_credit_refunded);
            date = itemView.findViewById(R.id.store_credit_date);
            amount = itemView.findViewById(R.id.store_credit_amount);


        }
    }
    public DetailCollection getWordAtPosition(int position) {
        return storeCreditDetailModels.get(position);
    }

    public void setOnItemClickListener(StoreCustomerDetailAdapter.ClickListener clickListener) {
        StoreCustomerDetailAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
