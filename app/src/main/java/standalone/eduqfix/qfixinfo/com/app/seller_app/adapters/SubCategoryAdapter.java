package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import net.hydromatic.linq4j.Linq4j;
import net.hydromatic.linq4j.function.Predicate1;

import java.util.ArrayList;
import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Custom_attributes;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Items;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SubCategoryModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryHolder> implements Filterable {

    Context context;
    List<SubCategoryModel> subcategoriesList;
    private List<SubCategoryModel> catList;
    private static SubCategoryAdapter.ClickListener clickListener;
    SharedPreferences sharedPreferences;
    int CustomerId,CustomerAddressId;

    public SubCategoryAdapter(){

    }
    public SubCategoryAdapter(Context context , List<SubCategoryModel> subcategoriesList){
        this.context = context;
        this.subcategoriesList = subcategoriesList;
        this.catList = subcategoriesList;
    }

    @NonNull
    @Override
    public SubCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_view,parent,false);
        return new SubCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategoryHolder holder,final int position) {

        final SubCategoryModel categories = subcategoriesList.get(position);

            sharedPreferences = context.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            String imageUrl = null;


        if(imageUrl != null){
            Glide.with(context).load(categories.getImageurl()).into(holder.categoryImageView);
        }else{
            holder.categoryImageView.setImageResource(R.drawable.no_product);
        }
           holder.nameTextView.setText(categories.getName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickListener.onItemClick(view, position);
              /* Intent intent = new Intent(view.getContext(), ProductListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("SubCategoryId",categories.getId());
                bundle.putInt("CategoryId",categories.getParent_id());
                bundle.putInt("CustomerId",CustomerId);
                bundle.putInt("CustomerAddressId",CustomerAddressId);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return subcategoriesList.size();
    }

    public class SubCategoryHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView categoryImageView;
        public SubCategoryHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.categoryNameTextView);
            categoryImageView = itemView.findViewById(R.id.categoryImageView);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    subcategoriesList = catList;
                } else {
                    List<SubCategoryModel> filteredList = new ArrayList<>();
                    for (SubCategoryModel row : catList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    subcategoriesList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = subcategoriesList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                subcategoriesList = (ArrayList<SubCategoryModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public SubCategoryModel getWordAtPosition(int position) {
        return subcategoriesList.get(position);
    }

    public void setOnItemClickListener(SubCategoryAdapter.ClickListener clickListener) {
        SubCategoryAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }



}
