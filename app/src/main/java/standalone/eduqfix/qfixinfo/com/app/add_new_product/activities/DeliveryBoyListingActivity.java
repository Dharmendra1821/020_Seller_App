package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.DeliveryboyAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyList;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class DeliveryBoyListingActivity extends AppCompatActivity implements DeliveryboyAdapter.ClickListener {
    RecyclerView recyclerView;
    ArrayList<DeliveryBoyList> deliveryBoyLists;
    DeliveryboyAdapter deliveryboyAdapter;
    ProgressDialog progressDialog;
    LinearLayoutManager linearLayoutManager;
    DeliveryBoyList deliveryBoyList;
    SharedPreferences sharedPreferences;
    int sellerId;
    int page;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    RelativeLayout bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_listing);
        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
        getSupportActionBar().setTitle("Delivery boy list");
        recyclerView = findViewById(R.id.delivery_boy_list_recyclerview);
        bottomLayout =  findViewById(R.id.loadItemsLayout_recyclerView);
        progressDialog = new ProgressDialog(DeliveryBoyListingActivity.this);
        linearLayoutManager = new LinearLayoutManager(DeliveryBoyListingActivity.this);

        page =1;

        deliveryboyAdapter = new DeliveryboyAdapter();
        deliveryboyAdapter.setOnItemClickListener(this);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        getDeliveryBoy(String.valueOf(sellerId), String.valueOf(page));

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

                         //   getDeliveryBoy1(String.valueOf(sellerId), String.valueOf(page));
                        }
                    }
                }
            }
        });
    }

    public void getDeliveryBoy(final String sellerId,final String limit){

        //String url = Constant.REVAMP_URL1+"customapi/getDeliveryBoyList.php?seller_id=74";
        String shopUrl  = sharedPreferences.getString("shop_url",null);

        String url = Constant.REVAMP_URL2+"?function=DeliveryBoy&method=list&seller_id="+sellerId;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading data.......");

        final JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("seller_id", sellerId);
            jsonObject1.put("limit", limit);
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
                    deliveryBoyLists = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String entityId = jsonObject2.getString("deliveryboy_id");
                        String email = jsonObject2.getString("email_id");
                        String firstname = jsonObject2.getString("first_name");
                        String lastname = jsonObject2.getString("last_name");
                        String contact = jsonObject2.getString("contact_number");
                        String status = jsonObject2.getString("status");

                      deliveryBoyLists.add(new DeliveryBoyList(i,entityId,email,firstname,lastname,contact,status));

                    }
                    deliveryboyAdapter = new DeliveryboyAdapter(DeliveryBoyListingActivity.this,deliveryBoyLists);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(deliveryboyAdapter);
                    deliveryboyAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
        MaintainRequestQueue.getInstance(DeliveryBoyListingActivity.this).addToRequestQueue(req, "tag");




    }

    public void getDeliveryBoy1(final String sellerId,final String limit){

        //    String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/getDeliveryBoyList";
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url ="https://"+shopUrl+ Constant.BASE_URL+"customapi/getDeliveryBoyList";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

     /*   progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading My Orders.......");*/

     bottomLayout.setVisibility(View.VISIBLE);

        final JSONObject jsonObject1 = new JSONObject();
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

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String entityId = jsonObject.getString("entity_id");
                        String email = jsonObject.getString("email");
                        String firstname = jsonObject.getString("firstname");
                        String lastname = jsonObject.getString("lastname");
                        String contact = jsonObject.getString("contact");
                        String status = jsonObject.getString("status");

                    //    deliveryBoyLists.add(new DeliveryBoyList(i,entityId,email,firstname,lastname,contact,status));

                    }
                   /* deliveryboyAdapter = new DeliveryboyAdapter(DeliveryBoyListingActivity.this,deliveryBoyLists);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(deliveryboyAdapter);*/
                    deliveryboyAdapter.notifyDataSetChanged();
                    loading = true;
                    bottomLayout.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


           //     progressDialog.dismiss();


            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
           //     progressDialog.dismiss();

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
        MaintainRequestQueue.getInstance(DeliveryBoyListingActivity.this).addToRequestQueue(req, "tag");




    }
    @Override
    public void onItemClick(View v, int position) {
        deliveryBoyList = deliveryboyAdapter.getWordAtPosition(position);
        Intent intent = new Intent(DeliveryBoyListingActivity.this,AddDeliveryBoyActivity.class);
        intent.putExtra("fname",deliveryBoyList.getFirstname());
        intent.putExtra("lname",deliveryBoyList.getLastname());
        intent.putExtra("entity_id",deliveryBoyList.getEntityId());
        intent.putExtra("email",deliveryBoyList.getEmail());
        intent.putExtra("contact",deliveryBoyList.getContact());
        intent.putExtra("status",deliveryBoyList.getStatus());
        startActivity(intent);
    }
}
