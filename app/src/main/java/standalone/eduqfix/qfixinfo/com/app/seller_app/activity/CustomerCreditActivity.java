package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SellerCustomerListAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.UdharCustomerDetailAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerListModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.UdharCustomerModel;
import standalone.eduqfix.qfixinfo.com.app.util.AppController;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSApplication;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;

public class CustomerCreditActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    UdharCustomerModel udharCustomerModel;
    ArrayList<UdharCustomerModel> udharCustomerModels;
    UdharCustomerDetailAdapter udharCustomerDetailAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    int sellerId;
    String customerId;
    Button markPaid,markCancel,markSubmit;
    TextView udharamt,pendingamt,paidamt,noData;
    EditText paying_amount,summary;
    LinearLayout mark_layout,main_layout;
    String mRequestBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_credit);


        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(CustomerCreditActivity.this);
        recyclerView = findViewById(R.id.udhar_detail_recyclerview);
        linearLayoutManager = new LinearLayoutManager(CustomerCreditActivity.this);

        udharamt = findViewById(R.id.total_udhar_amount);
        pendingamt = findViewById(R.id.total_udhar_pending);
        paidamt = findViewById(R.id.total_udhar_paid);
        paying_amount = findViewById(R.id.udhar_paying_amount);
        summary = findViewById(R.id.udhar_paying_summary);
        markPaid = findViewById(R.id.udhar_mark_paid);
        markCancel = findViewById(R.id.udhar_mark_cancel);
        markSubmit = findViewById(R.id.udhar_mark_submit);
        mark_layout = findViewById(R.id.udhar_paid_layout);
        main_layout = findViewById(R.id.udhar_main_layout);
        noData = findViewById(R.id.udhar_no_data);

        customerId = getIntent().getStringExtra("customerId");

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        customerDetails();

        markPaid.setOnClickListener(this);
        markCancel.setOnClickListener(this);
        markSubmit.setOnClickListener(this);

    }

    private void customerDetails(){
        String tag_json_obj = "json_obj_req";
        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Getting data.......");

        String shopUrl  = sharedPreferences.getString("shop_url",null);
       //  String url = "https://"+shopUrl + BASE_URL+"carts/mine/qfix-udhar/seller/credits";
        String url = Constant.REVAMP_URL1+"udhar/getseller_credits.php?sellerId="+sellerId+"&telephone=&customerId="+customerId;
        Log.e("Url...", "" + url);

        JSONObject obj = new JSONObject();
        try {
            obj.put("customer_id", customerId);
            obj.put("seller_id", String.valueOf(sellerId));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("request",obj.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.GET, url,obj , response -> {
            try{
                Log.d("request",response.toString());
                udharCustomerModels = new ArrayList<>();
                udharCustomerModels.clear();
                boolean status = response.getBoolean("status");
                if(status) {

                    JSONObject message = response.getJSONObject("message");
                    String totalUdharAmount = message.getString("total_udhar_amount");
                    String totalPaidAmount = message.getString("total_paid_amount");
                    String totalPendingAmount = message.getString("total_pending_amount");

                    JSONArray collection = message.getJSONArray("collection");
                    if (collection.length() > 0) {
                        for (int i = 0; i < collection.length(); i++) {
                            JSONObject jsonObject1 = collection.getJSONObject(i);

                            String transactionId = jsonObject1.getString("transaction_id");
                            String createdAt = jsonObject1.getString("created_at");
                            String transactionType = jsonObject1.getString("transaction_type");
                            String pendingAmount = jsonObject1.getString("pending_amount");
                            String paidAmount = jsonObject1.getString("paid_amount");
                            String summary = jsonObject1.getString("summary");
                            String name = jsonObject1.getString("customer_name");
                            String email = jsonObject1.getString("customer_email");
                            String order_increment_id = jsonObject1.getString("order_increment_id");


                            udharCustomerModels.add(new UdharCustomerModel(transactionId,order_increment_id,name,createdAt,totalUdharAmount,paidAmount,summary,transactionType,totalPaidAmount));

                        }
                        udharCustomerDetailAdapter = new UdharCustomerDetailAdapter(CustomerCreditActivity.this,udharCustomerModels);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(udharCustomerDetailAdapter);

                        udharamt.setText(getString(R.string.Rs)+totalUdharAmount);
                        pendingamt.setText(getString(R.string.Rs)+totalPendingAmount);
                        paidamt.setText(getString(R.string.Rs)+totalPaidAmount);
                    }
                    else {
                        main_layout.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        markPaid.setVisibility(View.GONE);
                    }
                }
            }catch (Exception e){

            }

            progressDialog.dismiss();
        }, error -> {
            //    Log.e("TAG", "Error: " + error.getMessage());
            progressDialog.dismiss();
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer ufst0mpdb54ua27ieqmul03bivlpxmpg");
                return params;
            }
        };

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        //MaintainRequestQueue.getInstance(CustomerCreditActivity.this).addToRequestQueue(request, "tag4354");
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(CustomerCreditActivity.this).addToRequestQueue(jsonObjReq, "tag");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.udhar_mark_paid:
                markPaid.setVisibility(View.GONE);
                mark_layout.setVisibility(View.VISIBLE);
               break;
            case R.id.udhar_mark_cancel:
                markPaid.setVisibility(View.VISIBLE);
                mark_layout.setVisibility(View.GONE);
                break;
            case R.id.udhar_mark_submit:
                String payingAmt = paying_amount.getText().toString();
                String payingSummary = summary.getText().toString();
                if(paying_amount.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(CustomerCreditActivity.this,"Enter amount",Toast.LENGTH_LONG).show();
                }
                else if(summary.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(CustomerCreditActivity.this,"Enter summary",Toast.LENGTH_LONG).show();
                }
                else {

                    JSONObject jsonObject1 = new JSONObject();
                    try {
                        jsonObject1.put("customer_id",Integer.parseInt(customerId));
                        jsonObject1.put("seller_id",String.valueOf(sellerId));
                        jsonObject1.put("paying_amount",payingAmt);
                        jsonObject1.put("summary",payingSummary);
                        //  request.put("request",jsonObject1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final String mRequestBody = jsonObject1.toString();
                    Log.e("Url...", "" + mRequestBody);
                    offlinePayment(mRequestBody);
                }
                break;
        }
    }

    public void offlinePayment(final String mRequestBody){
        String tag_json_obj = "json_obj_req1";
        String shopUrl  = sharedPreferences.getString("shop_url",null);
         String url =  Constant.REVAMP_URL1+"udhar/ofline-payment.php";
    //    String url = "https://standalonetest.eduqfix.com/rest/V1/carts/mine/qfix-udhar/seller/make/offline/payment";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Saving data.......");


        StringRequest jsonObjReq1 = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    udharCustomerModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if(status) {

                        JSONObject message = jsonObject.getJSONObject("message");
                        String collection = message.getString("collection");
                        Toast.makeText(CustomerCreditActivity.this,collection,Toast.LENGTH_LONG).show();

                        mark_layout.setVisibility(View.GONE);
                        markPaid.setVisibility(View.VISIBLE);



                        customerDetails();

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
     //   MaintainRequestQueue.getInstance(CustomerCreditActivity.this).addToRequestQueue(req, "tag");

        jsonObjReq1.setShouldCache(false);
        //MaintainRequestQueue.getInstance(CustomerCreditActivity.this).addToRequestQueue(request, "tag4354");
    //    MPOSApplication.getInstance().addToRequestQueue(jsonObjReq1, tag_json_obj);
        jsonObjReq1.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(CustomerCreditActivity.this).addToRequestQueue(jsonObjReq1, "tag");

    }
}
