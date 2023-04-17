package standalone.eduqfix.qfixinfo.com.app.login.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.databinding.ActivityLoginBinding;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.login.viewmodel.LoginViewModel;
import standalone.eduqfix.qfixinfo.com.app.qpos_login.activity.QPOSLoginActivity;
import standalone.eduqfix.qfixinfo.com.app.qpos_login.activity.SelectMPOSDeviceActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.MainDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigDetails;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSApplication;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginBinding;

    public NewLoginResponse loginResponse;
    static ProgressDialog progressDialog = null;

    MPOSApplication application;
    private static final int REQUEST_CODE_PHONE_STATE_PERMISSION = 101;
    private static long back_pressed;

    SharedPreferences sharedPreferences;

    EditText usernameEditText;
    EditText passwordEditText;
    TextView show_password;
    Button continue_with_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        try {

            loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
            application = new MPOSApplication();

            usernameEditText = findViewById(R.id.usernameEditText);
            passwordEditText = findViewById(R.id.passwordEditText);
            show_password    = findViewById(R.id.show_password);
            continue_with_phone = findViewById(R.id.continue_with_phone);



            String email = getIntent().getStringExtra("email");
            usernameEditText.setText(email);

            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.CALL_PHONE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("Please give us permission so we can help you with phone calls!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showBuiltInPermissionDialog();
                    }
                });
                builder.setNegativeButton("No!", null);
                builder.show();
            }
            else {
                showBuiltInPermissionDialog();
            }

             loginBinding.setLoginViewModel(new LoginViewModel(getApplicationContext(), LoginActivity.this,email));
             loginBinding.executePendingBindings();

//             sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
//             String adminToken = sharedPreferences.getString("adminToken", null);
//            if (adminToken == null) {
//                getAdminToken();
//            }

            show_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(show_password.getText().toString().equalsIgnoreCase("Show password")){
                        passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        show_password.setText("Hide password");
                    }
                    else {
                        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        show_password.setText("Show password");
                    }

                }
            });


        }catch (Exception e){

        }

        continue_with_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,NewLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        Log.d("mesghjgdjasd","jbjkbjkb");
        /*if(progressDialog == null){
            progressDialog = new ProgressDialog(view.getContext());
        }*/
        if (message != null) {
            if(message.equalsIgnoreCase("success")){
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("StandAlone",MODE_PRIVATE);
                String isSeller = sharedPreferences.getString("isSeller",null);
                if(isSeller != null && isSeller.equalsIgnoreCase("true")){
                    Intent intent = new Intent(view.getContext(), MainDashboardActivity.class);
                    view.getContext().startActivity(intent);
                    ((Activity) view.getContext() ).finish();
                }else{
                    Intent intent = new Intent(view.getContext(), SelectMPOSDeviceActivity.class);
                    view.getContext().startActivity(intent);
                    ((Activity) view.getContext() ).finish();
                }
            }else if(message.equalsIgnoreCase("show")){
                /*progressDialog.setMessage("Loading...");
                progressDialog.hide();*/

            }else if(message.equalsIgnoreCase("hide")){
            }else if(message.equalsIgnoreCase("fail")){
            }else if(message.equalsIgnoreCase("forgotPassword")){
                Intent intent = new Intent(view.getContext(), ForgotPasswordActivity.class);
                view.getContext().startActivity(intent);
            }else{
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showBuiltInPermissionDialog(){
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.REQUEST_INSTALL_PACKAGES},
                                REQUEST_CODE_PHONE_STATE_PERMISSION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PHONE_STATE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                application.writeToFile("=================Logs==============");
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.CALL_PHONE)) {
                    Toast.makeText(LoginActivity.this, "Please go to settings!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finishAffinity();
    }

    public void getAdminToken(){
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);

        ConfigDetails configDetails = new ConfigDetails();
        configDetails.setUsername("admin");
        configDetails.setPassword("Suraj@1234");

        Gson gson = new Gson();
        String productGson = gson.toJson(configDetails);
        Log.d("Product JSON","Product Json == "+productGson);

        mposServices.getAdminToken(configDetails).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 200 || response.isSuccessful()){
                    //progressDialog.hide();
                    SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
                    sharedPreferences.edit().putString("adminToken",response.body()).apply();
                }else {
                    //progressDialog.hide();
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //progressDialog.hide();
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });

    }
}
