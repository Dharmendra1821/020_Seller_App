package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ComplaintsAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ComplaintsModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class PrintFlyerActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    String page;
    String shopUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_flyer);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(PrintFlyerActivity.this);
        imageView  = findViewById(R.id.print_flyer_imageview);

        try {
             page = getIntent().getStringExtra("from_page");
        }catch (Exception e){

        }


        getPrintFlyer();



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrintFlyerActivity.this,PriintFlyerDetailActivityt.class);
                intent.putExtra("from_page",page);
                startActivity(intent);
            }
        });
    }

    public void getPrintFlyer(){

         shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1 +"customapi/mobilePortraitViewTemplate.php";

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

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if(status.equalsIgnoreCase("true")){

                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String templateImg = jsonObject1.getString("templateimage");
                        String imageUrl =   insertString(templateImg,Constant.REVAMP_URL1+".",7);
                        Glide.with(PrintFlyerActivity.this).load(templateImg).into(imageView);
                       // Glide.with(PrintFlyerActivity.this).load(str).into(imageView);

                    }
                    progressDialog.dismiss();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
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
                params.put("Accept","application/json");
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
        MaintainRequestQueue.getInstance(PrintFlyerActivity.this).addToRequestQueue(req, "tag");

    }

    public static String insertString(
            String originalString,
            String stringToBeInserted,
            int index)
    {

        // Create a new string
        String newString = originalString.substring(0, index + 1)
                + stringToBeInserted
                + originalString.substring(index + 1);

        // return the modified String

        Log.d("new string",newString);
        return newString;
    }
}
