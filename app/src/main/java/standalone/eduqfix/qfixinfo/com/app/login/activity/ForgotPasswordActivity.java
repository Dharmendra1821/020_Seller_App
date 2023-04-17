package standalone.eduqfix.qfixinfo.com.app.login.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.ForgotPassword;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

import static android.content.Context.MODE_PRIVATE;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText usernameEditText;
    Button forgotPasswordButton;
    TextView messageTextView;
    SharedPreferences sharedPreferences = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().hide();

        usernameEditText = findViewById(R.id.usernameEditText);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        messageTextView = findViewById(R.id.messageTextView);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForgotPasswordLink();
            }
        });
    }

    public void getForgotPasswordLink(){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);

        MPOSServices mposServices = retrofit.create(MPOSServices.class);

        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setEmail(usernameEditText.getText().toString());
        forgotPassword.setTemplate("email_reset");
        forgotPassword.setWebsiteId(1);
        String adminToken = sharedPreferences.getString("adminToken",null);
        Gson gson = new Gson();
        String productGson = gson.toJson(forgotPassword);
        Log.d("add to cart","add to cart == "+productGson);
        mposServices.sendForgotPasswordLink(forgotPassword,adminToken).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if(response.code() == 200 || response.isSuccessful()){
                    Boolean flag = response.body();
                    try{
                        if(flag){
                            messageTextView.setVisibility(View.VISIBLE);
                            usernameEditText.setText("");
                            messageTextView.setText("Link to reset your password has been sent to your entered email.Please visit your email to reset the password.");
                        }else{
                            messageTextView.setText("Invalid username provided or something went wrong ! Please try after some time");
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                }else{
                    Toast.makeText(ForgotPasswordActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }
}
