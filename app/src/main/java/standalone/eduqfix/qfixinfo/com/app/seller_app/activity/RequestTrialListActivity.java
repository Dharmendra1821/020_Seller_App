package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.RequestTrialAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.StoreCustomerAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.RequestTrialModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCustomerListModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class RequestTrialListActivity extends AppCompatActivity implements RequestTrialAdapter.ClickListener {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    RequestTrialModel requestTrialModel;
    ArrayList<RequestTrialModel> requestTrialModels;
    RequestTrialAdapter requestTrialAdapter;
    ProgressDialog progressDialog;
    int sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_trial_list);

        progressDialog = new ProgressDialog(RequestTrialListActivity.this);
        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
        linearLayoutManager  = new LinearLayoutManager(RequestTrialListActivity.this);

        recyclerView = findViewById(R.id.request_trial_recyclerview);

        requestTrialAdapter = new RequestTrialAdapter();
        requestTrialAdapter.setOnItemClickListener(this);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;


        getRequestTrialList(String.valueOf(sellerId));

    }

    public void getRequestTrialList(final String sellerId){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"marketplace/getAllDemoRequestBySeller.php?sellerId="+sellerId;

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);


        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Getting data.......");
        progressDialog.show();


        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                requestTrialModels = new ArrayList<>();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if(status.equalsIgnoreCase("true")){
                        JSONArray demorequest = jsonObject.getJSONArray("demoreques");
                        if (demorequest.length() > 0){
                            for (int i = 0; i < demorequest.length(); i++){
                                JSONObject jsonObject2 = demorequest.getJSONObject(i);

                                String id = jsonObject2.getString("id");
                                String productId = jsonObject2.getString("product_id");
                                String vendorId = jsonObject2.getString("vendor_id");
                                String customerId = jsonObject2.getString("customer_id");
                                String requestStatus = jsonObject2.getString("request_status");
                                String vendorComments = jsonObject2.getString("vendor_comments");
                                String productName = jsonObject2.getString("product_name");
                                String customerName = jsonObject2.getString("customer_name");
                                requestTrialModels.add(new RequestTrialModel(id,productId,vendorId,customerId,requestStatus,vendorComments,productName,customerName));
                            }


                            requestTrialAdapter = new RequestTrialAdapter(RequestTrialListActivity.this,requestTrialModels);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(requestTrialAdapter);


                        }

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
        MaintainRequestQueue.getInstance(RequestTrialListActivity.this).addToRequestQueue(req, "tag");



    }

    @Override
    public void onItemClick(View v, int position) {
        requestTrialModel = requestTrialAdapter.getWordAtPosition(position);

        Intent intent = new Intent(RequestTrialListActivity.this,RequestTrialDetailsActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("request_trial", requestTrialModel);
        intent.putExtras(b);
        startActivity(intent);
    }
}
