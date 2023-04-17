package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DeliveryBoyOrderActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.MyProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.AddNewProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ComplaintsAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SellerCustomerListAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ComplaintsModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerListModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.config.MyApplication;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSApplication;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class CustomerListActivity extends AppCompatActivity implements SellerCustomerListAdapter.ClickListener {

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    CustomerListModel customerListModel;
    ArrayList<CustomerListModel> customerListModels;
    SellerCustomerListAdapter sellerCustomerListAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    int sellerId;
    SearchableSpinner searchableSpinner;
    ArrayList<String> statusArray;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(CustomerListActivity.this);
        recyclerView = findViewById(R.id.seller_customer_recyclerview);
        linearLayoutManager = new LinearLayoutManager(CustomerListActivity.this);

        searchableSpinner = findViewById(R.id.seller_credit_dropdown);
        statusArray   = new ArrayList<>();
        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        getCustomerList(String.valueOf(sellerId));

        sellerCustomerListAdapter = new SellerCustomerListAdapter();
        sellerCustomerListAdapter.setOnItemClickListener(this);


        statusArray.add("Actions");
        statusArray.add("Enable Credit");
        statusArray.add("Disable Credit");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(CustomerListActivity.this, R.layout.spinner_text_layout, statusArray);
        searchableSpinner.setAdapter(statusAdapter);

        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue =  parent.getSelectedItem().toString();

                if(selectedValue.equalsIgnoreCase("Enable Credit")){
                    status = 1;
                    udharActions(status);
                }
                else if(selectedValue.equalsIgnoreCase("Disable Credit")){
                    status = 0;
                    udharActions(status);
                }
                else {
                  //  Toast.makeText(CustomerListActivity.this, "Select Action", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getCustomerList(final String sellerId){
        String tag_json_obj = "json_obj_req2";
        String shopUrl  = sharedPreferences.getString("shop_url",null);
    //    String url = "https://"+shopUrl + BASE_URL+"carts/mine/qfix-udhar/seller/customers/"+sellerId;

        String url = Constant.REVAMP_URL1+"udhar/seller_customers.php?sellerId="+sellerId;

     //   String url = "https://standalonetest.eduqfix.com/rest/V1/carts/mine/qfix-udhar/seller/customers/"+sellerId;

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading data.......");
        progressDialog.setCancelable(false);

        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {
                    customerListModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if(status){

                        JSONObject message = jsonObject.getJSONObject("message");
                        JSONArray collection = message.getJSONArray("collection");
                        if(collection.length() > 0){
                            for (int i=0; i < collection.length(); i++){
                                JSONObject jsonObject1 = collection.getJSONObject(i);

                                String firstName = jsonObject1.getString("first_name");
                                String lastName = jsonObject1.getString("first_name");
                                String email = jsonObject1.getString("email");
                                String mobile = jsonObject1.getString("mobile");
                                String customerId = jsonObject1.getString("customer_front_id");
                                String isCredit = jsonObject1.getString("is_credit_available");

                                customerListModels.add(new CustomerListModel(firstName+" "+lastName,email,mobile,isCredit,customerId));

                            }
                            sellerCustomerListAdapter = new SellerCustomerListAdapter(CustomerListActivity.this,customerListModels);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(sellerCustomerListAdapter);
                        }
                    }

                    progressDialog.dismiss();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept","application/json");
                return params;
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


        req.setShouldCache(false);
        //MaintainRequestQueue.getInstance(CustomerCreditActivity.this).addToRequestQueue(request, "tag4354");
      //  MyApplication.getAppContext().addToRequestQueue(req, tag_json_obj);
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(CustomerListActivity.this).addToRequestQueue(req, "tag");

    }

    @Override
    public void onItemClick(View v, int position, String value) {
        customerListModel = sellerCustomerListAdapter.getWordAtPosition(position);

        if(value.equalsIgnoreCase("2")){
            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1.put("customer_id",customerListModel.getCustomerId());
                jsonObject1.put("seller_id",String.valueOf(sellerId));
                //  request.put("request",jsonObject1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final String mRequestBody = jsonObject1.toString();
            sendReminder(mRequestBody);
        }
        else {
            Intent intent = new Intent(CustomerListActivity.this,CustomerCreditActivity.class);
            intent.putExtra("customerId",customerListModel.getCustomerId());
            startActivity(intent);
        }
    }

    public void udharActions(final int status){
        if(!SellerCustomerListAdapter.getCustomerIds().isEmpty()){
            JSONObject jsonObject1 = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray types=new JSONArray();
            for (int i=0; i <SellerCustomerListAdapter.getCustomerIds().size(); i++){
                try {
                    types.put(SellerCustomerListAdapter.getCustomerIds().get(i));
                    jsonObject1.put("customer_ids", types);
                    jsonArray.put(jsonObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            try {
                jsonObject1.put("status",status);
                jsonObject1.put("seller_id",sellerId);
              //  request.put("request",jsonObject1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final String mRequestBody = jsonObject1.toString();

            Log.d("req",mRequestBody);
            saveData(mRequestBody);

        }
    }

    public void saveData(final String mRequestBody){
        String tag_json_obj = "json_obj_req3";
        String shopUrl  = sharedPreferences.getString("shop_url",null);
     //  String url = "https://"+shopUrl + BASE_URL+"carts/mine/qfix-udhar/seller/enable/credits";
        String url = Constant.REVAMP_URL1+"udhar/sellerCreditsEnable.php";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Saving data.......");


        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        Toast.makeText(CustomerListActivity.this,"Successfully Updated",Toast.LENGTH_LONG).show();
                        getCustomerList(String.valueOf(sellerId));
                    }
                }catch (Exception e){

                }

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
        req.setShouldCache(false);
        //MaintainRequestQueue.getInstance(CustomerCreditActivity.this).addToRequestQueue(request, "tag4354");
    //    MPOSApplication.getInstance().addToRequestQueue(req, tag_json_obj);
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(CustomerListActivity.this).addToRequestQueue(req, "tag");



    }

    public void sendReminder(final String mRequestBody){
        String tag_json_obj = "json_obj_req4";
        String shopUrl  = sharedPreferences.getString("shop_url",null);
       //  String url = "https://"+shopUrl + BASE_URL+"carts/mine/qfix-udhar/seller/creditreminder";

         String url = Constant.REVAMP_URL1+"udhar/seller_creditreminder.php";
      //  String url = "https://standalonetest.eduqfix.com/rest/V1/carts/mine/qfix-udhar/seller/creditreminder";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Saving reminder.......");


        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        Toast.makeText(CustomerListActivity.this,"Payment reminder email sent suscessfully to customer",Toast.LENGTH_LONG).show();
                        getCustomerList(String.valueOf(sellerId));
                    }
                }catch (Exception e){

                }

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
        req.setShouldCache(false);
        //MaintainRequestQueue.getInstance(CustomerCreditActivity.this).addToRequestQueue(request, "tag4354");
     //   MPOSApplication.getInstance().addToRequestQueue(req, tag_json_obj);
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(CustomerListActivity.this).addToRequestQueue(req, "tag");



    }
}
