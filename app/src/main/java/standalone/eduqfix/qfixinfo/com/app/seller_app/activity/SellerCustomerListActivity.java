package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.CategoryAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.MySellerCustomerNewAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SellerCustomerListAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Categories;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SellerCustomerNewModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class SellerCustomerListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MySellerCustomerNewAdapter mySellerCustomerNewAdapter;
    SellerCustomerNewModel sellerCustomerNewModel;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    LinearLayoutManager linearLayoutManager;
    ArrayList<SellerCustomerNewModel> sellerCustomerNewModels;
    int sellerId;
    int textlength = 0;
    public static ArrayList<SellerCustomerNewModel> array_sort;
    EditText filterSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_customer_list);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(SellerCustomerListActivity.this);
        recyclerView = findViewById(R.id.seller_customerNew_recyclerview);
        filterSearch = findViewById(R.id.filter_customer_list);
        linearLayoutManager = new LinearLayoutManager(SellerCustomerListActivity.this);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;


        getCustomerList(String.valueOf(sellerId));
        array_sort = new ArrayList<>();

        filterSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                textlength = filterSearch.getText().length();
                array_sort.clear();
                for (int j = 0; j < sellerCustomerNewModels.size(); j++) {
                    if (textlength <= sellerCustomerNewModels.get(j).getCustomerName().length()) {
                        if (sellerCustomerNewModels.get(j).getCustomerName().toLowerCase().trim().contains(
                                filterSearch.getText().toString().toLowerCase().trim())) {
                            array_sort.add(sellerCustomerNewModels.get(j));
                        }
                    }
                }
                mySellerCustomerNewAdapter = new MySellerCustomerNewAdapter(SellerCustomerListActivity.this, array_sort);
                recyclerView.setAdapter(mySellerCustomerNewAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
            }
        });
    }

    public void getCustomerList(final String sellerId){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        //  String url = "https://"+shopUrl+ BASE_URL+"marketplace/getTotalSellerSale?seller_id="+sellerId;
        String url = Constant.REVAMP_URL1+"customapi/MyCustomerList.php?sellerId="+sellerId;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);


        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Getting data.......");
        progressDialog.show();




        StringRequest req = new StringRequest(com.android.volley.Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    sellerCustomerNewModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray customer = jsonObject.getJSONArray("customers");
                    if(customer.length() > 0){
                        for (int i=0; i < customer.length(); i++) {
                            JSONObject jsonObject1 = customer.getJSONObject(i);
                            String customerId = jsonObject1.getString("customer_id");
                            String customerName = jsonObject1.getString("customer_name");
                            String customerEmail = jsonObject1.getString("customer_email");
                            String customerMobile = jsonObject1.getString("customer_mobile");
                            String status = jsonObject1.getString("status");
                            String addedOn = jsonObject1.getString("added_on");

                            sellerCustomerNewModels.add(new SellerCustomerNewModel(customerId,customerName,customerEmail,customerMobile,status,"",addedOn));
                        }
                        mySellerCustomerNewAdapter = new MySellerCustomerNewAdapter(SellerCustomerListActivity.this,sellerCustomerNewModels);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(mySellerCustomerNewAdapter);
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
            public com.android.volley.Request.Priority getPriority() {
                return com.android.volley.Request.Priority.IMMEDIATE;
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
        // Adding request to request queue
        MaintainRequestQueue.getInstance(SellerCustomerListActivity.this).addToRequestQueue(req, "tag");



    }
}
