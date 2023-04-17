package standalone.eduqfix.qfixinfo.com.app.invoices.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.invoices.activity.InvoiceDetailsActivity;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.SearchInvoiceResponse;

/**
 * Created by darshan on 27/2/19.
 */

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {

    List<SearchInvoiceResponse> invoiceResponses;
    Context mContext;
    SearchInvoiceResponse invoiceResponse = null;
    SharedPreferences sharedPreferences;

    public InvoiceAdapter(Context mContext,List<SearchInvoiceResponse> responses){
        this.mContext = mContext;
        invoiceResponses = responses;
    }

    @Override
    public InvoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_view,parent,false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InvoiceViewHolder holder, final int position) {

        invoiceResponse = invoiceResponses.get(position);
        String customerName = invoiceResponse.getCustomer_name() != null ? invoiceResponse.getCustomer_name() : "";

        holder.customerNameTextView.setText("Customer Name : "+customerName);
        holder.invoiceIdTextView.setText("Invoice No. : "+invoiceResponse.getInvoice_number());
        double amount = invoiceResponse.getGrand_total() != null ? Math.round(Integer.parseInt(invoiceResponse.getGrand_total()) * 100) / 100.00 : 0.0;
        holder.amountTextView.setText(String.valueOf(amount));
       // holder.paymentStatus.setText(invoiceResponse.getPayment_status() != null );
        if(invoiceResponse.getPayment_status().equalsIgnoreCase("0")){
            holder.paymentStatus.setText("Unpaid");
            holder.paymentStatus.setTextColor(Color.RED);
        }
        if(invoiceResponse.getPayment_status().equalsIgnoreCase("1")){
            holder.paymentStatus.setText("Paid");
            holder.paymentStatus.setTextColor(Color.parseColor("#FFA500"));
        }
        if(invoiceResponse.getOrder_status().equalsIgnoreCase("6")){
            holder.paymentStatus.setText("Delivered");
            holder.paymentStatus.setTextColor(Color.GREEN);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InvoiceDetailsActivity.class);
                Gson gson = new Gson();
                Integer selectedPosition = holder.getAdapterPosition();
                invoiceResponse = invoiceResponses.get(selectedPosition);
                String selectedInvoice = gson.toJson(invoiceResponse);
                sharedPreferences = mContext.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("SelectedInvoice",selectedInvoice).apply();
                Bundle bundle = new Bundle();
                bundle.putString("SelectedInvoice",selectedInvoice);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return invoiceResponses.size();
    }

    public class InvoiceViewHolder extends RecyclerView.ViewHolder{

        TextView customerNameTextView,invoiceIdTextView,amountTextView,paymentStatus;

        public InvoiceViewHolder(View itemView) {
            super(itemView);

            customerNameTextView = itemView.findViewById(R.id.customerNameTextView);
            invoiceIdTextView = itemView.findViewById(R.id.invoiceIdTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            paymentStatus = itemView.findViewById(R.id.deliveryPaymentStatus);
        }
    }
}
