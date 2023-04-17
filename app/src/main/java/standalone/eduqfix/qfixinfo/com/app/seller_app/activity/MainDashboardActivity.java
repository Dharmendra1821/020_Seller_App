package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.FirebaseApp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
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
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DeliveryBoyOrderActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DeliveryManagementActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DetectSwipeDirectionActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.MyOrdersListActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.MyProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.MyProfileDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.ProfileActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.RefreshToken;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.Request;
import standalone.eduqfix.qfixinfo.com.app.customer.activity.CustomerTabActivity;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.AddProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.login.activity.LoginActivity;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginRequest;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.StoreCustomerAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.community.CommunityActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.fragments.BottomSheeetFragment;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigDetails;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCustomerListModel;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static android.content.Context.MODE_PRIVATE;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;

public class MainDashboardActivity extends AppCompatActivity implements View.OnClickListener, BottomSheeetFragment.ClickListener, BottomSheeetFragment.closeClickListener {

    CardView complaints,product_list,place_order,dashboard,out_of_stock,logout,add_product,card_delivery_management,store_credit;
    CardView request_trial,card_marketing,customers,send_invoice,card_myCustomer,card_question;
    SharedPreferences sharedPreferences;
    TextView seller_name;
    String loginDetails = null;
    ProgressDialog progressDialog = null;
    TextView welcomeMessageTextView;
    String gender = null;
    String sellerGenderPrefix = "";
    LoginRequest customerDetails;
    ImageView my_profile;
    String totalSaleAmount;
    TextView total_sale_amount;
    BottomSheeetFragment bottomSheeetFragment;
    ImageView filter;
    TextView filter_applied;
    LinearLayout communityMarketPlaceLayout;
    int sellerId;
    TextView total_orders,total_products,total_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);

        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(MainDashboardActivity.this);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        FirebaseApp.initializeApp(MainDashboardActivity.this);

        add_product   =  findViewById(R.id.card_add_product);
        product_list  =  findViewById(R.id.card_product_list);
        place_order   =  findViewById(R.id.cardview_place_order);
        dashboard     =  findViewById(R.id.card_myorders);
        out_of_stock  =  findViewById(R.id.outOfStockCard);
        complaints    =  findViewById(R.id.card_complaints);
        store_credit  =  findViewById(R.id.card_store_credit);
        request_trial = findViewById(R.id.card_request_trial);
        card_marketing = findViewById(R.id.card_marketing);
        seller_name   =  findViewById(R.id.dashboard_user_name);
        total_sale_amount = findViewById(R.id.total_sale_amount);
        customers = findViewById(R.id.card_customers);
        filter_applied = findViewById(R.id.filter_applied);
        filter = findViewById(R.id.total_sale_filter);
        card_delivery_management = findViewById(R.id.card_delivery_management);
        my_profile   = findViewById(R.id.dashboard_user_profile);
        send_invoice = findViewById(R.id.card_send_invoice);
        total_orders = findViewById(R.id.total_seller_order);
        total_customer = findViewById(R.id.total_seller_customer);
        total_products = findViewById(R.id.total_seller_products);
        card_myCustomer = findViewById(R.id.card_my_customers);
        card_question = findViewById(R.id.card_seller_qa);

      //  communityMarketPlaceLayout = findViewById(R.id.community_market_layout);

       // getCustomerToken();

        if (sharedPreferences.contains("customerLoginDetails")) {
            loginDetails = sharedPreferences.getString("customerLoginDetails", null);
            customerDetails = new Gson().fromJson(loginDetails, new TypeToken<LoginRequest>() {}.getType());

            if (customerDetails != null) {
                String customerDetails = sharedPreferences.getString("CustomerDetails", null);
                NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {}.getType());
                if (loginResponse != null && loginResponse.getSellerDetails() != null) {
                    gender = loginResponse.getSellerDetails().getGender();
                    Log.d("gender", String.valueOf(gender));
                    if(gender!=null) {
                        if (gender.equalsIgnoreCase("1")) {
                            sellerGenderPrefix = "Mr.";
                        }
                        else if(gender.equalsIgnoreCase("2")){
                            sellerGenderPrefix = "Mrs.";
                        }
                        else {
                            sellerGenderPrefix = "";
                        }
                    }else {
                        sharedPreferences.edit().clear().apply();
                        Intent intent = new Intent(MainDashboardActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                String name = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getFirstName() : "NA";
                seller_name.setText(String.format("Welcome  %s %s", sellerGenderPrefix, name));
            }
        }

        add_product.setOnClickListener(this);
        product_list.setOnClickListener(this);
        place_order.setOnClickListener(this);
        dashboard.setOnClickListener(this);
        out_of_stock.setOnClickListener(this);
        complaints.setOnClickListener(this);
        store_credit.setOnClickListener(this);
        request_trial.setOnClickListener(this);
        card_marketing.setOnClickListener(this);
//        logout.setOnClickListener(this);
        card_delivery_management.setOnClickListener(this);
        my_profile.setOnClickListener(this);
        customers.setOnClickListener(this);
        send_invoice.setOnClickListener(this);
        card_myCustomer.setOnClickListener(this);
        card_question.setOnClickListener(this);
    //    communityMarketPlaceLayout.setOnClickListener(this);

        BottomSheeetFragment.newInstance().setOnItemClickListener(this);
        BottomSheeetFragment.newInstance().setOnCloseClickListener(this);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheeetFragment = BottomSheeetFragment.newInstance();
                bottomSheeetFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");
            }
        });

        try {
            String customerDetails = sharedPreferences.getString("CustomerDetails", null);
            Log.d("customer...", customerDetails);
            NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
            }.getType());
            sellerId = loginResponse != null && loginResponse.getSellerDetails().getSeller_id() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        }
        catch (Exception e){

        }
        getTotalSale(String.valueOf(sellerId));
        checkCommunityOwner();
       //     getTotalCustomers(String.valueOf(sellerId));
       //    getTotalOrders(String.valueOf(sellerId));
       //  getTotalProducts(String.valueOf(sellerId));


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.card_add_product:
                Intent addintent = new Intent(MainDashboardActivity.this, DetectSwipeDirectionActivity.class);
                addintent.putExtra("position",0);
                startActivity(addintent);
                break;
            case R.id.cardview_place_order:
                Intent intent = new Intent(MainDashboardActivity.this, CustomerTabActivity.class);
                startActivity(intent);
                break;
            case R.id.card_delivery_management:
                Intent deliveryintent = new Intent(MainDashboardActivity.this, DeliveryManagementActivity.class);
                startActivity(deliveryintent);
                break;
            case R.id.card_product_list:
                Intent productintent = new Intent(MainDashboardActivity.this, MyProductListActivity.class);
                startActivity(productintent);
                break;
            case R.id.card_myorders:
                Intent intent1 = new Intent(MainDashboardActivity.this, MyOrdersListActivity.class);
                startActivity(intent1);
                break;
            case R.id.outOfStockCard:
                Intent intent2 = new Intent(MainDashboardActivity.this, OutofStockActivity.class);
                startActivity(intent2);
                break;
            case R.id.dashboard_user_profile:
                Intent profile = new Intent(MainDashboardActivity.this, MyProfileDashboardActivity.class);
                startActivity(profile);
                break;
            case R.id.card_complaints:
                Intent complaintsIntent = new Intent(MainDashboardActivity.this, ComplaintsActivity.class);
                startActivity(complaintsIntent);
                break;
            case R.id.card_store_credit:
                Intent storeIntent = new Intent(MainDashboardActivity.this, StoreCreditActivity.class);
                startActivity(storeIntent);
                break;
            case R.id.card_request_trial:
                Intent request = new Intent(MainDashboardActivity.this, RequestTrialListActivity.class);
                startActivity(request);
                break;
            case R.id.card_marketing:
                Intent marketing = new Intent(MainDashboardActivity.this, MarketingDashboard.class);
                startActivity(marketing);
                break;
            case R.id.card_customers:
                Intent customers = new Intent(MainDashboardActivity.this, CustomerListActivity.class);
                startActivity(customers);
                break;
            case R.id.card_send_invoice:
                Intent sendInvoice = new Intent(MainDashboardActivity.this, SendInvoiceDashboard.class);
                startActivity(sendInvoice);
                break;
            case R.id.card_my_customers:
                Intent customer = new Intent(MainDashboardActivity.this, SellerCustomerListActivity.class);
                startActivity(customer);
                break;
            case R.id.card_seller_qa:
                getQA();
                break;
//            case R.id.community_market_layout:
//                Intent communityMarket = new Intent(MainDashboardActivity.this, CommunityActivity.class);
//                startActivity(communityMarket);
//                break;
        }
    }

    public void getCustomerToken(){
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+ Constant.BASE_URL);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        LoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<LoginResponse>() {
        }.getType());

        String email = loginResponse.getCustomerDetails().getEmail();
        String password = sharedPreferences.getString("customerPassword",null);

        Request request = new Request();
        ConfigDetails configDetails = new ConfigDetails();
        configDetails.setUsername(email);
        configDetails.setPassword(password);
        request.setData(configDetails);

        Gson gson = new Gson();
        String productGson = gson.toJson(request);
        Log.d("Product JSON","Product Json == "+productGson);

        mposServices.getCustomerToken(request).enqueue(new Callback<RefreshToken>() {
            @Override
            public void onResponse(Call<RefreshToken> call, retrofit2.Response<RefreshToken> response) {
                if(response.code() == 200 || response.isSuccessful()){

                    String adminToken = response.body().getAdmin_token();
                    String userToken  = response.body().getCustomer_token();

                    Log.d("adminToken...",adminToken);
                    Log.d("userToken...",userToken);
                    SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
                    sharedPreferences.edit().putString("adminToken",adminToken).apply();
                    sharedPreferences.edit().putString("customerToken",userToken).apply();


                }else {
                    //progressDialog.hide();
                 //   Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<RefreshToken> call, Throwable t) {
                //progressDialog.hide();
             //   Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });

    }


    public void getTotalSale(final String sellerId){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
      //  String url = "https://"+shopUrl+ BASE_URL+"marketplace/getTotalSellerSale?seller_id="+sellerId;
        String url = Constant.REVAMP_URL1+"marketplace/dashboard_data.php?sellerId="+sellerId;
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
                    JSONObject jsonObject = new JSONObject(response);
                    String totalSale = jsonObject.getString("total_sale");
                    String totalCustomer = jsonObject.getString("total_customer");
                    String totalOrder = jsonObject.getString("total_order");
                    String totalProduct = jsonObject.getString("total_product");
                    total_sale_amount.setText(getString(R.string.Rs)+ " "+totalSale);
                    total_customer.setText(totalCustomer);
                    total_orders.setText(totalOrder);
                    total_products.setText(totalProduct);


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
        MaintainRequestQueue.getInstance(MainDashboardActivity.this).addToRequestQueue(req, "tag");



    }

    public void getFilterTotalSale(final String sellerId,final String fromDate,final String toDate){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
     //   String url = "https://"+shopUrl+ BASE_URL+"marketplace/getSellerDetailsFromToDate";
        String url = Constant.REVAMP_URL+"marketplace/getSellerDetailsFromToDate.php?sellerId="+sellerId+"&fromDate="+fromDate+"&toDate="+toDate;
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
                    JSONObject jsonObject = new JSONObject(response);
                    String totalSale = jsonObject.getString("total_sale");
                    total_sale_amount.setText(getString(R.string.Rs)+ " "+totalSale);

                    filter_applied.setVisibility(View.VISIBLE);


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

//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    Log.e("Url...", "" + mRequestBody);
//                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
//                    return null;
//                }
//            }

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
        MaintainRequestQueue.getInstance(MainDashboardActivity.this).addToRequestQueue(req, "tag");



    }
    @Override
    public void onItemClick(View v, String fromDate, String toDate) {

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("seller_id",sellerId);
            jsonObject1.put("from_date", fromDate);
            jsonObject1.put("to_date", toDate);
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request.toString();
        getFilterTotalSale(String.valueOf(sellerId),fromDate,toDate);
        bottomSheeetFragment.dismiss();
    }

    @Override
    public void onCloseClick(View v) {
        String fromDate = sharedPreferences.getString("from_date",null);
        if(fromDate == null){
            filter_applied.setVisibility(View.GONE);
            getTotalSale(String.valueOf(sellerId));
        }
        bottomSheeetFragment.dismiss();

    }


    public void checkCommunityOwner(){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1 +"customapi/checkCommmunityOwner.php";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("sellerId",String.valueOf(sellerId));
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request.toString();

        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Getting data.......");
        progressDialog.show();


        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equalsIgnoreCase("success")) {
                        JSONObject request = jsonObject.getJSONObject("request");
                        String microMarketId = request.getString("micromarket_id");
                        if (microMarketId.equalsIgnoreCase("null")) {
                            communityMarketPlaceLayout.setVisibility(View.GONE);
                        }
                        else {
                            sharedPreferences.edit().putString("microMarketId",microMarketId).apply();
                            communityMarketPlaceLayout.setVisibility(View.VISIBLE);
                        }
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
        MaintainRequestQueue.getInstance(MainDashboardActivity.this).addToRequestQueue(req, "tag");
    }

    public void getTotalOrders(final String sellerId){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        //  String url = "https://"+shopUrl+ BASE_URL+"marketplace/getTotalSellerSale?seller_id="+sellerId;
        String url = Constant.REVAMP_URL1+"customapi/TotalNumberOfOrders.php?sellerId="+sellerId;
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
                    JSONObject jsonObject = new JSONObject(response);
                    String totalOrder = jsonObject.getString("total_order");
                    total_orders.setText(totalOrder);


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
        MaintainRequestQueue.getInstance(MainDashboardActivity.this).addToRequestQueue(req, "tag");



    }
    public void getTotalProducts(final String sellerId){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        //  String url = "https://"+shopUrl+ BASE_URL+"marketplace/getTotalSellerSale?seller_id="+sellerId;
        String url = Constant.REVAMP_URL1+"customapi/TotalNumberOfProducts.php?sellerId="+sellerId;
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
                    JSONObject jsonObject = new JSONObject(response);
                    String totalProducts = jsonObject.getString("total_product");
                    total_products.setText(totalProducts);


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
        MaintainRequestQueue.getInstance(MainDashboardActivity.this).addToRequestQueue(req, "tag");



    }
    public void getTotalCustomers(final String sellerId){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        //  String url = "https://"+shopUrl+ BASE_URL+"marketplace/getTotalSellerSale?seller_id="+sellerId;
        String url = Constant.REVAMP_URL1+"customapi/TotalNumberOfCustomers.php?sellerId="+sellerId;
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
                    JSONObject jsonObject = new JSONObject(response);
                    String totalCustomer = jsonObject.getString("total_customer");
                    total_customer.setText(totalCustomer);


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
        MaintainRequestQueue.getInstance(MainDashboardActivity.this).addToRequestQueue(req, "tag");



    }
    public void getQA(){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        //  String url = "https://"+shopUrl+ BASE_URL+"marketplace/getTotalSellerSale?seller_id="+sellerId;
        String url = Constant.REVAMP_URL1+"customapi/QuestionToAdmin.php";
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

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response));
                    startActivity(browserIntent);

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
        MaintainRequestQueue.getInstance(MainDashboardActivity.this).addToRequestQueue(req, "tag");



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
}
