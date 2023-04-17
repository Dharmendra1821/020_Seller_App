package standalone.eduqfix.qfixinfo.com.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import standalone.eduqfix.qfixinfo.com.app.login.activity.LoginActivity;
import standalone.eduqfix.qfixinfo.com.app.login.activity.WelcomeActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.EmailActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.MainDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.util.UpdateVersion;

import static android.content.Context.MODE_PRIVATE;


public class SplashScreenActivity extends AppCompatActivity {

    private final static int SPLASH_SCREEN_TIMEOUT = 3000;
    SharedPreferences sharedPreferences;
    String details = null;
    ProgressDialog bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        details = sharedPreferences.getString("CustomerDetails",null);

      //  Log.d("sdk version", String.valueOf(Build.VERSION.SDK_INT));

       // new DownloadNewVersion().execute();
//        UpdateVersion updateVersion = new UpdateVersion(SplashScreenActivity.this,SplashScreenActivity.this);
//        updateVersion.downloadFile("http://standalonetest.eduqfix.com/pub/media/QShop.apk");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(details)){
                    Intent intent = new Intent(SplashScreenActivity.this, EmailActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashScreenActivity.this, MainDashboardActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        },SPLASH_SCREEN_TIMEOUT);

    }

    class DownloadNewVersion extends AsyncTask<String,Integer,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar = new ProgressDialog(SplashScreenActivity.this);
            bar.setCancelable(false);
            bar.setMessage("Downloading...");
            bar.setIndeterminate(true);
            bar.setCanceledOnTouchOutside(false);
            bar.show();
        }
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            bar.setIndeterminate(false);
            bar.setMax(100);
            bar.setProgress(progress[0]);
            String msg = "";
            if(progress[0]>99){
                msg="Finishing... ";
            }else {
                msg="Downloading... "+progress[0]+"%";
            }
            bar.setMessage(msg);
        }
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            bar.dismiss();
            if(result){
                installApk();
                Toast.makeText(SplashScreenActivity.this,"Done!!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SplashScreenActivity.this,"Error: Try Again", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected Boolean doInBackground(String... arg0) {
            Boolean flag = false;
            try {
                URL url = new URL("http://standalonetest.eduqfix.com/pub/media/QShop.apk");
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(false);
                c.connect();
               // String PATH = Environment.getExternalStorageDirectory()+"/Download/";
                String PATH = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file,"QShop.apk");
                if(outputFile.exists()){
                    outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                int total_size = 4581692;//size of apk
                byte[] buffer = new byte[1024];
                int len1 = 0;
                int per = 0;
                int downloaded=0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                    downloaded +=len1;
                    per = (int) (downloaded * 100 / total_size);
                    publishProgress(per);
                }
                fos.close();
                is.close();

                flag = true;
            } catch (Exception e) {
                Log.e("TAG", "Update Error: " + e.getMessage());
                e.printStackTrace();
                flag = false;
            }
            return flag;
        }
    }

    public void installApk() {
        try {
            String PATH = Objects.requireNonNull(SplashScreenActivity.this.getExternalFilesDir(null)).getAbsolutePath();
            File file = new File(PATH + "/QShop.apk");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= 24) {
                Uri downloaded_apk = FileProvider.getUriForFile(SplashScreenActivity.this, SplashScreenActivity.this.getApplicationContext().getPackageName() + ".provider", file);
                intent.setDataAndType(downloaded_apk, "application/vnd.android.package-archive");
                List<ResolveInfo> resInfoList = SplashScreenActivity.this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    SplashScreenActivity.this.grantUriPermission(SplashScreenActivity.this.getApplicationContext().getPackageName() + ".provider", downloaded_apk, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);

            } else {
                intent.setAction(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("path",e.getMessage());
        }
    }

}
