package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.ExpListViewAdapterWithCheckbox;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.MyOrderProductDetailAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.MyOrderProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductModel;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModelNew;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.ProductDetailModel;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.CreditMemoDetailActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.CustomerCreditActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSApplication;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class OrderListActivity extends AppCompatActivity implements View.OnClickListener {

    Button invoice,shipped,cancelled,delivered,create_memo,cancel_udharBtn;
    TextView cust_name,cust_address,cust_email,cust_mobile;
    TextView subtotal,shipping,taxamount,grand_total;
    String custName,custAddress,custEmail,custMobile;
    String subTotal,shippingAmt,taxAmt,grandTotal,invoiceId,status;
    RecyclerView recyclerView;
    MyOrderProductDetailAdapter myOrderProductDetailAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ProductDetailModel> productDetailModels;
    LinearLayout product_layout;
    ProgressDialog progressDialog;
    String incrementId;
    String orderId;
    String entityId;
    String shipmentId;
    String carrierName;
    String trackingNumber;
    String paymentMethod;
    SharedPreferences sharedPreferences;
    int sellerId;
    TextView order_id;
    String entity_Id;
    BroadcastReceiver mMessageReceiver;
    String productType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        getSupportActionBar().setTitle("Orders Details");

        progressDialog = new ProgressDialog(OrderListActivity.this);

        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        cust_name = findViewById(R.id.order_detail_custname);
        cust_address = findViewById(R.id.order_detail_address);
        cust_email = findViewById(R.id.order_detail_email);
        cust_mobile = findViewById(R.id.order_detail_mobile);
        subtotal = findViewById(R.id.order_detail_subtotal);
        shipping = findViewById(R.id.order_detail_shippingamount);
        taxamount = findViewById(R.id.order_detail_taxamount);
        grand_total = findViewById(R.id.order_detail_grandtotal);
        invoice = findViewById(R.id.invoice_Btn);
        shipped = findViewById(R.id.shipped_Btn);
        cancelled = findViewById(R.id.cancelled_Btn);
        delivered = findViewById(R.id.delivered_Btn);
        create_memo = findViewById(R.id.create_memoBtn);
        recyclerView = findViewById(R.id.myorder_product_detail_recyclerview);
        product_layout = findViewById(R.id.product_detail_layout);
        order_id = findViewById(R.id.order_detail_id);
        cancel_udharBtn = findViewById(R.id.cancel_udharBtn);

        custName = getIntent().getStringExtra("name");
        custAddress = getIntent().getStringExtra("address");
        custEmail = getIntent().getStringExtra("email");
        custMobile = getIntent().getStringExtra("mobile");

        subTotal = getIntent().getStringExtra("base_total");
        shippingAmt = getIntent().getStringExtra("shipping");
        taxAmt = getIntent().getStringExtra("tax");
        grandTotal = getIntent().getStringExtra("grand_total");
        invoiceId = getIntent().getStringExtra("invoice_id");
        status   = getIntent().getStringExtra("status");
        incrementId = getIntent().getStringExtra("increment_id");
        orderId   = getIntent().getStringExtra("order_id");
        entityId = getIntent().getStringExtra("entity_id");

        shipmentId = getIntent().getStringExtra("shipment_id");
        carrierName = getIntent().getStringExtra("carrier_name");
        trackingNumber = getIntent().getStringExtra("tracking_number");
        paymentMethod = getIntent().getStringExtra("payment_method");

        Log.d("invoice..id....",orderId);

        cust_name.setText(custName);
        cust_address.setText(custAddress);
        cust_email.setText("Email : "+custEmail);
        cust_mobile.setText("Mobile : "+custMobile);
        if(incrementId.equalsIgnoreCase("null")){
            order_id.setText("");
        }
        else {
            order_id.setText("# "+incrementId);
        }


        linearLayoutManager = new LinearLayoutManager(OrderListActivity.this);

        productDetailModels = new ArrayList<>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        MyOrderListModelNew myOrderListModelNew = (MyOrderListModelNew) bundle.getSerializable("my_order");

        for (int i = 0;i < myOrderListModelNew.getProductDetailModels().size();i++){

            String name = myOrderListModelNew.getProductDetailModels().get(i).getProductName();
            String sku = myOrderListModelNew.getProductDetailModels().get(i).getSku();
            String price = myOrderListModelNew.getProductDetailModels().get(i).getProductPrice();
             productType = myOrderListModelNew.getProductDetailModels().get(i).getProductType();

            if(name.equalsIgnoreCase("null")){
                product_layout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
            else {
                product_layout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            productDetailModels.add(new ProductDetailModel(sku,name,price,productType));
        }
        myOrderProductDetailAdapter = new MyOrderProductDetailAdapter(OrderListActivity.this,productDetailModels);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myOrderProductDetailAdapter);


        if(paymentMethod.equalsIgnoreCase("Udhar")){
         //   cancel_udharBtn.setVisibility(View.VISIBLE);
        }
        else {
            cancel_udharBtn.setVisibility(View.GONE);
        }

        if(!invoiceId.equalsIgnoreCase("") ){
            invoice.setBackgroundColor(Color.parseColor("#2C357E"));
            invoice.setTextColor(Color.WHITE);
       //     create_memo.setVisibility(View.VISIBLE);
        }
        else {
            invoice.setBackgroundResource(R.drawable.button_text_border);
            invoice.setTextColor(Color.parseColor("#333333"));

        }

        if(productType.equalsIgnoreCase("virtual")){
            shipped.setVisibility(View.GONE);
        }
        else {
            if(!shipmentId.equalsIgnoreCase("")&& !shipmentId.equalsIgnoreCase("null")){
                shipped.setBackgroundColor(Color.parseColor("#2C357E"));
                shipped.setTextColor(Color.WHITE);

            }
            else {
                shipped.setBackgroundResource(R.drawable.button_text_border);
                shipped.setTextColor(Color.parseColor("#333333"));
            }
        }


         if(status.equalsIgnoreCase("delivered")){
             delivered.setBackgroundColor(Color.parseColor("#2C357E"));
             delivered.setTextColor(Color.WHITE);
             invoice.setEnabled(false);
             shipped.setEnabled(false);
             cancelled.setEnabled(false);

        }
        else if(status.equalsIgnoreCase("canceled")){
            cancelled.setBackgroundColor(Color.parseColor("#2C357E"));
            cancelled.setTextColor(Color.WHITE);
             invoice.setEnabled(false);
             shipped.setEnabled(false);
             delivered.setEnabled(false);

        }
        else {
            invoice.setEnabled(true);
            shipped.setEnabled(true);
            delivered.setEnabled(true);
            cancelled.setEnabled(true);
        }


        String subTotalVal = subTotal == null || subTotal.equalsIgnoreCase("null") ? "" : String.format("%.2f", Double.valueOf(subTotal));
        subtotal.setText("SubTotal : "+getString(R.string.Rs)+" "+subTotalVal);
        String shippingAmtTotalVal = shippingAmt == null || shippingAmt.equalsIgnoreCase("null") ? "" : String.format("%.2f", Double.valueOf(shippingAmt)) ;
        shipping.setText("Shipping : "+getString(R.string.Rs)+" "+shippingAmtTotalVal);
        String taxAmtVal = taxAmt == null || taxAmt.equalsIgnoreCase("null") ? "" : String.format("%.2f", Double.valueOf(taxAmt)) ;
        taxamount.setText("Tax : "+getString(R.string.Rs)+" "+taxAmtVal);
        String grandTotalVal = grandTotal == null || grandTotal.equalsIgnoreCase("null") ? "" : String.format("%.2f", Double.valueOf(grandTotal)) ;
        grand_total.setText("Grand Total : "+getString(R.string.Rs)+" "+grandTotalVal);

        invoice.setOnClickListener(this);
        shipped.setOnClickListener(this);
        cancelled.setOnClickListener(this);
        delivered.setOnClickListener(this);
      //  create_memo.setOnClickListener(this);
      //  cancel_udharBtn.setOnClickListener(this);

          //  create_memo.setVisibility(View.VISIBLE);




    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.invoice_Btn:
                if(invoiceId.equalsIgnoreCase("")){
                  alertDialog("Are you sure want to create Invoice?",String.valueOf(sellerId));
                }
                else if(status.equalsIgnoreCase("canceled")){
                    normalAlert("Orders whose Invoice/Shipment have been created cannot be cancelled");
                }
                else {
                    normalAlert("Invoice for this Order is already generated");
                }
                break;

            case R.id.shipped_Btn:
                if(!invoiceId.equalsIgnoreCase("") && shipmentId.equalsIgnoreCase("")){
                    Intent intent  = new Intent(OrderListActivity.this,CustomDialogActivity.class);
                    intent.putExtra("carrier_name",carrierName);
                    intent.putExtra("tracking_number",trackingNumber);
                    intent.putExtra("order_id",orderId);
                    startActivity(intent);
                }
                else if(status.equalsIgnoreCase("canceled")){
                    normalAlert("Orders whose Invoice/Shipment have been created cannot be cancelled");
                }
                else if(invoiceId.equalsIgnoreCase("")){
                    normalAlert("Create invoice first");
                }
                else {
                    normalAlert("Order has already been shipped");
                }
                break;

            case R.id.cancelled_Btn:

                if(productType.equalsIgnoreCase("virtual") && !invoiceId.equalsIgnoreCase("null")){
                        normalAlert("Orders whose Invoice have been created cannot be cancelled");

                }
                else if(!invoiceId.equalsIgnoreCase("") || !shipmentId.equalsIgnoreCase("")
                    ){
                    normalAlert("Orders whose Invoice/Shipment have been created cannot be cancelled");
                }
                else if(status.equalsIgnoreCase("canceled")){
                    normalAlert("This order already cancelled");
                }
                else {
                    alertDialog1("Are you sure want to cancel this order?",String.valueOf(sellerId));
                }
                break;

            case R.id.delivered_Btn:
                if(productType.equalsIgnoreCase("virtual") && !invoiceId.equalsIgnoreCase("null")){
                    deliveredOrder(String.valueOf(sellerId));
                }
                else if(!invoiceId.equalsIgnoreCase("") && !shipmentId.equalsIgnoreCase("")
                ){
                    deliveredOrder(String.valueOf(sellerId));
                }
                else if(status.equalsIgnoreCase("delivered")){
                    normalAlert("This order has been already delivered");
                }
                else {
                    if(productType.equalsIgnoreCase("virtual")){
                        normalAlert("Generate Invoice first");
                    }
                    else {
                        normalAlert("Generate Shipment to mark the Order as Delivered");
                    }

                }
                break;
            case R.id.create_memoBtn:
              //   createMemo("144");
                Intent intent = new Intent(OrderListActivity.this, CreditMemoDetailActivity.class);
                intent.putExtra("entityId",entity_Id);
                intent.putExtra("order_id",orderId);
                intent.putExtra("invoice_id",invoiceId);
                startActivity(intent);
                break;
            case R.id.cancel_udharBtn:
                cancelUdharDialog();
                break;
        }
    }

    public void createMemo(final String seller_Id){

        //     String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/createSellerOrderInvoice";

        String shopUrl  = sharedPreferences.getString("shop_url",null);
       // String url ="https://"+shopUrl+ Constant.BASE_URL+"customapi/createSellerOrderInvoice";
        String url = "https://m3.vdcprojects.xyz/qfixstandalonemaster/rest/V1/carts/mine/creditmemos";
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
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        JSONObject message = jsonObject.getJSONObject("message");
                        JSONArray jsonArray = message.getJSONArray("item");
                        if(jsonArray.length() > 0){
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                                  entity_Id = jsonObject2.getString("entity_id");
                            }

                            Intent intent = new Intent(OrderListActivity.this, CreditMemoDetailActivity.class);
                            intent.putExtra("entityId",entity_Id);
                            intent.putExtra("order_id",orderId);
                            startActivity(intent);
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
                Toast.makeText(OrderListActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
                params.put("Authorization", "Bearer ufst0mpdb54ua27ieqmul03bivlpxmpg");

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
        MaintainRequestQueue.getInstance(OrderListActivity.this).addToRequestQueue(req, "tag");



    }

    public void createInvoice(final String seller_Id){

   //     String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/createSellerOrderInvoice";

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"marketplace/createSellerOrderInvoice.php?order_id="+orderId+"&seller_id="+seller_Id;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Creating invoice.......");
        progressDialog.setCancelable(true);

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("id", orderId);
            jsonObject1.put("seller_id", seller_Id);
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
                    Toast.makeText(OrderListActivity.this,"Successfully created invoice",Toast.LENGTH_LONG).show();
                    invoice.setBackgroundColor(Color.parseColor("#2C357E"));
                    invoice.setTextColor(Color.WHITE);
                    invoiceId = "1";
                   /* Intent intent = new Intent(OrderListActivity.this,MyOrdersListActivity.class);
                    startActivity(intent);
                    finish();*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderListActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
        MaintainRequestQueue.getInstance(OrderListActivity.this).addToRequestQueue(req, "tag");



    }

    public void cancelInvoice(final String seller_Id){

      //  String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/cancelSellerOrder";
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"customapi/cancelSellerOrder.php";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Canceling order.......");

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("id", orderId);
            jsonObject1.put("seller_id", seller_Id);
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
                    Toast.makeText(OrderListActivity.this,response,Toast.LENGTH_LONG).show();
                    status = "canceled";
                    cancelled.setBackgroundColor(Color.parseColor("#2C357E"));
                    cancelled.setTextColor(Color.WHITE);
                    invoice.setEnabled(false);
                    shipped.setEnabled(false);
                    delivered.setEnabled(false);
                   /* Intent intent = new Intent(OrderListActivity.this,MyOrdersListActivity.class);
                    startActivity(intent);
                    finish();*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderListActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
        MaintainRequestQueue.getInstance(OrderListActivity.this).addToRequestQueue(req, "tag");



    }

    public void deliveredOrder(final String seller_Id){

      //  String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/deliveredSellerOrder";
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"customapi/deliveredSellerOrder.php";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Saving data.......");

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("id", orderId);
            jsonObject1.put("seller_id", seller_Id);
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
                    Toast.makeText(OrderListActivity.this,response,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(OrderListActivity.this,MyOrdersListActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderListActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
        MaintainRequestQueue.getInstance(OrderListActivity.this).addToRequestQueue(req, "tag");



    }
    public void alertDialog(final String message,final String sellerId){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderListActivity.this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        createInvoice(sellerId);
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void cancelUdharDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderListActivity.this);
        builder1.setMessage("Are you sure want to cancel Udhar Payment?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        JSONObject jsonObject1 = new JSONObject();
                        try {
                            jsonObject1.put("seller_id",String.valueOf(sellerId));
                            jsonObject1.put("order_id",orderId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        final String mRequestBody = jsonObject1.toString();
                        Log.e("Url...", "" + mRequestBody);
                         cancalUdharPayment(mRequestBody);
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void alertDialog1(final String message,final String sellerId){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderListActivity.this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cancelInvoice(sellerId);
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

//    public void alertDialogWithInput(final String sellerId,final String carrierName,final String trackingNumber){
//        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
//        dialogBuilder.setView(dialogView);
//
//        final EditText carrier_name    =     dialogView.findViewById(R.id.carrier_name);
//        final EditText tracking_number =     dialogView.findViewById(R.id.tracking_number);
//
//        if(carrierName.equalsIgnoreCase("null")){
//            carrier_name.setText("");
//        }
//        else {
//            carrier_name.setText(carrierName);
//            carrier_name.setEnabled(false);
//        }
//
//        if(trackingNumber.equalsIgnoreCase("null")){
//            tracking_number.setText("");
//        }
//        else {
//            tracking_number.setText(trackingNumber);
//            tracking_number.setEnabled(false);
//        }
//
//        dialogBuilder.setTitle("Shipment Detail");
//        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//
//                String carrierName = carrier_name.getText().toString();
//                String trackingNumber = tracking_number.getText().toString();
//
//                if(carrier_name.getText().toString().equalsIgnoreCase("")){
//                    carrier_name.setError("Enter carrier name");
//
//                }
//                else if(tracking_number.getText().toString().equalsIgnoreCase("")){
//                    tracking_number.setError("Enter tracking number");
//
//                }
//                else {
//
//                }
//
//
//            }
//        });
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //pass
//            }
//        });
//        AlertDialog b = dialogBuilder.create();
//        b.show();
//    }


    public void normalAlert(final String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderListActivity.this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
             try{
                 shipmentId = intent.getStringExtra("shippment");
                 shipped.setBackgroundColor(Color.parseColor("#2C357E"));
                 shipped.setTextColor(Color.WHITE);

                }catch (Exception e){

                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("shippment-message"));

    }

    public void cancalUdharPayment(final String mRequestBody){
        String tag_json_obj = "json_obj_req6";
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        // String url = "https://"+shopUrl + BASE_URL+"marketplace/changeProductVisibility";
        String url = "https://standalonetest.eduqfix.com/rest/V1/carts/mine/qfix-udhar/seller/udhar/reject";
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
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if(status) {

                        JSONObject message = jsonObject.getJSONObject("message");
                        Toast.makeText(OrderListActivity.this,"Your order is rejected for udhar payment",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(OrderListActivity.this,MyOrdersListActivity.class);
                        startActivity(intent);
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
       // MPOSApplication.getInstance().addToRequestQueue(jsonObjReq1, tag_json_obj);
        jsonObjReq1.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(OrderListActivity.this).addToRequestQueue(jsonObjReq1, "tag");

    }


}
