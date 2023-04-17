package standalone.eduqfix.qfixinfo.com.app.seller_app.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.community.model.CommunitySellerListModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.community.model.ImageModel;

public class CommunitySellerListAdapter extends RecyclerView.Adapter<CommunitySellerListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CommunitySellerListModel> communitySellerListModels;
    CommunitySellerListAdapter.MyViewHolder viewholder;
    int pos = 0;
    private static CommunitySellerListAdapter.ClickListener clickListener;

    public  CommunitySellerListAdapter(){

    }

    public CommunitySellerListAdapter(Context context, ArrayList<CommunitySellerListModel> communitySellerListModels) {
        this.mContext = context;
        this.communitySellerListModels = communitySellerListModels;
    }

    @NonNull
    @Override
    public CommunitySellerListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_seller_list, parent, false);
        CommunitySellerListAdapter.MyViewHolder viewHolder = new CommunitySellerListAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CommunitySellerListAdapter.MyViewHolder holder, final int position) {

        final CommunitySellerListModel communitySellerListModel = communitySellerListModels.get(position);
        pos = position;
        viewholder = holder;

        holder.sellerName.setText(String.format("Customer Name : %s", communitySellerListModel.getSellerName()));
        holder.sellerContact.setText(String.format("Contact No. : %s", communitySellerListModel.getSellerContactNo()));
        holder.shopName.setText(String.format("Shop Name : %s", communitySellerListModel.getShopName()));
        holder.sellerEmail.setText(String.format("Email : %s", communitySellerListModel.getSellerEmail()));
        holder.status.setText(String.format("Status : %s",communitySellerListModel.getStatus()));

        holder.salesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return communitySellerListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sellerName,sellerContact,shopName,sellerEmail,status;
        Button salesBtn,productBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            sellerName = itemView.findViewById(R.id.microMarket_seller_name);
            sellerContact = itemView.findViewById(R.id.microMarket_contact_no);
            shopName = itemView.findViewById(R.id.microMarket_shopName);
            sellerEmail = itemView.findViewById(R.id.microMarket_shopEmail);
            status = itemView.findViewById(R.id.microMarketStatus);

            salesBtn = itemView.findViewById(R.id.microMarket_sales);
        }
    }

    public CommunitySellerListModel getWordAtPosition(int position) {
        return communitySellerListModels.get(position);
    }

    public void setOnItemClickListener(CommunitySellerListAdapter.ClickListener clickListener) {
        CommunitySellerListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
