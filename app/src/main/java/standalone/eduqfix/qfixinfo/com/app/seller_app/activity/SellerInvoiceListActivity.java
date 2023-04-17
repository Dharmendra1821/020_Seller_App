package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DetectSwipeDirectionActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.AddNewProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ComplaintsAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SellerCustomerListAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SellerInvoiceAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ComplaintsModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SellerInvoiceModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.DownloadTask;
import standalone.eduqfix.qfixinfo.com.app.util.FilePath;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SAMPLE_DOWNLOAD_URL;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class SellerInvoiceListActivity extends AppCompatActivity implements SellerInvoiceAdapter.ClickListener, View.OnClickListener {

    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    SellerInvoiceModel sellerInvoiceModel;
    SellerInvoiceAdapter sellerInvoiceAdapter;
    ArrayList<SellerInvoiceModel> sellerInvoiceModels;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    int sellerId;
    int page;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    RelativeLayout bottomLayout;
    Button export_csv, bulk_upload, download_sampleBtn, importBtn;
    String invoiceIds;
    public int PDF_REQ_CODE = 1;
    String PdfPathHolder;
    Uri uri;
    String result;
    File file;
    private String selectedFilePath;
    private static final int PICK_FILE_REQUEST = 1;
    LinearLayout uploadLayout;
    private static final int PERMISSION_REQUEST_CODE = 200;


    private boolean storagePermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_invoice_list);

        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
        progressDialog = new ProgressDialog(SellerInvoiceListActivity.this);
        recyclerView = findViewById(R.id.seller_invoice_recyclerview);
        export_csv = findViewById(R.id.invoice_export_csv);
        bulk_upload = findViewById(R.id.invoice_bulk_upload);
        download_sampleBtn = findViewById(R.id.invoice_download_sample);
        importBtn = findViewById(R.id.invoice_import);
        uploadLayout = findViewById(R.id.upload_layout);
        linearLayoutManager = new LinearLayoutManager(SellerInvoiceListActivity.this);
        bottomLayout = findViewById(R.id.loadItemsLayout_recyclerView);
        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        getSellerInvoiceList(String.valueOf(sellerId), "1");
        sellerInvoiceAdapter = new SellerInvoiceAdapter();
        sellerInvoiceAdapter.setOnItemClickListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            page++;

                            getSellerInvoiceList1(String.valueOf(sellerId), String.valueOf(page));

                        }
                    }
                }
            }
        });

        export_csv.setOnClickListener(this);
        bulk_upload.setOnClickListener(this);
        download_sampleBtn.setOnClickListener(this);
        importBtn.setOnClickListener(this);

    }

    public void getSellerInvoiceList(final String sellerId, final String currentPage) {

        String shopUrl = sharedPreferences.getString("shop_url", null);
        //  String url = "https://"+shopUrl + BASE_URL+"invoice/list?seller_id="+sellerId+"&current_page="+currentPage;
        String url = Constant.REVAMP_URL1 + "customapi/invoicelist.php?seller_id=" + sellerId;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading data.......");
        progressDialog.setCancelable(false);

        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {
                    sellerInvoiceModels = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String invoice_id = jsonObject1.getString("invoice_id");
                            String invoicenumber = jsonObject1.getString("invoicenumber");
                            String cname = jsonObject1.getString("cname");
                            String gstin = jsonObject1.getString("gstin");
                            String invoiceDate = jsonObject1.getString("invoice_date");
                            String mobile_number = jsonObject1.getString("mobile_number");
                            String email = jsonObject1.getString("email");
                            String amount = jsonObject1.getString("amount");
                            String expiryDate = jsonObject1.getString("expiry_date");
                            String paymentstatus = jsonObject1.getString("paymentstatus");
                            String receiptLink = jsonObject1.getString("receipt_link").toString();

                            sellerInvoiceModels.add(new SellerInvoiceModel(invoice_id, invoicenumber, cname, gstin, invoiceDate, mobile_number,
                                    email, amount, expiryDate, paymentstatus, receiptLink));

                        }

                        sellerInvoiceAdapter = new SellerInvoiceAdapter(SellerInvoiceListActivity.this, sellerInvoiceModels);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(sellerInvoiceAdapter);
                        sellerInvoiceAdapter.notifyDataSetChanged();


                    } else {
                        export_csv.setVisibility(View.GONE);
                        bulk_upload.setVisibility(View.GONE);
                        Toast.makeText(SellerInvoiceListActivity.this, "No data available", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                progressDialog.dismiss();

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;

            }
        };
        // Adding request to request queue

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(SellerInvoiceListActivity.this).addToRequestQueue(req, "tag");

    }

    public void getSellerInvoiceList1(final String sellerId, final String currentPage) {

        String shopUrl = sharedPreferences.getString("shop_url", null);
        String url = Constant.REVAMP_URL1 + "invoice/list?seller_id=" + sellerId + "&current_page=" + currentPage;

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {
                    //  sellerInvoiceModels = new ArrayList<>();
                    bottomLayout.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String invoice_id = jsonObject1.getString("invoice_id");
                            String invoicenumber = jsonObject1.getString("invoicenumber");
                            String cname = jsonObject1.getString("cname");
                            String gstin = jsonObject1.getString("gstin");
                            String invoiceDate = jsonObject1.getString("invoiceDate");
                            String mobile_number = jsonObject1.getString("mobile_number");
                            String email = jsonObject1.getString("email");
                            String amount = jsonObject1.getString("amount");
                            String expiryDate = jsonObject1.getString("expiryDate");
                            String paymentstatus = jsonObject1.getString("paymentstatus");
                            String receiptLink = jsonObject1.getString("receipt_link");

                            sellerInvoiceModels.add(new SellerInvoiceModel(invoice_id, invoicenumber, cname, gstin, invoiceDate, mobile_number,
                                    email, amount, expiryDate, paymentstatus, receiptLink));

                        }

                        sellerInvoiceAdapter.notifyDataSetChanged();
                        loading = true;
                        bottomLayout.setVisibility(View.GONE);

                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                progressDialog.dismiss();

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;

            }
        };
        // Adding request to request queue

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(SellerInvoiceListActivity.this).addToRequestQueue(req, "tag");

    }

    @Override
    public void onItemClick(View v, int position) {
        sellerInvoiceModel = sellerInvoiceAdapter.getWordAtPosition(position);
        String shopUrl = sharedPreferences.getString("shop_url", null);
        String newUrl = sellerInvoiceModel.getReceipt_link();
        Uri uri = Uri.parse(newUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Invoices");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); // to notify when download is complete
        request.allowScanningByMediaScanner();// if you want to be available from media players
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        manager.enqueue(request);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse(newUrl), "application/pdf");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivity(intent);




        //  new DownloadTask(SellerInvoiceListActivity.this, newUrl,sellerInvoiceModel.getInvoicenumber());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invoice_export_csv:

                if (SellerInvoiceAdapter.getInvoiceIds().size() >= 0) {
                    invoiceIds = android.text.TextUtils.join(",", SellerInvoiceAdapter.getInvoiceIds());
                    String shopUrl = sharedPreferences.getString("shop_url", null);
                    //  String url = "https://"+shopUrl + BASE_URL+"invoice/exportcsv?seller_id="+String.valueOf(sellerId)+"&invoice_ids="+invoiceIds;
                    String url = Constant.REVAMP_URL1 + "customapi/exportInvoice.php?seller_id=" + sellerId+"&invoice_id="+invoiceIds;
                    Log.d("url..",url);
                    Uri uri = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Invoices");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); // to notify when download is complete
                    request.allowScanningByMediaScanner();// if you want to be available from media players
                    DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                 //   new DownloadTask(SellerInvoiceListActivity.this, url, "reports");

                }
                break;
            case R.id.invoice_bulk_upload:
                uploadLayout.setVisibility(View.VISIBLE);

                break;
            case R.id.invoice_download_sample:
                String shopUrl = sharedPreferences.getString("shop_url", null);
                String url = "https://" + shopUrl + SAMPLE_DOWNLOAD_URL + "invoiceapi/invoice/downloadsample";
                new DownloadTask(SellerInvoiceListActivity.this, url, "reports");
                break;
            case R.id.invoice_import:
                Intent intent = new Intent();
                //sets the select file to all types of files
                intent.setType("text/*");
                //    intent.setType("text/csv");
                //allows to select data and return it
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //starts new activity to select file and return data
                startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
                break;
            default:
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
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();
        }
    }

    public static String insertString(
            String originalString,
            String stringToBeInserted,
            int index) {

        // Create a new string
        String newString = originalString.substring(0, index + 1)
                + stringToBeInserted
                + originalString.substring(index + 1);

        // return the modified String

        Log.d("new string", newString);
        return newString;
    }

    public class RequestTask3 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                HttpClient client = new DefaultHttpClient();

                String shopUrl = sharedPreferences.getString("shop_url", null);
                String url = "https://" + shopUrl + BASE_URL + "invoice/importexcel";
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
                    Handler handler = new Handler(getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(SellerInvoiceListActivity.this, "uploaded successfully", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(SellerInvoiceListActivity.this, MainDashboardActivity.class);
//                            startActivity(intent);
//                            finish();
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    Handler handler = new Handler(getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(SellerInvoiceListActivity.this, "File format not proper download sample file", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
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
            } catch (Exception e) {

            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            progressDialog.dismiss();
        }


    }


//    private class DownloadFileCache extends AsyncTask<String, Void, Void> {
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            showLoader();
//        }
//
//        @Override
//        protected Void doInBackground(String... strings) {
//
//            // Log.d("TAG3","PDFUrl: " +pdfurl);
//
//            String fileUrl = "";
//
//
//
//
//
//
//            String internalCacheDirectory = getBaseActivity().getCacheDir().getAbsolutePath();
//
//            File folder;
//            LogTag.i(TAG, " filename:: " + fileName);
//            folder = new File(internalCacheDirectory, "uotmpdf");
//
//            if (!folder.exists()) {
//                Log.i(TAG, " DownloadFileCache::: " + folder.mkdir());
//                LogTag.i(TAG, " folder:: " + folder.getAbsolutePath());
//            }
//
//
//            File pdfFile = new File(folder, fileName);
//
//            try {
//                pdfFile.createNewFile();
//            } catch (IOException e) {
//                //Log.e("JSONException: ", e.getMessage());
//            }
//            if (fileName.contains(".jpg")) {
//                FileDownloader.downloadFile(fileUrl, pdfFile);
//            } else {
//                FileDownloader.downloadFile(fileUrl, pdfFile);
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            closeLoader();
//            if (fileName.contains(".jpg")) {
//                try {
//
//                    if (Build.VERSION.SDK_INT >= 19) {
//                        LogTag.i(TAG, " ifFileExist:: " + (new File(getBaseActivity().getCacheDir(), "uotmpdf/"+fileName)).exists());
//                        MediaScannerConnection.scanFile(getBaseActivity(), new String[]{new File(getBaseActivity().getCacheDir(), "uotmpdf/"+fileName).getAbsolutePath()},
//                                null, new MediaScannerConnection.OnScanCompletedListener() {
//                                    public void onScanCompleted(String path, Uri uri) {
//                                        Log.i(TAG, " scanCompleted:: ");
//                                    }
//                                });
//                    } else {
//                        context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(getBaseActivity().getCacheDir(), "uotmpdf/"+fileName))));
//                    }
//                } catch (Exception e) {
//                    LogTag.e("Exception: ", e.toString());
//                }
//            }
//            openPdfCache(fileName);
//        }
//
//
//
//
//
//
//
    private void getExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Permission not granted

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Can ask user for permission

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);

            } else {
                boolean userAskedPermissionBefore = sharedPreferences.getBoolean("external_storage", false);

                if (userAskedPermissionBefore) {
                    //If User was asked permission before and denied
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                    alertDialogBuilder.setTitle("Permission needed");
                    alertDialogBuilder.setMessage("Storage permission needed for accessing photos");
                    alertDialogBuilder.setPositiveButton("Open Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", SellerInvoiceListActivity.this.getPackageName(),
                                    null);
                            intent.setData(uri);
                            SellerInvoiceListActivity.this.startActivity(intent);
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d("TAG", "onClick: Cancelling");
                        }
                    });

                    AlertDialog dialog = alertDialogBuilder.create();
                    dialog.show();
                }
                else {
                    //If user is asked permission for first time
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_CODE);

                   sharedPreferences.edit().putBoolean("external_storage",true).apply();

                }
            }

        } else {
            storagePermissionGranted = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        storagePermissionGranted = false;
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Granted
                    storagePermissionGranted = true;

                } else {
                    //Denied
                }
            }
        }
    }

}


//    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
//        new AlertDialog.Builder(SellerInvoiceListActivity.this)
//                .setMessage(message)
//                .setPositiveButton("OK", okListener)
//                .setNegativeButton("Cancel", null)
//                .create()
//                .show();
//    }
//
//
//}
