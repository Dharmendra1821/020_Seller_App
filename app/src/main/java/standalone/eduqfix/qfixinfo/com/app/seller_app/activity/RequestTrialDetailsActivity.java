package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;


import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.BottomNavigationActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DetectSwipeDirectionActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.OrderListActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.ExpListViewAdapterWithCheckbox;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.fragment.SurfaceViewCameraFragment;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ComplaintsModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.RequestTrialModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;

public class RequestTrialDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView orderId,itemId,customerName,vendorComments,customerComments,productName;
    EditText comments;
    ArrayList<String> statusValue;
    SearchableSpinner status;
    Map<Integer, Integer> mSpinnerSelectedItem = new HashMap<Integer, Integer>();
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    Button submit;
    String pendingStatus;
    ImageView image1,image2,image3;
    BroadcastReceiver mMessageReceiver;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    int PERMISSION_ALL = 1;
    boolean flagPermissions = false;
    String filePath;
    int position;
    String stringBitmap;
    Bitmap bitmap1;
    Bitmap sizeBitmap;
    String result;
    String selectedStatus;
    ByteArrayInputStream imageStream,imageStream2,imageStream3;
    RequestTrialModel requestTrialModel;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_trial_details);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(RequestTrialDetailsActivity.this);
        statusValue   = new ArrayList<>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        requestTrialModel = (RequestTrialModel) bundle.getSerializable("request_trial");

        orderId = findViewById(R.id.rt_details_productId);
        customerName = findViewById(R.id.rt_details_customername);
        vendorComments = findViewById(R.id.rt_details_vendorcomments);
        productName =  findViewById(R.id.rt_details_productname);
        status = findViewById(R.id.rt_details_status);
        comments = findViewById(R.id.rt_details_comments);
        submit = findViewById(R.id.rt_details_submit);
        image1 = findViewById(R.id.rt_detail_image1);
        image2 = findViewById(R.id.rt_detail_image2);
        image3 = findViewById(R.id.rt_detail_image3);

        orderId.setText("Product Id : "+requestTrialModel.getProduct_id());
        customerName.setText(requestTrialModel.getCustomer_name());
        productName.setText(requestTrialModel.getProduct_name() != null ? requestTrialModel.getProduct_name() : "");

        if(requestTrialModel.getVendor_comments().equalsIgnoreCase("null")){
            vendorComments.setText("");
        }
        else {
            String vendorComm = requestTrialModel.getVendor_comments().replace("/", "");
            String vendorCommentsVal = vendorComm.replace("\n", "</br>");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                vendorComments.setText(Html.fromHtml(vendorCommentsVal, Html.FROM_HTML_MODE_COMPACT));
            } else {
                vendorComments.setText(Html.fromHtml(vendorCommentsVal));
            }
        }


        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        submit.setOnClickListener(this);

        statusValue.add("Reject");
        statusValue.add("Accept");
        statusValue.add("In Process");
        statusValue.add("Complete");

        if(requestTrialModel.getRequest_status().equals("1")){
            selectedStatus = "Accept";
        }
        if(requestTrialModel.getRequest_status().equals("2")){
            selectedStatus = "Reject";
        }
        if(requestTrialModel.getRequest_status().equals("3")){
            selectedStatus = "In Process";
        }
        if(requestTrialModel.getRequest_status().equals("4")){
            selectedStatus = "Complete";
        }


        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(RequestTrialDetailsActivity.this, R.layout.spinner_text_layout, statusValue);
        status.setAdapter(statusAdapter);
        int spinnerPosition = statusAdapter.getPosition(selectedStatus);
        status.setSelection(spinnerPosition);

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pendingStatus =  parent.getSelectedItem().toString();
                if(pendingStatus.equals("Accept")){
                    pendingStatus = "1";
                }
                if(pendingStatus.equals("Reject")){
                    pendingStatus = "2";
                }
                if(pendingStatus.equals("In Process")){
                    pendingStatus = "3";
                }
                if(pendingStatus.equals("Complete")){
                    pendingStatus = "4";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    void checkPermissions(final int position, final int flag) {
        if (!checkPermission()) {
            requestPermission();
            flagPermissions = false;
        }
        else {
            flagPermissions = true;
            Intent intent = new Intent(RequestTrialDetailsActivity.this,BottomNavigationActivity.class);
            intent.putExtra("position",position);
            intent.putExtra("flag",flag);
            startActivity(intent);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
                else {

                }
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.rt_detail_image1:
                checkPermissions(1,1);
                break;
            case R.id.rt_detail_image2:
                checkPermissions(2,1);
                break;
            case R.id.rt_detail_image3:
                checkPermissions(3,1);
                break;
            case R.id.rt_details_submit:
                saveDemoRequest();
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    position = intent.getIntExtra("position",0);
                    filePath = intent.getStringExtra("filePath").replace("file:","");
                    Log.d("imagepath",filePath);
                    bitmap1 = intent.getParcelableExtra("bitmap");
                    if(position!=0){
                        if (position==1){
                            image1.setImageBitmap(bitmap1);
                            sharedPreferences.edit().putString("image1", filePath).apply();
                            sizeBitmap = getResizedBitmap(bitmap1,700);
                            BitMapToString(sizeBitmap,"image1");
                        }
                        else if(position==2){
                            image2.setImageBitmap(bitmap1);
                            sharedPreferences.edit().putString("image2", filePath).apply();
                            sizeBitmap = getResizedBitmap(bitmap1,700);
                            BitMapToString(sizeBitmap,"image2");
                        }
                        else {
                            image3.setImageBitmap(bitmap1);
                            sharedPreferences.edit().putString("image3", filePath).apply();
                            sizeBitmap = getResizedBitmap(bitmap1,700);
                            BitMapToString(sizeBitmap,"image3");
                        }
                    }
                    else {
                        sharedPreferences.edit().putString("image1", null).apply();
                        sharedPreferences.edit().putString("image2", null).apply();
                        sharedPreferences.edit().putString("image3", null).apply();
                    }

                }catch (Exception e){

                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver , new IntentFilter("camera-message"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(RequestTrialDetailsActivity.this).unregisterReceiver(mMessageReceiver);

        }catch (Exception e){

        }
    }
    public String BitMapToString(Bitmap bitmap, String key){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            stringBitmap = Base64.encodeToString(b, Base64.DEFAULT);
            sharedPreferences.edit().putString(key, stringBitmap).apply();
        }catch (Exception e){
        }
        return stringBitmap;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public void saveDemoRequest(){


        String url = Constant.REVAMP_URL1 +"marketplace/saveDemoRequestBySeller.php";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("");
        progressDialog.setCancelable(true);

        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put("comment", comments.getText().toString());
            jsonObject1.put("status", pendingStatus);
            jsonObject1.put("id", requestTrialModel.getEntity_id());
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonObject1.toString();

        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(RequestTrialDetailsActivity.this, "comment saved successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RequestTrialDetailsActivity.this, RequestTrialListActivity.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RequestTrialDetailsActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }

        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    Log.e("Url...", "" + mRequestBody);
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "Bearer ufst0mpdb54ua27ieqmul03bivlpxmpg");

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
        MaintainRequestQueue.getInstance(RequestTrialDetailsActivity.this).addToRequestQueue(req, "tag");
    }

    public class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                String commentValue = comments.getText().toString();
                sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
                String imageDataBytes = sharedPreferences.getString("image1",null);
                String imageDataBytes2 = sharedPreferences.getString("image2",null);
                String imageDataBytes3 = sharedPreferences.getString("image3",null);
                if(imageDataBytes != null) {
                    imageStream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
                }
                if(imageDataBytes2 != null){
                     imageStream2 = new ByteArrayInputStream(Base64.decode(imageDataBytes2.getBytes(), Base64.DEFAULT));
                }
                if(imageDataBytes3 != null){
                     imageStream3 = new ByteArrayInputStream(Base64.decode(imageDataBytes3.getBytes(), Base64.DEFAULT));

                }


                HttpClient client = new DefaultHttpClient();


                String adminToken = "Bearer " + sharedPreferences.getString("adminToken", null);
                String shopUrl = sharedPreferences.getString("shop_url", null);

                String url = Constant.REVAMP_URL1 +"marketplace/saveDemoRequestBySeller.php";
                HttpPost post = new HttpPost(url);
               // post.setHeader("Authorization", adminToken);

                Log.d("url.....", url);
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("id", requestTrialModel.getEntity_id());
                entityBuilder.addTextBody("comment", commentValue);
             //   entityBuilder.addTextBody("request[status]", pendingStatus);

                String boundary = "---------------" + UUID.randomUUID().toString();
                entityBuilder.setBoundary(boundary);
                if (imageStream != null) {
                    entityBuilder.addBinaryBody("multiple_image[]",imageStream, ContentType.create("image/png"), "imagename.jpg");
                }
//                if (imageStream2 != null) {
//                    entityBuilder.addBinaryBody("image2", imageStream2, ContentType.create("image/png"), "imagename.jpg");
//                }
//                if (imageStream3 != null) {
//                    entityBuilder.addBinaryBody("image3", imageStream3, ContentType.create("image/png"), "imagename.jpg");
//                }
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
                            Toast.makeText(RequestTrialDetailsActivity.this, "comment saved successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RequestTrialDetailsActivity.this, RequestTrialListActivity.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();                            }
                    });
                } else {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(RequestTrialDetailsActivity.this, "Images not uploaded, please try again!", Toast.LENGTH_LONG).show();
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
                        progressDialog.setMessage("Saving data...");
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
            //  progressDialog.dismiss();
        }
    }

    private boolean checkPermission() {
        // int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

//                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted)
                        Toast.makeText(RequestTrialDetailsActivity.this, "Permission Granted, Now you can access  camera.", Toast.LENGTH_LONG).show();
                    else {

                        Toast.makeText(RequestTrialDetailsActivity.this, "Permission Denied, You cannot access camera.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(RequestTrialDetailsActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
