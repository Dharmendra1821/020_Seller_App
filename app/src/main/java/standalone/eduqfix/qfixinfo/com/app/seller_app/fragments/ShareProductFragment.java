package standalone.eduqfix.qfixinfo.com.app.seller_app.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.Login;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.NewWebviewActivity;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

public class ShareProductFragment extends BottomSheetDialogFragment {

    private static ShareProductFragment.ClickListener clickListener;
    private static ShareProductFragment.closeClickListener closeClickListener;
    Button submit;
    ImageView close;
    WebView webView;
    SharedPreferences sharedPreferences;
    TextView resetFilter;
    ProgressDialog progressDialog;
    public static ShareProductFragment newInstance() {
        return new ShareProductFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.share_product_fragment, container, false);

        sharedPreferences = getActivity().getSharedPreferences("StandAlone", Context.MODE_PRIVATE);

        close = view.findViewById(R.id.close_sheet);

        webView = view.findViewById(R.id.share_product_webview);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        webView = new WebView(getActivity());
        webView.getSettings().setJavaScriptEnabled(true);
        String URL  = sharedPreferences.getString("share_url",null);
        webView.loadUrl(URL);
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

        getActivity().setContentView(webView);

        return view;

    }

    public void setOnItemClickListener(ShareProductFragment.ClickListener clickListener) {
        ShareProductFragment.clickListener = clickListener;
    }

    public void setOnCloseClickListener(ShareProductFragment.closeClickListener clickListener) {
        ShareProductFragment.closeClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, String fromDate, String toDate);
    }

    public interface closeClickListener {
        void onCloseClick(View v);
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
