package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyList;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class AddDeliveryBoyActivity extends AppCompatActivity implements View.OnClickListener {

    EditText firstname,lastname,email,mobile,password,confirm_password;
    String firstName,lastName,emailValue,mobileNumber,passwordVal,confirmVal;
    Button submit,edit;
    ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String entityId;
    CheckBox status;
    String deliveryboyStatus;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    int sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_boy);

        getSupportActionBar().setTitle("Add Delivery Boy");
        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
        progressDialog = new ProgressDialog(AddDeliveryBoyActivity.this);

        firstname = findViewById(R.id.add_delivery_firstname);
        lastname = findViewById(R.id.add_delivery_lastname);
        email = findViewById(R.id.add_delivery_email);
        mobile = findViewById(R.id.add_delivery_mobile);
        password = findViewById(R.id.add_delivery_password);
        confirm_password = findViewById(R.id.add_delivery_confirmpassword);
        edit = findViewById(R.id.edit_delivery_boy);
        submit = findViewById(R.id.save_delivery_boy);
        status = findViewById(R.id.delivery_boy_status);


        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;


        entityId = getIntent().getStringExtra("entity_id");
        firstName = getIntent().getStringExtra("fname");
        lastName = getIntent().getStringExtra("lname");
        emailValue = getIntent().getStringExtra("email");
        mobileNumber = getIntent().getStringExtra("contact");
        deliveryboyStatus = getIntent().getStringExtra("status");

        if(!entityId.equalsIgnoreCase("")){
            password.setVisibility(View.GONE);
            confirm_password.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            status.setVisibility(View.VISIBLE);
            firstname.setText(firstName);
            lastname.setText(lastName);
            email.setText(emailValue);
            mobile.setText(mobileNumber);
            email.setEnabled(false);

            if(deliveryboyStatus.equalsIgnoreCase("1")){
                status.setChecked(true);
            }
            getSupportActionBar().setTitle("Edit Delivery Boy");

        }


        submit.setOnClickListener(this);
        edit.setOnClickListener(this);

        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    deliveryboyStatus = "1";
                }
                else {
                    deliveryboyStatus = "0";
                }
            }
        });



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.save_delivery_boy:
                 firstName = firstname.getText().toString();
                 lastName = lastname.getText().toString();
                 emailValue = email.getText().toString();
                 mobileNumber = mobile.getText().toString();
                 String passWord = password.getText().toString();
                 String confirmpassWord = confirm_password.getText().toString();

                if(firstname.getText().toString().equalsIgnoreCase("")){
                    firstname.setError("Enter first name");
                }
                else if(lastname.getText().toString().equalsIgnoreCase("")){
                    lastname.setError("Enter last name");
                }
                else if(email.getText().toString().equalsIgnoreCase("")){
                    email.setError("Invalid email");
                }
                else if(!emailValue.matches(emailPattern)){
                    email.setError("Invalid email");
                }
                else if(mobile.getText().toString().equalsIgnoreCase("")){
                    mobile.setError("Enter mobile");
                }
                else if(password.getText().toString().equalsIgnoreCase("")){
                    password.setError("Enter password");
                }
                else if(passWord.length()<6){
                    password.setError("Password length should be more than 6 character");
                }
                else if(!confirmpassWord.equalsIgnoreCase(passWord)){
                    confirm_password.setError("password does not match");
                }
                else {
                    addDeliveryBoy(String.valueOf(sellerId),firstName,lastName,emailValue,mobileNumber,passWord);
                }

                break;

            case R.id.edit_delivery_boy:
                 firstName = firstname.getText().toString();
                 lastName = lastname.getText().toString();
                 emailValue = email.getText().toString();
                 mobileNumber = mobile.getText().toString();


                if(firstname.getText().toString().equalsIgnoreCase("")){
                    firstname.setError("Enter first name");
                }
                else if(lastname.getText().toString().equalsIgnoreCase("")){
                    lastname.setError("Enter last name");
                }
                else if(email.getText().toString().equalsIgnoreCase("")){
                    email.setError("Invalid email");
                }
                else if(!emailValue.matches(emailPattern)){
                    email.setError("Invalid email");
                }
                else if(mobile.getText().toString().equalsIgnoreCase("")){
                    mobile.setError("Enter mobile");
                }
                else {
                    editDeliveryBoy(String.valueOf(sellerId),firstName,lastName,emailValue,mobileNumber,entityId,deliveryboyStatus);

                }
                break;
        }




    }


    public void addDeliveryBoy(final String sellerId,final String firstname,final String lastname,final String email,final String mobile,
                               final String password){

       // String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/saveDeliveryBoy";

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL2+"?function=AddDeliveryBoy&method=addDeliveryBoy";

        //String url =  Constant.REVAMP_URL1+"customapi/saveDeliveryBoy.php";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Adding delivery boy.......");

        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put("seller_id", sellerId);
            jsonObject1.put("first_name", firstname);
            jsonObject1.put("last_name", lastname);
            jsonObject1.put("email_id", email);
            jsonObject1.put("contact_number", mobile);
            jsonObject1.put("password", password);

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
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject data2 = data.getJSONObject("data");
                    String message = data2.getString("message");
                    Toast.makeText(AddDeliveryBoyActivity.this,message,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddDeliveryBoyActivity.this,DeliveryManagementActivity.class);
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
                Toast.makeText(AddDeliveryBoyActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
        MaintainRequestQueue.getInstance(AddDeliveryBoyActivity.this).addToRequestQueue(req, "tag");



    }

    public void editDeliveryBoy(final String sellerId,final String firstname,final String lastname,final String email,final String mobile,
                               final String entityId,final String status){
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"customapi/saveDeliveryBoy.php";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Editing delivery boy.......");

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("seller_id", sellerId);
            jsonObject1.put("firstname", firstname);
            jsonObject1.put("lastname", lastname);
      //      jsonObject1.put("email", email);
            jsonObject1.put("contact", mobile);
            jsonObject1.put("id", entityId);
            jsonObject1.put("status", status);
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
                    Toast.makeText(AddDeliveryBoyActivity.this,"Delivery boy edited",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddDeliveryBoyActivity.this,DeliveryManagementActivity.class);
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
                Toast.makeText(AddDeliveryBoyActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
        MaintainRequestQueue.getInstance(AddDeliveryBoyActivity.this).addToRequestQueue(req, "tag");



    }


}
//k2rusd6tp7