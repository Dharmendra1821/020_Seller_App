package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class ShippingRateActivity extends AppCompatActivity {

    EditText shipping_rate,shipping_upto;
    Button save;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    int sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_rate);

        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        LoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<LoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getCustomerDetails() != null ? loginResponse.getCustomerDetails().getId() : 0;

        try {
            if (sharedPreferences.getString("shipping_rate", null) == null) {
                for (int i = 0; i < loginResponse.getCustomerDetails().getCustomAttributes().size(); i++) {

                    String attributeCode = loginResponse.getCustomerDetails().getCustomAttributes().get(i).getAttributeCode();

                    if (attributeCode.equalsIgnoreCase("mpshipping_fixrate")) {
                        String value = loginResponse.getCustomerDetails().getCustomAttributes().get(i).getValue();
                        sharedPreferences.edit().putString("shipping_rate", value).apply();
                    }

                    if (attributeCode.equalsIgnoreCase("mpshipping_fixrate_upto")) {
                        String value = loginResponse.getCustomerDetails().getCustomAttributes().get(i).getValue();
                        sharedPreferences.edit().putString("shipping_upto", value).apply();
                    }
                }
            }
        }catch (Exception e){

        }

        getSupportActionBar().setTitle("Shipping rate");
        progressDialog = new ProgressDialog(ShippingRateActivity.this);
        shipping_rate = findViewById(R.id.shipping_fixed_rate);
        shipping_upto = findViewById(R.id.shipping_upto_rate);

        save          = findViewById(R.id.shipping_btn);

        try{

            if(sharedPreferences.getString("shipping_rate", null)==null){
                shipping_rate.setText("");
            }
            else {
                shipping_rate.setText(sharedPreferences.getString("shipping_rate", null));
            }

            if(sharedPreferences.getString("shipping_upto", null)==null){
                shipping_upto.setText("");
            }
            else {
                shipping_upto.setText(sharedPreferences.getString("shipping_upto", null));
            }
        }catch (Exception e){

        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String shippingRate = shipping_rate.getText().toString().trim();
                String shippingUpto = shipping_upto.getText().toString().trim();

                if(shipping_rate.getText().toString().equalsIgnoreCase("")){
                  shipping_rate.setError("All fields are required");
                }
                else if(shipping_upto.getText().toString().equalsIgnoreCase("")){
                    shipping_upto.setError("All fields are required");
                }
                else {
                    saveShippingRate(String.valueOf(sellerId),shippingRate,shippingUpto);
                }
            }
        });
    }

    public void saveShippingRate(final String seller_Id,final String shippingrate,final String shippingupto){

      //  String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/editSellerFixRateShipping";
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url ="https://"+shopUrl+ Constant.BASE_URL+"customapi/editSellerFixRateShipping";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Saving data.......");

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject request0bj = new JSONObject();
        try {

            jsonObject1.put("seller_id", seller_Id);
            jsonObject1.put("mpshipping_fixrate", shippingrate);
            jsonObject1.put("mpshipping_fixrate_upto", shippingupto);
           // jsonArray.put(jsonObject1);

            try {
                request0bj.put("request", jsonObject1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request0bj.toString();

        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    Toast.makeText(ShippingRateActivity.this,"Successfully saved data",Toast.LENGTH_LONG).show();

                    sharedPreferences.edit().putString("shipping_rate", shippingrate).apply();
                    sharedPreferences.edit().putString("shipping_upto", shippingupto).apply();
                    Intent intent = new Intent(ShippingRateActivity.this,MyProfileDashboardActivity.class);
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
                Toast.makeText(ShippingRateActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
        MaintainRequestQueue.getInstance(ShippingRateActivity.this).addToRequestQueue(req, "tag");



    }
}
