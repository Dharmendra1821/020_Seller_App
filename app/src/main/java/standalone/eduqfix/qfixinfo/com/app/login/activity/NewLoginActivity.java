package standalone.eduqfix.qfixinfo.com.app.login.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.AddDeliveryBoyActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DeliveryManagementActivity;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginRequest;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.EmailActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.MainDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.OtpRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.OtpVerifyRequest;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSApplication;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class NewLoginActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedPreferences;

    EditText phoneNumber;
    EditText otpNumber;
    Button sendOtpBtn,continueBtn;
    TextInputLayout inputLayout;
    ProgressDialog progressDialog;
    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        getSupportActionBar().hide();
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(NewLoginActivity.this);

        phoneNumber = findViewById(R.id.new_phone_number);
        otpNumber = findViewById(R.id.new_otp_number);
        inputLayout = findViewById(R.id.new_otp_number_layout);
        sendOtpBtn = findViewById(R.id.create_otp);
        continueBtn = findViewById(R.id.continue_with_email);

        String phoneNum = getIntent().getStringExtra("phone");
        if(phoneNum != null){
            phoneNumber.setText(phoneNum);
            sendOtp(phoneNum);
        }

        sendOtpBtn.setOnClickListener(this);
        continueBtn.setOnClickListener(this);

        phoneNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() < 10){
                    sendOtpBtn.setText("Send Otp");
                    inputLayout.setVisibility(View.GONE);
                    otpNumber.setText("");
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.create_otp:
                if(sendOtpBtn.getText().toString().equalsIgnoreCase("Send Otp")){
                    String mobile = phoneNumber.getText().toString().trim();
                    if(mobile.equalsIgnoreCase("") || mobile.length() < 10){
                        Toast.makeText(NewLoginActivity.this,"Invalid phone number",Toast.LENGTH_LONG).show();
                    }
                    else {
                        sendOtp(mobile);
                    }
                }
                if(sendOtpBtn.getText().toString().equalsIgnoreCase("Verify Otp")){
                    String mobile = phoneNumber.getText().toString().trim();
                    String otpNum = otpNumber.getText().toString().trim();
                    if(mobile.equalsIgnoreCase("") || mobile.length() < 10){
                        Toast.makeText(NewLoginActivity.this,"Invalid phone number",Toast.LENGTH_LONG).show();
                    }
                    else if(otpNum.equalsIgnoreCase("")){
                        Toast.makeText(NewLoginActivity.this,"Enter otp number",Toast.LENGTH_LONG).show();
                    }
                    else {
                        verifyOtp(mobile,otpNum);
                    }
                }


                break;
            case R.id.continue_with_email:
                Intent intent = new Intent(NewLoginActivity.this,LoginActivity.class);
                startActivity(intent);
                break;


        }
    }

    public void sendOtp(final String mobile){
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url ="https://"+shopUrl+ Constant.SKU_URL+"customapi/getSellerotp";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Sending otp .......");

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("mobile", mobile);
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
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equalsIgnoreCase("Success")){
                        inputLayout.setVisibility(View.VISIBLE);
                        sendOtpBtn.setText("Verify Otp");
                    }
                    else {
                        Toast.makeText(NewLoginActivity.this,"Your Number is not registered",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewLoginActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(NewLoginActivity.this).addToRequestQueue(req, "tag");



    }



    public void verifyOtp(final String mobile,final String otpNumber){

            progressDialog = new ProgressDialog(NewLoginActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);

            sharedPreferences= getSharedPreferences("StandAlone", MODE_PRIVATE);
            String firebaseToken = sharedPreferences.getString("firebase_token", null);



            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.SKU_URL);
            MPOSServices mposServices = retrofit.create(MPOSServices.class);
            OtpVerifyRequest request = new OtpVerifyRequest();
            request.setMobile(mobile);
            request.setOtp(otpNumber);
            request.setDevice_id(firebaseToken);

            OtpRequest loginRequest = new OtpRequest();
            loginRequest.setRequest(request);


            final String data = gson.toJson(loginRequest);
            Log.d("Request Data ===","Request Data ==="+data);
            mposServices.verifyOtp(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if(response.code() == 200){
                        progressDialog.dismiss();
                        SharedPreferences sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
                        String responseDetails = gson.toJson(response.body());

                        final String data = gson.toJson(response.body());
                        Log.d("Request Data ===","Request Data ==="+data);

                        String isSeller = null;
                        if(response.body() != null){
                            isSeller = response.body().getIsSeller();
                            sharedPreferences.edit().putString("customerLoginDetails",data).apply();
                        }
                        sharedPreferences.edit().putString("isSeller",isSeller).apply();
                        sharedPreferences.edit().putString("CustomerDetails", responseDetails).apply();
                        sharedPreferences.edit().putString("customerToken", response.body().getCustomerToken()).apply();
                        sharedPreferences.edit().putString("customerPassword","").apply();
                        sharedPreferences.edit().putString("adminToken",response.body().getSellerAdminToken()).apply();


                        SharedPreferences sharedPreferences1 = getSharedPreferences("StandAlone", MODE_PRIVATE);

                        // Put the json format string to SharedPreferences object.
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("customer_json", data);
                        editor.commit();

                        Intent intent = new Intent(NewLoginActivity.this,MainDashboardActivity.class);
                        startActivity(intent);
                        finish();

                    }else{

                        progressDialog.dismiss();

                    }
                }
                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                }
            });

    }
}
