package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;

import net.hydromatic.linq4j.Linq4j;
import net.hydromatic.linq4j.function.Predicate1;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.activity.LoginActivity;
import standalone.eduqfix.qfixinfo.com.app.login.activity.NewLoginActivity;
import standalone.eduqfix.qfixinfo.com.app.login.activity.SelfRegisterActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ProductAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Product;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSApplication;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

public class EmailActivity extends AppCompatActivity {

    EditText sellerEmail,phoneEditText;
    Button next;
    ProgressDialog progressDialog;
    Button selfRegisterBtn;
    private static final int REQUEST_CODE_PHONE_STATE_PERMISSION = 101;
    MPOSApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        getSupportActionBar().hide();
        application = new MPOSApplication();
        sellerEmail = findViewById(R.id.emailEditText);
        next        = findViewById(R.id.emailButton);
        selfRegisterBtn = findViewById(R.id.self_register_btn);
        progressDialog = new ProgressDialog(EmailActivity.this);

        if (ActivityCompat.shouldShowRequestPermissionRationale(EmailActivity.this, Manifest.permission.CALL_PHONE)) {
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = sellerEmail.getText().toString();

                if(sellerEmail.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(EmailActivity.this,"Enter atleast one field",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(email.contains("@")){
                        getShopUrl(email,"email");
                    }
                    else {
                        getShopUrl(email,"phone");
                    }

                }
            }
        });

        selfRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailActivity.this, SelfRegisterActivity.class);
                startActivity(intent);
            }
        });

    }


    public void getShopUrl(final String email,final String flag){
        progressDialog.setMessage("Validating......");
        progressDialog.show();
//        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
//        Retrofit retrofit = mposRetrofitClient.getClient(Constant.DUMMY_URL);
//        MPOSServices services = retrofit.create(MPOSServices.class);
        SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        //  sharedPreferences.edit().putString("shop_url",responseString).apply();
        sharedPreferences.edit().putString("shop_url","uatstandalone").apply();
        if(flag.equalsIgnoreCase("email")){
            Intent intent = new Intent(EmailActivity.this, LoginActivity.class);
            intent.putExtra("email",email);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(EmailActivity.this, NewLoginActivity.class);
            intent.putExtra("phone",email);
            startActivity(intent);
            finish();
        }
//        services.getShopUrl(email).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if(response.code() == 200 ||response.isSuccessful()){
//                    progressDialog.dismiss();
//
//                    String responseString = String.valueOf(response.body());
//                    Log.d("response...",responseString);
//                    SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
//                  //  sharedPreferences.edit().putString("shop_url",responseString).apply();
//                   sharedPreferences.edit().putString("shop_url","uatstandalone").apply();
//                   if(flag.equalsIgnoreCase("email")){
//                       Intent intent = new Intent(EmailActivity.this, LoginActivity.class);
//                       intent.putExtra("email",email);
//                       startActivity(intent);
//                       finish();
//                   }
//                   else {
//                       Intent intent = new Intent(EmailActivity.this, NewLoginActivity.class);
//                       intent.putExtra("phone",email);
//                       startActivity(intent);
//                       finish();
//                   }
//
//                }else if(response.code() == 400){
//                    progressDialog.dismiss();
//                    Toast.makeText(EmailActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
//                }else if(response.code() == 401){
//                    progressDialog.dismiss();
//                    Toast.makeText(EmailActivity.this,"Unauthorised",Toast.LENGTH_LONG).show();
//                }else if(response.code() == 500){
//                    progressDialog.dismiss();
//                    Toast.makeText(EmailActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
//                }else {
//                    progressDialog.dismiss();
//                    Toast.makeText(EmailActivity.this,"Something went wrongggg",Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(EmailActivity.this,"Something went ",Toast.LENGTH_LONG).show();
//            }
//        });

        if (getCurrentFocus() !=null){
            InputMethodManager imm = (InputMethodManager) getSystemService((INPUT_METHOD_SERVICE));
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    public void showBuiltInPermissionDialog(){
        ActivityCompat.requestPermissions(EmailActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.REQUEST_INSTALL_PACKAGES},
                REQUEST_CODE_PHONE_STATE_PERMISSION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PHONE_STATE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                application.writeToFile("=================Logs==============");
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(EmailActivity.this, Manifest.permission.CALL_PHONE)) {
                    Toast.makeText(EmailActivity.this, "Please go to settings!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }


}
