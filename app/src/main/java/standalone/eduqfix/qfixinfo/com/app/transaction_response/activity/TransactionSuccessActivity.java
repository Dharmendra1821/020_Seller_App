package standalone.eduqfix.qfixinfo.com.app.transaction_response.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.customer.activity.CustomerTabActivity;
import standalone.eduqfix.qfixinfo.com.app.invoices.activity.SearchInvoiceActivity;
import standalone.eduqfix.qfixinfo.com.app.login.activity.WelcomeActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.MainDashboardActivity;

public class TransactionSuccessActivity extends AppCompatActivity {
    int CustomerId,CustomerAddressId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_success);

        getSupportActionBar().hide();

        TextView amountPaidTextView = findViewById(R.id.amountPaidTextView);
        Button exitButton = findViewById(R.id.exitButton);
        Button continueButton = findViewById(R.id.continueButton);

        Bundle bundle = getIntent().getExtras();
        String amount = bundle.getString("Amount");
        CustomerId = bundle.getInt("CustomerId");
        CustomerAddressId = bundle.getInt("CustomerAddressId");

        final Boolean isSeller =  bundle.getBoolean("isSeller");
        amountPaidTextView.setText("Paid Amount : "+ amount);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSeller){
                    Intent intent = new Intent(TransactionSuccessActivity.this, SearchInvoiceActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(TransactionSuccessActivity.this, MainDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
