package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.OrderListActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.MyOrderProductDetailAdapter;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.MemoDetailAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.OutofStockAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CreditMemoModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class CreditMemoDetailActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    ArrayList<CreditMemoModel> creditMemoModels;
    CreditMemoModel creditMemoModel;
    MemoDetailAdapter memoDetailAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    TextView increment_id,sub_total,shipping_amount,grand_total;
    Button refund_store_credit,offline_refund;
    EditText shippingRefund,adjustmentRefund;
    int shippingAmountVal,subTotalAmount;
    JSONObject requestObj;
    JSONArray jsonArray;
    String invoiceId;
    String entityId;
    String orderId;
    int sellerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_memo_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         entityId   = getIntent().getStringExtra("entityId");
         orderId    = getIntent().getStringExtra("order_id");
         invoiceId  = getIntent().getStringExtra("invoice_id");

        progressDialog = new ProgressDialog(CreditMemoDetailActivity.this);
        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;


        recyclerView = findViewById(R.id.memo_detail_recyclerview);
        increment_id = findViewById(R.id.memo_detail_id);
        sub_total = findViewById(R.id.memo_detail_subtotal);
        shippingRefund = findViewById(R.id.shipping_refund);
        adjustmentRefund = findViewById(R.id.adjustment_refund);
        grand_total = findViewById(R.id.memo_detail_grandtotal);
        refund_store_credit = findViewById(R.id.refund_store_credit);
        offline_refund = findViewById(R.id.refund_offline);
        linearLayoutManager = new LinearLayoutManager(CreditMemoDetailActivity.this);

      //  getMemoDetails(entityId,orderId);
        getOrderDetails(orderId);


        refund_store_credit.setOnClickListener(this);
        offline_refund.setOnClickListener(this);

        shippingRefund.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value=s.toString();
                if(isNumeric(value)){
                    if(Integer.parseInt(value)>shippingAmountVal){
                        shippingRefund.setText(String.valueOf(shippingAmountVal));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        adjustmentRefund.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value=s.toString();
                if(isNumeric(value)){
                    if(Integer.parseInt(value)>subTotalAmount){
                        adjustmentRefund.setText(String.valueOf(subTotalAmount));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(.\\d+)?");
    }

    public void getOrderDetails(String orderId){

        //     String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/createSellerOrderInvoice";
//https://apis.eduqfix.com/seller/rest/V1/products/media.php
        String shopUrl  = sharedPreferences.getString("shop_url",null);
         String url = Constant.REVAMP_URL1+"customapi/getOrderDetailByOrderId.php?orderid="+orderId;
     //   String url = "https://m3.vdcprojects.xyz/qfixstandalonemaster/rest/V1/orders/"+orderId+"/";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("");
        progressDialog.setCancelable(true);
        String adminToken  = "Bearer "+ sharedPreferences.getString("adminToken",null);
        Log.d("admindddddddd..",adminToken);
        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    creditMemoModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);

                    String incrementId = jsonObject.getString("increment_id");
                    shippingAmountVal = jsonObject.getInt("shipping_amount");
                    subTotalAmount = jsonObject.getInt("subtotal");

                    JSONArray items = jsonObject.getJSONArray("items");

                    if(items.length() > 0) {
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject jsonObject2 = items.getJSONObject(i);
                            String name = jsonObject2.getString("name");
                            String sku = jsonObject2.getString("sku");
                            String entityId = jsonObject2.getString("item_id");
                            String qty = jsonObject2.getString("qty_ordered");
                            String base_price = jsonObject2.getString("original_price");
                            String orderItemId = jsonObject2.getString("quote_item_id");

                            creditMemoModels.add(new CreditMemoModel(name, sku, qty, entityId, String.valueOf(shippingAmountVal), String.valueOf(subTotalAmount), "", base_price, orderItemId));
                        }

                        memoDetailAdapter = new MemoDetailAdapter(CreditMemoDetailActivity.this,creditMemoModels);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(memoDetailAdapter);

                        increment_id.setText("# "+incrementId);
                        shippingRefund.setText(String.valueOf(shippingAmountVal));
                        sub_total.setText("Sub Total  : "+getString(R.string.Rs)+subTotalAmount);
                        adjustmentRefund.setText(String.valueOf(subTotalAmount));

                        int grandTotal = shippingAmountVal + subTotalAmount ;
                        grand_total.setText("Grand Total  : "+getString(R.string.Rs) +String.valueOf(grandTotal));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreditMemoDetailActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", adminToken);

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
        MaintainRequestQueue.getInstance(CreditMemoDetailActivity.this).addToRequestQueue(req, "tag");



    }

    public void getMemoDetails(final String entity_Id,final String orderId){

        //     String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/createSellerOrderInvoice";

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        // String url ="https://"+shopUrl+ Constant.BASE_URL+"customapi/createSellerOrderInvoice";
        String url = "https://m3.vdcprojects.xyz/qfixstandalonemaster/rest/V1/carts/mine/creditmemo/"+entity_Id;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("");
        progressDialog.setCancelable(true);

        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put("order_id", Integer.parseInt(orderId));
            jsonObject1.put("seller_id", 144);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonObject1.toString();

        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    creditMemoModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        JSONObject message = jsonObject.getJSONObject("message");
                        JSONObject item = message.getJSONObject("item");
                        String grandTotal = item.getString("grand_total");
                        String shippingAmount = item.getString("shipping_amount");
                        String subtotal = item.getString("subtotal");
                        String incrementId = item.getString("increment_id");
                        invoiceId = item.getString("invoice_id");
                        Log.d("invoice id",invoiceId);
                        JSONArray items = item.getJSONArray("items");

                        if(items.length() > 0){
                           for (int i = 0; i < items.length(); i++){
                               JSONObject jsonObject2 = items.getJSONObject(i);
                               String name = jsonObject2.getString("name");
                               String sku = jsonObject2.getString("sku");
                               String entityId = jsonObject2.getString("entity_id");
                               String qty = jsonObject2.getString("qty");
                               String base_price = jsonObject2.getString("base_price");
                               String orderItemId = jsonObject2.getString("order_item_id");

                               creditMemoModels.add(new CreditMemoModel(name,sku,qty,entityId,shippingAmount,subtotal,grandTotal,base_price,orderItemId));
                           }

                            memoDetailAdapter = new MemoDetailAdapter(CreditMemoDetailActivity.this,creditMemoModels);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(memoDetailAdapter);



                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreditMemoDetailActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
        MaintainRequestQueue.getInstance(CreditMemoDetailActivity.this).addToRequestQueue(req, "tag");



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.refund_store_credit:
                createJsonData("false");
                break;
            case R.id.refund_offline:
                createJsonData("true");
                break;
        }



    }

    public void createJsonData(final String offline){
        String shippingPrice = shippingRefund.getText().toString();
        String refundPrice = adjustmentRefund.getText().toString();

        int size = MemoDetailAdapter.getCreditMemoItems().size();
        requestObj = new JSONObject();
        jsonArray = new JSONArray();
        for (int i =0 ; i< size; i++){
            StringTokenizer tokens = new StringTokenizer((String) MemoDetailAdapter.getCreditMemoItems().get(i), ":");
            String itemId  = tokens.nextToken();
            String qty = tokens.nextToken();
            try {
                requestObj.put("seller_id",sellerId);
                JSONObject data = new JSONObject();
                data.put("order_item_id", Integer.parseInt(itemId));
                data.put("qty", Integer.parseInt(qty));
                jsonArray.put(data);
                requestObj.put("items", jsonArray);
                if(offline.equalsIgnoreCase("true")){
                    requestObj.put("isOnline",true);
                    requestObj.put("notify",true);
                }
                JSONObject arguments = new JSONObject();
                arguments.put("shipping_amount", Integer.parseInt(shippingPrice));
                arguments.put("adjustment_positive", 0);
                arguments.put("adjustment_negative", 0);
                JSONObject extension_attributes = new JSONObject();
                extension_attributes.put("return_to_store_credit", 1);
                extension_attributes.put("store_credit_return_amount", Integer.parseInt(refundPrice));
                arguments.put("extension_attributes",extension_attributes);
                requestObj.put("arguments", arguments);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        generateMemo(String.valueOf(requestObj));
    }

    public void generateMemo(final String mRequestBody){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
      //  String url ="https://"+shopUrl+ Constant.BASE_URL+"invoice/"+invoiceId+"/refund";
       String url = Constant.REVAMP_URL1+"customapi/invoiceRefund.php";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Creating memo.......");
        String adminToken  = "Bearer "+ sharedPreferences.getString("adminToken",null);
        Log.e("Url...", "" + mRequestBody);

        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                        Toast.makeText(CreditMemoDetailActivity.this,"Credit memo generated successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreditMemoDetailActivity.this,MainDashboardActivity.class);
                        startActivity(intent);
                        finish();

                }catch (Exception e){

                }

                progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("TAG", "Error: " + error.getMessage());
                Toast.makeText(CreditMemoDetailActivity.this,"Something went wrong, Try again!",Toast.LENGTH_LONG).show();
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
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", adminToken);

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
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        MaintainRequestQueue.getInstance(CreditMemoDetailActivity.this).addToRequestQueue(req, "tag");



    }
}
