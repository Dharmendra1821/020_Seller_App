package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyList;
import standalone.eduqfix.qfixinfo.com.app.database.DeliveryDBManager;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCustomerListModel;

public class StoreCustomerAdapter extends RecyclerView.Adapter<StoreCustomerAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<StoreCustomerListModel> storeCustomerListModels;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static StoreCustomerAdapter.ClickListener clickListener;
    String imageUrl;
    DeliveryDBManager deliveryDBManager;
    ArrayList<String> deliveryValue;


    public StoreCustomerAdapter(){

    }


    public StoreCustomerAdapter(Context context, ArrayList<StoreCustomerListModel> storeCustomerListModels) {
        this.mContext = context;
        this.storeCustomerListModels = storeCustomerListModels;
        this.flag = flag;
    }

    @NonNull
    @Override
    public StoreCustomerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_customer_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final StoreCustomerAdapter.MyViewHolder holder, final int position) {

        final StoreCustomerListModel storeCustomerListModel = storeCustomerListModels.get(position);
        pos = position;
        viewholder = holder;

        holder.name.setText(storeCustomerListModel.getCustomerName());
        holder.balance.setText(storeCustomerListModel.getBalance());
        holder.mobile.setText(storeCustomerListModel.getCustomer_mobile());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });


    }
    @Override
    public int getItemCount() {
        return storeCustomerListModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView balance;
        TextView mobile;


        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.store_credit_name);
            balance = itemView.findViewById(R.id.store_credit_balance);
            mobile = itemView.findViewById(R.id.store_credit_mobile);


        }
    }
    public StoreCustomerListModel getWordAtPosition(int position) {
        return storeCustomerListModels.get(position);
    }

    public void setOnItemClickListener(StoreCustomerAdapter.ClickListener clickListener) {
        StoreCustomerAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
