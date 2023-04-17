package standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyList;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyOrder;
import standalone.eduqfix.qfixinfo.com.app.database.DeliveryDBManager;

public class DeliveryboyAdapter extends RecyclerView.Adapter<DeliveryboyAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<DeliveryBoyList> deliveryBoyLists;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static DeliveryboyAdapter.ClickListener clickListener;
    String imageUrl;
    DeliveryDBManager deliveryDBManager;
    ArrayList<String> deliveryValue;


    public DeliveryboyAdapter(){

    }


    public DeliveryboyAdapter(Context context, ArrayList<DeliveryBoyList> deliveryBoyLists) {
        this.mContext = context;
        this.deliveryBoyLists = deliveryBoyLists;
        this.flag = flag;
    }

    @NonNull
    @Override
    public DeliveryboyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_boy_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final DeliveryboyAdapter.MyViewHolder holder, final int position) {

        final DeliveryBoyList deliveryBoyList = deliveryBoyLists.get(position);
        pos = position;
        viewholder = holder;

        holder.name.setText(deliveryBoyList.getFirstname()+" "+deliveryBoyList.getLastname());
        holder.email.setText(deliveryBoyList.getEmail());
        holder.contact.setText(deliveryBoyList.getContact());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, position);
            }
        });


    }
    @Override
    public int getItemCount() {
        return deliveryBoyLists.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        TextView contact;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.delivery_boy_fullname);
            email = itemView.findViewById(R.id.delivery_boy_email);
            contact = itemView.findViewById(R.id.delivery_boy_contact);


        }
    }
    public DeliveryBoyList getWordAtPosition(int position) {
        return deliveryBoyLists.get(position);
    }

    public void setOnItemClickListener(DeliveryboyAdapter.ClickListener clickListener) {
        DeliveryboyAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
