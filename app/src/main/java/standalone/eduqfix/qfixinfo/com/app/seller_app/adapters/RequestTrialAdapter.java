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
import standalone.eduqfix.qfixinfo.com.app.database.DeliveryDBManager;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.RequestTrialModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCustomerListModel;

public class RequestTrialAdapter extends RecyclerView.Adapter<RequestTrialAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<RequestTrialModel> requestTrialModels;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static RequestTrialAdapter.ClickListener clickListener;
    String imageUrl;
    DeliveryDBManager deliveryDBManager;
    ArrayList<String> deliveryValue;


    public RequestTrialAdapter(){

    }


    public RequestTrialAdapter(Context context, ArrayList<RequestTrialModel> requestTrialModels) {
        this.mContext = context;
        this.requestTrialModels = requestTrialModels;
        this.flag = flag;
    }

    @NonNull
    @Override
    public RequestTrialAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_trial_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final RequestTrialAdapter.MyViewHolder holder, final int position) {

        final RequestTrialModel requestTrialModel = requestTrialModels.get(position);
        pos = position;
        viewholder = holder;

        holder.id.setText("Product Id: "+requestTrialModel.getProduct_id());
        holder.product_name.setText(requestTrialModel.getProduct_name());
        holder.customer_name.setText(requestTrialModel.getCustomer_name());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });


    }
    @Override
    public int getItemCount() {
        return requestTrialModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView product_name;
        TextView customer_name;


        public MyViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.rt_productid);
            product_name = itemView.findViewById(R.id.rt_productname);
            customer_name = itemView.findViewById(R.id.rt_customername);


        }
    }
    public RequestTrialModel getWordAtPosition(int position) {
        return requestTrialModels.get(position);
    }

    public void setOnItemClickListener(RequestTrialAdapter.ClickListener clickListener) {
        RequestTrialAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
