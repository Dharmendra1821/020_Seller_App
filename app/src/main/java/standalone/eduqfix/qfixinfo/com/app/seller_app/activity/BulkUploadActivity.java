package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.UUID;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.DownloadTask;
import standalone.eduqfix.qfixinfo.com.app.util.FilePath;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SAMPLE_DOWNLOAD_URL;

public class BulkUploadActivity extends AppCompatActivity implements View.OnClickListener {

    Button bulkUpload,downloadSample;
    private String selectedFilePath;
    private static final int PICK_FILE_REQUEST = 1;
    String result;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    int sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk_upload);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(BulkUploadActivity.this);
        bulkUpload = findViewById(R.id.bulk_upload_invoice);
        downloadSample = findViewById(R.id.bulk_download_invoice);

        bulkUpload.setOnClickListener(this);
        downloadSample.setOnClickListener(this);
        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        LoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<LoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getCustomerDetails() != null ? loginResponse.getCustomerDetails().getId() : 0;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bulk_upload_invoice:
                Intent intent = new Intent();
                //sets the select file to all types of files
                intent.setType("text/*");
                //    intent.setType("text/csv");
                //allows to select data and return it
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //starts new activity to select file and return data
                startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
                break;
            case R.id.bulk_download_invoice:
                String shopUrl  = sharedPreferences.getString("shop_url",null);
                String url = "https://uatstandalone.eduqfix.com/invoiceapi/invoice/downloadsample";
                new DownloadTask(BulkUploadActivity.this, url,"reports");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == PICK_FILE_REQUEST) {
                    if (data == null) {
                        //no data present
                        return;
                    }


                    Uri selectedFileUri = data.getData();
                    selectedFilePath = FilePath.getPath(this, selectedFileUri);
                    Log.i("TAG", "Selected File Path:" + selectedFilePath);

                    if (selectedFilePath != null && !selectedFilePath.equals("")) {
                        //  tvFileName.setText(selectedFilePath);
                        Log.d("file........", selectedFilePath);
                        new RequestTask3().execute();
                    } else {
                        Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (Exception e){
            Toast.makeText(this, "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();
        }
    }
    public class RequestTask3 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                HttpClient client = new DefaultHttpClient();

                String shopUrl  = sharedPreferences.getString("shop_url",null);
                String url = "https://"+shopUrl + BASE_URL+"invoice/importexcel";
                //  String url = "https://surajshop.eduqfix.com/rest/V1/products/" + sku + "/media";

                HttpPost post = new HttpPost(url);
                //  post.setHeader("Authorization", adminToken);

                Log.d("url.....", url);
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("seller_id", String.valueOf(sellerId));
                String boundary = "---------------" + UUID.randomUUID().toString();
                entityBuilder.setBoundary(boundary);
                entityBuilder.addBinaryBody("fileToUpload", new File(selectedFilePath));

                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
                Log.v("result", result);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(BulkUploadActivity.this, "uploaded successfully", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(SellerInvoiceListActivity.this, MainDashboardActivity.class);
//                            startActivity(intent);
//                            finish();
                            progressDialog.dismiss();                            }
                    });
                } else {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(BulkUploadActivity.this, "File format not proper download sample file", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();                          }
                    });


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        progressDialog.setMessage("Uploading ...");
                        progressDialog.show();
                    }
                });
            }catch (Exception e){

            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            progressDialog.dismiss();
        }


    }
}
