package standalone.eduqfix.qfixinfo.com.app.image_add_product.activities;

import android.content.Context;
import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ImageProductModel> imageProductModels;
    ProductImageAdapter.MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static ProductImageAdapter.ClickListener clickListener;

    public ProductImageAdapter() {

    }


    public ProductImageAdapter(Context context, ArrayList<ImageProductModel> imageProductModels) {
        this.mContext = context;
        this.imageProductModels = imageProductModels;
        this.flag = flag;
    }

    @NonNull
    @Override
    public ProductImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_image_listing, parent, false);
        ProductImageAdapter.MyViewHolder viewHolder = new ProductImageAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductImageAdapter.MyViewHolder holder, final int position) {

        final ImageProductModel imageProductModel = imageProductModels.get(position);
        pos = position;
        viewholder = holder;

        sharedPreferences = mContext.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);

        String imageUrl = "https://"+shopUrl+Constant.PRODUCT_MEDIA_URL1 + imageProductModel.getFile();

        if(imageProductModel.getFile().equalsIgnoreCase("")){
            Glide.with(mContext).load(R.drawable.download).error(R.drawable.download).into(holder.imageView);
        }else {
            Glide.with(mContext).load(imageUrl).into(holder.imageView);
        }


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position,0);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position,1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return imageProductModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageView edit;


        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.edit_product_images);
            edit  = itemView.findViewById(R.id.edit_image_list);

        }
    }

    public ImageProductModel getWordAtPosition(int position) {
        return imageProductModels.get(position);
    }

    public void setOnItemClickListener(ProductImageAdapter.ClickListener clickListener) {
        ProductImageAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position,int flag);
    }
}