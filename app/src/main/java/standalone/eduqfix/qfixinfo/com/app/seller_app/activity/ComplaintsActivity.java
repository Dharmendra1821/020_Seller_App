package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ComplaintsAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.OutofStockAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ComplaintsModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.OutofStcokModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class ComplaintsActivity extends AppCompatActivity implements ComplaintsAdapter.ClickListener {

    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    ComplaintsAdapter complaintsAdapter;
    ArrayList<ComplaintsModel> complaintsModels;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ComplaintsModel complaintsModel;
    int sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(ComplaintsActivity.this);
        recyclerView = findViewById(R.id.complaints_recyclerview);
        linearLayoutManager = new LinearLayoutManager(ComplaintsActivity.this);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        getComplaintsList(String.valueOf(sellerId));
        complaintsAdapter = new ComplaintsAdapter();
        complaintsAdapter.setOnItemClickListener(this);



    }

    public void getComplaintsList(final String sellerId){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
      //  String url = "https://"+shopUrl + SKU_URL+"marketplace/getAllComplaintBySeller?sellerId="+sellerId;
        String url = Constant.REVAMP_URL1+ "marketplace/getAllComplaintBySeller.php?sellerId="+sellerId;
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
                    complaintsModels = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String entity_id = jsonObject1.optString("id");
                                String product_id = jsonObject1.optString("product_id");
                                String vendor_id = jsonObject1.optString("vendor_id");
                                String customer_comments = jsonObject1.optString("customer_comment");
                                String customer_id = jsonObject1.optString("customer_id");
                                String request_status = jsonObject1.optString("status");
                                String vendor_comments = jsonObject1.optString("vendor_comment");
                                String product_name = jsonObject1.optString("product_name");
                                String customer_name = jsonObject1.optString("name");
                                String orderid = jsonObject1.optString("order_id");
                                String itemid = jsonObject1.optString("itemid");

                                complaintsModels.add(new ComplaintsModel(entity_id, product_id, vendor_id, customer_comments, customer_id,
                                        request_status, vendor_comments, "", product_name, customer_name, "", orderid, itemid));

                            }
                        }

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }




                            complaintsAdapter = new ComplaintsAdapter(ComplaintsActivity.this, complaintsModels);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(complaintsAdapter);
                            complaintsAdapter.notifyDataSetChanged();


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

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(ComplaintsActivity.this).addToRequestQueue(req, "tag");

    }

    @Override
    public void onItemClick(View v, int position) {
        complaintsModel = complaintsAdapter.getWordAtPosition(position);

        Intent intent = new Intent(ComplaintsActivity.this,ComplaintsDetailsActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("complaints", complaintsModel);
        intent.putExtras(b);
        startActivity(intent);
    }
}
