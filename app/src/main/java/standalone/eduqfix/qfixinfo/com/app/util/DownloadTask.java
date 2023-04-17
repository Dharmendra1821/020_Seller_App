package standalone.eduqfix.qfixinfo.com.app.util;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.SplashScreenActivity;

public class DownloadTask {
    private static final String TAG = "Download Task";
    private Context context;

    private String downloadUrl = "", downloadFileName = "";
    private ProgressDialog progressDialog;

    public DownloadTask(Context context, String downloadUrl,final String invoiceNumber) {
        this.context = context;

        this.downloadUrl = downloadUrl;
        this.downloadFileName = invoiceNumber;

//        if(invoiceNumber.equalsIgnoreCase("reports")){
//            downloadFileName = invoiceNumber+".csv";//Create file name by picking download file name from URL
//        }
//        if(!invoiceNumber.equalsIgnoreCase("reports")) {
//            downloadFileName = invoiceNumber+".pdf";//Create file name by picking download file name from URL
//        }


        Log.e(TAG, downloadFileName);

        //Start Downloading Task
        new DownloadingTask().execute();
    }




    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Downloading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    Log.d("resulll", String.valueOf(result));
                    Log.d("djfljdl", String.valueOf(outputFile));
                    progressDialog.dismiss();
                 //   ContextThemeWrapper ctw = new ContextThemeWrapper( context, R.style.Theme_AlertDialog);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Document  ");
                    alertDialogBuilder.setMessage("Document Downloaded Successfully ");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                    alertDialogBuilder.setNegativeButton("Open report",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Qfix Seller/" + downloadFileName );  // -> filename = maven.pdf
                            Log.d("pdf file", String.valueOf(pdfFile));
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            if (Build.VERSION.SDK_INT >= 24) {
                                Uri downloaded_apk = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", pdfFile);
                                if(downloadFileName.equalsIgnoreCase("reports.csv")) {
                                    intent.setDataAndType(downloaded_apk, "text/csv");
                                }
                                else {
                                    intent.setDataAndType(downloaded_apk, "text/csv");
                                }
                                List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                                for (ResolveInfo resolveInfo : resInfoList) {
                                    context.grantUriPermission(context.getApplicationContext().getPackageName() + ".provider", downloaded_apk, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                }
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                context.startActivity(intent);

                            }
                            else {

                                intent.setAction(Intent.ACTION_VIEW);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                                intent.setDataAndType(Uri.fromFile(pdfFile), "text/csv");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }

                    });

                    alertDialogBuilder.show();
//                    Toast.makeText(context, "Document Downloaded Successfully", Toast.LENGTH_SHORT).show();
                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }


            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                //Get File if SD card is present


//                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File
//
//
//                //Create New File if not present
//                if (!outputFile.exists()) {
//
//                    outputFile.createNewFile();
//                    Log.e(TAG, "File Created");
//                }
                File dir = new File(String.valueOf(context.getExternalFilesDir(".csv")));

                if(!dir.exists()){
                    dir.mkdir();
                }
                outputFile = new File(dir, "invoice");
                outputFile.createNewFile();

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                fos.flush();

                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }

}
