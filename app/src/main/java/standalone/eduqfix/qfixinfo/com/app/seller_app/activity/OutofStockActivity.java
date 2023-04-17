package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.MyOrdersListActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.MyProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.AddNewProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.MyOrderProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductAttributes;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductImages;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductMediaGallery;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductModel;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.CategoryIdsModel;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModelNew;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.ProductDetailModel;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.GroupProductAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.OutofStockAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.BundleSizeContainer;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.OutofStcokModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class OutofStockActivity extends AppCompatActivity implements OutofStockAdapter.ClickListener{

    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    OutofStockAdapter outofStockAdapter;
    ArrayList<OutofStcokModel> outofStcokModels;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    OutofStcokModel outofStcokModel;
    Button submit;
    JSONObject requestObj;JSONArray jsonArray;
    String sku,qty;
    TextView noDataAvailable;
    int sellerId;
    public static ArrayList<String> outOfStockQuantity = new ArrayList<>();
    public static ArrayList<String> outOfStockSku = new ArrayList<>();

    public static ArrayList<String> getOutOfStockQuantity() {
        return outOfStockQuantity;
    }

    public static void setOutOfStockQuantity(ArrayList<String> outOfStockQuantity) {
        OutofStockActivity.outOfStockQuantity = outOfStockQuantity;
    }

    public static ArrayList<String> getOutOfStockSku() {
        return outOfStockSku;
    }

    public static void setOutOfStockSku(ArrayList<String> outOfStockSku) {
        OutofStockActivity.outOfStockSku = outOfStockSku;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outof_stock);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(OutofStockActivity.this);
        recyclerView = findViewById(R.id.out_of_stock_recyclerview);
        noDataAvailable = findViewById(R.id.out_of_stock_nodata);
        linearLayoutManager = new LinearLayoutManager(OutofStockActivity.this);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;


        getOutofStcokList(String.valueOf(sellerId));
        outofStockAdapter = new OutofStockAdapter();
        outofStockAdapter.setOnItemClickListener(this);

        submit = findViewById(R.id.out_of_stock_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outOfStockQuantity.clear();
                outOfStockSku.clear();
                Iterator myVeryOwnIterator = OutofStockAdapter.OutofStockHashMap.keySet().iterator();
                int i = 0;
                int size = OutofStockAdapter.OutofStockHashMap.size();
                while(myVeryOwnIterator.hasNext()) {

                    int key=(int)myVeryOwnIterator.next();
                    String value= (String) OutofStockAdapter.OutofStockHashMap.get(key);
                    StringTokenizer tokens = new StringTokenizer((String) value, ":");
                     sku  = tokens.nextToken();
                     qty = tokens.nextToken();
                     outOfStockSku.add(sku);
                     outOfStockQuantity.add(qty);
                    i++;

                }
                requestObj = new JSONObject();
                jsonArray = new JSONArray();
                for (int ii =0 ; ii < size; ii++){

                    try {
                        JSONObject data = new JSONObject();
                        data.put("sku", outOfStockSku.get(ii));
                        data.put("qty", outOfStockQuantity.get(ii));
                        jsonArray.put(data);
                        requestObj.put("request", jsonArray);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if(size == 0){
                    Toast.makeText(OutofStockActivity.this,"Please select at least one product",Toast.LENGTH_LONG).show();
                }
                else {
                    updateQuantity(String.valueOf(requestObj));
                }

             }

        });

    }

    public void getOutofStcokList(final String sellerId){

        String shopUrl  = sharedPreferences.getString("shop_url",null);

        String url = Constant.REVAMP_URL1+"marketplace/getAllOutOfStockProduct.php?seller_id="+sellerId;

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
                    outofStcokModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if(status.equalsIgnoreCase("true")){
                        JSONArray jsonArray = jsonObject.getJSONArray("productArray");
                        if(jsonArray.length() > 0){

                          for (int i = 0; i < jsonArray.length();i++){
                              JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                              String sku = jsonObject1.getString("sku");
                              String productName = jsonObject1.getString("productName");
                              String productPrice = jsonObject1.getString("productPrice");
                              String thumbnail = jsonObject1.getString("thumbnail");
                              String productType = jsonObject1.getString("productType");

                              outofStcokModels.add(new OutofStcokModel(sku,productName,thumbnail,productType,productPrice,"100"));

                          }

                            outofStockAdapter = new OutofStockAdapter(OutofStockActivity.this, outofStcokModels,"100");
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(outofStockAdapter);
                            outofStockAdapter.notifyDataSetChanged();


                        }
                        else {
                           noDataAvailable.setVisibility(View.VISIBLE);
                           recyclerView.setVisibility(View.GONE);
                           submit.setVisibility(View.GONE);
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

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(OutofStockActivity.this).addToRequestQueue(req, "tag");

    }

    @Override
    public void onItemClick(View v, int position) {
        outofStcokModel = outofStockAdapter.getWordAtPosition(position);
    }

    public void updateQuantity(final String mRequestBody){

        String shopUrl  = sharedPreferences.getString("shop_url",null);

        String url = Constant.REVAMP_URL1+"marketplace/updateQuantityOfProduct.php";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Updating quantity.......");


        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if(status.equalsIgnoreCase("true")){
                        Toast.makeText(OutofStockActivity.this,"Qty Successfully Updated",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(OutofStockActivity.this,MainDashboardActivity.class);
                        startActivity(intent);
                        finish();
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
        MaintainRequestQueue.getInstance(OutofStockActivity.this).addToRequestQueue(req, "tag");



    }
}
