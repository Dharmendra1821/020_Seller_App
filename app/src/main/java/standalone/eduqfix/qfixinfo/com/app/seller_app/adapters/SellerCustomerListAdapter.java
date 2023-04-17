package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.database.DeliveryDBManager;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerListModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCustomerListModel;

public class SellerCustomerListAdapter extends RecyclerView.Adapter<SellerCustomerListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CustomerListModel> customerListModels;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static SellerCustomerListAdapter.ClickListener clickListener;
    public static ArrayList<String> customerIds = new ArrayList<>();

    public static ArrayList<String> getCustomerIds() {
        return customerIds;
    }

    public static void setCustomerIds(ArrayList<String> customerIds) {
        SellerCustomerListAdapter.customerIds = customerIds;
    }

    public SellerCustomerListAdapter(){

    }


    public SellerCustomerListAdapter(Context context, ArrayList<CustomerListModel> customerListModels) {
        this.mContext = context;
        this.customerListModels = customerListModels;
        this.flag = flag;
    }

    @NonNull
    @Override
    public SellerCustomerListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_customer_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final SellerCustomerListAdapter.MyViewHolder holder, final int position) {

        final CustomerListModel customerListModel = customerListModels.get(position);
        pos = position;
        viewholder = holder;

        SellerCustomerListAdapter.getCustomerIds().clear();

        holder.name.setText(customerListModel.getName());
        holder.email.setText(customerListModel.getEmail());
        if (!customerListModel.getMobile().equalsIgnoreCase("null")){
            holder.mobile.setText(customerListModel.getMobile());
        }

        if(customerListModel.getIs_credit_available().equalsIgnoreCase("1")){
            holder.credit.setVisibility(View.VISIBLE);
            holder.reminder.setVisibility(View.VISIBLE);
        }
        else {
            holder.credit.setVisibility(View.GONE);
            holder.reminder.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position,"1");
            }
        });

        holder.reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position,"2");
            }
        });

        holder.enable_credit.setChecked(customerListModels.get(position).isSelected());
        holder.enable_credit.setTag(position);

        holder.enable_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) holder.enable_credit.getTag();
                if (customerListModels.get(pos).isSelected()) {
                    customerListModels.get(pos).setSelected(false);
                    customerIds.remove(customerListModel.getCustomerId());
                } else {
                    customerListModels.get(pos).setSelected(true);
                    customerIds.add(customerListModel.getCustomerId());
                }
            }
        });


    }
    @Override
    public int getItemCount() {
        return customerListModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        TextView mobile;
        TextView credit;
        TextView reminder;
        CheckBox enable_credit;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.seller_customer_name);
            email = itemView.findViewById(R.id.seller_customer_email);
            mobile = itemView.findViewById(R.id.seller_customer_mobile);
            credit = itemView.findViewById(R.id.seller_customer_udhar);
            reminder = itemView.findViewById(R.id.seller_customer_reminder);
            enable_credit = itemView.findViewById(R.id.enable_credit);
        }
    }
    public CustomerListModel getWordAtPosition(int position) {
        return customerListModels.get(position);
    }

    public void setOnItemClickListener(SellerCustomerListAdapter.ClickListener clickListener) {
        SellerCustomerListAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onItemClick(View v, int position,String value);
    }


}
