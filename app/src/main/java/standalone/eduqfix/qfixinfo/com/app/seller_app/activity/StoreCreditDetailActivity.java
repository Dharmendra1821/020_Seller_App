package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.ProgressDialog;
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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
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
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.StoreCustomerAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.StoreCustomerDetailAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.DetailCollection;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCreditDetailModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCreditDetailResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCreditListResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCreditRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCustomerListModel;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;

public class StoreCreditDetailActivity extends AppCompatActivity {

    StoreCreditDetailModel storeCreditDetailModel;
    ArrayList<StoreCreditDetailModel> storeCreditDetailModels;
    StoreCustomerDetailAdapter storeCustomerDetailAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String customerName,customerId;
    int sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_credit_detail);

        progressDialog = new ProgressDialog(StoreCreditDetailActivity.this);
        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);

        customerName = getIntent().getStringExtra("customerName");
        customerId = getIntent().getStringExtra("customerId");
        recyclerView = findViewById(R.id.store_credit_detail_recyclerview);
        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;


        linearLayoutManager  = new LinearLayoutManager(StoreCreditDetailActivity.this);
        storeCreditDetailModels = new ArrayList<>();
        getStoreCreditDetail();

    }

    public void getStoreCreditDetail(){
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);


        String adminToken = sharedPreferences.getString("adminToken",null);

        StoreCreditRequest request = new StoreCreditRequest();
        request.setSeller_id(String.valueOf(sellerId));
        request.setCustomer_id(customerId);
        request.setMobile("");

        Gson gson = new Gson();
        String productGson = gson.toJson(request);
        Log.d("Product JSON","Product Json == "+productGson);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading data.......");

        mposServices.getStoreCreditDetail(String.valueOf(sellerId),customerId,"Bearer "+adminToken).enqueue(new Callback<StoreCreditDetailResponse>() {
            @Override
            public void onResponse(Call<StoreCreditDetailResponse> call, retrofit2.Response<StoreCreditDetailResponse> response) {
                if(response.code() == 200 || response.isSuccessful()){

                    Gson gson = new Gson();
                    String productGson = gson.toJson(response.body());
                    Log.d("res",productGson);

                    try{
                        JSONObject jsonObject = new JSONObject(productGson);
                        boolean status = jsonObject.getBoolean("status");

                        if(status){
                            ArrayList<DetailCollection> storeCreditDetailModels = response.body().getMessage().getCollection();
                            storeCustomerDetailAdapter = new StoreCustomerDetailAdapter(StoreCreditDetailActivity.this,storeCreditDetailModels,customerName);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(storeCustomerDetailAdapter);
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
            public void onFailure(Call<StoreCreditDetailResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });

    }
}
