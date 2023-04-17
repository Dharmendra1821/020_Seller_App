package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;

import android.os.Bundle;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

import static android.content.Context.MODE_PRIVATE;

public class ShowWebviewActivity extends AppCompatActivity {

    private WebView webView;
    final Activity activity = this;
    public Uri imageUri;

    private static final int FILECHOOSER_RESULTCODE   = 2888;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;


    @RequiresApi(api = Build.VERSION_CODES.ECLAIR_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_webview);
        webView =  findViewById(R.id.sellerHomeWebView);

        SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);

        String customerDetails = sharedPreferences.getString("CustomerDetails",null);
        LoginResponse loginResponse = new Gson().fromJson(customerDetails,new TypeToken<LoginResponse>(){}.getType());
        String username = loginResponse != null && loginResponse.getCustomerDetails() != null ? loginResponse.getCustomerDetails().getEmail() : "NA";

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        //Other webview settings
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setSupportZoom(true);

        //Load url in webview
       // webView.loadUrl(webViewUrl);
        webView.loadUrl(Constant.BASE_DOMAIN_URL+"/marketplace/account/sellerpostlogin?customerpost="+username);
        // Define Webview manage classes
        startWebView();


    }

    private void startWebView() {
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are open in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("google")){
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    view.loadUrl(url);
                    return true;
                }
            }
            //Show loader on url load
            public void onLoadResource (WebView view, String url) {

                // if url contains string androidexample
                // Then show progress  Dialog
                if (progressDialog == null && url.contains("androidexample")) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(ShowWebviewActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }

            // Called when all page resources loaded
            public void onPageFinished(WebView view, String url) {

                try{
                    // Close progressDialog
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });


        // You can create external class extends with WebChromeClient
        // Taking WebViewClient as inner class
        // we will define openFileChooser for select file from camera or sdcard

        webView.setWebChromeClient(new WebChromeClient() {

            // openFileChooser for Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType){

                // Update message
                mUploadMessage = uploadMsg;

                try{

                    // Create AndroidExampleFolder at sdcard

                    File imageStorageDir = new File(
                            Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES)
                            , "AndroidExampleFolder");

                    if (!imageStorageDir.exists()) {
                        // Create AndroidExampleFolder at sdcard
                        imageStorageDir.mkdirs();
                    }

                    // Create camera captured image file path and name
                    File file = new File(
                            imageStorageDir + File.separator + "IMG_"
                                    + String.valueOf(System.currentTimeMillis())
                                    + ".jpg");

                    mCapturedImageURI = Uri.fromFile(file);

                    // Camera capture image intent
                    final Intent captureIntent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");

                    // Create file chooser intent
                    Intent chooserIntent = Intent.createChooser(i, "Image Chooser");

                    // Set camera intent to file chooser
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                            , new Parcelable[] { captureIntent });

                    // On select image call onActivityResult method of activity
                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                }
                catch(Exception e){
                    Toast.makeText(getBaseContext(), "Exception:"+e,
                            Toast.LENGTH_LONG).show();
                }

            }

            // openFileChooser for Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg){
                openFileChooser(uploadMsg, "");
            }

            //openFileChooser for other Android versions
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType,
                                        String capture) {

                openFileChooser(uploadMsg, acceptType);
            }



            // The webPage has 2 filechoosers and will send a
            // console message informing what action to perform,
            // taking a photo or updating the file

            public boolean onConsoleMessage(ConsoleMessage cm) {

                onConsoleMessage(cm.message(), cm.lineNumber(), cm.sourceId());
                return true;
            }

            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                //Log.d("androidruntime", "Show console messages, Used for debugging: " + message);

            }
        });   // End setWebChromeClient

    }



    // Return here when file selected from camera or from SDcard

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {


        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {

            if (null == this.mUploadMessage) {
                return;

            }

            Uri result = null;

            try {
                if (resultCode != RESULT_OK) {

                    result = null;

                } else {

                    // retrieve from the private variable if the intent is null
                    result = intent == null ? mCapturedImageURI : intent.getData();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "activity :" + e,
                        Toast.LENGTH_LONG).show();
            }

            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        }

    }

    // Open previous opened link from history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {

        if(webView.canGoBack()) {

            webView.goBack();

        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

}
