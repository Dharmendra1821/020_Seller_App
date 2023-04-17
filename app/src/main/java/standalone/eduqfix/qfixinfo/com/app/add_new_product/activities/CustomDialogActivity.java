package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.MainDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class CustomDialogActivity extends Activity {

    EditText qty,weight,color;
    EditText tracking_number;
    Button submit,cancel;
    String carrierName,trackingNumber;
    ProgressDialog progressDialog;
    String orderId;
    SharedPreferences sharedPreferences;
    int sellerId;
    String result;
    String Qty;
    String Weight;
    String Color;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_alert_dialog);

        progressDialog = new ProgressDialog(CustomDialogActivity.this);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;


        qty =  findViewById(R.id.shipped_qty);
        weight = findViewById(R.id.shipped_weight);
        color = findViewById(R.id.shipped_color);

        tracking_number =  findViewById(R.id.tracking_number);
        submit = findViewById(R.id.shipment_submit);
        cancel = findViewById(R.id.shipment_cancel);

        carrierName = getIntent().getStringExtra("carrier_name");
        trackingNumber = getIntent().getStringExtra("tracking_number");
        orderId = getIntent().getStringExtra("order_id");


        if(trackingNumber.equalsIgnoreCase("")){
            tracking_number.setText("");
        }
        else {
            tracking_number.setText(trackingNumber);
            tracking_number.setEnabled(false);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 Qty     = qty.getText().toString();
                 Weight  = weight.getText().toString();
                 Color   = color.getText().toString();
                 trackingNumber = tracking_number.getText().toString();

                if (qty.getText().toString().equalsIgnoreCase("")){
                    qty.setError("Enter quantity");
                }
                else if(tracking_number.getText().toString().equalsIgnoreCase("")){
                    tracking_number.setError("Enter tracking number");
                }
                else if(weight.getText().toString().equalsIgnoreCase("")){
                    weight.setError("Enter weight");
                }
                else if(color.getText().toString().equalsIgnoreCase("")){
                    color.setError("Enter color");
                }
                else {
                    //createShippment(String.valueOf(sellerId),Qty,Weight,Color,trackingNumber);
                    new RequestTask().execute();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);

                HttpClient client = new DefaultHttpClient();


                String adminToken = "Bearer " + sharedPreferences.getString("adminToken", null);
                String shopUrl = sharedPreferences.getString("shop_url", null);

                //   String url = "https://" + shopUrl + Constant.BASE_URL + "products/" + sku + "/media";
                String url = Constant.REVAMP_URL2+"?function=createShipment&method=Shipment";
                HttpPost post = new HttpPost(url);
                post.setHeader("Authorization", adminToken);

                Log.d("admin",adminToken);

                Log.d("url.....", url);
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                entityBuilder.addTextBody("order_id", orderId);
                entityBuilder.addTextBody("tracking_number", trackingNumber);
                entityBuilder.addTextBody("qty", Qty);
                entityBuilder.addTextBody("weight", Weight);
                entityBuilder.addTextBody("colored", Color);

                String boundary = "---------------" + UUID.randomUUID().toString();
                entityBuilder.setBoundary(boundary);


                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
                Log.v("result", result);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    // progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status_api");
                    String message = jsonObject.getString("message");
                    Handler handler = new Handler(getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            if(status.contains("Fail")){
                                Toast.makeText(CustomDialogActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(CustomDialogActivity.this, "Shipment created successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CustomDialogActivity.this, MainDashboardActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            progressDialog.dismiss();
                        }
                    });


                } else {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(CustomDialogActivity.this, "Please try again!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();                          }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        progressDialog.setMessage("Saving data ....");
                        progressDialog.show();
                    }
                });
            }catch (Exception e){

            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            //  progressDialog.dismiss();
        }
    }

    public void createShippment(final String seller_Id,final String Qty,final String Weight,final String Color,final String trackingNumber){
        String shopUrl  = sharedPreferences.getString("shop_url",null);
     //   String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/createSellerOrderShipment";
        String url = Constant.REVAMP_URL2+"?function=createShipment&method=Shipment";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Creating shipment.......");

        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put("order_id", orderId);
            jsonObject1.put("tracking_number", trackingNumber);
            jsonObject1.put("qty", Qty);
            jsonObject1.put("weight", Weight);
            jsonObject1.put("colored", Color);

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
                    String status = jsonObject.getString("status_api");
                    String message = jsonObject.getString("message");
                    if(status.contains("Fail")){
                        Toast.makeText(CustomDialogActivity.this,message,Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                    else {
                        Toast.makeText(CustomDialogActivity.this, response, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent("shippment-message");
                        intent.putExtra("shippment", "1");
                        LocalBroadcastManager.getInstance(CustomDialogActivity.this).sendBroadcast(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CustomDialogActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
        MaintainRequestQueue.getInstance(CustomDialogActivity.this).addToRequestQueue(req, "tag");



    }

}
