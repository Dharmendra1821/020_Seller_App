package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.activity.WelcomeActivity;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

import static android.content.Context.MODE_PRIVATE;

public class SellersHomeActivity extends AppCompatActivity {
    private final static int CAPTURE_RESULTCODE = 1;
    public ValueCallback<Uri> mUploadMessage;
    public String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_home);

        SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);

        String customerDetails = sharedPreferences.getString("CustomerDetails",null);
        LoginResponse loginResponse = new Gson().fromJson(customerDetails,new TypeToken<LoginResponse>(){}.getType());
        String username = loginResponse != null && loginResponse.getCustomerDetails() != null ? loginResponse.getCustomerDetails().getEmail() : "NA";
        final ProgressDialog progressDialog = new ProgressDialog(SellersHomeActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        WebView webView = findViewById(R.id.sellerHomeWebView);
        webView.getSettings().setJavaScriptEnabled(true);


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                Log.i("vipul", "Progress " + progress);
                progressBar.setProgress(progress);

                if (progress == 100) {
                    progressDialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        String url = null;

        webView.setWebViewClient(new WebViewClient());
        if(username != null && !username.equalsIgnoreCase("NA")){
            url = Constant.BASE_DOMAIN_URL+"/marketplace/account/sellerpostlogin?customerpost="+username;
            Log.d("Url...",url);
            webView.loadUrl(url);
            progressDialog.dismiss();
        }else{
            progressDialog.dismiss();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(SellersHomeActivity.this,"Something went wrong, Please try again!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SellersHomeActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == CAPTURE_RESULTCODE) {
            if (null == this.mUploadMessage || (resultCode != RESULT_OK && !new File(filePath).exists())) {
                this.mUploadMessage.onReceiveValue(null);
            } else {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, this.filePath);
                this.mUploadMessage.onReceiveValue(this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values));
            }
            this.mUploadMessage = null;
        }
    }
}
