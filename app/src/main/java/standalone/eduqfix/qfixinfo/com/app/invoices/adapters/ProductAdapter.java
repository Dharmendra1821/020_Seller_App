package standalone.eduqfix.qfixinfo.com.app.invoices.adapters;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.Item;

/**
 * Created by darshan on 27/2/19.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context mContext;
    List<Item> itemList;

    public ProductAdapter(Context mContext,List<Item> itemList){
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_view,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        Log.d("Current Position","Current Position"+position);
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
        Item item = itemList.get(position);
     //   double amount = item.getBasePriceInclTax() != null ?  Math.round(item.getBasePriceInclTax() * 100) / 100.00 : 0;
        if(Integer.parseInt(itemList.get(position).getAmount()) > 0){
            holder.productNameTextView.setText(itemList.get(position).getProduct_name());
            holder.priceTextView.setText(itemList.get(position).getAmount());
            holder.qtyTextView.setText(itemList.get(position).getQty());
        }else{
            param.height = 0;
            param.width = 0;
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView productNameTextView,skuTextView,priceTextView,qtyTextView;
        public ProductViewHolder(View itemView) {
            super(itemView);

            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            qtyTextView = itemView.findViewById(R.id.qtyTextView);
        }
    }
}
