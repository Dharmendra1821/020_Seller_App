package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SMSContactListAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.fragments.SMSContactListFragment;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSContactListData;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSContactModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class SMSContactListActivity extends AppCompatActivity implements View.OnClickListener {
    Button submit;
    ImageView close;
    SharedPreferences sharedPreferences;
    TextView resetFilter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    String result;
    ArrayList<SMSContactModel> smsContactModels;
    ArrayList<SMSContactListData> smsContactListData;
    ArrayList<String> smsContactListDataSize;
    SMSContactModel smsContactModel;
    SMSContactListAdapter smsContactListAdapter;
    LinearLayoutManager linearLayoutManager;
    EditText sms_search;
    public static ArrayList<SMSContactModel> array_sort;
    int textlength = 0;
    Button smsBtn;
    String message;
    String smsIds;
    String page,path;
    ByteArrayInputStream imageStream;
    LoginResponse loginResponse;
    int sellerId;
    LinearLayout mainLayout;
    TextView noDataText;
    JSONArray jsonArray1;
    String filePath;
    Bitmap bitmap;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_m_s_contact_list);

        sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);

        progressDialog = new ProgressDialog(SMSContactListActivity.this);
        recyclerView = findViewById(R.id.sms_contact_recylerview);
        sms_search = findViewById(R.id.sms_contact_search);
        smsBtn  = findViewById(R.id.sms_contact_save);
        mainLayout = findViewById(R.id.sms_contact_layout);
        noDataText = findViewById(R.id.sms_nodata);

        linearLayoutManager = new LinearLayoutManager(SMSContactListActivity.this);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        try{
            message = getIntent().getStringExtra("message");
            page    = getIntent().getStringExtra("from_page");
            path    = getIntent().getStringExtra("path");
            filePath = getIntent().getStringExtra("filePath");

            Log.d("page",page);
            Log.d("path",path);
        }catch (Exception e){

        }
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(path));
//            imageView.setImageBitmap(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        sendFlyer();

        array_sort = new ArrayList<>();
        sms_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                textlength = sms_search.getText().length();
                array_sort.clear();
                for (int j = 0; j < smsContactModels.size(); j++) {
                    if (textlength <= smsContactModels.get(j).getListdata().get(j).getName().length()) {
                        if (smsContactModels.get(j).getListdata().get(j).getName().trim().contains(
                                sms_search.getText().toString().toLowerCase().trim())) {
                            array_sort.add(smsContactModels.get(j));
                            Log.d("data", String.valueOf(array_sort));
                        }
                    }
                }
                smsContactListAdapter = new SMSContactListAdapter(SMSContactListActivity.this,array_sort);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(smsContactListAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
            }
        });

        smsBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(page.equalsIgnoreCase("print")) {
            if (SMSContactListAdapter.getSmsIds().size() > 0) {
                smsIds = android.text.TextUtils.join(",", SMSContactListAdapter.getSmsIds());
                Log.d("ids", smsIds);
                JSONObject jsonObject1 = new JSONObject();
                JSONObject request = new JSONObject();
                try {
                    jsonObject1.put("seller_id", sellerId);
                    jsonObject1.put("message", message);
                    jsonObject1.put("sender_name", "");
                    jsonObject1.put("sms_list_ids", smsIds);
                    request.put("sms", jsonObject1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final String mRequestBody = request.toString();

                Log.d("req", mRequestBody);
                saveData(mRequestBody);

            } else {
                Toast.makeText(SMSContactListActivity.this, "Select list", Toast.LENGTH_LONG).show();
            }
        }
        if(page.equalsIgnoreCase("email")){
            if (SMSContactListAdapter.getSmsIds().size() > 0) {
                smsIds = android.text.TextUtils.join(",", SMSContactListAdapter.getSmsIds());
                new RequestTask2().execute();
            }
            else {
                Toast.makeText(SMSContactListActivity.this, "Select list", Toast.LENGTH_LONG).show();
            }
        }
    }

//    public class RequestTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//
//                sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
//
//
//                HttpClient client = new DefaultHttpClient();
//
//                String adminToken = sharedPreferences.getString("adminToken",null);
//             //   String adminToken = "Bearer cik6o2i5ymqgomcoxmt0wmdtb14b7wai" ;
//                String shopUrl = sharedPreferences.getString("shop_url", null);
//
//                String url = "https://"+shopUrl+ BASE_URL +"qfix-sendflyer/list";
//                HttpPost post = new HttpPost(url);
//                post.setHeader("Authorization", "Bearer "+adminToken);
//
//                Log.d("url.....", url);
//                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
//                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//                entityBuilder.addTextBody("seller_id", "204");
//
//                String boundary = "---------------" + UUID.randomUUID().toString();
//                entityBuilder.setBoundary(boundary);
//
//                HttpEntity entity = entityBuilder.build();
//                post.setEntity(entity);
//                client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
//                HttpResponse response = client.execute(post);
//                HttpEntity httpEntity = response.getEntity();
//                smsContactModels = new ArrayList<>();
//                smsContactListData = new ArrayList<>();
//                result = EntityUtils.toString(httpEntity);
//
//                Log.v("result", result);
//                StatusLine statusLine = response.getStatusLine();
//                int statusCode = statusLine.getStatusCode();
//                if (statusCode == 200) {
//                    JSONArray jsonArray = new JSONArray(result);
//                    if(jsonArray.length() > 0){
//                        for(int i =0; i < jsonArray.length(); i++){
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            String id = jsonObject.getString("list_id");
//                            String listName = jsonObject.getString("listname");
//
//                            JSONArray jsonArray1 = jsonObject.getJSONArray("listdata");
//
//                            if(jsonArray1.length() > 0) {
//                                for (int ii = 0; ii < jsonArray1.length(); ii++) {
//                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(ii);
//
//                                    String smsId = jsonObject2.getString("id");
//                                    String smslistId = jsonObject2.getString("list_id");
//                                    String smsName = jsonObject2.getString("name");
//                                    String smsMobile = jsonObject2.getString("mobile");
//                                    String smsEmail = jsonObject2.getString("emailid");
//
//                                    smsContactListData.add(new SMSContactListData(smsId,smslistId,smsName,smsMobile,smsEmail));
//
//                                }
//                            }
//
//
//                            smsContactModels.add(new SMSContactModel(id,listName,smsContactListData));
//                        }
//
//
//
//                    }
//                    Handler handler =  new Handler(getMainLooper());
//                    handler.post( new Runnable(){
//                        public void run(){
//                            smsContactListAdapter = new SMSContactListAdapter(SMSContactListActivity.this,smsContactModels,"");
//                            recyclerView.setLayoutManager(linearLayoutManager);
//                            recyclerView.setAdapter(smsContactListAdapter);
//                            progressDialog.dismiss();                            }
//                    });
//                } else {
//                    Handler handler =  new Handler(getMainLooper());
//                    handler.post( new Runnable(){
//                        public void run(){
//                            Toast.makeText(SMSContactListActivity.this, " please try again!", Toast.LENGTH_LONG).show();
//                            progressDialog.dismiss();                          }
//                    });
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            try {
//                Handler handler = new Handler(getMainLooper());
//                handler.post(new Runnable() {
//                    public void run() {
//                        progressDialog.setMessage("Saving data...");
//                        progressDialog.show();
//                    }
//                });
//            }catch (Exception e){
//
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            //Do anything with response..
//            //  progressDialog.dismiss();
//        }
//    }

    public void saveData(final String mRequestBody){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://surajshop.eduqfix.com/rest/V1/qfix-campaigns/sms";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Saving data.......");

        String adminToken = sharedPreferences.getString("customerToken",null);

        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try{

                    String smsId = response.toString();
                    new RequestTask1().execute(smsId);

                }catch (Exception e){

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","Bearer "+adminToken);
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
        MaintainRequestQueue.getInstance(SMSContactListActivity.this).addToRequestQueue(req, "tag");



    }

    public void sendFlyer(){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1 +"campaigns/sendflyerlist.php";
 //       String url = "https://surajshop.eduqfix.com/rest/V1/qfix-campaigns/sendflyerlist";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Saving data.......");

        String adminToken = sharedPreferences.getString("customerToken",null);
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("seller_id",sellerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonObject1.toString();

        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try{
                    smsContactModels = new ArrayList<>();
                    smsContactListData = new ArrayList<>();
                    smsContactListDataSize = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("list_id");
                            String listName = jsonObject.getString("list_name");



                             jsonArray1 = jsonObject.getJSONArray("listdata");
                             smsContactListDataSize.add(String.valueOf(jsonArray1.length()));

                            if (jsonArray1.length() > 0) {
                                for (int ii = 0; ii < jsonArray1.length(); ii++) {
                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(ii);

                                    String smsId = jsonObject2.getString("id");
                                    String smslistId = jsonObject2.getString("list_id");
                                    String smsName = jsonObject2.getString("name");
                                    String smsMobile = jsonObject2.getString("mobile");
                                    String smsEmail = jsonObject2.getString("emailid");




                                    smsContactListData.add(new SMSContactListData(smsId, smslistId, smsName, smsMobile, smsEmail,String.valueOf(jsonArray1.length())));

                                }
                            }


                            smsContactModels.add(new SMSContactModel(id, listName, smsContactListData));
                        }

                        smsContactListAdapter = new SMSContactListAdapter(SMSContactListActivity.this,smsContactModels,smsContactListData,smsContactListDataSize,"");
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(smsContactListAdapter);
                        mainLayout.setVisibility(View.VISIBLE);
                        noDataText.setVisibility(View.GONE);

                    }
                    else {
                        mainLayout.setVisibility(View.GONE);
                        noDataText.setVisibility(View.VISIBLE);
                    }
                    }catch (Exception e){

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","Bearer "+adminToken);
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
        MaintainRequestQueue.getInstance(SMSContactListActivity.this).addToRequestQueue(req, "tag");



    }

    public class RequestTask1 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                String smsId = strings[0];
                sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);


                HttpClient client = new DefaultHttpClient();

                String adminToken = sharedPreferences.getString("adminToken",null);
              //  String adminToken = "Bearer cik6o2i5ymqgomcoxmt0wmdtb14b7wai" ;
                String shopUrl = sharedPreferences.getString("shop_url", null);

                String url = "https://"+shopUrl+ BASE_URL +"qfix-sendflyer/sendsms";
                HttpPost post = new HttpPost(url);
                post.setHeader("Authorization", adminToken);

                Log.d("url.....", url);
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("seller_id", String.valueOf(sellerId));
                entityBuilder.addTextBody("sms_id", smsId);
                entityBuilder.addTextBody("list_ids", smsIds);

                String boundary = "---------------" + UUID.randomUUID().toString();
                entityBuilder.setBoundary(boundary);

                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();
                smsContactModels = new ArrayList<>();
                smsContactListData = new ArrayList<>();
                result = EntityUtils.toString(httpEntity);

                Log.v("result", result);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                JSONObject jsonObject = new JSONObject(result);
                String message = jsonObject.getString("message");
                if (statusCode == 200) {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(SMSContactListActivity.this,message,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SMSContactListActivity.this,MarketingDashboard.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();                            }
                    });
                } else {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(SMSContactListActivity.this, " please try again!", Toast.LENGTH_LONG).show();
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


    public class RequestTask2 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
                 String pathFile = sharedPreferences.getString("template_path",null);
                 File f =new File(pathFile, "template.jpg");

                ByteArrayInputStream imageStream = new ByteArrayInputStream(Base64.decode(String.valueOf(f).getBytes(), Base64.DEFAULT));
                Log.d("ur.....", String.valueOf(f));
                HttpClient client = new DefaultHttpClient();


                String url = Constant.REVAMP_URL1 +"marketplace/sendCampaignsEmail.php";
                HttpPost post = new HttpPost(url);
               //  post.setHeader("Content-Type", "multipart/form-data");

                Log.d("url.....", url);
                Log.d("url.....", smsIds);
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("subject", sharedPreferences.getString("subject",null));
                entityBuilder.addTextBody("to_ids", smsIds);

                String boundary = "---------------" + UUID.randomUUID().toString();
                entityBuilder.setBoundary(boundary);
                if (pathFile != null) {
                    entityBuilder.addBinaryBody("file", f,ContentType.create("image/jpg"), String.valueOf(f.getAbsoluteFile()));
                //    entityBuilder.addPart("file", new FileBody(f, ""));
                }
                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
                Log.v("result", result);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                Log.v("result", String.valueOf(statusCode));
                if (statusCode == 200) {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(SMSContactListActivity.this, "Email send successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SMSContactListActivity.this, MarketingDashboard.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();                            }
                    });
                } else {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(SMSContactListActivity.this, "Please try again!", Toast.LENGTH_LONG).show();
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

}
