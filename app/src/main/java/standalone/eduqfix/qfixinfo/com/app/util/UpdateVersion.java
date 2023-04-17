package standalone.eduqfix.qfixinfo.com.app.util;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import android.util.Log;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;


import standalone.eduqfix.qfixinfo.com.app.BuildConfig;

import static android.net.Uri.fromFile;

public class UpdateVersion extends AsyncTask<String,Void,Void>  {


    private Context context;
    Uri uri;
    DownloadManager manager;
    long downloadId;
    Activity activity;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public void setContext(Context contextf) {
        context = contextf;
    }

    public UpdateVersion(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
 progressDialog = new ProgressDialog(context);

    }

    @Override
    protected Void doInBackground(String... strings) {
        return null;
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {

            Log.d("coming",intent.toString());
            File file = new File("file:///storage/emulated/0/Download/", "QShop.apk");
            progressDialog.hide();
            String filePath = "file:///storage/emulated/0/Download/QShop.apk";
            installApplication(context,"QShop.apk");

            /*Uri apkUri =  Uri.fromFile(file);
            Intent install = new Intent(Intent.ACTION_PACKAGE_INSTALL);
            install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //install.setDataAndType(uri,manager.getMimeTypeForDownloadedFile(downloadId));
            intent.setData(apkUri);
            intent.setDataAndType(fromFile(file), "application/vnd.android" + ".package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(install,1);*/
            //installApk();
            context.unregisterReceiver(this);
            //finish();
        }
    };

    public void downloadFile(String downloadungUrl) {

        progressDialog.show();


        if (haveStoragePermission(downloadungUrl)) {
            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
            String fileName = "QShop.apk";
            destination += fileName;
            uri = Uri.parse("file://" + destination);

            String url = downloadungUrl;

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDescription("MPOS Download");
            request.setTitle("MPOS");

            //set destination
            request.setDestinationUri(uri);

            // get download service and enqueue file
            manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            downloadId = manager.enqueue(request);

            context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }


    public  boolean haveStoragePermission(String downloadungUrl) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                return true;
            } else {

                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INSTALL_PACKAGES,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                downloadFile(downloadungUrl);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            return true;
        }
    }







    public static void installApplication(Context context, String fileName) {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/" + fileName;
        Log.d("filemame",filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uriFromFile(context, new File(filePath)), "application/vnd.android" + ".package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            Log.d("intent",intent.toString());
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Log.e("TAG", "Error in opening the file!");
        }
    }

    private static Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }
}

