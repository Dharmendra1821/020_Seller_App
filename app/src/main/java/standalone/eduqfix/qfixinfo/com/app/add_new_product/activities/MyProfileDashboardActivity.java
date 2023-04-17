package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.EmailActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.SellerDashboardActivity;

public class MyProfileDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout profile_layout;
    LinearLayout shipping_layout;
    LinearLayout logout_layout;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_dashboard);

        getSupportActionBar().setTitle("My Profile Dashboard");
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);

        profile_layout = findViewById(R.id.my_profile_layout);
        shipping_layout = findViewById(R.id.my_shipping_layout);
        logout_layout = findViewById(R.id.my_logout_layout);

        profile_layout.setOnClickListener(this);
        shipping_layout.setOnClickListener(this);
        logout_layout.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
//        LoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<LoginResponse>() {
//        }.getType());
//
//        for(int i=0;i < loginResponse.getCustomerDetails().getCustomAttributes().size();i++){
//
//            String attribute = loginResponse.getCustomerDetails().getCustomAttributes().get(i).getAttributeCode();
//            if(attribute.equalsIgnoreCase("shipping_type")){
//                String value = loginResponse.getCustomerDetails().getCustomAttributes().get(i).getValue();
//                Log.d("value......",value);
//
//                if(value.equalsIgnoreCase("qshipping")){
//                    shipping_layout.setVisibility(View.GONE);
//                }
//                else {
//                    shipping_layout.setVisibility(View.VISIBLE);
//                }
//            }
//        }

        shipping_layout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.my_profile_layout:
                Intent intent = new Intent(MyProfileDashboardActivity.this,ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.my_shipping_layout:
                Intent intent1 = new Intent(MyProfileDashboardActivity.this,ShippingRateActivity.class);
                startActivity(intent1);
                break;

            case R.id.my_logout_layout:
                sharedPreferences.edit().putString("CustomerDetails","").apply();
               // sharedPreferences.edit().clear().apply();
                Intent intent2 = new Intent(MyProfileDashboardActivity.this, EmailActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
               // finish();
                break;

        }
    }
}
