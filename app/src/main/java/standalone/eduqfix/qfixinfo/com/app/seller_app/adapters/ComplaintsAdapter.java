package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

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
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.MyOrderProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModelNew;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ComplaintsModel;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ComplaintsModel> complaintsModels;
    ComplaintsAdapter.MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static ComplaintsAdapter.ClickListener clickListener;
    String imageUrl;

    public ComplaintsAdapter() {

    }


    public ComplaintsAdapter(Context context, ArrayList<ComplaintsModel> complaintsModels) {
        this.mContext = context;
        this.complaintsModels = complaintsModels;
        this.flag = flag;
    }

    @NonNull
    @Override
    public ComplaintsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaints_list, parent, false);
        ComplaintsAdapter.MyViewHolder viewHolder = new ComplaintsAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ComplaintsAdapter.MyViewHolder holder, final int position) {

        final ComplaintsModel complaintsModel = complaintsModels.get(position);
        pos = position;
        viewholder = holder;


        holder.status.setText(complaintsModel.getRequest_status());

        if (complaintsModel.getCustomer_name().equalsIgnoreCase("null")) {
            holder.cust_name.setText("");
        } else {
            holder.cust_name.setText(complaintsModel.getCustomer_name());

        }

        holder.order_id.setText(complaintsModel.getOrderid());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return complaintsModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView status;
        TextView cust_name;
        TextView order_id;

        public MyViewHolder(View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.complaints_list_status);
            cust_name = itemView.findViewById(R.id.complaints_list_name);
            order_id = itemView.findViewById(R.id.complaints_list_orderid);
        }
    }

    public ComplaintsModel getWordAtPosition(int position) {
        return complaintsModels.get(position);
    }

    public void setOnItemClickListener(ComplaintsAdapter.ClickListener clickListener) {
        ComplaintsAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}