package standalone.eduqfix.qfixinfo.com.app.seller_app.community;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.community.model.CommunitySellerListModel;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;

public class CommunitySellerSalesCollection extends AppCompatActivity {

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String microMarketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_seller_sales_collection);

        getSupportActionBar().setTitle("Community Sales List");

        progressDialog = new ProgressDialog(CommunitySellerSalesCollection.this);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        microMarketId = sharedPreferences.getString("microMarketId",null);

        String sellerId = getIntent().getStringExtra("sellerId");
        getCommunitySellerSalesList("6272");


    }

    public void getCommunitySellerSalesList(final String sellerId){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl+ BASE_URL+"customapi/viewSalesCollection?seller_id="+sellerId;

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Getting data.......");
        progressDialog.show();

        StringRequest req = new StringRequest(com.android.volley.Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{



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
        MaintainRequestQueue.getInstance(CommunitySellerSalesCollection.this).addToRequestQueue(req, "tag");
    }
}
