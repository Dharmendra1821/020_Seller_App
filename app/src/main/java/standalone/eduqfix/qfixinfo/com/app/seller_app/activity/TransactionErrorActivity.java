package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.invoices.activity.SearchInvoiceActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.DetailResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.DoTransactionRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.HeaderRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.PaymentModeEnum;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Utility.UIUtils;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.activities.BasePineActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.helper.PineServiceHelper;
import standalone.eduqfix.qfixinfo.com.app.transaction_response.activity.TransactionSuccessActivity;

public class TransactionErrorActivity extends BasePineActivity {

    TextView failTxt;
    Button continueButton,upiStatus;
    private static TransactionErrorActivity instance;
    static String newMarkPaidToken;
    @NonNull
    private PineServiceHelper pineSH;
    String newAccessCode;
    String posAcknowledgeResponse;
    String posAcknowledgeRequest;
    int transactionType;
    String billingNumber;
    String amount;
    int customerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_error);

        getSupportActionBar().hide();
        pineSH = PineServiceHelper.getInstance();
        setData();
        instance = this;

        failTxt = findViewById(R.id.payment_fail_reason);
        continueButton = findViewById(R.id.fail_continueButton);
        upiStatus = findViewById(R.id.get_upi_status);

        Bundle bundle = getIntent().getExtras();
        String reason = bundle.getString("fail_reason");
        billingNumber = bundle.getString("billing_number");
        amount        = bundle.getString("amount");
        customerId    = bundle.getInt("customerId");

        failTxt.setText(reason);

        upiStatus.setVisibility(View.GONE);

        if(reason.equalsIgnoreCase("TRANSACTION INITIATED CHECK GET STATUS")){
             upiStatus.setVisibility(View.VISIBLE);
             continueButton.setVisibility(View.GONE);
        }

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TransactionErrorActivity.this, MainDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        upiStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HeaderRequest<DoTransactionRequest> headerRequest = PaymentModeEnum.UPI_GET_STATUS.getRequest();
                callPineService(headerRequest);

            }
        });


    }

    public static TransactionErrorActivity getInstance() {
        return instance;
    }

    private void setData() {
        //Connecting to the Pine Service Helper
        pineSH.connect(this);
    }
    private void callPineService(final HeaderRequest<DoTransactionRequest> headerRequest) {

        String amountNum = amount.replace("\u20B9","");
        float amt = Float.parseFloat(amountNum);
        DoTransactionRequest request = headerRequest.getDetail();
        request.setBillingRefNo(billingNumber);
        request.setPaymentAmount((long) (amt*100));
        pineSH.callPineService(headerRequest);
        Gson gson = new Gson();
        posAcknowledgeRequest = gson.toJson(headerRequest);
        Log.d("request....",posAcknowledgeRequest);
    }

    @Override
    public void sendResult(DetailResponse detailResponse) {
        super.sendResult( detailResponse);
        if (detailResponse != null) {
            //Check if the transaction is successful or not
            if (detailResponse.getResponse().isSuccess()) {

                try {
                    Gson gson = new Gson();
                    posAcknowledgeResponse = gson.toJson(detailResponse);

                    upiStatus.setVisibility(View.GONE);
                    Log.d("response....", posAcknowledgeResponse);
                    Intent intent = new Intent(TransactionErrorActivity.this, MainDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }catch(NullPointerException e) {
                    e.printStackTrace();
                }

            } else {

                Log.d("errrrrrrr...", String.valueOf(detailResponse.getResponse().getResponseCode()));
                Log.d("errrrrrrr...",detailResponse.getResponse().getResponseMsg());
                upiStatus.setVisibility(View.GONE);
                Intent intent = new Intent(TransactionErrorActivity.this, MainDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        } else {
            UIUtils.makeToast(this, "Error Occurred");
            upiStatus.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIUtils.dismissProgressbar();
    }

    @Override
    public void onBackPressed() {
    }
}