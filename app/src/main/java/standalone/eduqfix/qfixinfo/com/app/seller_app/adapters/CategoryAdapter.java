package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.SellerSubCategoriesActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Categories;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyCategories> implements Filterable {

    Context context;
    List<Categories> categoriesList;
    private List<Categories> catList;
    int CustomerId,CustomerAddressId;
    SharedPreferences sharedPreferences;
   // ArrayList<Categories> displayedList;
    private static ClickListener clickListener;
     ArrayList<Categories> arraylist;
    int index = 0;

    public CategoryAdapter() {

    }
    public CategoryAdapter(Context context, ArrayList<Categories> categories){
        this.context = context;
        this.arraylist = new ArrayList<Categories>();
        this.categoriesList = categories;
    }

    public CategoryAdapter(Context context , List<Categories> categoriesList,int customerId,int customerAddressId){
        this.context = context;
        this.categoriesList = categoriesList;
        CustomerId=customerId;
        this.catList = categoriesList;
        CustomerAddressId=customerAddressId;
    }

    @NonNull
    @Override
    public MyCategories onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_view,parent,false);
        return new MyCategories(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCategories holder, final int position) {

        final Categories categories = categoriesList.get(position);
        sharedPreferences = context.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String imageUrl = categories.getImageurl();

            /*holder.categoryImageView.setVisibility(View.GONE);
            holder.nameTextView.setVisibility(View.GONE);*/

                Glide.with(context).load(imageUrl).into(holder.categoryImageView);
                holder.nameTextView.setText(categories.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        clickListener.onItemClick(view, position);
                    }
                });

            /*else {

                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                holder.itemView.setLayoutParams(layoutParams);
                holder.linearLayout.setVisibility(View.GONE);
                holder.nameTextView.setVisibility(View.GONE);
                holder.categoryImageView.setVisibility(View.GONE);
                holder.itemView.setVisibility(View.GONE);
            }*/

       /* holder.nameTextView.setVisibility(View.VISIBLE);
        holder.categoryImageView.setVisibility(View.VISIBLE);
        holder.itemView.setVisibility(View.VISIBLE);
        Picasso.with(context).load(imageUrl).into(holder.categoryImageView);
        holder.nameTextView.setText(categories.getName());
        holder.itemView.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });*/

    //    holder.nameTextView.setText(categories.getName());

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDisplaySubCategories(categories.getCategory_id(),view);
            }
        });*/


    }


    public void getDisplaySubCategories(final Integer categoryId,final View view){
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.BASE_URL);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);
        final SharedPreferences sharedPreferences = context.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences. getString("adminToken",null);
        mposServices.getDisplayCategories(2,2,categoryId,token).enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(@NonNull Call<String[]> call, @NonNull Response<String[]> response) {
                if(response.code() == 200 || response.isSuccessful()){
                    String [] stringCategories = response.body();
                    sharedPreferences.edit().putString("displayCategories", Arrays.toString(stringCategories)).apply();
                    Intent intent = new Intent(view.getContext(), SellerSubCategoriesActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("CategoryId",categoryId);
                    bundle.putInt("CustomerId",CustomerId);
                    bundle.putInt("CustomerAddressId",CustomerAddressId);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }
                else if(response.code() == 400){
                    Toast.makeText(context,"Bad Request",Toast.LENGTH_LONG).show();
                }
                else if(response.code() == 401){
                    Toast.makeText(context,"Unauthorised",Toast.LENGTH_LONG).show();
                }
                else if(response.code() == 403){
                    Toast.makeText(context,"Forbidden",Toast.LENGTH_LONG).show();
                }
                else if(response.code() == 500){
                    Toast.makeText(context,"Internal Server Error",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String[]> call, @NonNull Throwable t) {
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }



    public class MyCategories extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView categoryImageView;
        CardView cardView;
        LinearLayout linearLayout;
        public MyCategories(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.categoryNameTextView);
            categoryImageView = itemView.findViewById(R.id.categoryImageView);
            cardView   = itemView.findViewById(R.id.category_cardview);
            linearLayout   = itemView.findViewById(R.id.linear_category);
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    categoriesList = catList;
                } else {
                    List<Categories> filteredList = new ArrayList<>();
                    for (Categories row : catList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    categoriesList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = categoriesList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                categoriesList = (ArrayList<Categories>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public Categories getWordAtPosition(int position) {
        return categoriesList.get(position);
    }

    public void setOnItemClickListener(CategoryAdapter.ClickListener clickListener) {
        CategoryAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

   /* public void removeAt(int position) {

        categoriesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, categoriesList.size());
    }*/

    public void removeItem(@NonNull Object object) {
        categoriesList.remove(object);
        notifyDataSetChanged();
    }


    /*private void deleteItem(int position) {
        int removeIndex = position;
        categoriesList.remove(removeIndex);
        notifyDataSetChanged();
        notifyItemRemoved(removeIndex);
        }*/

      /*  categoriesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, categoriesList.size());*/


}
