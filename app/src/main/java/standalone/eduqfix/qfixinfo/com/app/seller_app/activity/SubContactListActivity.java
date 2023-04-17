package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.EmailSubContactListAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SMSContactListAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSContactListData;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

public class SubContactListActivity extends Activity {

    SMSContactListData smsContactListData;
    ArrayList<SMSContactListData> smsContactListDataArrayList;
    RecyclerView recyclerView;
    EmailSubContactListAdapter emailSubContactListAdapter;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    Button send;
    ProgressDialog progressDialog;
    String result;
    String smsIds;
    LinearLayout close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sub_contact_list);



        progressDialog = new ProgressDialog(SubContactListActivity.this);

        sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
        Intent intent =  getIntent();
        Bundle bundle =  intent.getExtras();
        smsContactListDataArrayList = new ArrayList<>();
        smsContactListDataArrayList = SMSContactListAdapter.getSmsContactListData();

        // smsContactListDataArrayList = sharedPreferences.getString("contact_data",null);
        recyclerView = findViewById(R.id.sub_contact_recyclerview);
        send = findViewById(R.id.send_sms);

        linearLayoutManager = new LinearLayoutManager(SubContactListActivity.this);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emailSubContactListAdapter = new EmailSubContactListAdapter(SubContactListActivity.this,smsContactListDataArrayList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(emailSubContactListAdapter);




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
                    entityBuilder.addBinaryBody("file", f, ContentType.create("image/jpg"), String.valueOf(f.getAbsoluteFile()));
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
                            Toast.makeText(SubContactListActivity.this, "Email send successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SubContactListActivity.this, MarketingDashboard.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();                            }
                    });
                } else {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(SubContactListActivity.this, "Please try again!", Toast.LENGTH_LONG).show();
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