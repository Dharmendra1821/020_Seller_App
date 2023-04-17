package standalone.eduqfix.qfixinfo.com.app.image_add_product.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.StringTokenizer;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

public class AddProductListAdapter extends RecyclerView.Adapter<AddProductListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<AddProductListingModel> addProductListingModels;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static AddProductListAdapter.ClickListener clickListener;
    String imageUrl;

    public AddProductListAdapter(){

    }


    public AddProductListAdapter(Context context, ArrayList<AddProductListingModel> addProductListingModels) {
        this.mContext = context;
        this.addProductListingModels = addProductListingModels;
        this.flag = flag;
    }

    @NonNull
    @Override
    public AddProductListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_product_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AddProductListAdapter.MyViewHolder holder,final int position) {

        final AddProductListingModel addProductListingModel = addProductListingModels.get(position);
        pos = position;
        viewholder = holder;

        sharedPreferences = mContext.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);

        Log.d("Imag file....",addProductListingModel.getFile());

             imageUrl = "https://"+shopUrl+Constant.PRODUCT_MEDIA_URL1+addProductListingModel.getFile();
             Glide.with(mContext).load(imageUrl).error(R.drawable.download).into(holder.imageView);


        holder.productname.setText(addProductListingModel.getProductname());
        holder.sku.setText(addProductListingModel.getSku());
        try {
            if (addProductListingModel.getPrice().equalsIgnoreCase("null")) {
                holder.price.setText("0");
            } else if (addProductListingModel.getPrice().equalsIgnoreCase("0")) {
                holder.price.setText("0");
            } else {
                StringTokenizer tokens = new StringTokenizer(addProductListingModel.getPrice(), ".");
                String first = tokens.nextToken();// this will contain "Fruit"
                String second = tokens.nextToken();
                Log.d("....", first);
                Log.d("....", second);
                holder.price.setText(first);
            }
        }catch (Exception e){

        }

        holder.add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });


    }
    @Override
    public int getItemCount() {
        return addProductListingModels.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView productname;
        TextView sku;
        TextView price;
        Button add_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.add_productImage);
            productname =  itemView.findViewById(R.id.add_product_productName);
            sku  =  itemView.findViewById(R.id.add_product_skuname);
            price  =  itemView.findViewById(R.id.add_product_rupees);
            add_image = itemView.findViewById(R.id.add_image_btn);
        }
    }
    public AddProductListingModel getWordAtPosition(int position) {
        return addProductListingModels.get(position);
    }

    public void setOnItemClickListener(AddProductListAdapter.ClickListener clickListener) {
        AddProductListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
