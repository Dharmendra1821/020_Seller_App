package standalone.eduqfix.qfixinfo.com.app.seller_app.community;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.MainDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;

public class AddSellerActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    EditText addEmail;
    Button verifyBtn,submitBtn;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    LinearLayout sellerInfo,addSellerInfo;
    TextView firstName,lastName,mobile,shop_url,shop_title;
    EditText firstNameEdit,lastNameEdit,mobileEdit,shopUrlEdit,shopTitleEdit;
    String microMarketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seller);

        getSupportActionBar().setTitle("Add Seller");

        progressDialog = new ProgressDialog(AddSellerActivity.this);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        microMarketId = sharedPreferences.getString("microMarketId",null);

        addEmail = findViewById(R.id.add_email_micro);
        verifyBtn = findViewById(R.id.verifyBtn);
        sellerInfo = findViewById(R.id.micro_seller_info);
        firstName = findViewById(R.id.micro_seller_firstname);
        lastName = findViewById(R.id.micro_seller_lastname);
        mobile = findViewById(R.id.micro_seller_mobile);
        shop_url = findViewById(R.id.micro_seller_shopurl);
        shop_title = findViewById(R.id.micro_seller_shoptitle);
        addSellerInfo = findViewById(R.id.new_seller_layout);
        firstNameEdit = findViewById(R.id.add_seller_firstname);
        lastNameEdit = findViewById(R.id.add_seller_lastname);
        mobileEdit = findViewById(R.id.add_seller_contact);
        shopUrlEdit = findViewById(R.id.add_seller_shop_url);
        shopTitleEdit = findViewById(R.id.add_seller_shop_name);
        submitBtn = findViewById(R.id.addSellerBtn);

        verifyBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);



    }

    public void verifySeller(final String email){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl+ BASE_URL+"customapi/checkSellerEmailExists";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("email",email);
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request.toString();

        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Verifying email.......");
        progressDialog.show();


        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equalsIgnoreCase("success")) {
                        JSONObject sellerDetails = jsonObject.getJSONObject("seller_data");
                        String firstNameSeller = sellerDetails.getString("firstname");
                        String lastNameSeller = sellerDetails.getString("lastname");
                        String mobileSeller = sellerDetails.getString("mobile");
                        String shopUrlSeller = sellerDetails.getString("shop_url");
                        String shopTitleSeller = sellerDetails.getString("shop_title");

                        sellerInfo.setVisibility(View.VISIBLE);
                        addSellerInfo.setVisibility(View.GONE);
                        firstName.setText(String.format("FirstName: %s", firstNameSeller));
                        lastName.setText(String.format("LastName: %s", lastNameSeller));
                        mobile.setText(String.format("Mobile: %s", mobileSeller));
                        shop_url.setText(String.format("Shop Url: %s", shopUrlSeller));
                        shop_title.setText(String.format("Shop Title: %s", shopTitleSeller));

                    }
                    else {
                        addSellerInfo.setVisibility(View.VISIBLE);
                        sellerInfo.setVisibility(View.GONE);
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
        MaintainRequestQueue.getInstance(AddSellerActivity.this).addToRequestQueue(req, "tag");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.verifyBtn:
                if(addEmail.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(AddSellerActivity.this,"Add valid email",Toast.LENGTH_LONG).show();
                }
                else if(!addEmail.getText().toString().matches(emailPattern)){
                    Toast.makeText(AddSellerActivity.this,"Add valid email",Toast.LENGTH_LONG).show();
                }
                else {
                    verifySeller(addEmail.getText().toString());
                }
                break;
            case R.id.addSellerBtn:
                if(firstNameEdit.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(AddSellerActivity.this,"Enter first name",Toast.LENGTH_LONG).show();
                }
                else if(lastNameEdit.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(AddSellerActivity.this,"Enter last name",Toast.LENGTH_LONG).show();
                }
                else if(mobileEdit.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(AddSellerActivity.this,"Enter contact number",Toast.LENGTH_LONG).show();
                }
                else if(shopTitleEdit.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(AddSellerActivity.this,"Enter shop name",Toast.LENGTH_LONG).show();
                }
                else if(shopUrlEdit.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(AddSellerActivity.this,"Enter shop url",Toast.LENGTH_LONG).show();
                }
                else {
                     checkShopUrl();
                }
                break;
        }
    }

    public void checkShopUrl(){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl+ BASE_URL+"customapi/checkshopUrlexists";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("shop_url",shopUrlEdit.getText().toString());
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request.toString();

        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Verifying email.......");
        progressDialog.show();


        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equalsIgnoreCase("success")) {
                        addNewSeller();
                    }
                    else {
                        Toast.makeText(AddSellerActivity.this,"Shop Url Already Exits!",Toast.LENGTH_LONG).show();
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
        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(AddSellerActivity.this).addToRequestQueue(req, "tag");
    }

    public void addNewSeller(){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl+ BASE_URL+"customapi/addSellerTocommunity";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject request = new JSONObject();

        try {
            jsonObject1.put("emailverified",true);
            jsonObject1.put("email",addEmail.getText().toString());
            jsonObject1.put("micromarket_id",microMarketId);
            jsonObject2.put("firstname",firstNameEdit.getText().toString());
            jsonObject2.put("lastname",lastNameEdit.getText().toString());
            jsonObject2.put("contact_number",mobileEdit.getText().toString());
            jsonObject2.put("shop_name",shopTitleEdit.getText().toString());
            jsonObject2.put("shop_url",shopUrlEdit.getText().toString());
            jsonObject1.put("seller",jsonObject2);
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request.toString();
        Log.d("request",mRequestBody);

        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Verifying email.......");
        progressDialog.show();


        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equalsIgnoreCase("success")) {
                        String sellerId = jsonObject.getString("sellerId");
                        showAlert(sellerId);
                    }
                    else {
                        Toast.makeText(AddSellerActivity.this,"Shop Url Already Exits!",Toast.LENGTH_LONG).show();
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
        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(AddSellerActivity.this).addToRequestQueue(req, "tag");
    }


    public void showAlert(final String sellerId){
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(AddSellerActivity.this)
                //set message, title, and icon
                .setTitle("Alert")
                .setMessage("Seller added successfully! Your seller id is : "+sellerId)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Intent intent = new Intent(AddSellerActivity.this,CommunityActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .create();
        myQuittingDialogBox.show();
    }
}
