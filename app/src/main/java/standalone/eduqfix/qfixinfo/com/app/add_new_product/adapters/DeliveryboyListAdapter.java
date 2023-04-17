package standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DeliveryBoyOrderActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.MyOrdersListActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyList;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyOrder;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModelNew;
import standalone.eduqfix.qfixinfo.com.app.customer.model.State;
import standalone.eduqfix.qfixinfo.com.app.database.DeliveryDBManager;

public class DeliveryboyListAdapter extends RecyclerView.Adapter<DeliveryboyListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<DeliveryBoyOrder> deliveryBoyOrderArrayList;
    MyViewHolder viewholder;
    int pos = 0;
    int mSelectedItem;
    SharedPreferences sharedPreferences;
    String flag;
    private static DeliveryboyListAdapter.ClickListener clickListener;
    String imageUrl;
    DeliveryDBManager deliveryDBManager;
    ArrayList<String> deliveryValue;
    ArrayList<DeliveryBoyList> deliveryBoyLists = null;
    ArrayList<DeliveryBoyList> newdeliveryBoyLists;
    public static java.util.HashMap<String,String> HashMap=new HashMap<String, String>();
    public static java.util.HashMap<Integer,Integer> selectionSpinner=new HashMap<Integer, Integer>();
    Map<Integer, Integer> mSpinnerSelectedItem = new HashMap<Integer, Integer>();
    String hello;

    String firstName;
    String lastName;
    String entityId;
    public DeliveryboyListAdapter(){

    }


    public DeliveryboyListAdapter(Context context, ArrayList<DeliveryBoyOrder> deliveryBoyOrderArrayList) {
        this.mContext = context;
        this.deliveryBoyOrderArrayList = deliveryBoyOrderArrayList;
        this.flag = flag;

        HashMap.clear();

    }

    @NonNull
    @Override
    public DeliveryboyListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_boy_order_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final DeliveryboyListAdapter.MyViewHolder holder, final int position) {

        final DeliveryBoyOrder deliveryBoyOrder = deliveryBoyOrderArrayList.get(position);
        pos = position;
        viewholder = holder;

        deliveryValue = new ArrayList<>();
      //  deliveryBoyLists = new ArrayList<>();
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
        holder.order_id.setText("# " + deliveryBoyOrder.getIncrement_id());

        if (mSpinnerSelectedItem.containsKey(position)) {
            holder.delivery.setSelection(mSpinnerSelectedItem.get(position));
        }

        deliveryBoyLists = deliveryDBManager.getAllDeliveryBoy();

            for(DeliveryBoyList deliveryBoyList : deliveryBoyLists){
                deliveryValue.add(deliveryBoyList.getFirstname()+" "+deliveryBoyList.getLastname());
        }

        deliveryValue.add(0, "Select Delivery boy");

        final ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text_layout, deliveryValue);
        holder.delivery.setAdapter(statusAdapter);

        if (selectionSpinner.get(position) != null ) {
            holder.delivery.setSelection(selectionSpinner.get(position));
        }

        newdeliveryBoyLists = deliveryDBManager.getDeliveryBoyList(deliveryBoyOrder.getDeliveryBoyId());
        for (int i = 0; i < newdeliveryBoyLists.size(); i++) {
            String name = newdeliveryBoyLists.get(i).getFirstname() + " " + newdeliveryBoyLists.get(i).getLastname();

            if(name!=null) {
                int spinnerPosition = statusAdapter.getPosition(name);
                holder.delivery.setSelection(spinnerPosition);
            }
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
                            HashMap.put(deliveryBoyOrder.getInvoice_id(),deliveryBoyList.getEntityId()+":"+deliveryBoyOrder.getOrder_id());
                            selectionSpinner.put(position,i);
                        }
                        else {
                            HashMap.remove(deliveryBoyOrder.getInvoice_id());
                            Log.d("coming eheeee","ccccccc");
                        }
                    }
                }
                hideKeyboard((Activity) mContext);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    /*    deliveryBoyLists = deliveryDBManager.getDeliveryBoyList(deliveryBoyOrder.getDeliveryBoyId());
        for (int i = 0; i < deliveryBoyLists.size(); i++) {
            String name = deliveryBoyLists.get(i).getFirstname() + " " + deliveryBoyLists.get(i).getLastname();

            if(name!=null) {
                int spinnerPosition = statusAdapter.getPosition(name);
                holder.delivery.setSelection(spinnerPosition);
            }
        }*/

        deliveryDBManager.close();

     /*   holder.delivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectionSpinner.put(position,i);
                if (!adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Delivery boy")) {

                    newdeliveryBoyLists = deliveryDBManager.getDeliveryBoyEntity(adapterView.getSelectedItem().toString());
                    String entityId = newdeliveryBoyLists.get(0).getEntityId();
                    Log.d("enityid...",entityId);
                    HashMap.put(deliveryBoyOrder.getInvoice_id(),entityId);
                }
                else {
                    HashMap.remove(deliveryBoyOrder.getInvoice_id());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

       /* deliveryBoyLists = deliveryDBManager.getDeliveryBoyList(deliveryBoyOrder.getDeliveryBoyId());
        for (int i = 0; i < deliveryBoyLists.size(); i++) {
            String name = deliveryBoyLists.get(i).getFirstname() + " " + deliveryBoyLists.get(i).getLastname();

            if(name!=null) {
                int spinnerPosition = statusAdapter.getPosition(name);
                holder.delivery.setSelection(spinnerPosition);
            }
        }

        deliveryDBManager.close();*/
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

    public void setOnItemClickListener(DeliveryboyListAdapter.ClickListener clickListener) {
        DeliveryboyListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
