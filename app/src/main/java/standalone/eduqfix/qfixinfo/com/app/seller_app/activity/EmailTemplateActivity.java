package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;

public class EmailTemplateActivity extends AppCompatActivity {

    EditText campaignName,subjectLine,fromEmail,fromName;
    SharedPreferences sharedPreferences;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_template);

        campaignName = findViewById(R.id.email_campaign_name);
        subjectLine = findViewById(R.id.email_subject_line);
        fromEmail = findViewById(R.id.email_from_email);
        fromName  = findViewById(R.id.email_from_name);
        next     = findViewById(R.id.email_next);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());

        String email = loginResponse.getSellerDetails().getEmail();

        fromName.setText(shopUrl);
        fromEmail.setText(email);
        fromName.setEnabled(false);
        fromEmail.setEnabled(false);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String campaign = campaignName.getText().toString();
                String subject = subjectLine.getText().toString();

                if(campaignName.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(EmailTemplateActivity.this,"Enter Campaign Name",Toast.LENGTH_LONG).show();
                }
                else if(subjectLine.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(EmailTemplateActivity.this,"Enter Subject Line",Toast.LENGTH_LONG).show();
                }
                else {
                    sharedPreferences.edit().putString("campaign",campaign).apply();
                    sharedPreferences.edit().putString("subject",subject).apply();
                    Intent intent = new Intent(EmailTemplateActivity.this,PrintFlyerActivity.class);
                    intent.putExtra("from_page","email");
                    startActivity(intent);
                }

            }
        });
    }
}
