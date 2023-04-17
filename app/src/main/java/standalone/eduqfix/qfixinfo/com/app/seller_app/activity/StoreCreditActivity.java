package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.RefreshToken;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.MemoDetailAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.StoreCustomerAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigDetails;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductLink;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCreditListResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCreditRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCustomerListModel;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class StoreCreditActivity extends AppCompatActivity implements StoreCustomerAdapter.ClickListener {

    StoreCustomerListModel storeCustomerListModel;
    ArrayList<StoreCustomerListModel> storeCustomerListModels;
    StoreCustomerAdapter storeCustomerAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    TextView totalRefundAmount;
    int sellerId;
    TextView noDataFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_credit);


        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
        progressDialog = new ProgressDialog(StoreCreditActivity.this);
        recyclerView = findViewById(R.id.store_credit_recyclerview);
        totalRefundAmount = findViewById(R.id.totalRefundAmount);
        noDataFound = findViewById(R.id.store_credit_nodata);
        linearLayoutManager  = new LinearLayoutManager(StoreCreditActivity.this);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        getStoreList();

        storeCustomerAdapter = new StoreCustomerAdapter();
        storeCustomerAdapter.setOnItemClickListener(this);



    }

    public void getStoreList(){
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        LoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<LoginResponse>() {
        }.getType());

        String adminToken = sharedPreferences.getString("adminToken",null);

        StoreCreditRequest request = new StoreCreditRequest();
        request.setSeller_id(String.valueOf(sellerId));
        request.setCustomer_id("");
        request.setMobile("");

        Gson gson = new Gson();
        String productGson = gson.toJson(request);
        Log.d("Product JSON","Product Json == "+productGson);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading data.......");

        mposServices.getStoreCredit(String.valueOf(sellerId),"","Bearer "+adminToken).enqueue(new Callback<StoreCreditListResponse>() {
            @Override
            public void onResponse(Call<StoreCreditListResponse> call, retrofit2.Response<StoreCreditListResponse> response) {
                if(response.code() == 200 || response.isSuccessful()){


                     Gson gson = new Gson();
                     String productGson = gson.toJson(response.body());
                    storeCustomerListModels = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(productGson);
                        boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            JSONObject message = jsonObject.getJSONObject("message");
                            JSONObject collection = message.getJSONObject("collection");
                            String totalRefunds = collection.getString("total_store_refunds");
                            if (totalRefunds.equalsIgnoreCase("₹ 0")) {
                                recyclerView.setVisibility(View.GONE);
                                noDataFound.setVisibility(View.VISIBLE);
                            }
                            JSONArray items = collection.getJSONArray("items");
                            if (items.length() > 0) {
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject jsonObject2 = items.getJSONObject(i);
                                    String name = jsonObject2.getString("customer_name");
                                    String balance = jsonObject2.getString("balance");
                                    String customerMobile = jsonObject2.getString("customer_mobile");
                                    String customerId = jsonObject2.getString("customer_id");

                                    storeCustomerListModels.add(new StoreCustomerListModel(name, balance, customerMobile, customerId));
                                }

                                storeCustomerAdapter = new StoreCustomerAdapter(StoreCreditActivity.this, storeCustomerListModels);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(storeCustomerAdapter);

                                totalRefundAmount.setText("Total Store Refunds " + totalRefunds);
                            }
                        }
                    }catch (Exception e){

                    }

                    progressDialog.dismiss();

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<StoreCreditListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MaintainRequestQueue.getInstance(StoreCreditActivity.this).cancelRequest("tag");
    }

    @Override
    public void onItemClick(View v, int position) {
        storeCustomerListModel = storeCustomerAdapter.getWordAtPosition(position);
        Intent intent = new Intent(StoreCreditActivity.this,StoreCreditDetailActivity.class);
        intent.putExtra("customerName",storeCustomerListModel.getCustomerName());
        intent.putExtra("customerId",storeCustomerListModel.getCustomer_id());
        startActivity(intent);
    }
}
