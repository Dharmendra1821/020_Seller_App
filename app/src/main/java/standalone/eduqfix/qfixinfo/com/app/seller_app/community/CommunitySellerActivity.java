package standalone.eduqfix.qfixinfo.com.app.seller_app.community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.CustomerCreditActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.UdharCustomerDetailAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.community.model.CommunitySellerListModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.community.model.ImageModel;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;

public class CommunitySellerActivity extends AppCompatActivity implements CommunitySellerListAdapter.ClickListener {

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String microMarketId;
    ArrayList<CommunitySellerListModel> communitySellerListModels;
    RecyclerView recyclerView;
    CommunitySellerListAdapter communitySellerListAdapter;
    LinearLayoutManager linearLayoutManager;
    CommunitySellerListModel communitySellerListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_seller);

        getSupportActionBar().setTitle("Community Seller List");

        progressDialog = new ProgressDialog(CommunitySellerActivity.this);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        microMarketId = sharedPreferences.getString("microMarketId",null);

        recyclerView = findViewById(R.id.communitySellerList);
        linearLayoutManager = new LinearLayoutManager(CommunitySellerActivity.this);

        communitySellerListAdapter = new CommunitySellerListAdapter();
        communitySellerListAdapter.setOnItemClickListener(this);


        getCommunitySellerList(microMarketId);

    }

    public void getCommunitySellerList(final String microMarketId){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl+ BASE_URL+"customapi/getCommunitysellerCollection";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("micromarket_id",microMarketId);
            jsonObject1.put("customer_name","");
            jsonObject1.put("shop_title","");
            jsonObject1.put("email","");
            jsonObject1.put("billing_telephone","");
            jsonObject1.put("status","");
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request.toString();

        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Getting data.......");
        progressDialog.show();

        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                   communitySellerListModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equalsIgnoreCase("success")){
                        JSONArray data = jsonObject.getJSONArray("data");
                        if(data.length() > 0){
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String sellerId = jsonObject1.getString("seller_id");
                                String sellerName = jsonObject1.getString("customer_name");
                                String sellerContact = jsonObject1.getString("merchant_contact_number");
                                String sellerEmail = jsonObject1.getString("email");
                                String shopName = jsonObject1.getString("shop_name");
                                String sellerStatus = jsonObject1.getString("status");

                                communitySellerListModels.add(new CommunitySellerListModel(sellerName,sellerContact,shopName,sellerEmail,sellerStatus,id,sellerId));

                            }
                            communitySellerListAdapter = new CommunitySellerListAdapter(CommunitySellerActivity.this,communitySellerListModels);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(communitySellerListAdapter);
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
            public com.android.volley.Request.Priority getPriority() {
                return com.android.volley.Request.Priority.IMMEDIATE;
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
        MaintainRequestQueue.getInstance(CommunitySellerActivity.this).addToRequestQueue(req, "tag");
    }

    @Override
    public void onItemClick(View v, int position) {
        communitySellerListModel = communitySellerListAdapter.getWordAtPosition(position);
        Intent intent = new Intent(CommunitySellerActivity.this,CommunitySellerSalesCollection.class);
        intent.putExtra("sellerId",communitySellerListModel.getSellerId());
        startActivity(intent);

    }
}
