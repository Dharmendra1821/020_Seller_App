package standalone.eduqfix.qfixinfo.com.app.invoice_seller.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.CreateInvoiceActivity;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.constant.Constant;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.database.AppDatabase;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.model.InvoiceProductModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ComplaintsAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ComplaintsModel;

public class SellerProductAdaptor extends RecyclerView.Adapter<SellerProductAdaptor.MyViewHolder> {

    private Context context;
    private List<InvoiceProductModel> invoiceProductModels;
    private static SellerProductAdaptor.ClickListener clickListener;
    public SellerProductAdaptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.invoice_seller_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerProductAdaptor.MyViewHolder myViewHolder, int i) {

        myViewHolder.id.setText("Id : "+invoiceProductModels.get(i).getInvoiceId());
        myViewHolder.name.setText("Name : "+invoiceProductModels.get(i).getName());
        myViewHolder.price.setText("Price : "+context.getString(R.string.Rs)+invoiceProductModels.get(i).getPrice());
        myViewHolder.qty.setText("Qty : "+invoiceProductModels.get(i).getQty());

        if(invoiceProductModels.get(i).getTax().equalsIgnoreCase("CGST/SGST")){
            myViewHolder.invoice_product_tax.setText(invoiceProductModels.get(i).getCgstValue());
        }
        else if(invoiceProductModels.get(i).getTax().equalsIgnoreCase("IGST")){
            myViewHolder.invoice_product_tax.setText(invoiceProductModels.get(i).getCgstValue());
        }
        else {
            myViewHolder.invoice_product_tax.setText("");
        }


        myViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(v, i,"edit");
            }
        });

        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(v, i,"delete");
            }
        });


    }

    @Override
    public int getItemCount() {
        if (invoiceProductModels == null) {
            return 0;
        }
        return invoiceProductModels.size();
    }

    public void setTasks(List<InvoiceProductModel> productModels) {
        invoiceProductModels = productModels;
        notifyDataSetChanged();
    }

    public void setData(List<InvoiceProductModel> newData) {
        if (invoiceProductModels != null) {
            /*PostDiffCallback postDiffCallback = new PostDiffCallback(data, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);*/

            invoiceProductModels.clear();
            invoiceProductModels.addAll(newData);
            notifyDataSetChanged();
            //diffResult.dispatchUpdatesTo(this);
        } else {
            // first initialization
            invoiceProductModels = newData;
        }
    }

    public List<InvoiceProductModel> getTasks() {

        return invoiceProductModels;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, qty, cgst, igst,invoice_product_tax,invoice_product_taxvalue,id;
        TextView edit,delete;
        AppDatabase mDb;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            mDb = AppDatabase.getInstance(context);
            id = itemView.findViewById(R.id.invoice_product_id);
            name = itemView.findViewById(R.id.invoice_product_name);
            price = itemView.findViewById(R.id.invoice_product_price);
            qty = itemView.findViewById(R.id.invoice_product_qty);
            invoice_product_tax = itemView.findViewById(R.id.invoice_product_tax);
            edit = itemView.findViewById(R.id.invoice_product_edit);
            delete = itemView.findViewById(R.id.invoice_product_delete);

        }
    }

    public InvoiceProductModel getWordAtPosition(int position) {
        return invoiceProductModels.get(position);
    }

    public void setOnItemClickListener(SellerProductAdaptor.ClickListener clickListener) {
        SellerProductAdaptor.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position,String flag);
    }
}
