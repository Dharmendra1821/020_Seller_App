package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.AssignDeliveryboyListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.DeliveryboyListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.MyOrderProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.UnAssignDeliveryboyListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyList;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyOrder;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModelNew;
import standalone.eduqfix.qfixinfo.com.app.database.DeliveryDBManager;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.ProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.BundleSizeContainer;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class DeliveryBoyOrderActivity extends AppCompatActivity implements DeliveryboyListAdapter.ClickListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    ProgressDialog progressDialog;
    ArrayList<DeliveryBoyList> deliveryBoyLists;
    int page;
    DeliveryDBManager deliveryDBManager;
    String billingStreet,shippingStreet;
    ArrayList<DeliveryBoyOrder> deliveryBoyOrderArrayList;
    DeliveryBoyOrder deliveryBoyOrder;
    DeliveryboyListAdapter deliveryboyListAdapter;
    UnAssignDeliveryboyListAdapter unAssignDeliveryboyListAdapter;
    AssignDeliveryboyListAdapter assignDeliveryboyListAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    String shippingcity;
    String shippingpostcode;
    String shippingtelephone;
    String shippingstate_code;
    String incrementId;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    RelativeLayout bottomLayout;
    Button assign;
    JSONArray jsonArray;
    JSONObject rootObject;
    String data;
    BroadcastReceiver mMessageReceiver;
    SearchableSpinner spinner;
    LinearLayout assign_layout;
    Button assign_btn,unassign_btn;
    int flag;
    SharedPreferences sharedPreferences;
    int sellerId;

    private RadioGroup radioroup;
    private RadioButton radioButton;

    public static ArrayList<String> invoiceId = new ArrayList<>();
    public static ArrayList<String> entityId = new ArrayList<>();

    public ArrayList<String> getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(ArrayList<String> invoiceId) {
        this.invoiceId = invoiceId;
    }

    public ArrayList<String> getEntityId() {
        return entityId;
    }

    public void setEntityId(ArrayList<String> entityId) {
        this.entityId = entityId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_order);

        getSupportActionBar().setTitle("Assigned Delivery");
        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;


        progressDialog = new ProgressDialog(DeliveryBoyOrderActivity.this);
        page = 1;
        deliveryDBManager = new DeliveryDBManager(DeliveryBoyOrderActivity.this);
        deliveryDBManager.open();

        linearLayoutManager = new LinearLayoutManager(DeliveryBoyOrderActivity.this);
        recyclerView = findViewById(R.id.delivery_boy_recyclerview);
        bottomLayout = findViewById(R.id.loadItemsLayout_recyclerView);
        assign       = findViewById(R.id.assign_delivery_boy);
       // radioroup    = findViewById(R.id.radioGroup);

        flag = 1;

     //   radioroup.setOnCheckedChangeListener(this);


        getOrderListNew(String.valueOf(sellerId),"1");
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            page++;
                            if(flag==1){
                              //  getOrderListNew1(String.valueOf(sellerId), String.valueOf(page));
                            }
                            else if(flag==2){
                           //     getAssignOrder1(String.valueOf(sellerId),"1",String.valueOf(page));
                            }
                            else {
                            //    getUnAssignOrder1(String.valueOf(sellerId),"0",String.valueOf(page));
                            }




                        }
                    }
                }
            }
        });


        deliveryboyListAdapter = new DeliveryboyListAdapter();
        deliveryboyListAdapter.setOnItemClickListener(this);

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(DeliveryboyListAdapter.HashMap.size()>0){
                    System.out.println(" All the key value pairs " + DeliveryboyListAdapter.HashMap);
                    Iterator myVeryOwnIterator = DeliveryboyListAdapter.HashMap.keySet().iterator();
                    while(myVeryOwnIterator.hasNext()) {

                        jsonArray = new JSONArray();
                        JSONObject jsonObject;
                        for(int i=0;i<DeliveryboyListAdapter.HashMap.size();i++){
                            String key   = (String) myVeryOwnIterator.next();
                            String value = DeliveryboyListAdapter.HashMap.get(key);
                            String[] dateSplit = value.split(":");
                            System.out.println("dateSplit[0]:" + dateSplit[0]);
                            System.out.println("dateSplit[1]:" + dateSplit[1]);

                            jsonObject = new JSONObject();
                            try {
                                jsonObject.putOpt("invoice_id", key);
                                jsonObject.putOpt("entity_id", dateSplit[0]);
                                jsonObject.putOpt("order_id", dateSplit[1]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(jsonObject);
                            JSONObject request0bj = new JSONObject();
                            try {
                                request0bj.put("request", jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            data = request0bj.toString();
                        }
                    }
                    Log.d("json array",data);
                    assignDeliveryBoy();
                }
                else {
                    Toast.makeText(DeliveryBoyOrderActivity.this,"Please select delivery boy",Toast.LENGTH_LONG).show();
                }
            }
        });

       /* incrementId = getIntent().getStringExtra("increment_id");
        Log.d("increment_id,,,,",incrementId);*/

    }

    public void getDeliveryBoy(final String sellerId,final String limit){

     //   String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/getDeliveryBoyList";
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"customapi/getDeliveryBoyList.php?seller_id="+sellerId;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading My Orders.......");

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("seller_id", sellerId);
            jsonObject1.put("limit", limit);
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request.toString();

        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    deliveryBoyLists = new ArrayList<>();
                    deliveryDBManager.delete();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String entityId = jsonObject.getString("entity_id");
                        String email = jsonObject.getString("email");
                        String firstname = jsonObject.getString("firstname");
                        String lastname = jsonObject.getString("lastname");
                        String contact = jsonObject.getString("contact");
                        String status = jsonObject.getString("status");

                        deliveryDBManager.insert(new DeliveryBoyList(i,entityId,email,firstname,lastname,contact,status));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progressDialog.dismiss();


            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                getOrderListNew(sellerId,"1");
            }

        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    Log.e("Url...", "" + mRequestBody);
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };
        MaintainRequestQueue.getInstance(DeliveryBoyOrderActivity.this).addToRequestQueue(req, "tag");



    }


    public void getOrderListNew(final String sellerId,final String page){

     //   String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/getDeliveryOderList?seller_id="+sellerId+"&limit="+page;

        String shopUrl  = sharedPreferences.getString("shop_url",null);
      //  String url ="https://"+shopUrl+ Constant.BASE_URL+"customapi/getDeliveryOderList?seller_id="+sellerId+"&limit="+page;

        String url = Constant.REVAMP_URL1+"customapi/getDeliveryOrderList.php?seller_id="+sellerId;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);
        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading My Orders.......");
        progressDialog.setCancelable(false);


        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    deliveryBoyOrderArrayList = new ArrayList<>();

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String orderId = jsonObject1.optString("order_id");
                        String totalQtyOrdered = jsonObject1.optString("totalQtyOrdered");
                        String grandTotal = jsonObject1.optString("grandTotal");
                        String shippingAmount = jsonObject1.optString("shippingAmount");
                        String taxAmount = jsonObject1.optString("taxAmount");
                        String weight = jsonObject1.optString("weight");
                        String increment_id = jsonObject1.optString("increment_id");
                        String created_at = jsonObject1.optString("created_at");
                        String subtotal = jsonObject1.optString("subtotal");
                        String state = jsonObject1.optString("state");
                        String status = jsonObject1.optString("status");
                        String invoiceId = jsonObject1.optString("invoice_id");
                        String invoice = jsonObject1.optString("invoice");
                        String deliveryBoyId = jsonObject1.optString("deliveryboy_id");

                        JSONObject customerDetail = jsonObject1.optJSONObject("customerDetails");

                        String custFirstName = null;
                        String custLastName = null;
                        String custEmail = null;
                        if(customerDetail != null){
                             custFirstName   = customerDetail.optString("customer_firstname");
                             custLastName    = customerDetail.optString("customer_lastname");
                             custEmail       = customerDetail.optString("customer_email");

                        }


                        JSONObject paymentDetail = jsonObject1.optJSONObject("paymentDetails");
                        String paymentMethod = null;
                        String paymentStatus = null;
                        if(paymentDetail != null){
                             paymentMethod = paymentDetail.optString("methodTitle");
                             paymentStatus = paymentDetail.optString("paymentModeStatus");

                        }

                        JSONObject billingDetail = jsonObject1.optJSONObject("billingDetails");

                        String billingcity = null;
                        String billingpostcode = null;
                        String billingtelephone = null;
                        String billingstate_code = null;
                        if(billingDetail != null){
                             billingcity        = billingDetail.optString("billingcity");
                             billingpostcode    = billingDetail.optString("billingpostcode");
                             billingtelephone   = billingDetail.optString("billingtelephone");
                             billingstate_code  = billingDetail.optString("billingstate_code");

                            JSONArray billingstreet = billingDetail.optJSONArray("billingstreet");

                            if(billingstreet.length() > 0 ){
                                for (int ii=0; ii<billingstreet.length(); ii++) {
                                    billingStreet = billingstreet.get(0).toString();
                                }

                            }
                        }

                        JSONObject shippingDetail = jsonObject1.optJSONObject("shippingDetails");

                        if(shippingDetail!=null){
                             shippingcity       = shippingDetail.optString("shippingcity");
                             shippingpostcode   = shippingDetail.optString("shippingpostcode");
                             shippingtelephone  = shippingDetail.optString("shippingtelephone");
                             shippingstate_code = shippingDetail.optString("shippingstate_code");

                            JSONArray shippingstreet = shippingDetail.optJSONArray("shippingstreet");
                            if(shippingstreet.length() > 0) {
                                for (int ii = 0; ii < shippingstreet.length(); ii++) {
                                    shippingStreet = shippingstreet.get(0).toString();
                                }
                            }

                        }

                        deliveryBoyOrderArrayList.add(new DeliveryBoyOrder(orderId,totalQtyOrdered,grandTotal,shippingAmount,taxAmount,weight,
                                increment_id,created_at,subtotal,state,status,custFirstName,custLastName,custEmail,
                                paymentMethod,paymentStatus,billingcity,billingpostcode,billingtelephone,billingstate_code,
                                shippingcity,shippingpostcode,shippingtelephone,shippingstate_code,billingStreet,shippingStreet,
                                invoiceId,i,deliveryBoyId,invoice
                        ));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                deliveryboyListAdapter = new DeliveryboyListAdapter(DeliveryBoyOrderActivity.this,deliveryBoyOrderArrayList);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(deliveryboyListAdapter);
                deliveryboyListAdapter.notifyDataSetChanged();
                assign.setVisibility(View.VISIBLE);
               // assign_layout.setVisibility(View.VISIBLE);
                progressDialog.dismiss();

                getDeliveryBoy(String.valueOf(sellerId),"1");

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }

        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }



        };
        // Adding request to request queue
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(DeliveryBoyOrderActivity.this).addToRequestQueue(req, "tag");



    }




    //////////////////////////////////


    public void assignDeliveryBoy(){

     //   String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/assignDeliveryBoy";
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"customapi/assignDeliveryBoy.php";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Assigning delivery boy.......");

        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    Toast.makeText(DeliveryBoyOrderActivity.this,"Delivery boy assigned successfully",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DeliveryBoyOrderActivity.this,DeliveryManagementActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DeliveryBoyOrderActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }

        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    Log.e("Url...", "" + data);
                    return data == null ? null : data.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", data, "utf-8");
                    return null;
                }
            }
            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };
        MaintainRequestQueue.getInstance(DeliveryBoyOrderActivity.this).addToRequestQueue(req, "tag");



    }


    @Override
    public void onItemClick(View v, int position) {
       /* deliveryBoyOrder = deliveryboyListAdapter.getWordAtPosition(position);
          spinner.setVisibility(View.VISIBLE);*/

       Log.d("value.......","coming here");


    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        RadioButton rb =  radioGroup.findViewById(checkedId);
        if(rb.getText().toString().equalsIgnoreCase("Un Assigned")){
            page=1;
           // getUnAssignOrder(String.valueOf(sellerId),"0", String.valueOf(page));
        }
        else {
           // getAssignOrder(String.valueOf(sellerId),"1", String.valueOf(page));
            page=1;
            flag=2;
        }
    }
}
