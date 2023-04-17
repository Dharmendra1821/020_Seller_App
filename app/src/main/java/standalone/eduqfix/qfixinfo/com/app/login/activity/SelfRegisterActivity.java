package standalone.eduqfix.qfixinfo.com.app.login.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.OrderListActivity;
import standalone.eduqfix.qfixinfo.com.app.customer.activity.AddCustomerAddressActivity;
import standalone.eduqfix.qfixinfo.com.app.customer.model.State;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.model.InvoiceMerchantIdModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.AddInvoiceActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.CreditMemoDetailActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.EmailActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.MainDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.SMSContactListActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.SellerInvoiceListActivity;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class SelfRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    List<State> stateList = null;
    String stateName,code,region_id;
    SharedPreferences sharedPreferences;
    EditText b_name,b_email,b_mobile,b_shop_url,b_merchant_name,b_address,b_city,b_pincode;
    SearchableSpinner stateSpinner,category;
    ProgressDialog progressDialog;
    Button save;
    ArrayList<String> categotyValue;
    String categoryId;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_register);

        stateSpinner = findViewById(R.id.self_reg_business_state);
        category  = findViewById(R.id.self_reg_business_categoy);
        b_name  = findViewById(R.id.self_reg_business_name);
        b_email  = findViewById(R.id.self_reg_business_emailid);
        b_mobile  = findViewById(R.id.self_reg_business_phoneno);
        b_shop_url  = findViewById(R.id.self_reg_business_shop_url);
        b_merchant_name = findViewById(R.id.self_reg_business_merchant_name);
        b_address = findViewById(R.id.self_reg_business_address);
        b_city = findViewById(R.id.self_reg_business_city);
        b_pincode = findViewById(R.id.self_reg_business_pincode);
        save = findViewById(R.id.self_reg_business_submit);

        progressDialog = new ProgressDialog(this);

        categotyValue = new ArrayList<>();

        getStateList();
        Category();

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateName = parent.getSelectedItem().toString();
                for(State state : stateList){
                    if(state.getName().equalsIgnoreCase(stateName)){
                     //   code = state.getCode();
                        region_id = state.getRegionId();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryId = adapterView.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        save.setOnClickListener(this);

    }

    public void getStateList(){
        try{

            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+ Constant.BASE_URL);
            MPOSServices mposServices = retrofit.create(MPOSServices.class);

            mposServices.getStateList().enqueue(new Callback<List<State>>() {
                @Override
                public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                    if(response.code() == 200 || response.isSuccessful()){
                        stateList = response.body();
                        List<String> stateNameList = new ArrayList<>();
                        for(State state : stateList){
                            stateNameList.add(state.getName());
                        }
                        ArrayAdapter<String> stateAdapter= new ArrayAdapter<String>(SelfRegisterActivity.this,android.R.layout.simple_list_item_1, stateNameList);
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        stateSpinner.setAdapter(stateAdapter);
                    }else if(response.code() == 400){
                        Toast.makeText(SelfRegisterActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 401){
                        Toast.makeText(SelfRegisterActivity.this,"Unauthorized",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        Toast.makeText(SelfRegisterActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<State>> call, Throwable t) {

                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void Category(){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url ="https://galastores.eduqfix.com/rest/V1/qfix-o2o/mastercategory/search?searchCriteria=1";
      //  String url ="https://"+shopUrl+ Constant.BASE_URL+"qfix-o2o/mastercategory/search?searchCriteria=1";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading .......");

        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    JSONObject res = new JSONObject(response);
                    JSONArray jsonArray = res.getJSONArray("items");
                    if(jsonArray.length() > 0){
                        for (int i = 0; i <jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String mastercategoryId = jsonObject.getString("mastercategory_id");
                            String title = jsonObject.getString("title");

                            categotyValue.add(title);
                        }

                        final ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(SelfRegisterActivity.this, R.layout.spinner_text_layout, categotyValue);
                        category.setAdapter(statusAdapter);

                    }




                }catch (Exception e){

                }

                progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("TAG", "Error: " + error.getMessage());
                Toast.makeText(SelfRegisterActivity.this,"Something went wrong, Try again!",Toast.LENGTH_LONG).show();
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
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        MaintainRequestQueue.getInstance(SelfRegisterActivity.this).addToRequestQueue(req, "tag");



    }

    @Override
    public void onClick(View v) {

        if(b_name.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(SelfRegisterActivity.this,"Enter Business name",Toast.LENGTH_LONG).show();
        }
        else if(b_email.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(SelfRegisterActivity.this,"Enter Email",Toast.LENGTH_LONG).show();
        }
        else if(!b_email.getText().toString().matches(emailPattern)){
            Toast.makeText(SelfRegisterActivity.this, "Please enter valid email ID", Toast.LENGTH_SHORT).show();
        }
        else if(b_mobile.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(SelfRegisterActivity.this,"Enter phone number",Toast.LENGTH_LONG).show();
        }
        else if(b_mobile.getText().toString().length() < 10){
            Toast.makeText(SelfRegisterActivity.this,"Enter valid phone number",Toast.LENGTH_LONG).show();
        }
        else if(b_shop_url.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(SelfRegisterActivity.this,"Enter shop url",Toast.LENGTH_LONG).show();
        }
        else if(b_merchant_name.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(SelfRegisterActivity.this,"Enter Merchant name",Toast.LENGTH_LONG).show();
        }
        else if(b_address.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(SelfRegisterActivity.this,"Enter address",Toast.LENGTH_LONG).show();
        }
        else if(b_city.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(SelfRegisterActivity.this,"Enter city",Toast.LENGTH_LONG).show();
        }
        else if(b_pincode.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(SelfRegisterActivity.this,"Enter pincode",Toast.LENGTH_LONG).show();
        }
        else {

            JSONObject jsonObject1 = new JSONObject();
            JSONObject request = new JSONObject();
            try {
                jsonObject1.put("busieness_name", b_name.getText().toString());
                jsonObject1.put("business_category", categoryId);
                jsonObject1.put("email_id", b_email.getText().toString());
                jsonObject1.put("phone_number",  b_mobile.getText().toString());
                jsonObject1.put("shop_url",  b_shop_url.getText().toString());
                jsonObject1.put("merchant_name",  b_merchant_name.getText().toString());
                jsonObject1.put("address",  b_address.getText().toString());
                jsonObject1.put("city",  b_city.getText().toString());
                jsonObject1.put("state", region_id);
                jsonObject1.put("pincode",  b_pincode.getText().toString());
                request.put("selfregistration", jsonObject1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final String mRequestBody = request.toString();
            saveData(mRequestBody);

            Log.d("req", mRequestBody);
        }
    }

    public void saveData(final String mRequestBody){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url ="https://galastores.eduqfix.com/rest/V1/qfix-selfregistration/selfregistration";
      //  String url ="https://"+shopUrl+ Constant.BASE_URL+"V1/qfix-selfregistration/selfregistration";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Saving data.......");


        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try{
                    alertDialog1();
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
        MaintainRequestQueue.getInstance(SelfRegisterActivity.this).addToRequestQueue(req, "tag");



    }

    public void alertDialog1(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SelfRegisterActivity.this);
        builder1.setMessage("Thank you for registering with us !!");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent =new Intent(SelfRegisterActivity.this, EmailActivity.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}
