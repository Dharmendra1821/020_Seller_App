package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.MyOrdersListActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModelNew;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ComplaintsModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class ComplaintsDetailsActivity extends AppCompatActivity {

    TextView orderId,itemId,customerName,vendorComments,customerComments;
    EditText comments;
    ArrayList<String> statusValue;
    SearchableSpinner status;
    Map<Integer, Integer> mSpinnerSelectedItem = new HashMap<Integer, Integer>();
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    Button submit;
    String pendingStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints_details);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(ComplaintsDetailsActivity.this);
        statusValue   = new ArrayList<>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ComplaintsModel complaintsModel = (ComplaintsModel) bundle.getSerializable("complaints");

        orderId = findViewById(R.id.complaints_details_orderId);
        itemId = findViewById(R.id.complaints_details_itemId);
        customerName = findViewById(R.id.complaints_details_custname);
        vendorComments = findViewById(R.id.complaints_details_vendorcomments);
        customerComments = findViewById(R.id.complaints_details_cust_comments);
        status = findViewById(R.id.complaints_details_status);
        comments = findViewById(R.id.seller_comments);
        submit = findViewById(R.id.complaints_submit);

        orderId.setText("Order Id : "+complaintsModel.getOrderid());
        itemId.setText("Item Id : "+complaintsModel.getItemid());
        customerName.setText(complaintsModel.getCustomer_name());

        if(complaintsModel.getVendor_comments().equalsIgnoreCase("null")){
            vendorComments.setText("");
        }
        else {
            String vendorComm = complaintsModel.getVendor_comments().replace("/", "");
            String vendorCommentsVal = vendorComm.replace("\n", "</br>");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                vendorComments.setText(Html.fromHtml(vendorCommentsVal, Html.FROM_HTML_MODE_COMPACT));
            } else {
                vendorComments.setText(Html.fromHtml(vendorCommentsVal));
            }
        }
        if(complaintsModel.getCustomer_comments().equalsIgnoreCase("null")){
            customerComments.setText("");
        }

        customerComments.setText(complaintsModel.getCustomer_comments());

        statusValue.add("Open");
        statusValue.add("Reject");
        statusValue.add("In Process");
        statusValue.add("Complete");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(ComplaintsDetailsActivity.this, R.layout.spinner_text_layout, statusValue);
        status.setAdapter(statusAdapter);
        int spinnerPosition = statusAdapter.getPosition(complaintsModel.getRequest_status());
        status.setSelection(spinnerPosition);

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pendingStatus =  parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commentsValue = comments.getText().toString();

                if(comments.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(ComplaintsDetailsActivity.this,"Enter comment",Toast.LENGTH_LONG).show();
                }
                else {
                    JSONObject jsonObject1 = new JSONObject();
                    JSONObject request = new JSONObject();
                    try {
                        jsonObject1.put("id", complaintsModel.getEntity_id());
                        jsonObject1.put("status", pendingStatus);
                        jsonObject1.put("vendor_comment", commentsValue);
                        request.put("request",jsonObject1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final String mRequestBody = jsonObject1.toString();
                    saveComments(mRequestBody);
                 }
                }
        });

    }

    public void saveComments(final String mRequestBody){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"marketplace/saveComplaintBySeller.php";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Saving Comments.......");


        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("success");
                    if(status.equalsIgnoreCase("true")){
                        Toast.makeText(ComplaintsDetailsActivity.this,"Comments Successfully Updated",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ComplaintsDetailsActivity.this,MainDashboardActivity.class);
                        startActivity(intent);
                        finish();
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
        MaintainRequestQueue.getInstance(ComplaintsDetailsActivity.this).addToRequestQueue(req, "tag");



    }

}
