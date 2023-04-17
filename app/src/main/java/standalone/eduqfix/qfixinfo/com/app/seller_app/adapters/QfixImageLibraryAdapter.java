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

import net.hydromatic.linq4j.Linq4j;
import net.hydromatic.linq4j.function.Predicate1;

import java.util.ArrayList;
import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Categories;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Custom_attributes;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Items;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.QfixLibraryModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.QFIX_LIBRARY_IMAGE;

public class QfixImageLibraryAdapter extends RecyclerView.Adapter<QfixImageLibraryAdapter.SubCategoryHolder> implements Filterable {

    Context context;
    List<QfixLibraryModel> qfixLibraryModels;
    private List<QfixLibraryModel> catList;
    private static QfixImageLibraryAdapter.ClickListener clickListener;
    SharedPreferences sharedPreferences;
    int CustomerId,CustomerAddressId;
    ArrayList<QfixLibraryModel> arraylist;
    String data;

    public QfixImageLibraryAdapter(){

    }
    public QfixImageLibraryAdapter(Context context, ArrayList<QfixLibraryModel> qfixLibraryModels){
        this.context = context;
        this.arraylist = new ArrayList<QfixLibraryModel>();
        this.qfixLibraryModels = qfixLibraryModels;
    }

    public QfixImageLibraryAdapter(Context context , List<QfixLibraryModel> qfixLibraryModels,String data){
        this.context = context;
        this.qfixLibraryModels = qfixLibraryModels;
        this.catList = qfixLibraryModels;
    }

    @NonNull
    @Override
    public SubCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_library_list_view,parent,false);
        return new SubCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategoryHolder holder,final int position) {

        final QfixLibraryModel qfixLibraryModel = qfixLibraryModels.get(position);

        sharedPreferences = context.getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
      //  String imageUrl = "https://"+shopUrl+Constant.CATEGORY_MEDIA_URL1+qfixLibraryModel.getImage();

        String imageUrl = qfixLibraryModel.getImage();

        if(imageUrl != null){
            Glide.with(context).load(imageUrl).into(holder.qfiximage);
        }else{
            //need to add image
            holder.qfiximage.setImageResource(R.drawable.no_product);
        }

       // holder.text.setText(qfixLibraryModel.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return qfixLibraryModels.size();
    }

    public class SubCategoryHolder extends RecyclerView.ViewHolder {


        ImageView qfiximage;
        TextView text;
        public SubCategoryHolder(View itemView) {
            super(itemView);

            qfiximage = itemView.findViewById(R.id.image_library_imgview);
            //text = itemView.findViewById(R.id.text_library);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    qfixLibraryModels = catList;
                } else {
                    List<QfixLibraryModel> filteredList = new ArrayList<>();
                    for (QfixLibraryModel row : catList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    qfixLibraryModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = qfixLibraryModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                qfixLibraryModels = (ArrayList<QfixLibraryModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public QfixLibraryModel getWordAtPosition(int position) {
        return qfixLibraryModels.get(position);
    }

    public void setOnItemClickListener(QfixImageLibraryAdapter.ClickListener clickListener) {
        QfixImageLibraryAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }



}
