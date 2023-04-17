package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.RefreshToken;
import standalone.eduqfix.qfixinfo.com.app.customer.model.CustomerDetails;
import standalone.eduqfix.qfixinfo.com.app.customer.model.CustomerResponse;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.CartProductAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.interfaces.ICartUpdate;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Address;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigDetails;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.EstimateShippingCharge;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.EstimateShippingResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewCartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.OrderSummary;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Request;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ShippingRateRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ShippingRates;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

public class ViewCartActivity extends AppCompatActivity implements ICartUpdate {

    SharedPreferences sharedPreferences;
    List<ProductCartResponse> productList = null;
    RecyclerView recyclerView;
    Double billAmount = 0.0;
    Double totalAmount = 0.0;
    Integer shippingRate = 0;
    Button placeOrderButton;
    Integer CustomerId,CustomerAddressId;
    TextView billAmountTextView,shippingChargesTextView,taxTextView,totalTextView;
    ProgressDialog progressDialog;
    Integer ProductCartId=0;
    String customerToken = null,loginDetails;
    String adminToken = null;
    String customerAddressDetails = null,newCustomerDetails = null;
    Integer SellerId = null;
    LoginResponse customerDetails = null;
    CustomerResponse customerResponse = null;
    Context context;
    private CartProductAdapter cartProductAdapter;
    CardView noProductCardView;
    LinearLayout summaryGridLyaout;
    TextView noCategoriesFoundTextView;
    double taxValue;
    int flag;
    List<EstimateShippingResponse> estimateShippingResponses;
    EstimateShippingResponse estimateShippingResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_cart);

        try {
            sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
            customerToken = sharedPreferences.getString("customerToken", null);
            adminToken = sharedPreferences.getString("adminToken", null);
            customerAddressDetails = sharedPreferences.getString("selectedCustomerDetails", "");
            newCustomerDetails = sharedPreferences.getString("newCustomer", null);
            customerResponse = new Gson().fromJson(loginDetails, new TypeToken<CustomerResponse>() {
            }.getType());
            loginDetails = sharedPreferences.getString("Cu" +
                    "stomerDetails", null);
            customerDetails = new Gson().fromJson(loginDetails, new TypeToken<LoginResponse>() {
            }.getType());
            recyclerView = findViewById(R.id.cartProductRecyclerView);
            noProductCardView = findViewById(R.id.noProductCardViewCart);
            summaryGridLyaout = findViewById(R.id.summaryGridLyaout);
            noCategoriesFoundTextView = findViewById(R.id.noCategoriesFoundTextView);
            cartProductAdapter = new CartProductAdapter(this);

            flag = 0;

            progressDialog = new ProgressDialog(ViewCartActivity.this);
            progressDialog.setMessage("Loading...");
            if (getIntent().getExtras() != null) {
                CustomerId = getIntent().getExtras().getInt("CustomerId");
                CustomerAddressId = getIntent().getExtras().getInt("CustomerAddressId");
            } else {
                CustomerId = sharedPreferences.getInt("CustomerId", 0);
                CustomerAddressId = sharedPreferences.getInt("CustomerAddressId", 0);
            }

            if (CustomerId == 0 || CustomerId == null) {
                CustomerDetails customerDetails = new Gson().fromJson(customerAddressDetails, new TypeToken<CustomerDetails>() {
                }.getType());
                CustomerId = customerDetails != null ? customerDetails.getItems().get(0).getId() : Integer.valueOf(customerResponse.getEntityId());
                sharedPreferences.edit().putInt("CustomerId", CustomerId).apply();
            }

            billAmountTextView = findViewById(R.id.billAmountTextView);
            taxTextView = findViewById(R.id.taxTextView);
            totalTextView = findViewById(R.id.totalTextView);
            placeOrderButton = findViewById(R.id.placeOrderButton);

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            placeOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    placeOrder();
                }
            });

        }catch (Exception e){
        }
      //  estimateShippingCharges();
      //  getPaymentInformation();
        getProductInCart();
        //getPaymentInformation();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getPaymentInformation(){
        try{
            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
            MPOSServices services = retrofit.create(MPOSServices.class);
            String token = "Bearer "+ customerToken;
            int cartId = sharedPreferences.getInt("ProductCartId",0);
            if(customerToken == null){
                sharedPreferences = context.getSharedPreferences("StandAlone",MODE_PRIVATE);
                customerToken = sharedPreferences.getString("customerToken",null);
                token = "Bearer "+ customerToken;
            }
            services.getPaymentInformation(String.valueOf(cartId)).enqueue(new Callback<OrderSummary>() {
                @Override
                public void onResponse(@NonNull Call<OrderSummary> call, @NonNull Response<OrderSummary> response) {
                    if(response.code() == 200 || response.isSuccessful()){

                        Gson gson = new Gson();
                        String productGson = gson.toJson(response.body());
                        Log.d("Product JSON", "Product Json Uniform== " + productGson);


                        OrderSummary orderSummary = response.body();
                        if(orderSummary != null && orderSummary.getTotals() != null){
                            billAmount = Double.parseDouble(String.valueOf(orderSummary.getTotals().getGrandTotal() != null ? orderSummary.getTotals().getGrandTotal() : 0));
                            if(billAmount > 0){
                                placeOrderButton.setVisibility(View.VISIBLE);
                                summaryGridLyaout.setVisibility(View.VISIBLE);
                                noProductCardView.setVisibility(View.GONE);
                                noCategoriesFoundTextView.setVisibility(View.GONE);
                            }else {
                                noProductCardView.setVisibility(View.VISIBLE);
                                noCategoriesFoundTextView.setVisibility(View.VISIBLE);
                                placeOrderButton.setVisibility(View.GONE);
                                summaryGridLyaout.setVisibility(View.GONE);
                            }
                            taxValue = orderSummary.getTotals().getTaxAmount() != null ? orderSummary.getTotals().getTaxAmount() : 00;
                            totalAmount = billAmount + shippingRate + taxValue;
                            billAmountTextView.setText(getString(R.string.Rs)+String.valueOf(billAmount != null && billAmount != 0.0 ? billAmount : 00.0));
                            String price = String.format("%.2f", Double.valueOf(totalAmount));
                         //   totalTextView.setText(getString(R.string.Rs)+String.valueOf(price));
                            taxTextView.setText(getString(R.string.Rs)+String.valueOf(orderSummary.getTotals().getTaxAmount() != null ? orderSummary.getTotals().getTaxAmount() : 00));

                        }

                    }else if(response.code() == 401){
                        Toast.makeText(ViewCartActivity.this, "Unauthorized customer", Toast.LENGTH_SHORT).show();
                    }else if(response.code() == 400){
                        Toast.makeText(ViewCartActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        Toast.makeText(ViewCartActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ViewCartActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<OrderSummary> call, @NonNull Throwable t) {
                    Toast.makeText(ViewCartActivity.this,"Somethig went wrong",Toast.LENGTH_LONG).show();
                }
            });

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


   /* public void getShippingRatesForSeller(){
        shippingChargesTextView = findViewById(R.id.shippingChargesTextView);
        try {
            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
            MPOSServices services = retrofit.create(MPOSServices.class);

            ShippingRateRequest shippingRateRequest = new ShippingRateRequest();
            final Request request = new Request();
            Integer sellerId = customerDetails.getCustomerDetails().getId();
            List<Integer> sellerList = new ArrayList<>();
            sellerList.add(sellerId);
            request.setSellers(sellerList);
            shippingRateRequest.setRequest(request);
            Gson gson = new Gson();
            final String data = gson.toJson(shippingRateRequest);
            Log.d("Request Data ===","Request Data ==="+data);
            services.getShippingRateForSeller(shippingRateRequest).enqueue(new Callback<List<ShippingRates>>() {
                @Override
                public void onResponse(@NonNull Call<List<ShippingRates>> call, @NonNull Response<List<ShippingRates>> response) {
                    if(response.code() == 200 || response.isSuccessful()){
                        List<ShippingRates> shippingRateRequestList = response.body();

                        Gson gson = new Gson();
                        String productGson = gson.toJson(shippingRateRequestList);
                        Log.d("Product JSON","Product Json == "+productGson);
                        if(shippingRateRequestList != null && shippingRateRequestList.size() > 0){
                            ShippingRates shippingRates = shippingRateRequestList.get(0);
                            shippingRate = Integer.parseInt(shippingRates.getShippingRate());
                            sharedPreferences.edit().putString("shipping_rate_service", shippingRates.getShippingRate()).apply();
                            shippingChargesTextView.setText(getString(R.string.Rs)+String.valueOf(shippingRate));
                        }
                    }else if(response.code() == 401){
                        Toast.makeText(ViewCartActivity.this,"Unauthorized",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 400){
                        Toast.makeText(ViewCartActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        Toast.makeText(ViewCartActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(ViewCartActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ShippingRates>> call, @NonNull Throwable t) {
                    Toast.makeText(ViewCartActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }*/

    public void estimateShippingCharges(){
        shippingChargesTextView = findViewById(R.id.shippingChargesTextView);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String adminToken = sharedPreferences.getString("adminToken",null);
        String custToken =  sharedPreferences.getString("customerToken",null);


        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices services = retrofit.create(MPOSServices.class);

        String shippedStreet = sharedPreferences.getString("shipping_street",null);
        String shippedCity = sharedPreferences.getString("shipping_city",null);
        String shippedPostCode = sharedPreferences.getString("shipping_postcode",null);
        String shippedRegion = sharedPreferences.getString("shipping_region",null);
        int regionId = sharedPreferences.getInt("shipping_regionId",0);
        String shippedTelephone = sharedPreferences.getString("shipping_telephone",null);


        EstimateShippingCharge estimateShippingCharge = new EstimateShippingCharge();
        Address address = new Address();
        address.setCity(shippedCity);
        address.setCountryId("IN");
        address.setFax("");
        address.setPostcode(shippedPostCode);
        address.setRegion(shippedRegion);
        address.setRegionId(regionId);
        address.setSaveInAddressBook(1);
        //address.setStreet(shippedStreet);
        address.setTelephone(shippedTelephone);
        estimateShippingCharge.setAddress(address);

        Gson gson = new Gson();
        final String cart = gson.toJson(estimateShippingCharge);
        Log.d("Product JSON","Product Json Uniform== "+cart);
        services.estimateShippingCharges(estimateShippingCharge,"Bearer "+custToken).enqueue(new Callback<List<EstimateShippingResponse>>() {
            @Override
            public void onResponse(Call<List<EstimateShippingResponse>> call, Response<List<EstimateShippingResponse>> response) {
                Log.d("url...", String.valueOf(call.request().url()));
                if(response.code() == 200 ||response.isSuccessful()){
                    estimateShippingResponses = response.body();
                    for (int i = 0;i<estimateShippingResponses.size();i++){
                        float amount = estimateShippingResponses.get(i).getAmount();
                        Log.d("amount..", String.valueOf(amount));
                        sharedPreferences.edit().putFloat("shipping_rate_service", amount).apply();
                        float shippedRate = sharedPreferences.getFloat("shipping_rate_service",0);
                       // shippingChargesTextView.setText(getString(R.string.Rs)+String.valueOf(shippedRate));
                    }
                }else if(response.code() == 400){
                }else if(response.code() == 401){
                }else if(response.code() == 500){
                }else {
                }
            }
            @Override
            public void onFailure(Call<List<EstimateShippingResponse>> call, Throwable t) {
            }
        });
    }

    public void placeOrder(){
        Intent intent = new Intent(ViewCartActivity.this, PaymentMethodActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("CustomerId",CustomerId);
        bundle.putInt("CustomerAddressId",CustomerAddressId);
        bundle.putString("Amount", totalTextView.getText().toString());
        bundle.putBoolean("isSeller",true);
        intent.putExtras(bundle);
        startActivity(intent);
    }



    public void getCartItemCount(){

        progressDialog = new ProgressDialog(ViewCartActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String adminToken = sharedPreferences.getString("adminToken",null);
        String custToken = sharedPreferences.getString("customerToken",null);
        NewCartRequest customerProductCart = new NewCartRequest();
        CartRequest customerProductCartRequest = new CartRequest();

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices services = retrofit.create(MPOSServices.class);
        int cartId = sharedPreferences.getInt("ProductCartId",0);
        customerProductCartRequest.setUser_id(String.valueOf(cartId));
       // customerProductCartRequest.setUserToken(custToken);
        customerProductCart.setRequest(customerProductCartRequest);
        services.getCustomerProductInCart(customerProductCart).enqueue(new Callback<List<ProductCartResponse>>() {
            @Override
            public void onResponse(Call<List<ProductCartResponse>> call, Response<List<ProductCartResponse>> response) {
                if(response.code() == 200 ||response.isSuccessful()){
                    progressDialog.dismiss();
                    sharedPreferences.edit().putString("cartCount", String.valueOf(response.body().size())).apply();
                }else if(response.code() == 400){
                    progressDialog.dismiss();
                    Toast.makeText(ViewCartActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                }else if(response.code() == 401){
                    progressDialog.dismiss();
                    Toast.makeText(ViewCartActivity.this,"Unauthorised",Toast.LENGTH_LONG).show();
                }else if(response.code() == 500){
                    progressDialog.dismiss();
                    Toast.makeText(ViewCartActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                }else {
                    progressDialog.dismiss();
                  //  Toast.makeText(ViewCartActivity.this,"Something went wronggg",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductCartResponse>> call, Throwable t) {
                progressDialog.dismiss();
            //    Toast.makeText(ViewCartActivity.this,"Something went wronggg",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getProductInCart(){
        try
        {

            progressDialog.show();
            progressDialog.setCancelable(false);
            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
            MPOSServices services = retrofit.create(MPOSServices.class);

            NewCartRequest customerProductCart = new NewCartRequest();
            CartRequest customerProductCartRequest = new CartRequest();
            int cartId = sharedPreferences.getInt("ProductCartId",0);
            customerProductCartRequest.setUser_id(String.valueOf(cartId));
          //  customerProductCartRequest.setUserToken(customerToken);
            customerProductCart.setRequest(customerProductCartRequest);
            Log.d("Request.....", String.valueOf(customerProductCartRequest));

            Gson gson = new Gson();
            final String data = gson.toJson(customerProductCartRequest);
            Log.d("Request Data ===","Request Data ==="+data);

            services.getCustomerProductInCart(customerProductCart).enqueue(new Callback<List<ProductCartResponse>>() {
                @Override
                public void onResponse(Call<List<ProductCartResponse>> call, Response<List<ProductCartResponse>> response) {
                    Log.d("Response code....", String.valueOf(response.code()));
                    if(response.code() == 200 ||response.isSuccessful()){
                        productList=response.body();

                        Gson gson = new Gson();
                        String cartList = gson.toJson(productList);
                        Log.d("Product list....",cartList);

                        if(productList.size()>0){
                            noProductCardView.setVisibility(View.GONE);
                            noCategoriesFoundTextView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            sharedPreferences.edit().putString("cartList", cartList).apply();
                        }
                        CartProductAdapter adapter = new CartProductAdapter(getApplicationContext(),productList,billAmountTextView,totalTextView,shippingChargesTextView,shippingRate,ViewCartActivity.this,flag,taxValue);
                        recyclerView.setAdapter(adapter);
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }else if(response.code() == 401){
                        progressDialog.dismiss();
                        Toast.makeText(ViewCartActivity.this, "Unauthorized customer", Toast.LENGTH_SHORT).show();
                    }else if(response.code() == 400){
                        progressDialog.dismiss();
                        Toast.makeText(ViewCartActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        progressDialog.dismiss();
                        Toast.makeText(ViewCartActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                    }else{
                        progressDialog.dismiss();
                     //   Toast.makeText(ViewCartActivity.this, "Something went wrongg", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<ProductCartResponse>> call, Throwable t) {
                    progressDialog.dismiss();
                //    Toast.makeText(ViewCartActivity.this,"Something went wrongg",Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception ex){
            progressDialog.dismiss();
            ex.printStackTrace();
        }
    }

    @Override
    public void onCartItemUpdatedOrDeleted(Context context) {
        this.context = context;
       // getPaymentInformation();
        getCartItemCount();
        getProductInCart();
    }



//    public void getCustomerToken(){
//        progressDialog.show();
//        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
//        String shopUrl  = sharedPreferences.getString("shop_url",null);
//        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
//        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+ Constant.BASE_URL);
//        MPOSServices mposServices = retrofit.create(MPOSServices.class);
//
//        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
//        LoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<LoginResponse>() {
//        }.getType());
//
//        String email = loginResponse.getCustomerDetails().getEmail();
//        String password = sharedPreferences.getString("customerPassword",null);
//
//        standalone.eduqfix.qfixinfo.com.app.add_new_product.model.Request request = new standalone.eduqfix.qfixinfo.com.app.add_new_product.model.Request();
//        ConfigDetails configDetails = new ConfigDetails();
//        configDetails.setUsername(email);
//        configDetails.setPassword(password);
//        request.setData(configDetails);
//
//        Gson gson = new Gson();
//        String productGson = gson.toJson(request);
//        Log.d("Product JSON","Product Json == "+productGson);
//
//        mposServices.getCustomerToken(request).enqueue(new Callback<RefreshToken>() {
//            @Override
//            public void onResponse(Call<RefreshToken> call, retrofit2.Response<RefreshToken> response) {
//                if(response.code() == 200 || response.isSuccessful()){
//
//                    String adminToken = response.body().getAdmin_token();
//                    String userToken  = response.body().getCustomer_token();
//
//                    Log.d("adminToken...",adminToken);
//                    Log.d("userToken...",userToken);
//                    SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
//                    sharedPreferences.edit().putString("adminToken",adminToken).apply();
//                    sharedPreferences.edit().putString("customerToken",userToken).apply();
//
//
//                }else {
//                    //progressDialog.hide();
//                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<RefreshToken> call, Throwable t) {
//                //progressDialog.hide();
//                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }
}
