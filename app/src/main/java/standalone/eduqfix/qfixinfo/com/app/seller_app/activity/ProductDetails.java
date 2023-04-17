package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.database.DBManager;
import standalone.eduqfix.qfixinfo.com.app.login.model.CustomerCartId;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ImageZoomAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ProductBundleAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ProductGroupAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.ConfigProductModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.ProductSizeContainer;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.ProductSizeModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.interfaces.IProductList;
import standalone.eduqfix.qfixinfo.com.app.seller_app.interfaces.datachanged;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CartItem;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigurableItemOption;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ExtAttributes;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ExtentionAttributes;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ImageZoom;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewCartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Product;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductBundle;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductGroup;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductOption;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductSKU;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Request;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SKURequest;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.TouchImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class ProductDetails extends AppCompatActivity implements datachanged, View.OnClickListener,IProductList {

    RecyclerView recyclerView,recyclerView1,recyclerView2;

    ImageZoomAdapter imageZoomAdapter;
    ArrayList<ImageZoom> imageZooms;
    List<ProductBundle> BundleList;
    ProductBundleAdapter productBundleAdapter;
    List<String> id1;
    ProductGroupAdapter productGroupAdapter;
    List<ProductGroup> productGroupList;
    List<ProductSKU> productSKUS = new ArrayList<>();
    double actualPrice;
    SKURequest skuRequest = new SKURequest();
    Request request = new Request();
    String largeImageUrl;
    String smallImageUrl;
    LinearLayout info,review,detail;
    Button cartbutton,paybutton;
    Integer sum1 =0;
    EditText editText;
    ScrollView scrollView;
    TextView textView,customizetextview,goback;
    PhotoView photoView;
    TouchImageView touchImageView;
    CardView dcard,icard,rcard;
    ImageView detailuparrow,detaildownarrow,infouparrow,infodownarrow,revuparrow,revdownarrow,imageView;
    TextView textCartItemCount,cartCountTextView;
    int mCartItemCount = 0;
    int CategoryId=0;
    int SubCategoryId=0;
    int CustomerId,CustomerAddressId,ProductCartId;
    Context context;
    LoginResponse customerDetails;
    SharedPreferences sharedPreferences;
    String cartCount;
    Menu menu;
    ProgressDialog progressDialog;
    Button addtocart,qpay;
    String skuValue;
    String productConfigId;
    ProductSizeContainer productSizeContainer;
    private final static String SHARED_PREFERENCES_FILE_USER_INFO_LIST = "configurable_product";
    //////////////////////////////////////////////////////////
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
    List<String> listDataHeader;
    HashMap<String, List<ProductSizeModel>> listDataChild;
    ArrayList<ProductSizeModel> productSizeModels;
    String priceVal;
    String attributeIdValue;
    String indexIdvalue;
    String labelvalue;
    TextView addText;
    TextView removeText;
    TextView config_sku;
    EditText quantityEdittext;
    int quantity;
    String productType;
    String productPrice;
    String productThumbnail;
    SharedPreferences mPOSharedPreferences;
    Button configCart;
    ProductSizeModel productSizeModel;
    TextView sizeText;
    PhotoViewAttacher pAttacher;
    private DBManager dbManager;
    int jj;

    //////////////////////////////////////////////////////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_layout);

        product_limage   = findViewById(R.id.product_large_img);
        product_simage   = findViewById(R.id.product_small_img);
        config_proname   = findViewById(R.id.config_product_name);
        productSizeView  = findViewById(R.id.product_size_view);
        product_price    = findViewById(R.id.config_product_price);
        addText          = findViewById(R.id.product_incrementTextView);
        removeText       = findViewById(R.id.product_decrementTextView);
        quantityEdittext = findViewById(R.id.product_quantityEditText);
        configCart       = findViewById(R.id.config_cart);
        config_sku       = findViewById(R.id.config_product_sku);
        sizeText         = findViewById(R.id.config_product_size);


        quantity = 1;

        dbManager = new DBManager(ProductDetails.this);
        dbManager.open();

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ProductSizeModel>>();

         progressDialog = new ProgressDialog(ProductDetails.this);
         progressDialog.setMessage("Loading.......");

         getCartItemCount();

         sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
         LocalBroadcastManager.getInstance(ProductDetails.this).registerReceiver(mMessageReceiver, new IntentFilter("config-message"));

        createCart();
        try{
            skuValue = getIntent().getStringExtra("sku");
            productConfigId = getIntent().getStringExtra("product_id");
            Log.d("product_id",productConfigId);
            getConfigurableProduct();
        }catch (Exception e){

        }
        String userInfoListJsonString = sharedPreferences.getString("config_product", "");
        Gson gson = new Gson();
        Log.d("Product JSON","Product Json Uniform== "+userInfoListJsonString);
        Product userInfoDtoArray[] = gson.fromJson(userInfoListJsonString, Product[].class);
        try {
            JSONArray jsonArray = new JSONArray(userInfoListJsonString);
            dbManager.delete();
            dbManager.delete1();
            for (int i = 0; i < jsonArray.length(); i++) {

                 productSizeModels = new ArrayList<>();
                 JSONObject jsonObject = jsonArray.getJSONObject(i);
                 sku          = jsonObject.getString("sku");
                 productId    = jsonObject.getString("productId");


                 if(jsonObject.has("productImage")){
                     largeImage = jsonObject.getString("productImage");
                 }
                 else {
                     largeImage = null;
                 }
                if(jsonObject.has("productSmallImage")) {
                    sImage = jsonObject.getString("productSmallImage");
                }
                else {
                    sImage = null;
                }
                if(jsonObject.has("productPrice")) {
                    productPrice = jsonObject.getString("productPrice");
                }
                if(jsonObject.has("productName")) {
                    productName  = jsonObject.getString("productName");
                }
                if(jsonObject.has("productType")) {
                    productType  = jsonObject.getString("productType");
                }
                if(jsonObject.has("thumbnail")) {
                    productThumbnail = jsonObject.getString("thumbnail");
                }
                dbManager.insert(new ConfigProductModel(i,Integer.parseInt(productId),largeImage,productName,productPrice,sImage,productType,sku,largeImage));
                JSONArray option_collection = jsonObject.getJSONArray("option_collection");

                for (int ii = 0; ii < option_collection.length(); ii++) {

                    JSONObject jsonObject1 = option_collection.getJSONObject(ii);
                     sku1            =   jsonObject1.getString("sku");
                     optionTitle     =   jsonObject1.getString("default_title");
                     optionProductId =   jsonObject1.getString("product_id");
                     display_label   =   jsonObject1.getString("display_label");
                     price           =   jsonObject1.getString("price");
                     attributeId     =   jsonObject1.getString("attribute_id");
                     indexValue      =   jsonObject1.getString("value_index");
               //      productSizeModels.add(new ProductSizeModel(ii,attributeId,Integer.parseInt(optionProductId),optionTitle,display_label,price,sku1,indexValue));
                     dbManager.insertOption(new ProductSizeModel(ii,attributeId,Integer.parseInt(optionProductId),optionTitle,display_label,price,sku1,indexValue));

                }
            }

             configProductModels = dbManager.getConfigProduct(Integer.parseInt(productConfigId));

            for (int i = 0; i < configProductModels.size(); i++) {
                ConfigProductModel configProductModel = configProductModels.get(i);
                sharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
                String shopUrl  = sharedPreferences.getString("shop_url",null);
                largeImageUrl = "https://"+shopUrl+Constant.PRODUCT_MEDIA_URL1+configProductModel.getProductimage();
                smallImageUrl = "https://"+shopUrl+Constant.PRODUCT_MEDIA_URL1+configProductModel.getProductsmallimage();

                if(largeImageUrl != null){
                    Glide.with(ProductDetails.this).load(largeImageUrl).into(product_limage);
                }
                else {
                    product_limage.setImageResource(R.drawable.no_product);
                }
                if(smallImageUrl != null){
                    Glide.with(ProductDetails.this).load(smallImageUrl).into(product_simage);
                }
                else {
                    product_simage.setImageResource(R.drawable.no_product);
                }

                config_proname.setText(configProductModel.getProductname());
                config_sku.setText(configProductModel.getProductsku());


            }

           int count =  dbManager.getProfilesCount();
            Log.d("count.....",String.valueOf(count));
            productSizeDBModels = dbManager.getProductOption(Integer.parseInt(productConfigId));
            for (jj = 0; jj < productSizeDBModels.size(); jj++) {
                ProductSizeModel productSizeModel = productSizeDBModels.get(jj);
                sku1           = productSizeModel.getSku();
                String attiD = productSizeModel.getIndex();
                listDataHeader.add(sku1);
                listDataChild.put(listDataHeader.get(jj), productSizeDBModels);
                final ProductSizeContainer productSizeContainer = new ProductSizeContainer(ProductDetails.this);
                productSizeContainer.setupView(listDataHeader.get(jj),listDataChild.get(listDataHeader.get(jj)));
                productSizeView.addView(productSizeContainer);
                productSizeDBModels.clear();
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }

        addText.setOnClickListener(this);
        removeText.setOnClickListener(this);
        configCart.setOnClickListener(this);


        product_limage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(ProductDetails.this,ImageZoomActivity.class);
               intent.putExtra("imagevalue",largeImageUrl);
               startActivity(intent);
            }
        });

   //     getCartItemCount();



    }

    public void getCartItemCount(){

        mPOSharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String adminToken = mPOSharedPreferences.getString("adminToken",null);
        String custToken = mPOSharedPreferences.getString("customerToken",null);
        NewCartRequest customerProductCart = new NewCartRequest();
     //   groupdata(adminToken,custToken);
        CartRequest customerProductCartRequest = new CartRequest();

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL);
        MPOSServices services = retrofit.create(MPOSServices.class);
        String cartId = sharedPreferences.getString("ProductCartId",null);
        customerProductCartRequest.setUser_id(cartId);
       // customerProductCartRequest.setUserToken(custToken);
        customerProductCart.setRequest(customerProductCartRequest);
        services.getCustomerProductInCart( customerProductCart).enqueue(new Callback<List<ProductCartResponse>>() {
            @Override
            public void onResponse(Call<List<ProductCartResponse>> call, Response<List<ProductCartResponse>> response) {
                if(response.code() == 200 ||response.isSuccessful()){
                    progressDialog.dismiss();
                    cartCount = String.valueOf(response.body().size());
                    sharedPreferences.edit().putString("cartCount", String.valueOf(response.body().size())).apply();
                    invalidateOptionsMenu();
                }else if(response.code() == 400){
                    progressDialog.dismiss();
                    Toast.makeText(ProductDetails.this,"Bad Request",Toast.LENGTH_LONG).show();
                }else if(response.code() == 401){
                    progressDialog.dismiss();
                    Toast.makeText(ProductDetails.this,"Unauthorised",Toast.LENGTH_LONG).show();
                }else if(response.code() == 500){
                    progressDialog.dismiss();
                    Toast.makeText(ProductDetails.this,"Internal server error",Toast.LENGTH_LONG).show();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(ProductDetails.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductCartResponse>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductDetails.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity, menu);
        this.menu = menu;
        MenuItem menuItem = menu.findItem(R.id.menu_item_cart);

        MenuItem item = menu.findItem(R.id.menu_item_logout);
        item.setVisible(false);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        if(TextUtils.isEmpty(cartCount) || cartCount == null || cartCount.equals("0"))
        {
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
                    Intent intent = new Intent(ProductDetails.this, ViewCartActivity.class);
                    intent.putExtra("CustomerId",CustomerId);
                    intent.putExtra("CustomerAddressId",CustomerAddressId);
                    startActivity(intent);
                }else {
                    Toast.makeText(ProductDetails.this,"No Products added to cart",Toast.LENGTH_LONG).show();
                }
            }
        });
        return true;
    }

    @Override
    public void productAddedOrRemoved() {
        invalidateOptionsMenu();

        getCartItemCount();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
       switch (i){
            case R.id.menu_item_cart:
                Intent intent = new Intent(ProductDetails.this, ViewCartActivity.class);
                startActivity(intent);
                return true;
           default:
               return false;
       }
    }

    public void getConfigurableProduct(){

        progressDialog.show();
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
        MPOSServices services = retrofit.create(MPOSServices.class);
        mPOSharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String adminToken = mPOSharedPreferences.getString("adminToken",null);
      //  String authToken = "Bearer "+adminToken;
        SKURequest skuRequest = new SKURequest();
        Request request = new Request();
        request.setSku(skuValue);
        request.setAdminToken(adminToken);
        skuRequest.setRequest(request);

        Call<ProductSKU> call = services.productsku(skuRequest);
        Gson gson = new Gson();
        String skuDetails = gson.toJson(skuRequest);

        Log.d("skuDetails JSON","skuDetails Json == "+skuDetails);
        call.enqueue(new Callback<ProductSKU>() {
            @Override
            public void onResponse(Call<ProductSKU> call, Response<ProductSKU> response) {
                if(response.code() == 200 ||response.isSuccessful()){
                    Gson gson = new Gson();
                    String skuDetails = gson.toJson(response.body());
                    Log.d("skuDetails JSON","skuDetails Json == "+skuDetails);
                    progressDialog.dismiss();
                 //   Toast.makeText(ProductDetails.this,"SUCCESS",Toast.LENGTH_LONG).show();

                }else if(response.code() == 400){
                    progressDialog.dismiss();
                    Toast.makeText(ProductDetails.this,"Bad Request",Toast.LENGTH_LONG).show();
                }else if(response.code() == 401){
                    progressDialog.dismiss();
                    Toast.makeText(ProductDetails.this,"Unauthorised",Toast.LENGTH_LONG).show();
                }else if(response.code() == 500){
                    progressDialog.dismiss();
                    Toast.makeText(ProductDetails.this,"Internal server error",Toast.LENGTH_LONG).show();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(ProductDetails.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ProductSKU> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductDetails.this,"FAILED",Toast.LENGTH_LONG).show();

            }
        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            try {
                 priceVal = intent.getStringExtra("price");
                 attributeIdValue = intent.getStringExtra("attribute");
                 indexIdvalue = intent.getStringExtra("index");
                 labelvalue   = intent.getStringExtra("label");
                 final double price = priceVal != null ?  Math.round(Double.parseDouble(priceVal) * 100)/100.00:0.0;
                 product_price.setText(getString(R.string.Rs)+" "+price);
                 quantityEdittext.setText("1");
                 sizeText.setText("Size "+"("+labelvalue+")");
                 actualPrice = Double.parseDouble(priceVal);


            }catch (Exception e){

            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(ProductDetails.this).unregisterReceiver(mMessageReceiver);
        }catch (Exception e){

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.product_incrementTextView:
                 quantity = Integer.valueOf(quantityEdittext.getText().toString());
                 quantity = quantity + 1;
                 quantityEdittext.setText(String.valueOf(quantity));
                 final double price = priceVal != null ?  Math.round(Double.parseDouble(priceVal) * 100)/100.00:0.0;
                  actualPrice = Double.valueOf(price) * quantity ;
                 product_price.setText(getString(R.string.Rs)+" "+actualPrice);


                break;
            case R.id.product_decrementTextView:
                 quantity = Integer.valueOf(quantityEdittext.getText().toString());
                if(quantity > 1) {
                    quantity = quantity - 1;
                    quantityEdittext.setText(String.valueOf(quantity));
                    final double price1 = priceVal != null ?  Math.round(Double.parseDouble(priceVal) * 100)/100.00:0.0;
                    double actualPrice1 = Double.valueOf(price1) * quantity ;
                    product_price.setText(getString(R.string.Rs)+" "+actualPrice1);

                }
                    break;

            case R.id.config_cart:

                addToCart(quantity,skuValue,String.valueOf(actualPrice),productName,productType,attributeIdValue,indexIdvalue);
                break;

                default:
                    break;
        }
    }

    public void createCart(){
        try {
            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
            MPOSServices services = retrofit.create(MPOSServices.class);
            mPOSharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
            String customerToken ="Bearer "+ mPOSharedPreferences.getString("customerToken",null);
            CustomerCartId customerCartId = new CustomerCartId();
            customerCartId.setCustomerId(CustomerId);

            services.getProductCartParentId(CustomerId,customerToken).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.code() == 200 ||response.isSuccessful()){
                        mPOSharedPreferences.edit().putInt("ProductCartId",Integer.valueOf(response.body())).apply();
                        Integer cartId = mPOSharedPreferences.getInt("ProductCartId",0);
                        Log.d("Cart Id...",String.valueOf(cartId));
                    }else {
                        Toast.makeText(ProductDetails.this,"Something went wrong in mine",Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(ProductDetails.this,"Something went wrong in mine",Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void addToCart(final int quantity,final String sku,final String price,final String productName,final String productType,
                          final String optionId, final String optionValue ){

        progressDialog.show();
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
        SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        Integer cartId = sharedPreferences.getInt("ProductCartId",0);
        MPOSServices services = retrofit.create(MPOSServices.class);
        boolean IsAllExecuted=true;

        ConfigProductCart configProductCart = new ConfigProductCart();
        CartItem cartItem = new CartItem();
        cartItem.setQty(quantity);
        cartItem.setSku(sku);
        cartItem.setUser_id(String.valueOf(cartId));
        cartItem.setPrice(Double.valueOf(price));
        cartItem.setName(productName);
        cartItem.setProductType(productType);
        ProductOption productOption = new ProductOption();
        cartItem.setProduct_option(productOption);
        ExtentionAttributes extentionAttributes = new ExtentionAttributes();
        productOption.setExtension_attributes(extentionAttributes);
        ConfigurableItemOption configurableItemOption = new ConfigurableItemOption();
        configurableItemOption.setOption_id(optionId);
        configurableItemOption.setOption_value(optionValue);
        ExtAttributes extAttributes = new ExtAttributes();
        configurableItemOption.setExtension_attributes(extAttributes);
        extentionAttributes.setConfigurable_item_options(Collections.singletonList(configurableItemOption));
        configProductCart.setCartItem(cartItem);

        Gson gson = new Gson();
        String productGson = gson.toJson(configProductCart);
        Log.d("Product JSON","Product Json == "+productGson);
        final String customerToken ="Bearer "+ sharedPreferences.getString("customerToken",null);

        Log.d("customer...",customerToken);
        services.addProductToCartConfig(configProductCart,customerToken).enqueue(new Callback<ConfigProductCartResponse>() {
            @Override
            public void onResponse(Call<ConfigProductCartResponse> call, Response<ConfigProductCartResponse> response) {
                if(response.code() == 200 ||response.isSuccessful()) {
                    Toast.makeText(ProductDetails.this,"Product Added To Cart Successfully",Toast.LENGTH_LONG).show();
                    getCartItemCount();
                    progressDialog.dismiss();
                    Intent intent = new Intent(ProductDetails.this,ProductListActivity.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    Toast.makeText(ProductDetails.this,response.message(),Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ConfigProductCartResponse> call, Throwable t) {
                     progressDialog.dismiss();
                Toast.makeText(ProductDetails.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }

////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
    }

}
