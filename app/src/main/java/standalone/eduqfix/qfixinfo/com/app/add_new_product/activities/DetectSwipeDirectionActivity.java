package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.DetectSwipeGestureListener;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.AddNewProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.ExpListViewAdapterWithCheckbox;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductModel;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddProductRequest;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AllHSNCode;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.ExtensionAttributes;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MediaGalleryEntries;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.Product;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.QuantityStockStatus;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.StockData;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.StockItem;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.WholeData;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.util.MultipartUtility;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.util.VolleyMultipartRequest;
import standalone.eduqfix.qfixinfo.com.app.database.SubDBManager;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.AddProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.Content;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.DatabaseCategoriesModel;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.DatabaseSubCategoriesModel;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.EditImageProductDetailActivity;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.Entry;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.EntryModel;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.ComplaintsDetailsActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.MainDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.ProductDetails;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.AddProductRequestMain;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.AddProductRequestNew;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigDetails;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductSKU;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SKURequest;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static net.hydromatic.linq4j.expressions.ExpressionType.Convert;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class DetectSwipeDirectionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView = null;

    // This is the gesture detector compat instance.
    private GestureDetectorCompat gestureDetectorCompat = null;
    Button basic_btn,more_info_btn;
    View basic_view,more_view;
    List<AddNewProductModel> addNewProductModels;
    AddNewProductListAdapter addProductListAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    ImageView productimage1,productimage2,productimage3;
    ImageView edit1,edit2,edit3;
    int PERMISSION_ALL = 1;
    boolean flagPermissions = false;
    private ActionBar toolbar;
    SharedPreferences sharedPreferences;
    int position;
    String result;
    int taxValue;
    String hsnCode;
    ArrayList<MediaGalleryEntries> mediaGalleryEntries ;
    LinearLayout basic_layout,more_layout;
    String productIdNew;
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    SearchableSpinner category,sub_category,product_stock_avail_new,product_tax_new;
    EditText p_name,p_description,p_sku,p_price,p_stock,p_availablity,p_weight;
    SubDBManager subDBManager;
    int sellerId;

    AutoCompleteTextView categories,subcategories;
    Spinner spinner,sub_spinner;
    ArrayList<String> categoryValue;
    ArrayList<String> stockvalue;
    ArrayList<String> taxvalue;
    ArrayList<String> subcategoryValue;
    Button searchBtn;
    String sub_categoryid;
    String sub_imageurl;
    String sub_parentid;
    String sub_name;
    String skuValue;
    TextView sku_exits;
    ArrayAdapter<String> spinnervalue;
    ArrayAdapter<String> spinnerTaxValue;
    ProgressDialog progressDialog;
    BroadcastReceiver mMessageReceiver;
    BroadcastReceiver mMessageReceiver1;
    BroadcastReceiver mMessageReceiver2;
    SearchableSpinner product_hsncode_new;
    ArrayList<String> productHsnCode;
    ArrayList<AllHSNCode> allHSNCodes;
    Button saveBtn,editBtn;
    String stringBitmap,image1,image2,image3;
    Bitmap sizeBitmap;
    String productCategory;
    String categoryId;
    String categoryLevel1;
    String categoryLevel2;
    String parentId;
    String subCategoryId;
    String productId;
    String inStockValue;
    String filePath;
    boolean stockStatus;
    long totalSize = 0;
    String newSku;
    List<String> listDataHeader;
    HashMap<String, List<DatabaseSubCategoriesModel>> listDataChild;
    TextView product_category;
    LinearLayout product_category_layout;
    private ArrayList<HashMap<String, String>> parentItems;
    AddNewProductModel productModel;
    Bitmap bitmap1;

    HttpURLConnection httpURLConnection ;
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter ;
    int RC;
    BufferedReader bufferedReader ;
    StringBuilder stringBuilder;
    boolean check = true;
    float weightVal;
    String categoriesname;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ArrayAdapter<String> stateAdapter2;
    static ArrayList<String> categoryIds = new ArrayList<>();
    static ArrayList<String> categoryName = new ArrayList<>();

    public static ArrayList<String> getCategoryIds() {
        return categoryIds;
    }

    public static void setCategoryIds(ArrayList<String> categoryIds) {
        DetectSwipeDirectionActivity.categoryIds = categoryIds;
    }

    public static ArrayList<String> getCategoryName() {
        return categoryName;
    }

    public static void setCategoryName(ArrayList<String> categoryName) {
        DetectSwipeDirectionActivity.categoryName = categoryName;
    }

    ArrayList<String> StringArray = new ArrayList<String>();

    SearchableSpinner categoryLevel1Spinner,categoryLevel2Spinner;
    List<DatabaseCategoriesModel> databaseCategoriesModels = null;
    ArrayList<DatabaseSubCategoriesModel> databaseSubCategoriesModels;
    List<String> categoryNameList;
    List<String> subcategoryNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_swipe_direction);

        getSupportActionBar().setTitle("Add Product");

        init();

    }

    public void init(){

        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        progressDialog = new ProgressDialog(DetectSwipeDirectionActivity.this);
        mediaGalleryEntries = new ArrayList<>();
        basic_btn     = findViewById(R.id.basic_info_btn);
        more_info_btn = findViewById(R.id.more_info_btn);
        basic_view    = findViewById(R.id.basic_info_view);
        more_view     = findViewById(R.id.more_info_view);
        //  recyclerView  = findViewById(R.id.add_new_product_recyclerview);
        basic_layout = findViewById(R.id.basic_layout);
        more_layout = findViewById(R.id.more_info_layout);

     //   category = findViewById(R.id.add_new_product_category);
      //  sub_category = findViewById(R.id.add_new_product_sub_category);
        p_name = findViewById(R.id.product_name_new);
        p_description = findViewById(R.id.product_description_new);
        p_sku = findViewById(R.id.product_sku_new);
        p_price = findViewById(R.id.product_price_new);
        p_stock = findViewById(R.id.product_stock_new);
        p_weight = findViewById(R.id.product_weight_new);
        product_stock_avail_new = findViewById(R.id.product_stock_avail_new);
        sku_exits = findViewById(R.id.sku_exist);
        product_tax_new = findViewById(R.id.product_tax_new);
        saveBtn = findViewById(R.id.save_new_product);
        editBtn = findViewById(R.id.edit_new_product);

        edit1  = findViewById(R.id.edit_image_list1);
        edit2  = findViewById(R.id.edit_image_list2);
        edit3  = findViewById(R.id.edit_image_list3);

        categoryLevel1Spinner = findViewById(R.id.category_level_1);
        categoryLevel2Spinner = findViewById(R.id.category_level_2);
        product_hsncode_new = findViewById(R.id.product_hsncode_new);

        progressDialog = new ProgressDialog(DetectSwipeDirectionActivity.this);

        basic_btn.setOnClickListener(this);
        more_info_btn.setOnClickListener(this);

        DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener();
        // Set activity in the listener.
        gestureListener.setActivity(this);


        // Create the gesture detector with the gesture listener.
        gestureDetectorCompat = new GestureDetectorCompat(this, gestureListener);

        linearLayoutManager = new LinearLayoutManager(DetectSwipeDirectionActivity.this);

        productimage1 = findViewById(R.id.edit_product_images1);
        productimage2 = findViewById(R.id.edit_product_images2);
        productimage3 = findViewById(R.id.edit_product_images3);

        productimage1.setOnClickListener(this);
        productimage2.setOnClickListener(this);
        productimage3.setOnClickListener(this);
//        product_category.setOnClickListener(this);
//        product_category_layout.setOnClickListener(this);



        edit1.setVisibility(View.GONE);
        edit2.setVisibility(View.GONE);
        edit3.setVisibility(View.GONE);

        edit1.setOnClickListener(this);
        edit2.setOnClickListener(this);
        edit3.setOnClickListener(this);

        position = getIntent().getIntExtra("position",0);

        productId ="";

        try {
             Intent intent =  getIntent();
             Bundle bundle =  intent.getExtras();
             productModel  =  (AddNewProductModel) bundle.getSerializable("product");


            if(productModel!=null){

                ExpListViewAdapterWithCheckbox.getCategory().clear();
                ExpListViewAdapterWithCheckbox.getCategoryName().clear();

                editBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.GONE);
                edit1.setVisibility(View.VISIBLE);
                edit2.setVisibility(View.VISIBLE);
                edit3.setVisibility(View.VISIBLE);

                p_name.setText(productModel.getProductName());
                p_description.setText(productModel.getAttributes().getDescription());
                p_sku.setText(productModel.getSku());
                p_price.setText(productModel.getProductPrice());
                p_weight.setText(productModel.getAttributes().getType());
                p_sku.setEnabled(false);

                productId = productModel.getProductId();

                p_stock.setText("10");
                p_weight.setText(productModel.getWeight().equalsIgnoreCase("null") ? "10" : productModel.getWeight());

                Log.d("categoryId......",productModel.getCategoryId());
                Log.d("sub categoryId......",productModel.getSubCategoryId());

                stockStatus = productModel.isStockStatus();
                Log.d("images,,,", String.valueOf(productModel.getMedia_gallery().getImages()));
            //    taxValue = Integer.parseInt(productModel.getAttributes().getTax_class_id());
             //   hsnCode = Integer.parseInt(productModel.getAttributes().getHsn_code());


            //      String shopUrl  = sharedPreferences.getString("shop_url",null);

                  for (int i = 0;i < productModel.getMedia_gallery().getImages().size();i++){

                    if(i==0){
                        String imageUrl = productModel.getMedia_gallery().getImages().get(0).getLabel();
                        Glide.with(DetectSwipeDirectionActivity.this).load(imageUrl).error(R.drawable.download).into(productimage1);
                    }
                     if(i==1){
                        String imageUrl2 = productModel.getMedia_gallery().getImages().get(1).getLabel();
                        Glide.with(DetectSwipeDirectionActivity.this).load(imageUrl2).error(R.drawable.download).into(productimage2);
                    }
                     if(i==2){
                        String imageUrl3 = productModel.getMedia_gallery().getImages().get(2).getLabel();
                        Glide.with(DetectSwipeDirectionActivity.this).load(imageUrl3).error(R.drawable.download).into(productimage3);
                    }
//                    else {
//                        Glide.with(DetectSwipeDirectionActivity.this).load("").error(R.drawable.download).into(productimage1);
//                        Glide.with(DetectSwipeDirectionActivity.this).load("").error(R.drawable.download).into(productimage2);
//                        Glide.with(DetectSwipeDirectionActivity.this).load("").error(R.drawable.download).into(productimage3);
//                    }
                }

            }

            sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
            categoryValue = new ArrayList<>();
            subcategoryValue = new ArrayList<>();
            stockvalue = new ArrayList<>();
            taxvalue = new ArrayList<>();

            productHsnCode = new ArrayList<>();

            sharedPreferences.edit().putString("image1", null).apply();
            sharedPreferences.edit().putString("image2", null).apply();
            sharedPreferences.edit().putString("image3", null).apply();

            sharedPreferences.edit().putString("imageUrl1", null).apply();
            sharedPreferences.edit().putString("imageUrl2", null).apply();
            sharedPreferences.edit().putString("imageUrl3", null).apply();

            sharedPreferences.edit().putString("imageId1", null).apply();
            sharedPreferences.edit().putString("imageId2", null).apply();
            sharedPreferences.edit().putString("imageId3", null).apply();

            String customerDetails = sharedPreferences.getString("CustomerDetails", null);
            NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
            }.getType());
            sellerId = loginResponse != null && loginResponse.getSellerDetails().getSeller_id() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

            Log.d("SellerId....", String.valueOf(sellerId));

            getCategoriesNew(String.valueOf(sellerId));

            subDBManager = new SubDBManager(DetectSwipeDirectionActivity.this);
            subDBManager.open();



            p_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    p_description.setText(p_name.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            stockvalue.add("In Stock");
            stockvalue.add("Out of Stock");

            spinnervalue = new ArrayAdapter<String>(DetectSwipeDirectionActivity.this, R.layout.spinner_text_layout, stockvalue);
            product_stock_avail_new.setAdapter(spinnervalue);
            spinnervalue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            product_stock_avail_new.setAdapter(spinnervalue);

            if(!stockStatus){
                String compareValue = "Out of Stock";
                if (compareValue != null) {
                    int spinnerPosition = spinnervalue.getPosition(compareValue);
                    product_stock_avail_new.setSelection(spinnerPosition);
                }
            }


            p_stock.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(p_stock.getText().toString().equalsIgnoreCase("0")){
                        if (stockvalue != null) {
                            int spinnerPosition = spinnervalue.getPosition("Out of Stock");
                            product_stock_avail_new.setSelection(spinnerPosition);
                        }
                    }
                    else {
                        int spinnerPosition = spinnervalue.getPosition("In Stock");
                        product_stock_avail_new.setSelection(spinnerPosition);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            p_sku.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if(hasFocus) {
                        skuValue = p_sku.getText().toString().trim();
                    } else {
                        skuValue = p_sku.getText().toString().trim();
                        checkSku(skuValue);
                    }
                }
            });


            taxvalue.add("None");
            taxvalue.add("GST 5% Taxable Goods");
            taxvalue.add("GST 12% Taxable Goods");
            taxvalue.add("GST 18% Taxable Goods");
            taxvalue.add("GST 28% Taxable Goods");

            spinnerTaxValue = new ArrayAdapter<String>(DetectSwipeDirectionActivity.this, R.layout.spinner_text_layout, taxvalue);
            product_tax_new.setAdapter(spinnerTaxValue);
            spinnerTaxValue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            product_tax_new.setAdapter(spinnerTaxValue);

            if(taxValue!=0){
                String compareValue = "";
                if (taxValue==17){
                    compareValue = "GST 5% Taxable Goods";
                }
                else if(taxValue==18){
                    compareValue = "GST 12% Taxable Goods";
                }
                else if(taxValue==19){
                    compareValue = "GST 18% Taxable Goods";
                }
                else if(taxValue==20){
                    compareValue = "GST 28% Taxable Goods";
                }
                else {
                    compareValue = "None";
                }

                if (compareValue != null) {
                    int spinnerPosition = spinnerTaxValue.getPosition(compareValue);
                    product_tax_new.setSelection(spinnerPosition);
                }
            }

        }catch (Exception e){

        }

        product_tax_new.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getSelectedItem().toString().equalsIgnoreCase("GST 5% Taxable Goods")){
                    taxValue = 17;
                }
                else if(parent.getSelectedItem().toString().equalsIgnoreCase("GST 12% Taxable Goods")){
                    taxValue = 18;
                }
                else if(parent.getSelectedItem().toString().equalsIgnoreCase("GST 18% Taxable Goods")){
                    taxValue = 19;
                }
                else if(parent.getSelectedItem().toString().equalsIgnoreCase("GST 28% Taxable Goods")){
                    taxValue = 20;
                }
                else {
                    taxValue = 0;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        product_hsncode_new.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hsnCode = allHSNCodes.get(i).getHsn_code();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        product_stock_avail_new.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("In stock")){
                    inStockValue = "1";
                }
                else {
                    inStockValue = "0";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        categoryLevel1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String categoryName = parent.getSelectedItem().toString();
                subcategoryNameList = new ArrayList<>();
                for(DatabaseCategoriesModel state : databaseCategoriesModels){
                    if(state.getName().equalsIgnoreCase(categoryName)){

                        parentId = state.getCategory_id();

                        for(DatabaseSubCategoriesModel state2 : databaseSubCategoriesModels){
                            if(Objects.equals(parentId, state2.getSub_parent_id())){
                                String subaCategory = state2.getSub_category_id();
                                String subCategoryName = state2.getSub_name();


                                subcategoryNameList.add(subCategoryName);
                            }
                        }
                    }
                }
                subcategoryNameList.add(0,"Select Category Level 2");
                 stateAdapter2= new ArrayAdapter<String>(DetectSwipeDirectionActivity.this,android.R.layout.simple_list_item_1, subcategoryNameList);
                stateAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categoryLevel2Spinner.setAdapter(stateAdapter2);


                if(productModel != null){
                    if (productModel.getSubCategoryName() != null) {
                        int spinnerPosition = stateAdapter2.getPosition(productModel.getSubCategoryName());
                        categoryLevel2Spinner.setSelection(spinnerPosition);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });





        categoryLevel2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String categoryName = parent.getSelectedItem().toString();

                for (DatabaseSubCategoriesModel state : databaseSubCategoriesModels) {
                    if (state.getSub_name().equalsIgnoreCase(categoryName)) {

                        subCategoryId = state.getSub_category_id();

                        Log.d("sub...",subCategoryId);

                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        saveBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);

        parentItems = new ArrayList<>();
     //   listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String,List<DatabaseSubCategoriesModel>>();

    //    getCategoriesNew(sellerId);


    //    getAdminToken();

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pass activity on touch event to the gesture detector.
        gestureDetectorCompat.onTouchEvent(event);
        // Return true to tell android OS that event has been consumed, do not pass it to other event listeners.
        return true;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void displayMessage(String message)
    {

        if(message.equalsIgnoreCase("Swipe to left")){
            more_view.setBackgroundResource(R.color.new_btn_color);
            basic_view.setBackgroundResource(R.color.colorWhite);
            more_layout.setVisibility(View.VISIBLE);
            basic_layout.setVisibility(View.GONE);
            saveBtn.setVisibility(View.GONE);
            getProductHSN();

        }
        else {
            more_view.setBackgroundResource(R.color.colorWhite);
            basic_view.setBackgroundResource(R.color.new_btn_color);

            more_layout.setVisibility(View.GONE);
            basic_layout.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.VISIBLE);

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.basic_info_btn:
                more_view.setBackgroundResource(R.color.colorWhite);
                basic_view.setBackgroundResource(R.color.new_btn_color);

                more_layout.setVisibility(View.GONE);
                basic_layout.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.VISIBLE);

                break;

            case R.id.more_info_btn:
                more_view.setBackgroundResource(R.color.new_btn_color);
                basic_view.setBackgroundResource(R.color.colorWhite);

                more_layout.setVisibility(View.VISIBLE);
                basic_layout.setVisibility(View.GONE);
                saveBtn.setVisibility(View.GONE);
                getProductHSN();

                break;

            case R.id.edit_product_images1:
                checkPermissions(1);
                break;
            case R.id.edit_product_images2:
                checkPermissions(2);
                break;
            case R.id.edit_product_images3:
                checkPermissions(3);
                break;

            case R.id.save_new_product:
                 image1 = sharedPreferences.getString("image1", null);
                 image2 = sharedPreferences.getString("image2", null);
                 image3 = sharedPreferences.getString("image3", null);
                 String productName = p_name.getText().toString();
                 String productSku  = p_sku.getText().toString();
                 String price       = p_price.getText().toString();
                 String description = p_description.getText().toString();

                 String weigh = p_weight.getText().toString();
                 String stock = p_stock.getText().toString();

                  if(p_name.getText().toString().equalsIgnoreCase("")){
                     p_name.setError("Enter product name");
                 }
                 else if(p_sku.getText().toString().equalsIgnoreCase("")){
                     p_sku.setError("Enter sku");
                 }
                 else if(p_price.getText().toString().equalsIgnoreCase("")){
                     p_price.setError("Enter price");
                 }
                 else if(p_weight.getText().toString().equalsIgnoreCase("")){
                     p_weight.setError("Enter weight");
                 }
                 else if(p_stock.getText().toString().equalsIgnoreCase("")){
                     p_stock.setError("Enter stock");
                 }

                 else {
                     float priceVal = Float.parseFloat(price);
                     float weightVal = Float.parseFloat(weigh);

                         addnewProduct1("",productSku,productName,description,"4",Integer.parseInt(price),Integer.parseInt(weigh),Integer.parseInt(stock),
                                 inStockValue,image1,image2,image3);

                 }

                break;

            case R.id.edit_new_product:
                image1 = sharedPreferences.getString("image1", null);
                image2 = sharedPreferences.getString("image2", null);
                image3 = sharedPreferences.getString("image3", null);
                String productName1 = p_name.getText().toString();
                String productSku1  = p_sku.getText().toString();
                String price1      = p_price.getText().toString();
                String description1 = p_description.getText().toString();

                String weigh1 = p_weight.getText().toString();
                String stock1 = p_stock.getText().toString();

                if(p_name.getText().toString().equalsIgnoreCase("")){
                    p_name.setError("Enter product name");
                }
                else if(p_sku.getText().toString().equalsIgnoreCase("")){
                    p_sku.setError("Enter sku");
                }
                else if(p_price.getText().toString().equalsIgnoreCase("")){
                    p_price.setError("Enter price");
                }
                else if(p_weight.getText().toString().equalsIgnoreCase("")){
                    p_weight.setError("Enter weight");
                }
                else if(p_stock.getText().toString().equalsIgnoreCase("")){
                    p_stock.setError("Enter stock");
                }

                else {
                    float priceVal = Float.parseFloat(price1);
                    if(weigh1.equalsIgnoreCase("null")){
                        weightVal = 0;
                    }
                    else {
                        weightVal = Float.parseFloat(weigh1);
                    }
                        editnewProduct1(productId,productSku1,productName1,description1,"4",Integer.parseInt(price1),Integer.parseInt(weigh1),Integer.parseInt(stock1),
                                inStockValue);
                }

                break;



            case R.id.edit_image_list1:
                if(productModel.getMedia_gallery().getImages().size()>0){
                    Log.d(".........",productModel.getMedia_gallery().getImages().get(0).getValue_id());

                    AlertDialog myQuittingDialogBox =new AlertDialog.Builder(DetectSwipeDirectionActivity.this)
                            //set message, title, and icon
                            .setTitle("Delete")
                            .setMessage("Are you sure you wish to delete this image?")
                            .setIcon(R.drawable.trash)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    deleteImages(1, Integer.parseInt(productModel.getMedia_gallery().getImages().get(0).getEntity_id()),"1");
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    myQuittingDialogBox.show();
                }

                break;

            case R.id.edit_image_list2:
                if(productModel.getMedia_gallery().getImages().size()>0){
                    Log.d(".........",productModel.getMedia_gallery().getImages().get(0).getValue_id());

                    AlertDialog myQuittingDialogBox =new AlertDialog.Builder(DetectSwipeDirectionActivity.this)
                            //set message, title, and icon
                            .setTitle("Delete")
                            .setMessage("Are you sure you wish to delete this image?")
                            .setIcon(R.drawable.trash)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    deleteImages(0, Integer.parseInt(productModel.getMedia_gallery().getImages().get(1).getEntity_id()),"2");
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    myQuittingDialogBox.show();
                }

                break;

            case R.id.edit_image_list3:
                if(productModel.getMedia_gallery().getImages().size()>0){
                    Log.d(".........",productModel.getMedia_gallery().getImages().get(2).getValue_id());

                    AlertDialog myQuittingDialogBox =new AlertDialog.Builder(DetectSwipeDirectionActivity.this)
                            //set message, title, and icon
                            .setTitle("Delete")
                            .setMessage("Are you sure you wish to delete this image?")
                            .setIcon(R.drawable.trash)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    deleteImages(0, Integer.parseInt(productModel.getMedia_gallery().getImages().get(2).getEntity_id()),"3");
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    myQuittingDialogBox.show();
                }

                break;

        }
    }


    public void deleteImages(final int cover,final int valueId,final String position){

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);

        progressDialog = new ProgressDialog(DetectSwipeDirectionActivity.this);
        progressDialog.setMessage("Deleting product image....");
        progressDialog.show();
        final String authToken = "Bearer " + sharedPreferences.getString("adminToken",null);
        Log.d("Auth Token.....",authToken);
        mposServices.deleteImages(valueId,cover,Integer.parseInt(productId)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {

                    if(response.code()==200){
                        progressDialog.dismiss();
                        if(position.equalsIgnoreCase("1")){
                            productimage1.setImageResource(R.drawable.download);
                            image1 = null;
                            Toast.makeText(DetectSwipeDirectionActivity.this,"Product image deleted successfully",Toast.LENGTH_LONG).show();

                        }
                        else if(position.equalsIgnoreCase("2")){
                            productimage2.setImageResource(R.drawable.download);
                            image2 = null;
                            Toast.makeText(DetectSwipeDirectionActivity.this,"Product image deleted successfully",Toast.LENGTH_LONG).show();

                        }
                        else {
                            productimage3.setImageResource(R.drawable.download);
                            image3 = null;
                            Toast.makeText(DetectSwipeDirectionActivity.this,"Product image deleted successfully",Toast.LENGTH_LONG).show();

                        }


                    }
                    else if(response.code() == 400){
                        progressDialog.dismiss();
                        Toast.makeText(DetectSwipeDirectionActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 401){
                        progressDialog.dismiss();
                        Toast.makeText(DetectSwipeDirectionActivity.this,"Unauthorised",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        progressDialog.dismiss();
                        Toast.makeText(DetectSwipeDirectionActivity.this,"Internal Server error",Toast.LENGTH_LONG).show();
                    }else {
                        progressDialog.dismiss();
                        Log.d("failure...", String.valueOf(response.body()));
                        Toast.makeText(DetectSwipeDirectionActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("failure...",t.getMessage());
                Toast.makeText(DetectSwipeDirectionActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }


        });


    }

    void checkPermissions(final int position) {
        if (!checkPermission()) {
            requestPermission();
            flagPermissions = false;
            Log.d("commm","coming 1");
        }
        else {
            Log.d("commm", String.valueOf(PERMISSIONS));
            flagPermissions = true;
            Intent intent = new Intent(DetectSwipeDirectionActivity.this,BottomNavigationActivity.class);
            intent.putExtra("position",position);
            startActivity(intent);
        }
    }

    private boolean checkPermission() {
       // int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

//                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted)
                        Toast.makeText(DetectSwipeDirectionActivity.this, "Permission Granted, Now you can access  camera.", Toast.LENGTH_LONG).show();
                    else {

                        Toast.makeText(DetectSwipeDirectionActivity.this, "Permission Denied, You cannot access camera.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DetectSwipeDirectionActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }




    public void checkSku(final String skuValue){
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices services = retrofit.create(MPOSServices.class);

        String adminToken = sharedPreferences.getString("adminToken",null);
        //  String authToken = "Bearer "+adminToken;
        SKURequest skuRequest = new SKURequest();
        standalone.eduqfix.qfixinfo.com.app.seller_app.model.Request request = new standalone.eduqfix.qfixinfo.com.app.seller_app.model.Request();
        request.setSku(skuValue);
        request.setAdminToken(adminToken);
        skuRequest.setRequest(request);

        Call<Boolean> call = services.checksku(skuRequest);
        Gson gson = new Gson();
        String skuDetails = gson.toJson(skuRequest);

        Log.d("skuDetails JSON","skuDetails Json == "+skuDetails);
        services.checkSku(skuValue).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if(response.code() == 200 ||response.isSuccessful()){
                    Gson gson = new Gson();
                    boolean flag = response.body();
                    if(flag){
                        sku_exits.setVisibility(View.VISIBLE);
                    }
                    else {
                        sku_exits.setVisibility(View.GONE);
                    }

                }else if(response.code() == 400){

                    Toast.makeText(DetectSwipeDirectionActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                }else if(response.code() == 401){

                    Toast.makeText(DetectSwipeDirectionActivity.this,"Unauthorised",Toast.LENGTH_LONG).show();
                }else if(response.code() == 500){

                    Toast.makeText(DetectSwipeDirectionActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                }else {

                    Toast.makeText(DetectSwipeDirectionActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(DetectSwipeDirectionActivity.this,"FAILED",Toast.LENGTH_LONG).show();

            }

        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                     position = intent.getIntExtra("position",0);
                     filePath = intent.getStringExtra("filePath").replace("file:","");
                     Log.d("imagepath",filePath);
                     bitmap1 = intent.getParcelableExtra("bitmap");
                        if(position!=0){
                            if (position==1){
                                productimage1.setImageBitmap(bitmap1);
                                sharedPreferences.edit().putString("image1", filePath).apply();
                                sizeBitmap = getResizedBitmap(bitmap1,700);
                                BitMapToString(sizeBitmap,"image1");
                                sharedPreferences.edit().putString("imageUrl1", null).apply();
                                sharedPreferences.edit().putString("imageId1", null).apply();
                            }
                            else if(position==2){
                                productimage2.setImageBitmap(bitmap1);
                                sharedPreferences.edit().putString("image2", filePath).apply();
                                sizeBitmap = getResizedBitmap(bitmap1,700);BitMapToString(sizeBitmap,"image2");

                                sharedPreferences.edit().putString("imageUrl2", null).apply();
                                sharedPreferences.edit().putString("imageId2", null).apply();
                            }
                            else {
                                productimage3.setImageBitmap(bitmap1);
                                sharedPreferences.edit().putString("image3", filePath).apply();
                                sizeBitmap = getResizedBitmap(bitmap1,700);
                                BitMapToString(sizeBitmap,"image3");

                                sharedPreferences.edit().putString("imageUrl3", null).apply();
                                sharedPreferences.edit().putString("imageId3", null).apply();
                            }
                        }
                        else {
                            sharedPreferences.edit().putString("image1", null).apply();
                            sharedPreferences.edit().putString("image2", null).apply();
                            sharedPreferences.edit().putString("image3", null).apply();
                        }

                }catch (Exception e){

                }
            }
        };


        mMessageReceiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {

                    categoryLevel1 = intent.getStringExtra("categoryLevel1");
                    categoryLevel2 = intent.getStringExtra("categoryLevel2");
//                    if(ExpListViewAdapterWithCheckbox.getCategory().size()==0){
//                        product_category.setText("");
//                    }
//                    else {
//                        product_category.setText(productCategory.replaceAll("[ ]", ""));
//                    }
//


                }catch (Exception e){

                }
            }
        };

        mMessageReceiver2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {

                  position = intent.getIntExtra("position",0);
                  String imageUrl = intent.getStringExtra("image");
                  String imageId = intent.getStringExtra("image_id");

                    if(position!=0) {
                        if (position == 1) {
                            Glide.with(context).load(imageUrl).into(productimage1);
                            sharedPreferences.edit().putString("imageUrl1", imageUrl).apply();
                            sharedPreferences.edit().putString("imageId1", imageId).apply();
                            sharedPreferences.edit().putString("image1", null).apply();

                        }
                        if (position == 2) {
                            Glide.with(context).load(imageUrl).into(productimage2);
                            sharedPreferences.edit().putString("imageUrl2", imageUrl).apply();
                            sharedPreferences.edit().putString("imageId2", imageId).apply();

                            sharedPreferences.edit().putString("image2", null).apply();

                        }
                        if (position == 3) {
                            Glide.with(context).load(imageUrl).into(productimage3);
                            sharedPreferences.edit().putString("imageUrl3", imageUrl).apply();
                            sharedPreferences.edit().putString("imageId3", imageId).apply();

                            sharedPreferences.edit().putString("image3", null).apply();
                        }
                    }
                    else {
                        sharedPreferences.edit().putString("imageUrl1", null).apply();
                        sharedPreferences.edit().putString("imageUrl2", null).apply();
                        sharedPreferences.edit().putString("imageUrl3", null).apply();
                    }

                }catch (Exception e){

                }
            }
        };


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver , new IntentFilter("camera-message"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver1 , new IntentFilter("products-message"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver2 , new IntentFilter("qfix-message"));

    }


   /* @Override
    protected void onStop() {
        super.onStop();
        try {
            LocalBroadcastManager.getInstance(DetectSwipeDirectionActivity.this).unregisterReceiver(mMessageReceiver);
        }catch (Exception e){

        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(DetectSwipeDirectionActivity.this).unregisterReceiver(mMessageReceiver);
            LocalBroadcastManager.getInstance(DetectSwipeDirectionActivity.this).unregisterReceiver(mMessageReceiver1);
            LocalBroadcastManager.getInstance(DetectSwipeDirectionActivity.this).unregisterReceiver(mMessageReceiver2);
        }catch (Exception e){

        }
    }

    public String BitMapToString(Bitmap bitmap,String key){
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            stringBitmap = Base64.encodeToString(b, Base64.DEFAULT);
            sharedPreferences.edit().putString(key, stringBitmap).apply();
        }catch (Exception e){

        }
        return stringBitmap;
    }


    public void getProductHSN(){

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"marketplace/getAllHsnCodeDetails.php";

        ///V1/customapi/getTaxByHsnCode?hsn_code=112221
     //   String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/getAllHsnCodeDetails";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);
        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading Categories.......");

        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {


                    try {
                        allHSNCodes = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(response);

                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String hsncode_id = jsonObject.getString("id");
                                String hsn_code = jsonObject.getString("code");
                                String description_of_goods =  jsonObject.getString("description");
                                String cgst_rate =  jsonObject.getString("cgst");
                                String sgst_rate =  jsonObject.getString("sgst");
                                String igst_rate =   jsonObject.getString("igst");

                                allHSNCodes.add(new AllHSNCode(hsncode_id,hsn_code,description_of_goods,cgst_rate,sgst_rate,igst_rate));

                                productHsnCode.add(hsn_code+" - "+description_of_goods);

                            }


                            ArrayAdapter<String> categoryAdap = new ArrayAdapter<String>(DetectSwipeDirectionActivity.this, R.layout.spinner_text_layout, productHsnCode);
                            product_hsncode_new.setAdapter(categoryAdap);
                            categoryAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            product_hsncode_new.setAdapter(categoryAdap);
                            //     spinner.setOnItemSelectedListener(this);
                            progressDialog.dismiss();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("TAG", "Error: " + error.getMessage());
                //   progressDialog.dismiss();
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
/*
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("state_id", String.valueOf(stateid));
                return checkParams(params);
            }*/

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
        MaintainRequestQueue.getInstance(DetectSwipeDirectionActivity.this).addToRequestQueue(req, "tag");

    }

    public void getTaxByProductHSN(final int hsnCode){

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url ="https://"+shopUrl+ BASE_URL+"customapi/getTaxByHsnCode?hsn_code="+hsnCode;

        ///V1/customapi/getTaxByHsnCode?hsn_code=112221
        //   String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/getAllHsnCodeDetails";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);
        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading Categories.......");

        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String igst = jsonObject.getString("igst_rate");

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("TAG", "Error: " + error.getMessage());
                //   progressDialog.dismiss();
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
/*
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("state_id", String.valueOf(stateid));
                return checkParams(params);
            }*/

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
        MaintainRequestQueue.getInstance(DetectSwipeDirectionActivity.this).addToRequestQueue(req, "tag");

    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
///meet.google.com/fvu-razc-aks

    public void editnewProduct1(final String productId,final String sku,final String productName,final String productDescription,final String attritubeId,final int price,
                               final int weight,final int stockQty,final String instock){

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url        = Constant.REVAMP_URL1+"customapi/saveproduct.php";

      //  String url = "https://surajshop.eduqfix.com/rest/V1/marketplaceproduct/save";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Editing new product.......");


//        AddProductRequest addProductRequest = new AddProductRequest();
//        standalone.eduqfix.qfixinfo.com.app.add_new_product.model.Request request = new standalone.eduqfix.qfixinfo.com.app.add_new_product.model.Request();
//        request.setSeller_id(String.valueOf(sellerId));
//        WholeData wholeData = new WholeData();
//        wholeData.setSeller_id(String.valueOf(sellerId));
//        wholeData.setSet(4);
//        wholeData.setType("simple");
//        wholeData.setProduct_id(productId);
//        wholeData.setId(productId);
//        wholeData.setApprove_status(0);
//        wholeData.setOld_status(0);
//        request.setWholedata(wholeData);
//        Product product = new Product();
//        product.setSku(sku);
//        product.setName(productName);
//        product.setDescription(productDescription);
//        product.setAttributeSetId(attritubeId);
//        product.setPrice(price);
//        product.setProduct_id(productId);
//        product.setStatus(1);
//        product.setVisibility(4);
//        product.setTypeId("simple");
//        product.setProduct_has_weight("1");
//        product.setTax_class_id(taxValue);
//        product.setHsn_code(hsnCode);
//        product.setWeight(weight);
//        product.setCategory_ids(ExpListViewAdapterWithCheckbox.getCategory());
//        StockData stockData = new StockData();
//        stockData.setManage_stock("");
//        stockData.setUse_config_manage_stock("1");
//        product.setStock_data(stockData);
//        QuantityStockStatus quantityStockStatus = new QuantityStockStatus();
//        quantityStockStatus.setQty(String.valueOf(stockQty));
//        quantityStockStatus.setIs_in_stock(instock);
//        ExtensionAttributes extensionAttributes = new ExtensionAttributes();
//        StockItem stockItem = new StockItem();
//        stockItem.setStockId(1);
//        stockItem.setQty(stockQty);
//        stockItem.setQtyDecimal(false);
//        stockItem.setUseConfigMinQty(true);
//        stockItem.setMinQty(0);
//        stockItem.setUseConfigMinQty(true);
//        stockItem.setMinSaleQty(0);
//        extensionAttributes.setStockItem(stockItem);
//        product.setExtensionAttributes(extensionAttributes);
//        product.setQuantity_and_stock_status(quantityStockStatus);
//        wholeData.setProduct(product);
//        request.setWholedata(wholeData);
//        addProductRequest.setRequest(request);
//        Gson gson = new Gson();
//        String placeOrderJson = gson.toJson(addProductRequest);
//        Log.d("PlaceOrder===","PlaceOrder==="+placeOrderJson);
//        final String mRequestBody = placeOrderJson;


        AddProductRequestMain newRequest = new AddProductRequestMain();
        AddProductRequestNew addProductRequestNew = new AddProductRequestNew();
        addProductRequestNew.setApprove_status(0);
        addProductRequestNew.setAttributeSetId(attritubeId);
        addProductRequestNew.setBackorders(0);
        addProductRequestNew.setCategory_level_1(parentId);
        addProductRequestNew.setCategory_level_2(subCategoryId);
      //  addProductRequestNew.setCategory_ids(ExpListViewAdapterWithCheckbox.getCategory());
        addProductRequestNew.setDecimalDivided(false);
        addProductRequestNew.setDescription(productDescription);
        addProductRequestNew.setEnableQtyIncrements(false);
        addProductRequestNew.setHsn_code(0);
        addProductRequestNew.setId("");
        addProductRequestNew.setInStock(false);
        addProductRequestNew.setIs_in_stock("1");
        addProductRequestNew.setManage_stock("");
        addProductRequestNew.setManageStock(false);
        addProductRequestNew.setMaxSaleQty(0);
        addProductRequestNew.setMinQty(0);
        addProductRequestNew.setMinSaleQty(0);
        addProductRequestNew.setName(productName);
        addProductRequestNew.setNotifyStockQty(0);
        addProductRequestNew.setOld_status(2);
        addProductRequestNew.setProduct_type(1);
        addProductRequestNew.setPrice(price);
        addProductRequestNew.setProduct_has_weight("1");
        addProductRequestNew.setProduct_id(productId);
        addProductRequestNew.setQty(String.valueOf(stockQty));
        addProductRequestNew.setQtyDecimal(false);
        addProductRequestNew.setQtyIncrements(0);
        addProductRequestNew.setSeller_id(String.valueOf(sellerId));
        addProductRequestNew.setSet(4);
        addProductRequestNew.setSku(sku);
        addProductRequestNew.setStatus(1);
        addProductRequestNew.setStockId(1);
        addProductRequestNew.setStockStatusChangedAuto(0);
        addProductRequestNew.setSwatch_image("");
        addProductRequestNew.setTax_class_id(0);
        addProductRequestNew.setType("simple");
        addProductRequestNew.setTypeId("simple");
        addProductRequestNew.setUse_config_manage_stock("1");
        addProductRequestNew.setUseConfigBackorders(false);
        addProductRequestNew.setUseConfigManageStock(false);
        addProductRequestNew.setUseConfigEnableQtyInc(false);
        addProductRequestNew.setUseConfigMinQty(true);
        addProductRequestNew.setUseConfigMaxSaleQty(false);
        addProductRequestNew.setUseConfigMinSaleQty(0);
        addProductRequestNew.setUseConfigNotifyStockQty(false);
        addProductRequestNew.setUseConfigQtyIncrements(false);
        addProductRequestNew.setVisibility(4);
        addProductRequestNew.setWeight(weight);
        newRequest.setRequest(addProductRequestNew);
        Gson gson = new Gson();
        String placeOrderJson = gson.toJson(newRequest);
        Log.d("PlaceOrder===","PlaceOrder==="+placeOrderJson);
        final String mRequestBody = placeOrderJson;

        String adminToken = "Bearer " + sharedPreferences.getString("adminToken", null);
        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("status");
                    if(success.equals("true")) {
                        if (image1 == null && image2 == null && image3 == null) {
                            String imageUrl = sharedPreferences.getString("imageUrl1", null);
                            String imageUrl2 = sharedPreferences.getString("imageUrl2", null);
                            String imageUrl3 = sharedPreferences.getString("imageUrl3", null);

                            if (imageUrl == null && imageUrl2 == null && imageUrl3 == null) {
                                Toast.makeText(DetectSwipeDirectionActivity.this, "product edited successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(DetectSwipeDirectionActivity.this, MainDashboardActivity.class);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                            }
                        }

                        if (image1 != null) {
                            new RequestTask().execute(sku);
                        } else {
                            String imageUrl = sharedPreferences.getString("imageUrl1", null);
                            if (imageUrl != null) {
                                String imageId = sharedPreferences.getString("imageId1", null);
                                saveImageLibrary(imageId, "", sku);
                            }
                        }
                        if (image2 != null) {
                            new RequestTask2().execute(sku);
                        } else {
                            String imageUrl = sharedPreferences.getString("imageUrl2", null);
                            if (imageUrl != null) {
                                String imageId = sharedPreferences.getString("imageId2", null);
                                saveImageLibrary2(imageId, "", sku);
                            }
                        }
                        if (image3 != null) {
                            new RequestTask3().execute(sku);
                        } else {
                            String imageUrl = sharedPreferences.getString("imageUrl3", null);
                            if (imageUrl != null) {
                                String imageId = sharedPreferences.getString("imageId3", null);
                                saveImageLibrary3(imageId, "", sku);
                            }
                        }

                    }
                    else {
                        JSONArray message = jsonObject.getJSONArray("mandatory_fields");
                        Log.d("mess...", String.valueOf(message));
                        progressDialog.dismiss();
                        Toast.makeText(DetectSwipeDirectionActivity.this, String.valueOf(message), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
             //   progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error,,,",error.toString());
                Toast.makeText(DetectSwipeDirectionActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
                params.put("Authorization",adminToken);
                return params;
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

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(DetectSwipeDirectionActivity.this).addToRequestQueue(req, "tag");

    }

    public void addnewProduct1(final String productId,final String sku,final String productName,final String productDescription,final String attritubeId,final int price,
                                final int weight,final int stockQty,final String instock,final String image1,
                                final String image2,final String image3){

          sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
          String shopUrl    = sharedPreferences.getString("shop_url",null);
          String url        = Constant.REVAMP_URL1+"customapi/saveproduct.php";

      //  String url = "https://shopping1.webiknows.net/rest/V1/customapi/saveproduct.php";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);
        final String adminToken = sharedPreferences.getString("adminToken",null);
        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Saving new product.......");
      //  Cannot do shipment for the order.

//        AddProductRequest addProductRequest = new AddProductRequest();
//        standalone.eduqfix.qfixinfo.com.app.add_new_product.model.Request request = new standalone.eduqfix.qfixinfo.com.app.add_new_product.model.Request();
//        request.setSeller_id(String.valueOf(74));
//        WholeData wholeData = new WholeData();
//        wholeData.setSet(4);
//        wholeData.setType("simple");
//        wholeData.setProduct_id(productId);
//        wholeData.setId(productId);
//        request.setWholedata(wholeData);
//        Product product = new Product();
//        product.setSku(sku);
//        product.setName(productName);
//        product.setDescription(productDescription);
//        product.setAttributeSetId(attritubeId);
//        product.setPrice(price);
//        product.setProduct_id(productId);
//        product.setStatus(1);
//        product.setVisibility(4);
//        product.setTax_class_id(taxValue);
//        product.setHsn_code(hsnCode);
//        product.setTypeId("simple");
//        product.setProduct_has_weight("1");
//        product.setWeight(weight);
//        product.setCategory_ids(ExpListViewAdapterWithCheckbox.getCategory());
//        product.setSwatch_image("");
//        StockData stockData = new StockData();
//        stockData.setManage_stock("");
//        stockData.setUse_config_manage_stock("1");
//        product.setStock_data(stockData);
//        QuantityStockStatus quantityStockStatus = new QuantityStockStatus();
//        quantityStockStatus.setQty(String.valueOf(stockQty));
//        quantityStockStatus.setIs_in_stock(instock);
//        ExtensionAttributes extensionAttributes = new ExtensionAttributes();
//        StockItem stockItem = new StockItem();
//        stockItem.setStockId(1);
//        stockItem.setQty(stockQty);
//        stockItem.setQtyDecimal(false);
//        stockItem.setUseConfigMinQty(true);
//        stockItem.setMinQty(0);
//        stockItem.setUseConfigMinQty(true);
//        stockItem.setMinSaleQty(0);
//        extensionAttributes.setStockItem(stockItem);
//        product.setExtensionAttributes(extensionAttributes);
//        product.setQuantity_and_stock_status(quantityStockStatus);
//        wholeData.setProduct(product);
//        request.setWholedata(wholeData);
//        addProductRequest.setRequest(request);
//        Gson gson = new Gson();
//        String placeOrderJson = gson.toJson(addProductRequest);
//        Log.d("PlaceOrder===","PlaceOrder==="+placeOrderJson);
//        final String mRequestBody = placeOrderJson;


        AddProductRequestMain newRequest = new AddProductRequestMain();
        AddProductRequestNew addProductRequestNew = new AddProductRequestNew();
        addProductRequestNew.setApprove_status(0);
        addProductRequestNew.setAttributeSetId(attritubeId);
        addProductRequestNew.setBackorders(0);
        addProductRequestNew.setCategory_level_1(parentId);
        addProductRequestNew.setCategory_level_2(subCategoryId);
       // addProductRequestNew.setCategory_ids(ExpListViewAdapterWithCheckbox.getCategory());
        addProductRequestNew.setDecimalDivided(false);
        addProductRequestNew.setDescription(productDescription);
        addProductRequestNew.setEnableQtyIncrements(false);
        addProductRequestNew.setHsn_code(0);
        addProductRequestNew.setId("");
        addProductRequestNew.setInStock(false);
        addProductRequestNew.setIs_in_stock("1");
        addProductRequestNew.setManage_stock("");
        addProductRequestNew.setManageStock(false);
        addProductRequestNew.setMaxSaleQty(0);
        addProductRequestNew.setMinQty(0);
        addProductRequestNew.setMinSaleQty(0);
        addProductRequestNew.setName(productName);
        addProductRequestNew.setNotifyStockQty(0);
        addProductRequestNew.setOld_status(2);
        addProductRequestNew.setProduct_type(1);
        addProductRequestNew.setPrice(price);
        addProductRequestNew.setProduct_has_weight("1");
        addProductRequestNew.setProduct_id("");
        addProductRequestNew.setQty(String.valueOf(stockQty));
        addProductRequestNew.setQtyDecimal(false);
        addProductRequestNew.setQtyIncrements(0);
        addProductRequestNew.setSeller_id(String.valueOf(sellerId));
        addProductRequestNew.setSet(4);
        addProductRequestNew.setSku(sku);
        addProductRequestNew.setStatus(1);
        addProductRequestNew.setStockId(1);
        addProductRequestNew.setStockStatusChangedAuto(0);
        addProductRequestNew.setSwatch_image("");
        addProductRequestNew.setTax_class_id(0);
        addProductRequestNew.setType("simple");
        addProductRequestNew.setTypeId("simple");
        addProductRequestNew.setUse_config_manage_stock("1");
        addProductRequestNew.setUseConfigBackorders(false);
        addProductRequestNew.setUseConfigManageStock(false);
        addProductRequestNew.setUseConfigEnableQtyInc(false);
        addProductRequestNew.setUseConfigMinQty(true);
        addProductRequestNew.setUseConfigMaxSaleQty(false);
        addProductRequestNew.setUseConfigMinSaleQty(0);
        addProductRequestNew.setUseConfigNotifyStockQty(false);
        addProductRequestNew.setUseConfigQtyIncrements(false);
        addProductRequestNew.setVisibility(4);
        addProductRequestNew.setWeight(weight);
        newRequest.setRequest(addProductRequestNew);
        Gson gson = new Gson();
        String placeOrderJson = gson.toJson(newRequest);
        Log.d("PlaceOrder===","PlaceOrder==="+placeOrderJson);
        final String mRequestBody = placeOrderJson;
        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

//                   JSONArray array = new JSONArray(response);
//                   for(int i=0; i<array.length(); i++){
//                        productIdNew = array.get(1).toString();
//                   }
                    if(status.equalsIgnoreCase("true")) {
                        String imageUrl = sharedPreferences.getString("imageUrl1",null);
                        if (image1 != null) {
                            new RequestTask().execute(sku);
                        }
                        else if(imageUrl!=null){
                                String imageId = sharedPreferences.getString("imageId1",null);
                                saveImageLibrary(imageId,"",sku);
                            }
                        else {
                            Toast.makeText(DetectSwipeDirectionActivity.this,"product saved successfully",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(DetectSwipeDirectionActivity.this,MainDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else {

                        Toast.makeText(DetectSwipeDirectionActivity.this,"Something went Wrong!!!",Toast.LENGTH_LONG).show();

                          progressDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
              //  progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error,,,",error.toString());
                Toast.makeText(DetectSwipeDirectionActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
                params.put("Authorization","Bearer "+adminToken);
                return params;
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

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(DetectSwipeDirectionActivity.this).addToRequestQueue(req, "tag");

    }


    public class RequestTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                String sku = strings[0];
                sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
                String imageDataBytes = sharedPreferences.getString("image1",null);
                ByteArrayInputStream imageStream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
                Log.d("url.....", String.valueOf(imageStream));
                HttpClient client = new DefaultHttpClient();


                String adminToken = "Bearer " + sharedPreferences.getString("adminToken", null);
                String shopUrl = sharedPreferences.getString("shop_url", null);

             //   String url = "https://" + shopUrl + Constant.BASE_URL + "products/" + sku + "/media";
                String url = Constant.REVAMP_URL1+"products/media.php";
                HttpPost post = new HttpPost(url);
                post.setHeader("Authorization", adminToken);

                Log.d("admin",adminToken);

                Log.d("url.....", url);
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("sku", sku);
                entityBuilder.addTextBody("seller_id", String.valueOf(sellerId));
                String boundary = "---------------" + UUID.randomUUID().toString();
                entityBuilder.setBoundary(boundary);

                if (imageStream != null) {
                    entityBuilder.addBinaryBody("coverImage", imageStream, ContentType.create("image/png"), "imagename.jpg");
                }
                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
                Log.v("result", result);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                   // progressDialog.dismiss();
                    if (image2 != null) {
                      new RequestTask2().execute(sku);
                    } else {
                        String imageUrl = sharedPreferences.getString("imageUrl2",null);
                        if(imageUrl!=null){
                            String imageId = sharedPreferences.getString("imageId2",null);
                            saveImageLibrary2(imageId,"",sku);
                        }
                        else {
                            Handler handler = new Handler(getMainLooper());
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(DetectSwipeDirectionActivity.this, "product saved successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(DetectSwipeDirectionActivity.this, MainDashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }
                } else {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(DetectSwipeDirectionActivity.this, "Images not uploaded, please try again!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();                          }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        progressDialog.setMessage("Uploading image 1...");
                        progressDialog.show();
                    }
                });
            }catch (Exception e){

            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
          //  progressDialog.dismiss();
        }
    }
    public class RequestTask2 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                String sku = strings[0];
                sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
                String imageDataBytes = sharedPreferences.getString("image2",null);
                ByteArrayInputStream imageStream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));

                HttpClient client = new DefaultHttpClient();


                String adminToken = "Bearer " + sharedPreferences.getString("adminToken", null);
                String shopUrl = sharedPreferences.getString("shop_url", null);

                String url = Constant.REVAMP_URL1+"products/media.php";
              //  String url = "https://surajshop.eduqfix.com/rest/V1/products/" + sku + "/media";
                HttpPost post = new HttpPost(url);
                post.setHeader("Authorization", adminToken);

                Log.d("url.....", url);
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("sku", sku);
                entityBuilder.addTextBody("seller_id", String.valueOf(sellerId));

                String boundary = "---------------" + UUID.randomUUID().toString();
                entityBuilder.setBoundary(boundary);
                if (imageStream != null) {
                    entityBuilder.addBinaryBody("picture", imageStream, ContentType.create("image/png"), "imagename.jpg");
                }
                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
                Log.v("result", result);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                   // progressDialog.dismiss();
                    if (image3 != null) {
                       new RequestTask3().execute(sku);
                    } else {
                        String imageUrl = sharedPreferences.getString("imageUrl2",null);
                        if(imageUrl!=null){
                            String imageId = sharedPreferences.getString("imageId2",null);
                            saveImageLibrary2(imageId,"",sku);
                        }
                        else {
                            Handler handler = new Handler(getMainLooper());
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(DetectSwipeDirectionActivity.this, "product saved successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(DetectSwipeDirectionActivity.this, MainDashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressDialog.dismiss();
                                }
                            });
                        }

                      //  progressDialog.dismiss();
                    }
                } else {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(DetectSwipeDirectionActivity.this, "Images not uploaded, please try again!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();                          }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        progressDialog.setMessage("Uploading image 2...");
                        progressDialog.show();
                    }
                });
            }catch (Exception e){

            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
           // progressDialog.dismiss();
        }
    }

    public class RequestTask3 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                String sku = strings[0];
                sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
                String imageDataBytes = sharedPreferences.getString("image3",null);
                ByteArrayInputStream imageStream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));

                HttpClient client = new DefaultHttpClient();


                String adminToken = "Bearer " + sharedPreferences.getString("adminToken", null);
                String shopUrl = sharedPreferences.getString("shop_url", null);

                String url = Constant.REVAMP_URL1+"products/media.php";
              //  String url = "https://surajshop.eduqfix.com/rest/V1/products/" + sku + "/media";

                HttpPost post = new HttpPost(url);
                post.setHeader("Authorization", adminToken);

                Log.d("url.....", url);
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("sku", sku);
                entityBuilder.addTextBody("seller_id", String.valueOf(sellerId));

                String boundary = "---------------" + UUID.randomUUID().toString();
                entityBuilder.setBoundary(boundary);
                if (imageStream != null) {
                    entityBuilder.addBinaryBody("picture_1", imageStream, ContentType.create("image/png"), "imagename.jpg");
                }
                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
                Log.v("result", result);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(DetectSwipeDirectionActivity.this, "product saved successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(DetectSwipeDirectionActivity.this, MainDashboardActivity.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();                            }
                    });


                } else {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(DetectSwipeDirectionActivity.this, "Images not uploaded, please try again!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();                          }
                    });


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        progressDialog.setMessage("Uploading image 3...");
                        progressDialog.show();
                    }
                });
            }catch (Exception e){

            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            progressDialog.dismiss();
        }
    }


    public void saveImageLibrary(final String imageId,final String productId,final String sku){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"marketplace/saveImageMaster.php";
      //  String url = "https://surajshop.eduqfix.com/rest/V1/marketplace/saveimagemaster";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                progressDialog.show();
                progressDialog.setTitle("Please wait...");
                progressDialog.setMessage("Uploading Image1.......");
            }
        });

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("imagemasterid", imageId);
            jsonObject1.put("sku", sku);
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request.toString();
        String adminToken = "Bearer " + sharedPreferences.getString("customerToken", null);
        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{

                    String imageUrl = sharedPreferences.getString("imageUrl2",null);
                    if(imageUrl!=null){
                        String imageId = sharedPreferences.getString("imageId2",null);
                        saveImageLibrary2(imageId,"",sku);
                    }
                    else {
                        if(image2!= null){
                            new RequestTask2().execute(sku);
                        }
                        else {
                            Toast.makeText(DetectSwipeDirectionActivity.this, "product saved successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(DetectSwipeDirectionActivity.this, MainDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }


                }catch (Exception e){

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
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", adminToken);

                return params;
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
        MaintainRequestQueue.getInstance(DetectSwipeDirectionActivity.this).addToRequestQueue(req, "tag");



    }

    public void saveImageLibrary2(final String imageId,final String productId,final String sku){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"marketplace/saveImageMaster.php";
      //  String url = "https://surajshop.eduqfix.com/rest/V1/marketplace/saveimagemaster";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                progressDialog.show();
                progressDialog.setTitle("Please wait...");
                progressDialog.setMessage("Uploading Image2.......");
            }
        });


        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("imagemasterid", imageId);
            jsonObject1.put("sku", sku);
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request.toString();
        String adminToken = "Bearer " + sharedPreferences.getString("customerToken", null);
        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{

                    String imageUrl = sharedPreferences.getString("imageUrl3",null);
                    if(imageUrl!=null){
                        String imageId = sharedPreferences.getString("imageId3",null);
                        saveImageLibrary3(imageId,"",sku);
                    }
                    else {
                        if(image3!= null){
                            new RequestTask3().execute(sku);
                        }
                        else {
                            Toast.makeText(DetectSwipeDirectionActivity.this, "product saved successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(DetectSwipeDirectionActivity.this, MainDashboardActivity.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        }
                    }


                }catch (Exception e){

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
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", adminToken);

                return params;
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
        MaintainRequestQueue.getInstance(DetectSwipeDirectionActivity.this).addToRequestQueue(req, "tag");



    }

    public void saveImageLibrary3(final String imageId,final String productId,final String sku){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"marketplace/saveImageMaster.php";
      //  String url = "https://surajshop.eduqfix.com/rest/V1/marketplace/saveimagemaster";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                progressDialog.show();
                progressDialog.setTitle("Please wait...");
                progressDialog.setMessage("Uploading Image3.......");
            }
        });


        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("imagemasterid", imageId);
            jsonObject1.put("sku", sku);
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request.toString();
        String adminToken = "Bearer " + sharedPreferences.getString("customerToken", null);
        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    Toast.makeText(DetectSwipeDirectionActivity.this, "product saved successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DetectSwipeDirectionActivity.this, MainDashboardActivity.class);
                    startActivity(intent);
                    finish();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", adminToken);

                return params;
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
        MaintainRequestQueue.getInstance(DetectSwipeDirectionActivity.this).addToRequestQueue(req, "tag");



    }
    public void getCategoriesNew(final String sellerId){


        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"customapi/getStanaloneSellerCategory.php?sellerId="+sellerId;

        //     String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/getStanaloneSellerCategory?sellerId="+sellerId;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);
        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading Categories.......");

        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response);

                        if(jsonArray.length()>0) {
                            //  ArrayList<DatabaseCategoriesModel> databaseCategoriesModels = new ArrayList<>();
                            categoryNameList = new ArrayList<>();

                            databaseCategoriesModels = new ArrayList<>();

                            databaseSubCategoriesModels = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject     = jsonArray.getJSONObject(i);
                                String categoriesId       = jsonObject.getString("category_id");
                                categoriesname            = jsonObject.getString("name");
                                String imageurl           = jsonObject.getString("imageurl");
                                String category_banner    = jsonObject.getString("category_banner");
                                String parent_id          = jsonObject.getString("parent_id");

                                databaseCategoriesModels.add(new DatabaseCategoriesModel(Integer.parseInt(categoriesId),categoriesId,categoriesname,imageurl,category_banner,parent_id));

                                JSONArray jsonArray1 = jsonObject.getJSONArray("sub_categories");


                                for (int j = 0;j< jsonArray1.length(); j++){

                                    JSONObject jsonObject1  = jsonArray1.getJSONObject(j);
                                    sub_categoryid          = jsonObject1.getString("category_id");
                                    sub_imageurl            = jsonObject1.getString("imageurl");
                                    sub_parentid            = jsonObject1.getString("parent_id");
                                    sub_name                = jsonObject1.getString("name");

                                    databaseSubCategoriesModels.add(new DatabaseSubCategoriesModel(j,sub_categoryid,sub_imageurl,sub_parentid,sub_name,categoriesname));

                                }


                            }
                            progressDialog.dismiss();
                            for(DatabaseCategoriesModel state : databaseCategoriesModels){
                                categoryNameList.add(state.getName());
                            }

                            ArrayAdapter<String> stateAdapter= new ArrayAdapter<String>(DetectSwipeDirectionActivity.this,android.R.layout.simple_list_item_1, categoryNameList);
                            stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            categoryLevel1Spinner.setAdapter(stateAdapter);

                            if (productModel.getCategoryName() != null) {
                                int spinnerPosition = stateAdapter.getPosition(productModel.getCategoryName());
                                categoryLevel1Spinner.setSelection(spinnerPosition);
                            }


                        }
                        else {
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //    Log.e("TAG", "Error: " + error.getMessage());
                //   progressDialog.dismiss();
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
/*
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("state_id", String.valueOf(stateid));
                return checkParams(params);
            }*/

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
        MaintainRequestQueue.getInstance(DetectSwipeDirectionActivity.this).addToRequestQueue(req, "tag");

    }
}
