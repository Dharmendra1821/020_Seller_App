package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.fragments.ShareProductFragment;

public class ShareProductActivity extends AppCompatActivity {
    private static ShareProductFragment.ClickListener clickListener;
    private static ShareProductFragment.closeClickListener closeClickListener;
    Button submit;
    ImageView close;
    WebView webView;
    SharedPreferences sharedPreferences;
    TextView resetFilter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_product);

        sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);


        webView = findViewById(R.id.share_product_webview);

        String shareUrl = getIntent().getStringExtra("share_url");


        progressDialog = new ProgressDialog(ShareProductActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        webView = new WebView(ShareProductActivity.this);
        webView.getSettings().setJavaScriptEnabled(true);
       // String URL  = sharedPreferences.getString("share_url",null);
        webView.loadUrl(shareUrl);
        WebSettings mWebSettings = webView.getSettings();
        webView.setWebViewClient(new myWebClient());
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowContentAccess(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                // Log.d("onPermissionRequest");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());
                }
            }
            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
        });

        setContentView(webView);

    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressDialog.dismiss();

        }
    }
}
