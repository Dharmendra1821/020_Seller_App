package standalone.eduqfix.qfixinfo.com.app.qpos_login.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.databinding.ActivityQposloginBinding;
import standalone.eduqfix.qfixinfo.com.app.invoices.activity.SearchInvoiceActivity;
import standalone.eduqfix.qfixinfo.com.app.login.activity.LoginActivity;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.qpos_login.model.QPOSLogin;
import standalone.eduqfix.qfixinfo.com.app.qpos_login.model.RegisterDevice;
import standalone.eduqfix.qfixinfo.com.app.qpos_login.model.RegisterDeviceRequest;
import standalone.eduqfix.qfixinfo.com.app.qpos_login.model.RegisterDeviceResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

import static android.content.Context.MODE_PRIVATE;

public class QPOSLoginActivity extends AppCompatActivity {

    ActivityQposloginBinding activityQposloginBinding;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog = null;
    Integer userId = null;
    MPOSRetrofitClient mposRetrofitClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qposlogin);

        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
        activityQposloginBinding = DataBindingUtil.setContentView(this,R.layout.activity_qposlogin);
        activityQposloginBinding.setMposLogin(new QPOSLogin());
        String details = sharedPreferences.getString("CustomerDetails",null);
        Gson gson = new Gson();
        NewLoginResponse response = gson.fromJson(details,NewLoginResponse.class);
        userId = Integer.valueOf(response.getSellerDetails().getId());
        activityQposloginBinding.setMposActivity(this);

    }

    public void doMposLogin(final QPOSLogin login){
        if(!TextUtils.isEmpty(login.getMosambeeId()) && !TextUtils.isEmpty(login.getMosambeePassword())){
            Log.d("Success","Success");
            mposRetrofitClient = new MPOSRetrofitClient();
            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
            MPOSServices mposServices = retrofit.create(MPOSServices.class);
            RegisterDeviceRequest request = new RegisterDeviceRequest();
            RegisterDevice device = new RegisterDevice();
            device.setDeviceId(login.getMosambeeId());
            device.setUserId(userId);//userId
            request.setRequest(device);

            Gson gson = new Gson();
            String requestJson = gson.toJson(request);
            Log.d("MPOSRequest","MPOSRequest=="+requestJson);

            mposServices.registerDevice(request).enqueue(new Callback<RegisterDeviceResponse>() {
                @Override
                public void onResponse(@NonNull Call<RegisterDeviceResponse> call, @NonNull Response<RegisterDeviceResponse> response) {
                    if(response.code() == 200){
                        Gson gson = new Gson();
                        String gsonObj = gson.toJson(login);
                        sharedPreferences.edit().putString("MPOSCredentials",gsonObj).apply();
                        showAlert();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<RegisterDeviceResponse> call, @NonNull Throwable t) {
                    Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void showAlert(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        final View customView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_device_register_dialog,null);
        final Button doneButton = customView.findViewById(R.id.doneButton);
        alertDialog.setView(customView);
        alertDialog.show();
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QPOSLoginActivity.this, SearchInvoiceActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity,menu);
        MenuItem item = menu.findItem(R.id.menu_item_cart);
        MenuItem item1 = menu.findItem(R.id.menu_item_refresh);
        item1.setVisible(false);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i){
            case R.id.menu_item_logout:
                sharedPreferences.edit().clear().apply();
                Intent intent = new Intent(QPOSLoginActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }
}
