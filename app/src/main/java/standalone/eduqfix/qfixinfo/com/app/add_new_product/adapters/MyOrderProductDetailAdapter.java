package standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters;

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
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.ProductDetailModel;
import standalone.eduqfix.qfixinfo.com.app.database.DeliveryDBManager;

public class MyOrderProductDetailAdapter extends RecyclerView.Adapter<MyOrderProductDetailAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ProductDetailModel> productDetailModels;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;



    public MyOrderProductDetailAdapter(){

    }


    public MyOrderProductDetailAdapter(Context context, ArrayList<ProductDetailModel> productDetailModels) {
        this.mContext = context;
        this.productDetailModels = productDetailModels;
        this.flag = flag;
    }

    @NonNull
    @Override
    public MyOrderProductDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_detail_model_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final MyOrderProductDetailAdapter.MyViewHolder holder, final int position) {

        final ProductDetailModel productDetailModel = productDetailModels.get(position);
        pos = position;
        viewholder = holder;

        holder.name.setText(productDetailModel.getProductName());
        holder.sku.setText(productDetailModel.getSku());
        String subTotalVal = String.format("%.2f", Double.valueOf(productDetailModel.getProductPrice()));
        holder.price.setText(subTotalVal);



    }
    @Override
    public int getItemCount() {
        return productDetailModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView sku;
        TextView price;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.myorder_product_name);
            sku = itemView.findViewById(R.id.myorder_product_sku);
            price = itemView.findViewById(R.id.myorder_product_price);


        }
    }


}
