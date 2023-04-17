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
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.UdharCustomerModel;

public class UdharCustomerDetailAdapter extends RecyclerView.Adapter<UdharCustomerDetailAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<UdharCustomerModel> udharCustomerModels;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static UdharCustomerDetailAdapter.ClickListener clickListener;
    String imageUrl;
    DeliveryDBManager deliveryDBManager;
    ArrayList<String> deliveryValue;
    String customerName;

    public UdharCustomerDetailAdapter(){

    }


    public UdharCustomerDetailAdapter(Context context, ArrayList<UdharCustomerModel> udharCustomerModels) {
        this.mContext = context;
        this.udharCustomerModels = udharCustomerModels;

    }

    @NonNull
    @Override
    public UdharCustomerDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.udhar_customer_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final UdharCustomerDetailAdapter.MyViewHolder holder, final int position) {

        final UdharCustomerModel udharCustomerModel = udharCustomerModels.get(position);
        pos = position;
        viewholder = holder;

        holder.date.setText(udharCustomerModel.getCreated_at());
        holder.name.setText(udharCustomerModel.getCustomer_name());

        int pendingAmount = Integer.parseInt(udharCustomerModel.getPending_amount()) - Integer.parseInt(udharCustomerModel.getTotalPaidAmount());

        holder.pending_amount.setText("Pending amt: "+mContext.getString(R.string.Rs)+pendingAmount);
        holder.paid_amount.setText("Paid amt: "+mContext.getString(R.string.Rs)+udharCustomerModel.getPaid_amount());
        holder.summary.setText(udharCustomerModel.getSummary().equalsIgnoreCase("null") ? "" : udharCustomerModel.getSummary());

        if(!udharCustomerModel.getOrder_increment_id().equalsIgnoreCase("null")){
            holder.increment_id.setText("#"+udharCustomerModel.getOrder_increment_id());
        }
    }
    @Override
    public int getItemCount() {
        return udharCustomerModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView name;
        TextView increment_id;
        TextView pending_amount;
        TextView paid_amount;
        TextView summary;

        public MyViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.udhar_date);
            name = itemView.findViewById(R.id.udhar_name);
            increment_id = itemView.findViewById(R.id.udhar_incrementid);
            pending_amount = itemView.findViewById(R.id.udhar_pending_amount);
            paid_amount = itemView.findViewById(R.id.udhar_paid_amount);
            summary = itemView.findViewById(R.id.udhar_summary);

        }
    }
    public UdharCustomerModel getWordAtPosition(int position) {
        return udharCustomerModels.get(position);
    }

    public void setOnItemClickListener(UdharCustomerDetailAdapter.ClickListener clickListener) {
        UdharCustomerDetailAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
