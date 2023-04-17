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
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyList;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyOrder;
import standalone.eduqfix.qfixinfo.com.app.database.DeliveryDBManager;

public class UnAssignDeliveryboyListAdapter extends RecyclerView.Adapter<UnAssignDeliveryboyListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<DeliveryBoyOrder> deliveryBoyOrderArrayList;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static UnAssignDeliveryboyListAdapter.ClickListener clickListener;
    String imageUrl;
    DeliveryDBManager deliveryDBManager;
    ArrayList<String> deliveryValue;
    ArrayList<DeliveryBoyList> deliveryBoyLists;
    ArrayList<DeliveryBoyList> newdeliveryBoyLists;

    public static java.util.HashMap<Integer,Integer> selectionSpinner=new HashMap<Integer, Integer>();
    Map<Integer, Integer> mSpinnerSelectedItem = new HashMap<Integer, Integer>();
    String hello;
    String name;
    String firstName;
    String lastName;
    public UnAssignDeliveryboyListAdapter(){

    }


    public UnAssignDeliveryboyListAdapter(Context context, ArrayList<DeliveryBoyOrder> deliveryBoyOrderArrayList) {
        this.mContext = context;
        this.deliveryBoyOrderArrayList = deliveryBoyOrderArrayList;
        this.flag = flag;
        DeliveryboyListAdapter.HashMap.clear();

    }

    @NonNull
    @Override
    public UnAssignDeliveryboyListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_boy_order_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final UnAssignDeliveryboyListAdapter.MyViewHolder holder, final int position) {

        final DeliveryBoyOrder deliveryBoyOrder = deliveryBoyOrderArrayList.get(position);
        pos = position;
        viewholder = holder;

        deliveryValue = new ArrayList<>();
        //    deliveryBoyLists = new ArrayList<>();
        newdeliveryBoyLists = new ArrayList<>();

        deliveryDBManager = new DeliveryDBManager(mContext);
        deliveryDBManager.open();


        String sFormAddress = deliveryBoyOrder.getShippingstreet() + "\n" + deliveryBoyOrder.getShippingcity() + "\n" +
                deliveryBoyOrder.getShippingpostcode() + "\n" +
                deliveryBoyOrder.getShippingstate_code();

        holder.address.setText(sFormAddress);
        holder.mobile.setText(deliveryBoyOrder.getShippingtelephone());
        holder.date.setText(deliveryBoyOrder.getCreated_at());
        holder.cust_name.setText(deliveryBoyOrder.getCustomer_firstname() + " " + deliveryBoyOrder.getCustomer_lastname());
        holder.order_id.setText("# " + deliveryBoyOrder.getInvoice());

        if (mSpinnerSelectedItem.containsKey(position)) {
            holder.delivery.setSelection(mSpinnerSelectedItem.get(position));
        }

        deliveryBoyLists = deliveryDBManager.getAllDeliveryBoy();

     //   for (int i = 0; i < deliveryBoyLists.size(); i++) {
            for(DeliveryBoyList deliveryBoyList : deliveryBoyLists){
                deliveryValue.add(deliveryBoyList.getFirstname()+" "+deliveryBoyList.getLastname());
            }

     //   }

        Log.d("delivery boy id...",deliveryBoyOrder.getDeliveryBoyId());

        deliveryValue.add(0, "Select Delivery boy");

        final ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text_layout, deliveryValue);
        holder.delivery.setAdapter(statusAdapter);



        if (selectionSpinner.get(position) != null ) {
            holder.delivery.setSelection(selectionSpinner.get(position));
        }


        holder.delivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("name value ....",adapterView.getSelectedItem().toString());
                StringTokenizer st = new StringTokenizer(adapterView.getSelectedItem().toString(), " ");
                String firstname = st.nextToken();
                String lastname = st.nextToken();

                firstName = firstname;
                lastName = lastname;

                for(DeliveryBoyList deliveryBoyList : deliveryBoyLists){
                    if(deliveryBoyList.getFirstname().equalsIgnoreCase(firstName) && deliveryBoyList.getLastname().equalsIgnoreCase(lastName)){
                        if (!adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Delivery boy")) {
                            DeliveryboyListAdapter.HashMap.put(deliveryBoyOrder.getInvoice_id(),deliveryBoyList.getEntityId());
                            selectionSpinner.put(position,i);
                        }
                        else {
                            DeliveryboyListAdapter.HashMap.remove(deliveryBoyOrder.getInvoice_id());
                        }
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        deliveryDBManager.close();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return deliveryBoyOrderArrayList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        SearchableSpinner delivery;
        TextView address;
        TextView mobile;
        TextView date;
        TextView cust_name;
        TextView order_id;

        public MyViewHolder(View itemView) {
            super(itemView);
            address =  itemView.findViewById(R.id.delivery_boy_list_address);
            mobile =  itemView.findViewById(R.id.delivery_boy_list_number);
            date  =  itemView.findViewById(R.id.delivery_boy_list_date);
            delivery  =  itemView.findViewById(R.id.filter_delivery_boy);
            cust_name = itemView.findViewById(R.id.delivery_boy_list_name);
            order_id = itemView.findViewById(R.id.delivery_boy_list_order);
        }
    }
    public DeliveryBoyOrder getWordAtPosition(int position) {
        return deliveryBoyOrderArrayList.get(position);
    }

    public void setOnItemClickListener(UnAssignDeliveryboyListAdapter.ClickListener clickListener) {
        UnAssignDeliveryboyListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
