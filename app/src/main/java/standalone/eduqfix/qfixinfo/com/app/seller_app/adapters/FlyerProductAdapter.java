package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.FlyerProductModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.QfixLibraryModel;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.QFIX_LIBRARY_IMAGE;

public class FlyerProductAdapter extends RecyclerView.Adapter<FlyerProductAdapter.SubCategoryHolder> implements Filterable {

    Context context;
    List<FlyerProductModel> flyerProductModels;
    private List<FlyerProductModel> catList;
    private static FlyerProductAdapter.ClickListener clickListener;
    SharedPreferences sharedPreferences;
    int CustomerId,CustomerAddressId;
    ArrayList<FlyerProductModel> arraylist;
    String imgPosition;

    public FlyerProductAdapter(){

    }
    public FlyerProductAdapter(Context context, ArrayList<FlyerProductModel> flyerProductModels){
        this.context = context;
        this.arraylist = new ArrayList<FlyerProductModel>();
        this.flyerProductModels = flyerProductModels;
    }

    public FlyerProductAdapter(Context context , List<FlyerProductModel> flyerProductModels, String imgPosition){
        this.context = context;
        this.flyerProductModels = flyerProductModels;
        this.catList = flyerProductModels;
        this.imgPosition = imgPosition;
    }

    @NonNull
    @Override
    public SubCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.flyer_product_list,parent,false);
        return new SubCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategoryHolder holder,final int position) {

        final FlyerProductModel flyerProductModel = flyerProductModels.get(position);

        sharedPreferences = context.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);


        holder.text.setText(flyerProductModel.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("flyer-message");
                intent.putExtra("position",imgPosition);
                intent.putExtra("image",flyerProductModel.getImage());
                intent.putExtra("id",flyerProductModel.getId());
                intent.putExtra("name",flyerProductModel.getName());
                intent.putExtra("price",flyerProductModel.getPrice());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return flyerProductModels.size();
    }

    public class SubCategoryHolder extends RecyclerView.ViewHolder {
        TextView text;
        public SubCategoryHolder(View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.flyer_product_txtname);
        }
    }

    public FlyerProductModel getWordAtPosition(int position) {
        return flyerProductModels.get(position);
    }

    public void setOnItemClickListener(FlyerProductAdapter.ClickListener clickListener) {
        FlyerProductAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    flyerProductModels = catList;
                } else {
                    List<FlyerProductModel> filteredList = new ArrayList<>();
                    for (FlyerProductModel row : catList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    flyerProductModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = flyerProductModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                flyerProductModels = (ArrayList<FlyerProductModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }





}
