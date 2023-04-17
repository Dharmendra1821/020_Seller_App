package standalone.eduqfix.qfixinfo.com.app.seller_app.community;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SellerInvoiceAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.community.model.ImageModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SellerInvoiceModel;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ImageModel> imageModels;
    ImageAdapter.MyViewHolder viewholder;
    int pos = 0;
    private static ImageAdapter.ClickListener clickListener;


    public ImageAdapter(Context context, ArrayList<ImageModel> imageModels) {
        this.mContext = context;
        this.imageModels = imageModels;
    }

    @NonNull
    @Override
    public ImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.micro_market_images, parent, false);
        ImageAdapter.MyViewHolder viewHolder = new ImageAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageAdapter.MyViewHolder holder, final int position) {

        final ImageModel imageModel = imageModels.get(position);
        pos = position;
        viewholder = holder;

        String imageUrl = imageModel.getBaseUrl()+imageModel.getImages();

            Glide.with(mContext).load(imageUrl).into(holder.imageView);

//        holder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickListener.onItemClick(view, position);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return imageModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.microMarket_banner);

        }
    }

    public ImageModel getWordAtPosition(int position) {
        return imageModels.get(position);
    }

    public void setOnItemClickListener(ImageAdapter.ClickListener clickListener) {
        ImageAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
