package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.BundleSizeContainer;
import standalone.eduqfix.qfixinfo.com.app.seller_app.interfaces.IProductList;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductLink;

public class GroupProductAdapter extends RecyclerView.Adapter<GroupProductAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ProductLink> productLinks;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    String flag;
    IProductList iProduct;

    public static ArrayList<String> groupQuantityValue = new ArrayList<>();

    public static ArrayList<String> getGroupQuantityValue() {
        return groupQuantityValue;
    }

    public static void setGroupQuantityValue(ArrayList<String> groupQuantityValue) {
        GroupProductAdapter.groupQuantityValue = groupQuantityValue;
    }

    public GroupProductAdapter(Context context, ArrayList<ProductLink> productLinks) {
        this.mContext = context;
        this.productLinks = productLinks;


    }

    @NonNull
    @Override
    public GroupProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_product_group, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupProductAdapter.MyViewHolder holder, int position) {

        final ProductLink productLink = productLinks.get(position);
        pos = position;
        viewholder = holder;
        holder.spinner.setTag(pos);

        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");


        GroupProductAdapter.getGroupQuantityValue().clear();
        BundleSizeContainer.HashMap.clear();

        holder.productname.setText(productLink.getProductName());
        Double price1= Double.parseDouble(productLink.getProductPrice());
        holder.productprice.setText(mContext.getString(R.string.Rs)+" "+price1);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.notifyDataSetChanged();
        holder.spinner.setAdapter(dataAdapter);

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = (Integer) holder.spinner.getTag();

                try{
                    GroupProductAdapter.getGroupQuantityValue().remove(holder.getPosition());
                }catch (Exception e){
                }
                groupQuantityValue.add(holder.getPosition(),productLink.getLinkedProductSku()+":"+holder.spinner.getSelectedItem().toString()
                  +":"+productLink.getLinkedProductType()+":"+productLink.getProductName()+":"+productLink.getProductPrice());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





    }

    @Override
    public int getItemCount() {
        return productLinks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
         TextView productname , productprice;
         Spinner spinner;

        public MyViewHolder(View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.group_product_name);
            productprice = itemView.findViewById(R.id.group_product_price);
            spinner      = itemView.findViewById(R.id.group_product_spinner);


        }
    }


}
