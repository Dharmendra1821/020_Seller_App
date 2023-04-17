package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.interfaces.IProductList;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.BundleProductOption;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductLink;

public class BundleProductAdapter extends RecyclerView.Adapter<BundleProductAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<BundleProductOption> bundleProductOptions;
    ArrayList<ProductLink> productLinks;
  //  ArrayList<BundleProductOption> bundleProductOptions;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    String flag;
    IProductList iProduct;
    ArrayList<String> bundle_product;
    ArrayAdapter<String> bundleAdapter;



    public BundleProductAdapter(Context context, ArrayList<BundleProductOption> bundleProductOptions,ArrayList<ProductLink> productLinks) {
        this.mContext = context;
        this.bundleProductOptions = bundleProductOptions;
        this.productLinks = productLinks;
    }

    @NonNull
    @Override
    public BundleProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bundle_product_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BundleProductAdapter.MyViewHolder holder, int position) {

        final BundleProductOption bundleProductOption = bundleProductOptions.get(position);
        final ProductLink productLink = productLinks.get(position);

        pos = position;
        viewholder = holder;
        holder.spinner.setTag(pos);

        bundle_product = new ArrayList<String>();
        for(int i=0;i<bundleProductOptions.size();i++){
            bundle_product.add(bundleProductOption.getProductLinks().get(i).getProductName());
        }

            holder.spinner.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.GONE);
            bundleAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text_layout, bundle_product);
            holder.spinner.setAdapter(bundleAdapter);

            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    pos = (Integer) holder.spinner.getTag();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

    }

    @Override
    public int getItemCount() {
        return bundleProductOptions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

         CheckBox checkBox;
         Spinner spinner;

        public MyViewHolder(View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.bundle_checkbox_product);
            spinner  = itemView.findViewById(R.id.bundle_spinner_product);


        }
    }


}
