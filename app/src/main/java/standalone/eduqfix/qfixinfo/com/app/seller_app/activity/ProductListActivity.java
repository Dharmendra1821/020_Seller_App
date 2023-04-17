package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.hydromatic.linq4j.Linq4j;
import net.hydromatic.linq4j.function.Predicate1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MainRequest;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.RefreshToken;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.Request;
import standalone.eduqfix.qfixinfo.com.app.database.DBManager;
import standalone.eduqfix.qfixinfo.com.app.login.model.CustomerCartId;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ProductAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.ConfigProductModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.ProductSizeModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.fragments.BottomSheeetFragment;
import standalone.eduqfix.qfixinfo.com.app.seller_app.fragments.ShareProductFragment;
import standalone.eduqfix.qfixinfo.com.app.seller_app.interfaces.IProductList;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CartItem;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigDetails;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewCartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Product;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductCartNewRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductListRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductRequest;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

public class ProductListActivity extends AppCompatActivity implements IProductList, ProductAdapter.ClickListener {

    ArrayList<Product> productList = null;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    TextView textCartItemCount,cartCountTextView;
    int mCartItemCount = 0;
    int CategoryId=0;
    int SubCategoryId=0;
    int CustomerId,CustomerAddressId,ProductCartId;
    Context context;
    NewLoginResponse customerDetails;
    SharedPreferences sharedPreferences;
    String cartCount;
    Menu menu;
    EditText productSearch;
     ProductAdapter productAdapter;
    CardView noProductCardView;
    ImageButton productcancel;
    private static boolean firstConnect = true;
    private final static String SHARED_PREFERENCES_FILE_USER_INFO_LIST = "configurable_product";
    String productGson;
    Product product;
    BroadcastReceiver mMessageReceiver;
    private ArrayList<ConfigProductModel> configProductModels;
    private ArrayList<ProductSizeModel> productSizeDBModels;
    ImageView product_limage,product_simage;
    String sku;
    String productId;
    String largeImage;
    String sImage;
    String productName;
    TextView config_proname;
    TextView product_price;
    String sku1;
    String display_label;
    String price;
    String attributeId;
    String indexValue;
    int optionid;
    String optionTitle;
    String optionProductId;
    LinearLayout productSizeView;
    ArrayList<ProductSizeModel> productSizeModels;
    DBManager dbManager;
    String productPrice;
    String productType;
    String productThumbnail;
    String labelvalue;
    ShareProductFragment shareProductFragment;
    LinearLayoutManager linearLayoutManager;
    int page;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    RelativeLayout bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        context=this;

        progressDialog = new ProgressDialog(ProductListActivity.this);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        ProductCartId= sharedPreferences.getInt("ProductCartId",0);
        String details = sharedPreferences.getString("CustomerDetails",null);
        cartCount = sharedPreferences.getString("cartCount",null);
        customerDetails = new Gson().fromJson(details, new TypeToken<NewLoginResponse>() {}.getType());
        noProductCardView = findViewById(R.id.noProductCardView);
        productSearch = findViewById(R.id.product_searchEditText);
        bottomLayout =  findViewById(R.id.loadItemsLayout_recyclerView);

        page =0;

        dbManager = new DBManager(this);
        dbManager.open();

     //   productAdapter = new ProductAdapter(this);
      //  LocalBroadcastManager.getInstance(ProductListActivity.this).registerReceiver(mMessageReceiver, new IntentFilter("custom-message"));
                productSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productAdapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
            }
        });


        if (getIntent().getExtras()!=null)
        {
            CategoryId=getIntent().getExtras().getInt("CategoryId");
            SubCategoryId=getIntent().getExtras().getInt("SubCategoryId");
            sharedPreferences.edit().putInt("SubCategoryId",SubCategoryId).apply();
            CustomerAddressId=getIntent().getExtras().getInt("CustomerAddressId");
            CustomerId=getIntent().getExtras().getInt("CustomerId");

            if(CustomerId == 0){
                CustomerId= customerDetails.getSellerDetails().getSeller_id();
            }
        }
        else
        {
            CategoryId= sharedPreferences.getInt("CategoryId",0);
            SubCategoryId= sharedPreferences.getInt("SubCategoryId",0);
            CustomerId= sharedPreferences.getInt("CustomerId",0);
            CustomerAddressId= sharedPreferences.getInt("CustomerAddressId",0);
        }

        recyclerView = findViewById(R.id.productListRecyclerView);
        linearLayoutManager = new LinearLayoutManager(ProductListActivity.this);
    //   getCustomerToken();
        getAllProductList();

        createCart();

        productAdapter = new ProductAdapter();
        productAdapter.setOnItemClickListener(ProductListActivity.this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {

                if(dy > 0) //check for scroll down
                {

                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        Log.d("dddd","djhjdhjhdjkdd");
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            page++;
                            Log.d("pages..", String.valueOf(page));
                            getAllProductList1();
                          //  getProduct1(sellerId,page,searchProductName,searchProductSku);
                        }
                    }
                }
            }
        });

    }//85600167

    public void getCartItemCount(final String cartId){
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String adminToken = sharedPreferences.getString("adminToken",null);
        String custToken =  sharedPreferences.getString("customerToken",null);
        NewCartRequest customerProductCart = new NewCartRequest();
        CartRequest customerProductCartRequest = new CartRequest();


        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices services = retrofit.create(MPOSServices.class);

        customerProductCartRequest.setUser_id(cartId);
        customerProductCart.setRequest(customerProductCartRequest);
        Gson gson = new Gson();
        final String cart = gson.toJson(customerProductCartRequest);
        Log.d("Product JSON","Product Json Uniform== "+cart);

        services.getCustomerProductInCart(customerProductCart).enqueue(new Callback<List<ProductCartResponse>>() {
            @Override
            public void onResponse(Call<List<ProductCartResponse>> call, Response<List<ProductCartResponse>> response) {
                Log.d("url...", String.valueOf(call.request().url()));
                if(response.code() == 200 ||response.isSuccessful()){
                    progressDialog.dismiss();
                    cartCount = String.valueOf(response.body().size());
                    Gson gson = new Gson();
                    String productGson = gson.toJson(response.body().size());
                    Log.d("Count","Countn == "+productGson);
                    sharedPreferences.edit().putString("cartCount", String.valueOf(response.body().size())).apply();
                    invalidateOptionsMenu();
                }else if(response.code() == 400){
                    progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                }else if(response.code() == 401){
                    progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Unauthorised",Toast.LENGTH_LONG).show();
                }else if(response.code() == 500){
                    progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductCartResponse>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductListActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getAllProductList(){

        progressDialog = new ProgressDialog(ProductListActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Integer sellerId = customerDetails.getSellerDetails().getSeller_id();

         sharedPreferences = getApplicationContext().getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
        sharedPreferences.edit().remove("selectedProductList").apply();

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices services = retrofit.create(MPOSServices.class);

        ProductListRequest productListRequest = new ProductListRequest();
        productListRequest.setSeller_id(String.valueOf(sellerId));
        productListRequest.setCategoryId(String.valueOf(CategoryId));
        productListRequest.setLimit("10");
        productListRequest.setPage(String.valueOf(page));

        ProductRequest request = new ProductRequest();
        request.setRequest(productListRequest);

        Gson gson = new Gson();
        final String data = gson.toJson(request);
        Log.d("Request Data ===","Request Data ==="+data);

        final String customerToken ="Bearer "+ sharedPreferences.getString("customerToken",null);
        Log.d("Seller id..", String.valueOf(sellerId));
        Log.d("SubCat...", String.valueOf(SubCategoryId));
        services.getProductList(request).enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                Log.d("url...", String.valueOf(call.request().url()));
                if(response.code() == 200 ||response.isSuccessful()){

                   // productList = response.body();
                    Gson gson = new Gson();
                    productGson = gson.toJson(response.body());
                    Log.d("Product JSON","Product Json Uniform== "+productGson);

                    try {
                        productList = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(productGson);
                        for (int ii=0; ii<jsonArray.length();ii++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(ii);
                            Product product = new Product();
                            String productId = jsonObject.optString("productId");
                            String productName = jsonObject.optString("productName");
                            String productImage = jsonObject.optString("productImage");
                            String productPrice = jsonObject.optString("productPrice");
                            String sku = jsonObject.optString("sku");
                            String producturl = jsonObject.optString("producturl");
                            String thumbnail = jsonObject.optString("thumbnail");
                            String productType = jsonObject.optString("productType");
                            int isProductInStock = jsonObject.optInt("isProductInStock");

                            product.setProductId(productId);
                            product.setProductName(productName);
                            product.setProductImage(productImage);
                            product.setProductPrice(productPrice);
                            product.setSku(sku);
                            product.setProducturl(producturl);
                            product.setThumbnail(thumbnail);
                            product.setProductType(productType);
                            product.setIsProductInStock(isProductInStock);

                            productList.add(product);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


//                    if(productList.size() == 0){
//                        noProductCardView.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.GONE);
//                    }else{
//                        noProductCardView.setVisibility(View.GONE);
//                        recyclerView.setVisibility(View.VISIBLE);
//                    }

                    productAdapter = new ProductAdapter(ProductListActivity.this,productList,String.valueOf(ProductCartId),customerToken,ProductListActivity.this);
                    ProductAdapter.cartTextView = textCartItemCount;
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(productAdapter);
                    productAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }else if(response.code() == 400){
                    progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                }else if(response.code() == 401){
                    progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Unauthorised",Toast.LENGTH_LONG).show();
                }else if(response.code() == 500){
                    progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductListActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });

        if (getCurrentFocus() !=null){
            InputMethodManager imm = (InputMethodManager) getSystemService((INPUT_METHOD_SERVICE));
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }




    public void getAllProductList1(){



        Integer sellerId = customerDetails.getSellerDetails().getSeller_id();

        sharedPreferences = getApplicationContext().getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
        sharedPreferences.edit().remove("selectedProductList").apply();

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices services = retrofit.create(MPOSServices.class);

        ProductListRequest productListRequest = new ProductListRequest();
        productListRequest.setSeller_id(String.valueOf(sellerId));
        productListRequest.setCategoryId(String.valueOf(CategoryId));
        productListRequest.setLimit("10");
        productListRequest.setPage(String.valueOf(page));

        ProductRequest request = new ProductRequest();
        request.setRequest(productListRequest);

        Gson gson = new Gson();
        final String data = gson.toJson(request);
        Log.d("Request Data ===","Request Data ==="+data);

        final String customerToken ="Bearer "+ sharedPreferences.getString("customerToken",null);
        Log.d("Seller id..", String.valueOf(sellerId));
        Log.d("SubCat...", String.valueOf(SubCategoryId));
        bottomLayout.setVisibility(View.VISIBLE);
        services.getProductList(request).enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                Log.d("url...", String.valueOf(call.request().url()));
                if(response.code() == 200 ||response.isSuccessful()){
                    Gson gson = new Gson();
                    String productGson = gson.toJson(response.body());
                    Log.d("Product JSON","Product Json Uniform== "+productGson);

                    try {
                     //   productList = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(productGson);
                        for (int ii=0; ii<jsonArray.length();ii++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(ii);
                            Product product = new Product();
                            String productId = jsonObject.optString("productId");
                            String productName = jsonObject.optString("productName");
                            String productImage = jsonObject.optString("productImage");
                            String productPrice = jsonObject.optString("productPrice");
                            String sku = jsonObject.optString("sku");
                            String producturl = jsonObject.optString("producturl");
                            String thumbnail = jsonObject.optString("thumbnail");
                            String productType = jsonObject.optString("productType");
                            int isProductInStock = jsonObject.optInt("isProductInStock");

                            product.setProductId(productId);
                            product.setProductName(productName);
                            product.setProductImage(productImage);
                            product.setProductPrice(productPrice);
                            product.setSku(sku);
                            product.setProducturl(producturl);
                            product.setThumbnail(thumbnail);
                            product.setProductType(productType);
                            product.setIsProductInStock(isProductInStock);

                            productList.add(product);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                 //   productAdapter = new ProductAdapter(ProductListActivity.this,productList,String.valueOf(ProductCartId),customerToken,ProductListActivity.this);
                    ProductAdapter.cartTextView = textCartItemCount;
                //    recyclerView.setAdapter(productAdapter);
                    productAdapter.notifyDataSetChanged();
                    loading = true;
                    bottomLayout.setVisibility(View.GONE);
              //      progressDialog.dismiss();
                }else if(response.code() == 400){
                 //   progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                }else if(response.code() == 401){
                //    progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Unauthorised",Toast.LENGTH_LONG).show();
                }else if(response.code() == 500){
                //    progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                }else {
                 //   progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
               // progressDialog.dismiss();
                Toast.makeText(ProductListActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });

        if (getCurrentFocus() !=null){
            InputMethodManager imm = (InputMethodManager) getSystemService((INPUT_METHOD_SERVICE));
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        this.menu = menu;
        MenuItem menuItem = menu.findItem(R.id.menu_item_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        MenuItem item = menu.findItem(R.id.menu_item_logout);
        item.setVisible(false);

        if(TextUtils.isEmpty(cartCount) || cartCount == null || cartCount.equals("0")){
            textCartItemCount.setVisibility(View.GONE);
        }else {
            textCartItemCount.setVisibility(View.VISIBLE);
            textCartItemCount.setText(cartCount);
        }

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer count = Integer.valueOf(!TextUtils.isEmpty(textCartItemCount.getText().toString()) ? textCartItemCount.getText().toString() : "0");
                if(count > 0){
                    Intent intent = new Intent(ProductListActivity.this, ViewCartActivity.class);
                    intent.putExtra("CustomerId",CustomerId);
                    intent.putExtra("CustomerAddressId",CustomerAddressId);
                    startActivity(intent);
                }else {
                    Toast.makeText(ProductListActivity.this,"No Products added to cart",Toast.LENGTH_LONG).show();
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i){
            case R.id.menu_item_cart:
                Intent intent = new Intent(ProductListActivity.this, ViewCartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("CategoryId",CategoryId);
                bundle.putInt("CustomerId",CustomerId);
                bundle.putInt("CustomerAddressId",CustomerAddressId);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    public void setupBadge(Integer count) {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(count, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void createCart(){
        try {
            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
            MPOSServices services = retrofit.create(MPOSServices.class);
            String customerToken ="Bearer "+ sharedPreferences.getString("customerToken",null);
            CustomerCartId customerCartId = new CustomerCartId();
            customerCartId.setCustomerId(CustomerId);
            Gson gson = new Gson();
            String productGson = gson.toJson(customerCartId);
            Log.d("add to cart","add to cart == "+productGson);

            services.getProductCartParentId(CustomerId,customerToken).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.code() == 200 ||response.isSuccessful()){
                        Gson gson = new Gson();
                        String productGson = gson.toJson(response.body());
                        Log.d("Product JSON","Product Json Uniform== "+productGson);
                        sharedPreferences.edit().putInt("ProductCartId",Integer.parseInt(response.body())).apply();
                        getCartItemCount(response.body());
                    }else {
                        Toast.makeText(ProductListActivity.this,"Something went wrong in mine",Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(ProductListActivity.this,"Something went wrong in mine",Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void productAddedOrRemoved() {
        invalidateOptionsMenu();
        int cartId = sharedPreferences.getInt("ProductCartId",0);
        getCartItemCount(String.valueOf(cartId));
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String price = intent.getStringExtra("price");
                    String qty = intent.getStringExtra("quantity");
                    String sku = intent.getStringExtra("sku");
                    String productName = intent.getStringExtra("product_name");
                    String productType = intent.getStringExtra("product_type");
         //           Toast.makeText(ProductListActivity.this, productName + " " + qty + "," + productType, Toast.LENGTH_SHORT).show();
                    addToCart(Integer.parseInt(qty), sku, price, productName, productType);
                }catch (Exception e){

                }
            }
        };

         createCart();

         LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver , new IntentFilter("custom-message"));
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            LocalBroadcastManager.getInstance(ProductListActivity.this).unregisterReceiver(mMessageReceiver);
        }catch (Exception e){

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            LocalBroadcastManager.getInstance(ProductListActivity.this).unregisterReceiver(mMessageReceiver);
        }catch (Exception e){

        }
    }

    public void addToCart(final int quantity, final String sku, final String price, final String productName, final String productType){

        progressDialog.setMessage("Adding product in cart");
        progressDialog.show();
        progressDialog.setCancelable(false);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        Integer cartId = sharedPreferences.getInt("ProductCartId",0);
        MPOSServices services = retrofit.create(MPOSServices.class);
        boolean IsAllExecuted=true;
        ProductCartResponse productCartResponse=new ProductCartResponse();

        ProductCartNewRequest productCartNewRequest = new ProductCartNewRequest();

        ProductCart productCart = new ProductCart();
        CartItem cItems = new CartItem();
        cItems.setQty(quantity);
        cItems.setSku(sku);
        cItems.setUser_id(String.valueOf(cartId));
        cItems.setPrice(Double.valueOf(price));
        cItems.setName(productName);
        cItems.setProductType(productType);
        cItems.setType("cart");
        productCart.setCart(cItems);
        productCartNewRequest.setRequest(productCart);

        Gson gson = new Gson();
        String productGson = gson.toJson(productCart);
        Log.d("add to cart","add to cart == "+productGson);
        final String customerToken ="Bearer "+ sharedPreferences.getString("customerToken",null);

        services.addProductToCart(productCartNewRequest).enqueue(new Callback<ProductCartResponse>() {
            @Override
            public void onResponse(Call<ProductCartResponse> call, Response<ProductCartResponse> response) {
                if(response.code() == 200 ||response.isSuccessful()) {
               //     Toast.makeText(ProductListActivity.this,"Product Added To Cart Successfully",Toast.LENGTH_LONG).show();
                    String cartId = sharedPreferences.getString("ProductCartId",null);
                    getCartItemCount(cartId);
                //    progressDialog.dismiss();
                }else if(response.code() == 400){
                    progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Product that you are trying to add is gone out of stock",Toast.LENGTH_LONG).show();
                }else if(response.code() == 401){
                    progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Unauthorised",Toast.LENGTH_LONG).show();
                }else if(response.code() == 500){
                    progressDialog.dismiss();
                    Toast.makeText(ProductListActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                }else{
                    //Incase to change the quantity value in edittext get super parent control
                      /* EditText editText= ((View)view.getParent().getParent()).findViewById(R.id.quantityEditText);
                       if (editText !=null){
                           checkBox.setEnabled(false);
                           editText.setText("0");
                       }
                        product.setSelectedQuantity("0");
                        product.setChecked(false);
                        checkBox.setChecked(false);
                        progressDialog.dismiss();
                        Toast.makeText(context,"Product that you are trying to add is not available.",Toast.LENGTH_LONG).show();
             */                    }
            }

            @Override
            public void onFailure(Call<ProductCartResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductListActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(ProductListActivity.this).unregisterReceiver(mMessageReceiver);
        }catch (Exception e){

        }
    }


    @Override
    public void onItemClick(View v, int position,String flag) {
        product = productAdapter.getWordAtPosition(position);

        if(flag.equalsIgnoreCase("share")){
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            String newUrl = insertString(product.getProducturl(),shopUrl+".",7);
            Intent intent = new Intent(ProductListActivity.this,ShareProductActivity.class);
            intent.putExtra("share_url",newUrl);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(ProductListActivity.this,ProductDetails.class);
            intent.putExtra("sku",product.getSku());
            intent.putExtra("product_id",product.getProductId());
            startActivity(intent);
        }


    }


//    public void getCustomerToken(){
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
//        Request request = new Request();
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
//                    getCartItemCount();
//                    createCart();
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
