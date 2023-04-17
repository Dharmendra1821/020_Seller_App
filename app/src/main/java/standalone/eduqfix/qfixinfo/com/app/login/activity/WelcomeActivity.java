package standalone.eduqfix.qfixinfo.com.app.login.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.AddProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.customer.activity.CustomerTabActivity;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginRequest;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.EmailActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.NewWebviewActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigDetails;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

public class WelcomeActivity extends AppCompatActivity {

    Button placeOrderButton,viewDashboardButton;
    LoginRequest customerDetails;
    String loginDetails = null;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog = null;
    TextView welcomeMessageTextView;
    Integer gender = null;
    String sellerGenderPrefix = "Mrs.";
    ImageView logout;
    Button addproductBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getSupportActionBar().hide();

        welcomeMessageTextView = findViewById(R.id.welcomeMessageTextView);
        logout = findViewById(R.id.welcome_screen_logout);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);

            if (sharedPreferences.contains("customerLoginDetails")) {
                loginDetails = sharedPreferences.getString("customerLoginDetails", null);
                customerDetails = new Gson().fromJson(loginDetails, new TypeToken<LoginRequest>() {
                }.getType());

                if (customerDetails != null) {
                    String customerDetails = sharedPreferences.getString("CustomerDetails", null);
                    LoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<LoginResponse>() {
                    }.getType());
                    if (loginResponse != null && loginResponse.getCustomerDetails() != null) {
                        gender = loginResponse.getCustomerDetails().getGender();
                        if(gender!=null) {
                            if (gender == 1) {
                                sellerGenderPrefix = "Mr.";
                            }
                        }else {
                            sharedPreferences.edit().clear().apply();
                            Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    String name = loginResponse != null && loginResponse.getCustomerDetails() != null ? loginResponse.getCustomerDetails().getFirstname() : "NA";
                    welcomeMessageTextView.setText(String.format("Welcome  %s %s", sellerGenderPrefix, name));
                }
            }

        viewDashboardButton = findViewById(R.id.viewDashboardButton);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        addproductBtn = findViewById(R.id.add_product_listpage);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, CustomerTabActivity.class);
                startActivity(intent);

            }
        });

        viewDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, NewWebviewActivity.class);
                startActivity(intent);
            }
        });

        addproductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, AddProductListActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().clear().apply();
                Intent intent = new Intent(WelcomeActivity.this, EmailActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

//    public void getCustomerToken(){
//
//        progressDialog = new ProgressDialog(WelcomeActivity.this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
//
//        Retrofit retrofit = mposRetrofitClient.getClient(Constant.BASE_DOMAIN_URL);
//        MPOSServices mposServices = retrofit.create(MPOSServices.class);
//
//        ConfigDetails configDetails = new ConfigDetails();
//        configDetails.setUsername(customerDetails.getRequest().getUsername());
//        configDetails.setPassword(customerDetails.getRequest().getPassword());
//
//        mposServices.getUserToken(configDetails).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if(response.code() == 200 || response.isSuccessful()){
//                    progressDialog.dismiss();
//                    SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
//                    sharedPreferences.edit().putString("customerToken",response.body()).apply();
//                }else if(response.code() == 400){
//                    Toast.makeText(getApplicationContext(),"Bad Request",Toast.LENGTH_LONG).show();
//                    progressDialog.dismiss();
//                }else if(response.code() == 401){
//                    Toast.makeText(getApplicationContext(),"Unauthorized",Toast.LENGTH_LONG).show();
//                    progressDialog.dismiss();
//                }else if(response.code() == 500){
//                    Toast.makeText(getApplicationContext(),"Internal server error",Toast.LENGTH_LONG).show();
//                    progressDialog.dismiss();
//                }else {
//                    progressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                //progressDialog.hide();
//                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
//            }
//        });
//    }
}
