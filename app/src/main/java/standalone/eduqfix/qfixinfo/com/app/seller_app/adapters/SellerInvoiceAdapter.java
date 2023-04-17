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
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.MyOrderProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModelNew;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SellerInvoiceModel;

public class SellerInvoiceAdapter extends RecyclerView.Adapter<SellerInvoiceAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<SellerInvoiceModel> sellerInvoiceModels;
    SellerInvoiceAdapter.MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static SellerInvoiceAdapter.ClickListener clickListener;

    public static ArrayList<String> getInvoiceIds() {
        return invoiceIds;
    }

    public static void setInvoiceIds(ArrayList<String> invoiceIds) {
        SellerInvoiceAdapter.invoiceIds = invoiceIds;
    }

    String imageUrl;
    public static ArrayList<String> invoiceIds = new ArrayList<>();
    public SellerInvoiceAdapter(){

    }


    public SellerInvoiceAdapter(Context context, ArrayList<SellerInvoiceModel> sellerInvoiceModels) {
        this.mContext = context;
        this.sellerInvoiceModels = sellerInvoiceModels;
        this.flag = flag;
        SellerInvoiceAdapter.getInvoiceIds().clear();
    }

    @NonNull
    @Override
    public SellerInvoiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_invoice_list, parent, false);
        SellerInvoiceAdapter.MyViewHolder viewHolder = new SellerInvoiceAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SellerInvoiceAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final SellerInvoiceModel sellerInvoiceModel = sellerInvoiceModels.get(position);
        pos = position;
        viewholder = holder;



        holder.gstin.setText(sellerInvoiceModel.getGstin());
        if(!sellerInvoiceModel.getGstin().equalsIgnoreCase("null")){
            holder.gstin.setText(sellerInvoiceModel.getGstin());
        }
        else {
            holder.gstin.setText("N/A");
        }
        holder.price.setText(mContext.getString(R.string.Rs)+" "+sellerInvoiceModel.getAmount());
        if(sellerInvoiceModel.getCname().equalsIgnoreCase("null")){
            holder.cust_name.setText("");
        }
        else {
            holder.cust_name.setText(sellerInvoiceModel.getCname());

        }
        holder.date.setText(sellerInvoiceModel.getInvoiceDate());
        holder.invoice_number.setText(sellerInvoiceModel.getInvoicenumber());
        if(sellerInvoiceModel.getPaymentstatus().equalsIgnoreCase("1")){
            holder.payment_status.setText("Payment link send");
        }if(sellerInvoiceModel.getPaymentstatus().equalsIgnoreCase("2")){
            holder.payment_status.setText("Payment Received");
        }
        if(sellerInvoiceModel.getPaymentstatus().equalsIgnoreCase("3")){
            holder.payment_status.setText("Link Canceled/Expired");
        }
        if(sellerInvoiceModel.getPaymentstatus().equalsIgnoreCase("4")){
            holder.payment_status.setText("Transaction Failed");
        }




        holder.invoice_id_checkbox.setChecked(sellerInvoiceModels.get(position).isSelected());
        holder.invoice_id_checkbox.setTag(position);

        holder.invoice_id_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) holder.invoice_id_checkbox.getTag();
                if (sellerInvoiceModels.get(pos).isSelected()) {
                    sellerInvoiceModels.get(pos).setSelected(false);
                    invoiceIds.remove(sellerInvoiceModel.getInvoice_id());
                } else {
                    sellerInvoiceModels.get(pos).setSelected(true);
                    invoiceIds.add(sellerInvoiceModel.getInvoice_id());
                }
            }
        });

        Log.d("recipt...", sellerInvoiceModels.get(position).getReceipt_link());

        if(sellerInvoiceModels.get(position).getReceipt_link().equalsIgnoreCase("null")){
            holder.download_pdf.setText("Receipt not available");
        }
        else {
            holder.download_pdf.setText("Download Receipt");
        }

            holder.download_pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clickListener.onItemClick(view, position);
                }
            });




    }
    @Override
    public int getItemCount() {
        return sellerInvoiceModels.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView invoice_number;
        TextView payment_status;
        TextView price;
        TextView date;
        TextView cust_name;
        TextView gstin;
        TextView download_pdf;
        CheckBox invoice_id_checkbox;

        public MyViewHolder(View itemView) {
            super(itemView);
            invoice_number =  itemView.findViewById(R.id.seller_invoice_number);
            payment_status =  itemView.findViewById(R.id.seller_invoice_status);
            date  =  itemView.findViewById(R.id.seller_invoice_date);
            price  =  itemView.findViewById(R.id.seller_invoice_amount);
            cust_name = itemView.findViewById(R.id.seller_invoice_name);
            gstin = itemView.findViewById(R.id.seller_invoice_gstin);
            download_pdf = itemView.findViewById(R.id.seller_invoice_pdf_link);
            invoice_id_checkbox = itemView.findViewById(R.id.invoice_id_checkbox);
        }
    }
    public SellerInvoiceModel getWordAtPosition(int position) {
        return sellerInvoiceModels.get(position);
    }

    public void setOnItemClickListener(SellerInvoiceAdapter.ClickListener clickListener) {
        SellerInvoiceAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
