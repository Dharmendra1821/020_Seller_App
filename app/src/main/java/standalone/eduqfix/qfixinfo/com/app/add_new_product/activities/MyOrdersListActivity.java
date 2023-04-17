package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.MyOrderProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MainRequest;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModel;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModelNew;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.ProductDetailModel;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.AddProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.AddProductListingModel;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SKURequest;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class MyOrdersListActivity extends AppCompatActivity implements MyOrderProductListAdapter.ClickListener, View.OnClickListener {

    ArrayList<MyOrderListModelNew> myOrderListModels;
    ArrayList<ProductDetailModel> productDetailModels;
    MyOrderProductListAdapter myOrderProductListAdapter;
    MyOrderListModelNew myOrderListModel;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    int sellerId;
    LinearLayoutManager linearLayoutManager;
    int page;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    RelativeLayout bottomLayout;
    ProgressDialog progressDialog;
    SearchableSpinner status,payment_status,mode_payment;
    ArrayList<String> statusValue,paymentStatus,modeOfPayment;
    String statusFilter,paymentStatusFilter,modePaymentStatusFilter;
    Button search;
    int flag;
    String billingStreet,shippingStreet;

    String paymentMethod;
    String paymentStatus1;
    String billingcity;
    String billingpostcode;
    String billingtelephone;
    String billingstate_code;
    String shippingcity;
    String shippingpostcode;
    String shippingtelephone;
    String shippingstate_code;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_list);

        getSupportActionBar().setTitle("My Orders");

        progressDialog = new ProgressDialog(MyOrdersListActivity.this);
        recyclerView = findViewById(R.id.my_order_list_recyclerview);
        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
        bottomLayout =  findViewById(R.id.loadItemsLayout_recyclerView);
        status = findViewById(R.id.filter_status);
        payment_status = findViewById(R.id.filter_payment);
        mode_payment = findViewById(R.id.filter_mode);
        search = findViewById(R.id.my_orders_search);

        statusValue   = new ArrayList<>();
        paymentStatus = new ArrayList<>();
        modeOfPayment = new ArrayList<>();

        search.setOnClickListener(this);

        page = 0;

        flag = 0;
        linearLayoutManager = new LinearLayoutManager(MyOrdersListActivity.this);
        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        getOrderListNew(String.valueOf(sellerId), String.valueOf(page));

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
                            if(flag==0){
                                getOrderListNew1(String.valueOf(sellerId), String.valueOf(page));
                            }
//                            else {
//                           //    getOrderListNewFilter1(String.valueOf(sellerId),String.valueOf(page));
//                            }

                        }
                    }
                }
            }
        });

        myOrderProductListAdapter = new MyOrderProductListAdapter();
        myOrderProductListAdapter.setOnItemClickListener(this);

        statusValue.add("Status");
        statusValue.add("processing");
        statusValue.add("pending");
        statusValue.add("delivered");
        statusValue.add("complete");
        statusValue.add("delivered_at_institute");
        statusValue.add("closed");

        paymentStatus.add("Payment Status");
        paymentStatus.add("Paid");
        paymentStatus.add("UnPaid");

        paymentStatus.add("Payment Mode");
        modeOfPayment.add("Online Payment");
        modeOfPayment.add("Bank Transfer Payment");
        modeOfPayment.add("Send Payment Link");
        modeOfPayment.add("Cash on Delivery");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(MyOrdersListActivity.this, R.layout.spinner_text_layout, statusValue);
        status.setAdapter(statusAdapter);

        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<String>(MyOrdersListActivity.this, R.layout.spinner_text_layout, paymentStatus);
        payment_status.setAdapter(paymentAdapter);

        ArrayAdapter<String> paymentmodeAdapter = new ArrayAdapter<String>(MyOrdersListActivity.this, R.layout.spinner_text_layout, modeOfPayment);
        mode_payment.setAdapter(paymentmodeAdapter);


//        status.setOnTouchListener(new View.OnTouchListener(){
//            @Override
//            public boolean onTouch(View v, MotionEvent event){
//                status.showDropDown();
//                return false;
//            }
//        });
//
//        payment_status.setOnTouchListener(new View.OnTouchListener(){
//            @Override
//            public boolean onTouch(View v, MotionEvent event){
//                payment_status.showDropDown();
//                return false;
//            }
//        });
//
//        mode_payment.setOnTouchListener(new View.OnTouchListener(){
//            @Override
//            public boolean onTouch(View v, MotionEvent event){
//                mode_payment.showDropDown();
//                return false;
//            }
//        });

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusFilter =  parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        payment_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentStatusFilter =  parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mode_payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modePaymentStatusFilter =  parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        mode_payment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                modePaymentStatusFilter = adapterView.getItemAtPosition(i).toString();
//            }
//        });



    }

    public void getOrderListNew(final String sellerId,final String page){

            String shopUrl  = sharedPreferences.getString("shop_url",null);

         //  String url ="https://"+shopUrl+Constant.BASE_URL+"customapi/getOrdersList";

        String url = Constant.REVAMP_URL1+"customapi/getOrdersList.php?seller_id="+sellerId+"&page="+page+"&limit=20";

            url = url.replace(" ", "%20");
            url = url.replace("\n", "%0A");
            Log.e("Url...", "" + url);

            progressDialog.show();
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("Loading My Orders.......");

           JSONObject jsonObject1 = new JSONObject();
           JSONObject request = new JSONObject();
            try {
            jsonObject1.put("seller_id", sellerId);
            jsonObject1.put("limit", "1");
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
            final String mRequestBody = request.toString();

            StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);
                    try {
                        myOrderListModels = new ArrayList<>();

                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String orderId = jsonObject1.optString("order_id");
                            String entityId = jsonObject1.optString("entity_id");
                            String totalQtyOrdered = jsonObject1.getString("totalQtyOrdered");
                            String grandTotal = jsonObject1.getString("grandTotal");
                            String shippingAmount = jsonObject1.getString("shippingAmount");
                            String taxAmount = jsonObject1.getString("taxAmount");
                            String weight = jsonObject1.getString("weight");
                            String increment_id = jsonObject1.getString("increment_id");
                            String created_at = jsonObject1.getString("created_at");
                            String subtotal = jsonObject1.optString("subtotal");
                            String state = jsonObject1.getString("state");
                            String status = jsonObject1.getString("status");
                            String invoiceId = jsonObject1.getString("invoice_id");
                            String shippingDescription = jsonObject1.getString("shippingDescription");
                            String shippingMethod = jsonObject1.getString("shippingMethod");
                            String shipment_id = jsonObject1.getString("shipment_id");
                            String carrier_name = jsonObject1.getString("carrier_name");
                            String tracking_number = jsonObject1.getString("tracking_number");

                            productDetailModels = new ArrayList<>();
                            JSONArray productDetails = jsonObject1.getJSONArray("productDetails");

                            for (int j = 0; j < productDetails.length(); j++) {
                                JSONObject jsonObject = productDetails.getJSONObject(j);

                                String sku = jsonObject.getString("sku");
                                String productName = jsonObject.getString("productName");
                                String productPrice = jsonObject.getString("productPrice");
                                String productType = jsonObject.getString("productType");

                                productDetailModels.add(new ProductDetailModel(sku,productName,productPrice,productType));

                            }

                            JSONObject customerDetail = jsonObject1.optJSONObject("customerDetails");

                            String custFirstName = null;
                            String custLastName = null;
                            String custEmail = null;

                            if(customerDetail != null){
                                 custFirstName =  customerDetail.optString("customer_firstname");
                                 custLastName  =  customerDetail.optString("customer_lastname");
                                 custEmail     =  customerDetail.optString("customer_email");

                            }

                            JSONObject paymentDetail = jsonObject1.optJSONObject("paymentDetails");
                            if(paymentDetail!=null){
                                 paymentMethod = paymentDetail.optString("methodTitle");
                                 paymentStatus1 = paymentDetail.optString("paymentModeStatus");
                            }


                            JSONObject billingDetail = jsonObject1.optJSONObject("billingDetails");
                            if(billingDetail!=null){
                                billingcity      = billingDetail.optString("billingcity");
                                billingpostcode  = billingDetail.optString("billingpostcode");
                                billingtelephone = billingDetail.optString("billingtelephone");
                                billingstate_code = billingDetail.optString("billingstate_code");

                                JSONArray billingstreet = billingDetail.optJSONArray("billingstreet");
                                for (int ii=0; ii<billingstreet.length(); ii++) {
                                    billingStreet = billingstreet.get(0).toString();
                                }
                            }


                            JSONObject shippingDetail = jsonObject1.optJSONObject("shippingDetails");
                            if(shippingDetail!=null){
                                 shippingcity       = shippingDetail.optString("shippingcity");
                                 shippingpostcode   = shippingDetail.optString("shippingpostcode");
                                 shippingtelephone  = shippingDetail.optString("shippingtelephone");
                                 shippingstate_code = shippingDetail.optString("shippingstate_code");

                                JSONArray shippingstreet = shippingDetail.optJSONArray("shippingstreet");
                                for (int ii=0; ii<shippingstreet.length(); ii++) {
                                    shippingStreet = shippingstreet.get(0).toString();
                                }
                            }


                            myOrderListModels.add(new MyOrderListModelNew(orderId,entityId,totalQtyOrdered,grandTotal,shippingAmount,taxAmount,weight,
                                    increment_id,created_at,subtotal,state,status,custFirstName,custLastName,custEmail,
                                    paymentMethod,paymentStatus1,billingcity,billingpostcode,billingtelephone,billingstate_code,
                                    shippingcity,shippingpostcode,shippingtelephone,shippingstate_code,billingStreet,shippingStreet,invoiceId,
                                    shippingDescription,shippingMethod,productDetailModels,
                                    shipment_id,carrier_name,tracking_number));



                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    myOrderProductListAdapter = new MyOrderProductListAdapter(MyOrdersListActivity.this,myOrderListModels);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(myOrderProductListAdapter);
                    myOrderProductListAdapter.notifyDataSetChanged();

                    progressDialog.dismiss();
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
            // Adding request to request queue
          req.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MaintainRequestQueue.getInstance(MyOrdersListActivity.this).addToRequestQueue(req, "tag");



    }

    public void getOrderListNew1(final String sellerId,final String page){

        String shopUrl  = sharedPreferences.getString("shop_url",null);

        String url = Constant.REVAMP_URL1+"customapi/getOrdersList.php?seller_id="+sellerId+"&page="+page+"&limit=20";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);
       /* progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading My Orders.......");*/


        bottomLayout.setVisibility(View.VISIBLE);

        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                  //  myOrderListModels = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String orderId = jsonObject1.optString("order_id");
                        String entityId = jsonObject1.optString("entity_id");
                        String totalQtyOrdered = jsonObject1.getString("totalQtyOrdered");
                        String grandTotal = jsonObject1.getString("grandTotal");
                        String shippingAmount = jsonObject1.getString("shippingAmount");
                        String taxAmount = jsonObject1.getString("taxAmount");
                        String weight = jsonObject1.getString("weight");
                        String increment_id = jsonObject1.getString("increment_id");
                        String created_at = jsonObject1.getString("created_at");
                        String subtotal = jsonObject1.optString("subtotal");
                        String state = jsonObject1.getString("state");
                        String status = jsonObject1.getString("status");
                        String invoiceId = jsonObject1.getString("invoice_id");
                        String shipment_id = jsonObject1.getString("shipment_id");
                        String carrier_name = jsonObject1.getString("carrier_name");
                        String tracking_number = jsonObject1.getString("tracking_number");

                        String shippingDescription = jsonObject1.getString("shippingDescription");
                        String shippingMethod = jsonObject1.getString("shippingMethod");

                        JSONArray productDetails = jsonObject1.getJSONArray("productDetails");

                        for (int j = 0; j < productDetails.length(); j++) {
                            JSONObject jsonObject = productDetails.getJSONObject(j);

                            String sku = jsonObject.getString("sku");
                            String productName = jsonObject.getString("productName");
                            String productPrice = jsonObject.getString("productPrice");
                            String productType = jsonObject.getString("productType");

                            productDetailModels.add(new ProductDetailModel(sku,productName,productPrice,productType));

                        }
                        String custFirstName = null;
                        String custLastName = null;
                        String custEmail = null;
                        JSONObject customerDetail = jsonObject1.optJSONObject("customerDetails");

                        if(customerDetail != null){
                            custFirstName   = customerDetail.optString("customer_firstname");
                            custLastName    = customerDetail.optString("customer_lastname");
                            custEmail       = customerDetail.optString("customer_email");

                        }



                        JSONObject paymentDetail = jsonObject1.optJSONObject("paymentDetails");
                        if(paymentDetail!=null){
                            paymentMethod = paymentDetail.optString("methodTitle");
                            paymentStatus1 = paymentDetail.optString("paymentModeStatus");
                        }


                        JSONObject billingDetail = jsonObject1.optJSONObject("billingDetails");
                        if(billingDetail!=null){
                            billingcity      = billingDetail.optString("billingcity");
                            billingpostcode  = billingDetail.optString("billingpostcode");
                            billingtelephone = billingDetail.optString("billingtelephone");
                            billingstate_code = billingDetail.optString("billingstate_code");

                            JSONArray billingstreet = billingDetail.optJSONArray("billingstreet");
                            for (int ii=0; ii<billingstreet.length(); ii++) {
                                billingStreet = billingstreet.get(0).toString();
                            }
                        }


                        JSONObject shippingDetail = jsonObject1.optJSONObject("shippingDetails");
                        if(shippingDetail!=null){
                            shippingcity       = shippingDetail.optString("shippingcity");
                            shippingpostcode   = shippingDetail.optString("shippingpostcode");
                            shippingtelephone  = shippingDetail.optString("shippingtelephone");
                            shippingstate_code = shippingDetail.optString("shippingstate_code");

                            JSONArray shippingstreet = shippingDetail.optJSONArray("shippingstreet");
                            for (int ii=0; ii<shippingstreet.length(); ii++) {
                                shippingStreet = shippingstreet.get(0).toString();
                            }
                        }


                        myOrderListModels.add(new MyOrderListModelNew(orderId,entityId,totalQtyOrdered,grandTotal,shippingAmount,taxAmount,weight,
                                increment_id,created_at,subtotal,state,status,custFirstName,custLastName,custEmail,
                                paymentMethod,paymentStatus1,billingcity,billingpostcode,billingtelephone,billingstate_code,
                                shippingcity,shippingpostcode,shippingtelephone,shippingstate_code,billingStreet,shippingStreet,invoiceId,
                                shippingDescription,shippingMethod,productDetailModels,
                                shipment_id,carrier_name,tracking_number));



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


              /*  myOrderProductListAdapter = new MyOrderProductListAdapter(MyOrdersListActivity.this,myOrderListModels);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(myOrderProductListAdapter);*/
                myOrderProductListAdapter.notifyDataSetChanged();
                loading = true;
                bottomLayout.setVisibility(View.GONE);
             //   progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("TAG", "Error: " + error.getMessage());
            //    progressDialog.dismiss();
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
        // Adding request to request queue
        MaintainRequestQueue.getInstance(MyOrdersListActivity.this).addToRequestQueue(req, "tag");



    }

    public void getOrderListNewFilter(final String sellerId,final String limit){

        String shopUrl  = sharedPreferences.getString("shop_url",null);

        String url ="https://"+shopUrl+Constant.BASE_URL+"customapi/getOrdersList";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading My Orders.......");

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("seller_id", sellerId);
            jsonObject1.put("limit", limit);
            jsonObject1.put("status",statusFilter);
            jsonObject1.put("payment_status",paymentStatusFilter);
            jsonObject1.put("mode_of_payment",modePaymentStatusFilter);
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
                    myOrderListModels = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String orderId = jsonObject1.optString("order_id");
                        String entityId = jsonObject1.optString("entity_id");
                        String totalQtyOrdered = jsonObject1.getString("totalQtyOrdered");
                        String grandTotal = jsonObject1.getString("grandTotal");
                        String shippingAmount = jsonObject1.getString("shippingAmount");
                        String taxAmount = jsonObject1.getString("taxAmount");
                        String weight = jsonObject1.getString("weight");
                        String increment_id = jsonObject1.getString("increment_id");
                        String created_at = jsonObject1.getString("created_at");
                        String subtotal = jsonObject1.optString("subtotal");
                        String state = jsonObject1.getString("state");
                        String status = jsonObject1.getString("status");
                        String invoiceId = jsonObject1.getString("invoice_id");
                        String shipment_id = jsonObject1.getString("shipment_id");
                        String carrier_name = jsonObject1.getString("carrier_name");
                        String tracking_number = jsonObject1.getString("tracking_number");

                        String shippingDescription = jsonObject1.getString("shippingDescription");
                        String shippingMethod = jsonObject1.getString("shippingMethod");

                        JSONArray productDetails = jsonObject1.getJSONArray("productDetails");

                        for (int j = 0; j < productDetails.length(); j++) {
                            JSONObject jsonObject = productDetails.getJSONObject(j);

                            String sku = jsonObject.getString("sku");
                            String productName = jsonObject.getString("productName");
                            String productPrice = jsonObject.getString("productPrice");
                            String productType = jsonObject.getString("productType");
                            productDetailModels.add(new ProductDetailModel(sku,productName,productPrice,productType));

                        }
                        JSONObject customerDetail = jsonObject1.optJSONObject("customerDetails");

                        String custFirstName = customerDetail.optString("customer_firstname");
                        String custLastName = customerDetail.optString("customer_lastname");
                        String custEmail = customerDetail.optString("customer_email");


                        JSONObject paymentDetail = jsonObject1.optJSONObject("paymentDetails");
                        if(paymentDetail!=null){
                            paymentMethod = paymentDetail.optString("methodTitle");
                            paymentStatus1 = paymentDetail.optString("paymentModeStatus");
                        }


                        JSONObject billingDetail = jsonObject1.optJSONObject("billingDetails");
                        if(billingDetail!=null){
                            billingcity      = billingDetail.optString("billingcity");
                            billingpostcode  = billingDetail.optString("billingpostcode");
                            billingtelephone = billingDetail.optString("billingtelephone");
                            billingstate_code = billingDetail.optString("billingstate_code");

                            JSONArray billingstreet = billingDetail.optJSONArray("billingstreet");
                            for (int ii=0; ii<billingstreet.length(); ii++) {
                                billingStreet = billingstreet.get(0).toString();
                            }
                        }


                        JSONObject shippingDetail = jsonObject1.optJSONObject("shippingDetails");
                        if(shippingDetail!=null){
                            shippingcity       = shippingDetail.optString("shippingcity");
                            shippingpostcode   = shippingDetail.optString("shippingpostcode");
                            shippingtelephone  = shippingDetail.optString("shippingtelephone");
                            shippingstate_code = shippingDetail.optString("shippingstate_code");

                            JSONArray shippingstreet = shippingDetail.optJSONArray("shippingstreet");
                            for (int ii=0; ii<shippingstreet.length(); ii++) {
                                shippingStreet = shippingstreet.get(0).toString();
                            }
                        }


                        myOrderListModels.add(new MyOrderListModelNew(orderId,entityId,totalQtyOrdered,grandTotal,shippingAmount,taxAmount,weight,
                                increment_id,created_at,subtotal,state,status,custFirstName,custLastName,custEmail,
                                paymentMethod,paymentStatus1,billingcity,billingpostcode,billingtelephone,billingstate_code,
                                shippingcity,shippingpostcode,shippingtelephone,shippingstate_code,billingStreet,shippingStreet,invoiceId,
                                shippingDescription,shippingMethod,productDetailModels,
                                shipment_id,carrier_name,tracking_number));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                myOrderProductListAdapter = new MyOrderProductListAdapter(MyOrdersListActivity.this,myOrderListModels);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(myOrderProductListAdapter);
                myOrderProductListAdapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        MaintainRequestQueue.getInstance(MyOrdersListActivity.this).addToRequestQueue(req, "tag");



    }

    public void getOrderListNewFilter1(final String sellerId,final String limit){

        String shopUrl  = sharedPreferences.getString("shop_url",null);

        String url ="https://"+shopUrl+Constant.BASE_URL+"customapi/getOrdersList";
        url = url.replace(" ", "%20");

        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

    /*    progressDialog.show();
          progressDialog.setTitle("Please wait...");
          progressDialog.setMessage("Loading My Orders.......");

          */

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("seller_id", sellerId);
            jsonObject1.put("limit", limit);
            jsonObject1.put("status",statusFilter);
            jsonObject1.put("payment_status",paymentStatusFilter);
            jsonObject1.put("mode_of_payment",modePaymentStatusFilter);
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bottomLayout.setVisibility(View.VISIBLE);
        final String mRequestBody = request.toString();
        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String orderId = jsonObject1.optString("order_id");
                        String entityId = jsonObject1.optString("entity_id");
                        String totalQtyOrdered = jsonObject1.getString("totalQtyOrdered");
                        String grandTotal = jsonObject1.getString("grandTotal");
                        String shippingAmount = jsonObject1.getString("shippingAmount");
                        String taxAmount = jsonObject1.getString("taxAmount");
                        String weight = jsonObject1.getString("weight");
                        String increment_id = jsonObject1.getString("increment_id");
                        String created_at = jsonObject1.getString("created_at");
                        String subtotal = jsonObject1.optString("subtotal");
                        String state = jsonObject1.getString("state");
                        String status = jsonObject1.getString("status");
                        String invoiceId = jsonObject1.getString("invoice_id");
                        String shipment_id = jsonObject1.getString("shipment_id");
                        String carrier_name = jsonObject1.getString("carrier_name");
                        String tracking_number = jsonObject1.getString("tracking_number");

                        String shippingDescription = jsonObject1.getString("shippingDescription");
                        String shippingMethod = jsonObject1.getString("shippingMethod");

                        JSONArray productDetails = jsonObject1.getJSONArray("productDetails");

                        for (int j = 0; j < productDetails.length(); j++) {
                            JSONObject jsonObject = productDetails.getJSONObject(j);

                            String sku = jsonObject.getString("sku");
                            String productName = jsonObject.getString("productName");
                            String productPrice = jsonObject.getString("productPrice");
                            String productType = jsonObject.getString("productType");

                            productDetailModels.add(new ProductDetailModel(sku,productName,productPrice,productType));

                        }
                        JSONObject customerDetail = jsonObject1.optJSONObject("customerDetails");

                        String custFirstName = customerDetail.optString("customer_firstname");
                        String custLastName = customerDetail.optString("customer_lastname");
                        String custEmail = customerDetail.optString("customer_email");


                        JSONObject paymentDetail = jsonObject1.optJSONObject("paymentDetails");
                        if(paymentDetail!=null){
                            paymentMethod = paymentDetail.optString("methodTitle");
                            paymentStatus1 = paymentDetail.optString("paymentModeStatus");
                        }


                        JSONObject billingDetail = jsonObject1.optJSONObject("billingDetails");
                        if(billingDetail!=null){
                            billingcity      = billingDetail.optString("billingcity");
                            billingpostcode  = billingDetail.optString("billingpostcode");
                            billingtelephone = billingDetail.optString("billingtelephone");
                            billingstate_code = billingDetail.optString("billingstate_code");

                            JSONArray billingstreet = billingDetail.optJSONArray("billingstreet");
                            for (int ii=0; ii<billingstreet.length(); ii++) {
                                billingStreet = billingstreet.get(0).toString();
                            }
                        }


                        JSONObject shippingDetail = jsonObject1.optJSONObject("shippingDetails");
                        if(shippingDetail!=null){
                            shippingcity       = shippingDetail.optString("shippingcity");
                            shippingpostcode   = shippingDetail.optString("shippingpostcode");
                            shippingtelephone  = shippingDetail.optString("shippingtelephone");
                            shippingstate_code = shippingDetail.optString("shippingstate_code");

                            JSONArray shippingstreet = shippingDetail.optJSONArray("shippingstreet");
                            for (int ii=0; ii<shippingstreet.length(); ii++) {
                                shippingStreet = shippingstreet.get(0).toString();
                            }
                        }


                        myOrderListModels.add(new MyOrderListModelNew(orderId,entityId,totalQtyOrdered,grandTotal,shippingAmount,taxAmount,weight,
                                increment_id,created_at,subtotal,state,status,custFirstName,custLastName,custEmail,
                                paymentMethod,paymentStatus1,billingcity,billingpostcode,billingtelephone,billingstate_code,
                                shippingcity,shippingpostcode,shippingtelephone,shippingstate_code,billingStreet,shippingStreet,invoiceId,
                                shippingDescription,shippingMethod,productDetailModels,
                                shipment_id,carrier_name,tracking_number));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


              /*  myOrderProductListAdapter = new MyOrderProductListAdapter(MyOrdersListActivity.this,myOrderListModels);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(myOrderProductListAdapter);*/
                myOrderProductListAdapter.notifyDataSetChanged();
                loading = true;
                bottomLayout.setVisibility(View.GONE);
           //     progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("TAG", "Error: " + error.getMessage());
             //   progressDialog.dismiss();
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
        // Adding request to request queue
        MaintainRequestQueue.getInstance(MyOrdersListActivity.this).addToRequestQueue(req, "tag");



    }

    @Override
    public void onItemClick(View v, int position) {
        myOrderListModel = myOrderProductListAdapter.getWordAtPosition(position);

        String sFormAddress = myOrderListModel.getBillingstreet() + "\n" +
                              myOrderListModel.getBillingcity()  + "\n" +
                              myOrderListModel.getBillingpostcode()+ "\n" +
                              myOrderListModel.getBillingstate_code();

        Intent intent = new Intent(MyOrdersListActivity.this,OrderListActivity.class);
        intent.putExtra("name",myOrderListModel.getCustomer_firstname()+" "+myOrderListModel.getCustomer_lastname());
        intent.putExtra("address",sFormAddress);
        intent.putExtra("email",myOrderListModel.getCustomer_email());
        intent.putExtra("mobile",myOrderListModel.getBillingtelephone());
        intent.putExtra("base_total",myOrderListModel.getSubtotal());
        intent.putExtra("shipping",myOrderListModel.getShippingAmount());
        intent.putExtra("tax",myOrderListModel.getTaxAmount());
        intent.putExtra("grand_total",myOrderListModel.getGrandTotal());
        intent.putExtra("invoice_id",myOrderListModel.getInvoiceId());
        intent.putExtra("status",myOrderListModel.getStatus());
        intent.putExtra("increment_id",myOrderListModel.getIncrement_id());
        intent.putExtra("shippingDesc",myOrderListModel.getShippingDescription());
        intent.putExtra("shippingMethod",myOrderListModel.getShippingMethod());
        intent.putExtra("productDetails",myOrderListModel.getProductDetailModels());
        intent.putExtra("order_id",myOrderListModel.getOrderId());
        intent.putExtra("entity_id",myOrderListModel.getEntityId());
        intent.putExtra("shipment_id",myOrderListModel.getShipment_id());
        intent.putExtra("carrier_name",myOrderListModel.getCarrier_name());
        intent.putExtra("tracking_number",myOrderListModel.getTracking_number());
        intent.putExtra("payment_method",myOrderListModel.getMethodTitle());

        Bundle b = new Bundle();
        b.putSerializable("my_order", myOrderListModel);
        intent.putExtras(b);
        startActivity(intent);
    }



    @Override
    public void onClick(View view) {
        flag = 1;
        if(statusFilter.equalsIgnoreCase("Status")){
            statusFilter = "";
        }
         if(paymentStatusFilter.equalsIgnoreCase("Payment Status")){
            paymentStatusFilter = "";
        }
        if(modePaymentStatusFilter.equalsIgnoreCase("Payment Mode")){
            modePaymentStatusFilter = "";
        }
        getOrderListNewFilter(String.valueOf(sellerId),"1");
    }
}
