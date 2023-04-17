package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.AddNewProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductAttributes;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductImages;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductMediaGallery;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductModel;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.CategoryIdsModel;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.AddProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.AddProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.DatabaseCategoriesModel;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.DatabaseSubCategoriesModel;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.ComplaintsDetailsActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.MainDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.RequestTrialDetailsActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SMSContactListAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class MyProductListActivity extends AppCompatActivity implements AddNewProductListAdapter.ClickListener, View.OnClickListener {

    ProgressDialog progressDialog;
    String sku;
    String productName;
    String productPrice;
    String productId;
    String productImage;
    String productSmallImage;
    String valueId;
    String file;
    String mediaType;
    String entityId;
    String label;
    String description;
    String shortDesc;
    String taxClassId;
    String type;
    String hsnCode;
    String weight;
    String stock;
    boolean stockStatus;
    String thumbnail;
    ArrayList<AddNewProductModel> addNewProductModels;
    AddNewProductListAdapter addProductListAdapter;
    AddNewProductModel addNewProductModel;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageView add_btn;
    ArrayList<String> category_ids;
    SharedPreferences sharedPreferences;
    LoginResponse customerDetails;
    int sellerId;
    int page;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    RelativeLayout bottomLayout;
    EditText search;
    SearchableSpinner product_visibility;
    ArrayList<String> visibleArray;
    String status;
    Button visibiltyBtn;
    String productIds;
    int visibilityStatus;
    CheckBox product_name_checkbox,product_sku_checkbox,product_clear_filter;
    ImageView search_filter_image;
    String searchProductName,searchProductSku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_list);

        getSupportActionBar().setTitle("My product list");
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);

        search = findViewById(R.id.myproduct_search);
        product_visibility = findViewById(R.id.product_visibility);
        visibiltyBtn = findViewById(R.id.visiblityBtn);
        visibleArray   = new ArrayList<>();

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails().getSeller_id() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        page =0;
        progressDialog = new ProgressDialog(MyProductListActivity.this);
        recyclerView = findViewById(R.id.my_product_list_recyclerview);
        add_btn  = findViewById(R.id.add_floating_btn);
        linearLayoutManager = new LinearLayoutManager(MyProductListActivity.this);
        bottomLayout =  findViewById(R.id.loadItemsLayout_recyclerView);
        product_name_checkbox = findViewById(R.id.search_product_name);
        product_sku_checkbox = findViewById(R.id.search_product_sku);
        product_clear_filter = findViewById(R.id.search_clear_filter);
        search_filter_image  = findViewById(R.id.search_filter_image);

        product_name_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (product_name_checkbox.isChecked())
                {
                    product_sku_checkbox.setChecked(false);
                    product_clear_filter.setChecked(false);
                }

            }
        });

        product_sku_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (product_sku_checkbox.isChecked())
                {
                    product_name_checkbox.setChecked(false);
                    product_clear_filter.setChecked(false);

                }

            }
        });

        product_clear_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (product_clear_filter.isChecked())
                {
                    product_name_checkbox.setChecked(false);
                    product_sku_checkbox.setChecked(false);
                    product_clear_filter.setChecked(false);
                    search.setText("");
                    getProduct(sellerId,1,null,null);

                }

            }
        });

        search_filter_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(search.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(MyProductListActivity.this,"Please Enter name or sku",Toast.LENGTH_LONG).show();
                }
                else if(!product_name_checkbox.isChecked() && !product_sku_checkbox.isChecked()){
                    Toast.makeText(MyProductListActivity.this,"Please select name or sku",Toast.LENGTH_LONG).show();
                }
                else {
                    if(product_name_checkbox.isChecked()){
                        searchProductName = search.getText().toString().trim();
                        searchProductSku = null;
                    }
                    if(product_sku_checkbox.isChecked()) {
                        searchProductSku = search.getText().toString().trim();
                        searchProductName = null;
                    }
                    getProduct(sellerId,page,searchProductName,searchProductSku);
                }
            }
        });


        category_ids = new ArrayList<>();
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProductListActivity.this,DetectSwipeDirectionActivity.class);
                intent.putExtra("position",0);
                startActivity(intent);
            }
        });


        getProduct(sellerId,page,searchProductName,searchProductSku);

        addProductListAdapter = new AddNewProductListAdapter();
        addProductListAdapter.setOnItemClickListener(this);

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
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            page++;
                            getProduct1(sellerId,page,searchProductName,searchProductSku);
                        }
                    }
                }
            }
        });

//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                addProductListAdapter.getFilter().filter(charSequence);
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//                //after the change calling the method and passing the search input
//            }
//        });

        visibleArray.add("Approved");
        visibleArray.add("Disapproved");
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(MyProductListActivity.this, R.layout.spinner_text_layout, visibleArray);
        product_visibility.setAdapter(statusAdapter);

        product_visibility.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue =  parent.getSelectedItem().toString();

                if(selectedValue.equalsIgnoreCase("Approved")){
                    status = "1";
                }
                else {
                    status = "2";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        visibiltyBtn.setOnClickListener(this);
        AddNewProductListAdapter.getProductIds().clear();

    }
    public void getProduct(final int sellerId,final int page,final String name, final String skuVal){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "";
        if(name != null){
             url =Constant.REVAMP_URL1+"customapi/getStandaloneProductBySellerId.php?sellerId="+sellerId+"&pageIndex="+page+
                    "&pageSize=10&name="+name;
        }
        else if(skuVal != null){
            url =Constant.REVAMP_URL1+"customapi/getStandaloneProductBySellerId.php?sellerId="+sellerId+"&pageIndex="+page+
                    "&pageSize=10&sku="+skuVal;
        }
        else {
            url = Constant.REVAMP_URL1+"customapi/getStandaloneProductBySellerId.php?sellerId="+sellerId+"&pageIndex=0&pageSize=10&sku=";

        }



     //   String url ="https://shoppingtest.eduqfix.com/rest/V1/customapi/getStandaloneProductBySellerId?sellerId="+sellerId+"&pageIndex="+page;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading my products.......");
        progressDialog.setCancelable(false);

        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {
                    addNewProductModels = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(response);
                        for (int ii=0; ii<jsonArray.length();ii++){
                            JSONObject jsonObject = jsonArray.getJSONObject(ii);
                            AddNewProductModel addNewProductModel = new AddNewProductModel();
                            sku               = jsonObject.getString("sku");
                            productName       = jsonObject.optString("productName");
                            productPrice      = jsonObject.optString("productPrice");
                            productId         = jsonObject.optString("productId");
                            productImage      = jsonObject.optString("productImage");
                            productSmallImage = jsonObject.optString("productSmallImage");
                            weight            = jsonObject.optString("weight");
                            stock             = jsonObject.optString("stock");
                            stockStatus       = jsonObject.optBoolean("stock_status");
                            thumbnail         = jsonObject.optString("thumbnail");
                            visibilityStatus  = jsonObject.optInt("status");

                            JSONObject categoryJson = jsonObject.optJSONObject("category_level_1");
                            String categoryName = categoryJson.optString("name");
                            String categoryId = categoryJson.optString("id");

                            String subcategoryName = "";
                            String subcategoryId = "";
                            JSONObject subcategoryJson = jsonObject.optJSONObject("category_level_2");
                            if(subcategoryJson != null){
                                 subcategoryName  = subcategoryJson.optString("name");
                                 subcategoryId    = subcategoryJson.optString("id");

                            }



//                            JSONArray categoryId = jsonObject.getJSONArray("categoryIds");
//                            ArrayList<CategoryIdsModel> categoryIdsModels = new ArrayList<>();
//                            for (int i=0; i<categoryId.length(); i++) {
//                                JSONObject jsonObject1 = categoryId.getJSONObject(i);
//                                String category_Id = jsonObject1.getString("category_id");
//                                String category_Name = jsonObject1.getString("category_name");
//
//                                CategoryIdsModel categoryIdsModel = new CategoryIdsModel();
//                                categoryIdsModel.setCategory_id(category_Id);
//                                categoryIdsModel.setCategory_name(category_Name);
//                                categoryIdsModels.add(categoryIdsModel);
//                            }

                            addNewProductModel.setSku(sku);
                            addNewProductModel.setProductName(productName);
                            addNewProductModel.setProductPrice(productPrice);
                            addNewProductModel.setProductId(productId);
                            addNewProductModel.setProductImage(productImage);
                            addNewProductModel.setProductSmallImage(productSmallImage);
                      //      addNewProductModel.setCategoryIds(categoryIdsModels);
                            addNewProductModel.setStock(stock);
                            addNewProductModel.setWeight(weight);
                            addNewProductModel.setStockStatus(stockStatus);
                            addNewProductModel.setThumbnail(thumbnail);
                            addNewProductModel.setVisibilityStatus(visibilityStatus);
                            addNewProductModel.setCategoryId(categoryId);
                            addNewProductModel.setCategoryName(categoryName);
                            addNewProductModel.setSubCategoryId(subcategoryId);
                            addNewProductModel.setSubCategoryName(subcategoryName);

                            JSONObject mediaGallery = jsonObject.getJSONObject("media_gallery");
                            JSONArray images = mediaGallery.getJSONArray("images");
                            AddNewProductMediaGallery addNewProductMediaGallery = new AddNewProductMediaGallery();
                            ArrayList<AddNewProductImages> addNewProductImagesArrayList = new ArrayList<>();

                                for (int i = 0; i < images.length(); i++) {
                                    JSONObject jsonObject1 = images.getJSONObject(i);

                                    valueId   = jsonObject1.optString("value_id");
                                    file      = jsonObject1.optString("file");
                                    mediaType = jsonObject1.optString("media_type");
                                    entityId  = jsonObject1.optString("entity_id");
                                    label  = jsonObject1.optString("label");

                                    AddNewProductImages addNewProductImages = new AddNewProductImages();
                                    addNewProductImages.setValue_id(valueId);
                                    addNewProductImages.setFile(file);
                                    addNewProductImages.setMedia_type(mediaType);
                                    addNewProductImages.setEntity_id(entityId);
                                    addNewProductImages.setLabel(label);

                                    addNewProductImagesArrayList.add(addNewProductImages);


                                }
                                addNewProductMediaGallery.setImages(addNewProductImagesArrayList);
                                addNewProductModel.setMedia_gallery(addNewProductMediaGallery);


                            JSONObject attributes = jsonObject.getJSONObject("attributes");
                            AddNewProductAttributes addNewProductAttributes = new AddNewProductAttributes();

                            description = attributes.optString("description");
                            shortDesc   = attributes.optString("short_description");
                            taxClassId  = attributes.optString("tax_class_id");
                            type        = attributes.optString("weight_type");
                            hsnCode     = attributes.optString("hsn_code");

                            addNewProductAttributes.setDescription(description);
                            addNewProductAttributes.setTax_class_id(taxClassId);
                            addNewProductAttributes.setType(type);
                            addNewProductAttributes.setHsn_code(hsnCode);
                            addNewProductAttributes.setShort_description(shortDesc);
                            addNewProductModel.setAttributes(addNewProductAttributes);

                            addNewProductModels.add(addNewProductModel);

                        }

                        progressDialog.dismiss();

                addProductListAdapter = new AddNewProductListAdapter(MyProductListActivity.this, addNewProductModels);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(addProductListAdapter);
                addProductListAdapter.notifyDataSetChanged();
                add_btn.setVisibility(View.VISIBLE);


                }
                catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
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

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(MyProductListActivity.this).addToRequestQueue(req, "tag");

    }

    public void getProduct1(final int sellerId,final int page,final String name, final String skuVal){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "";
        if(name != null){
            url =Constant.REVAMP_URL1+"customapi/getStandaloneProductBySellerId.php?sellerId="+sellerId+"&pageIndex="+page+
                    "&pageSize=10&name="+name;
        }
        else if(skuVal != null){
            url = Constant.REVAMP_URL1+"customapi/getStandaloneProductBySellerId.php?sellerId="+sellerId+"&pageIndex="+page+
                    "&pageSize=10&sku="+skuVal;
        }
        else {
            url = Constant.REVAMP_URL1+"customapi/getStandaloneProductBySellerId.php?sellerId="+sellerId+"&pageIndex="+page+"&pageSize=10&sku=";

        }

        //   String url ="https://shoppingtest.eduqfix.com/rest/V1/customapi/getStandaloneProductBySellerId?sellerId="+sellerId+"&pageIndex="+page;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

    /*    progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading my products.......");*/
        bottomLayout.setVisibility(View.VISIBLE);
        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {
                  //  addNewProductModels = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int ii=0; ii<jsonArray.length();ii++){
                        JSONObject jsonObject = jsonArray.getJSONObject(ii);
                        AddNewProductModel addNewProductModel = new AddNewProductModel();
                        sku               = jsonObject.getString("sku");
                        productName       = jsonObject.optString("productName");
                        productPrice      = jsonObject.optString("productPrice");
                        productId         = jsonObject.optString("productId");
                        productImage      = jsonObject.optString("productImage");
                        productSmallImage = jsonObject.optString("productSmallImage");
                        weight            = jsonObject.optString("weight");
                        stock             = jsonObject.optString("stock");
                        stockStatus       = jsonObject.optBoolean("stock_status");
                        thumbnail         = jsonObject.optString("thumbnail");
                        visibilityStatus  = jsonObject.optInt("status");

                        JSONObject categoryJson = jsonObject.optJSONObject("category_level_1");
                        String categoryName = categoryJson.optString("name");
                        String categoryId = categoryJson.optString("id");

                        String subcategoryName = "";
                        String subcategoryId = "";
                        JSONObject subcategoryJson = jsonObject.optJSONObject("category_level_2");
                        if(subcategoryJson != null){
                            subcategoryName     = subcategoryJson.optString("name");
                            subcategoryId       = subcategoryJson.optString("id");

                        }

//
//                        JSONArray categoryId = jsonObject.getJSONArray("categoryIds");
//                        ArrayList<CategoryIdsModel> categoryIdsModels = new ArrayList<>();
//                        for (int i=0; i<categoryId.length(); i++) {
//                            JSONObject jsonObject1 = categoryId.getJSONObject(i);
//                            String category_Id = jsonObject1.getString("category_id");
//                            String category_Name = jsonObject1.getString("category_name");
//
//                            CategoryIdsModel categoryIdsModel = new CategoryIdsModel();
//                            categoryIdsModel.setCategory_id(category_Id);
//                            categoryIdsModel.setCategory_name(category_Name);
//                            categoryIdsModels.add(categoryIdsModel);
//                        }

                        addNewProductModel.setSku(sku);
                        addNewProductModel.setProductName(productName);
                        addNewProductModel.setProductPrice(productPrice);
                        addNewProductModel.setProductId(productId);
                        addNewProductModel.setProductImage(productImage);
                        addNewProductModel.setProductSmallImage(productSmallImage);
                   //     addNewProductModel.setCategoryIds(categoryIdsModels);
                        addNewProductModel.setStock(stock);
                        addNewProductModel.setWeight(weight);
                        addNewProductModel.setStockStatus(stockStatus);
                        addNewProductModel.setThumbnail(thumbnail);
                        addNewProductModel.setVisibilityStatus(visibilityStatus);
                        addNewProductModel.setCategoryId(categoryId);
                        addNewProductModel.setCategoryName(categoryName);
                        addNewProductModel.setSubCategoryId(subcategoryId);
                        addNewProductModel.setSubCategoryName(subcategoryName);

                        JSONObject mediaGallery = jsonObject.getJSONObject("media_gallery");
                        JSONArray images = mediaGallery.getJSONArray("images");
                        AddNewProductMediaGallery addNewProductMediaGallery = new AddNewProductMediaGallery();
                        ArrayList<AddNewProductImages> addNewProductImagesArrayList = new ArrayList<>();

                        for (int i = 0; i < images.length(); i++) {
                            JSONObject jsonObject1 = images.getJSONObject(i);

                            valueId   = jsonObject1.optString("value_id");
                            file      = jsonObject1.optString("file");
                            mediaType = jsonObject1.optString("media_type");
                            entityId  = jsonObject1.optString("entity_id");
                            label  = jsonObject1.optString("label");

                            AddNewProductImages addNewProductImages = new AddNewProductImages();
                            addNewProductImages.setValue_id(valueId);
                            addNewProductImages.setFile(file);
                            addNewProductImages.setMedia_type(mediaType);
                            addNewProductImages.setEntity_id(entityId);
                            addNewProductImages.setLabel(label);

                            addNewProductImagesArrayList.add(addNewProductImages);


                        }
                        addNewProductMediaGallery.setImages(addNewProductImagesArrayList);
                        addNewProductModel.setMedia_gallery(addNewProductMediaGallery);


                        JSONObject attributes = jsonObject.getJSONObject("attributes");
                        AddNewProductAttributes addNewProductAttributes = new AddNewProductAttributes();

                        description = attributes.optString("description");
                        shortDesc   = attributes.optString("short_description");
                        taxClassId  = attributes.optString("tax_class_id");
                        type        = attributes.optString("weight_type");
                        hsnCode     = attributes.optString("hsn_code");

                        addNewProductAttributes.setDescription(description);
                        addNewProductAttributes.setTax_class_id(taxClassId);
                        addNewProductAttributes.setType(type);
                        addNewProductAttributes.setHsn_code(hsnCode);
                        addNewProductAttributes.setShort_description(shortDesc);
                        addNewProductModel.setAttributes(addNewProductAttributes);

                        addNewProductModels.add(addNewProductModel);

                    }

                  //  progressDialog.dismiss();

                   /* addProductListAdapter = new AddNewProductListAdapter(MyProductListActivity.this, addNewProductModels);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(addProductListAdapter);*/
                    addProductListAdapter.notifyDataSetChanged();
                    add_btn.setVisibility(View.VISIBLE);
                    loading = true;
                    bottomLayout.setVisibility(View.GONE);

                }
                catch (Exception e) {
                    e.printStackTrace();
                    //progressDialog.dismiss();
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

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(MyProductListActivity.this).addToRequestQueue(req, "tag");

    }
    @Override
    public void onItemClick(View v, int position) {
        addNewProductModel = addProductListAdapter.getWordAtPosition(position);

        Intent yourIntent = new Intent(this, DetectSwipeDirectionActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("product", addNewProductModel);
        yourIntent.putExtras(b); //pass bundle to your intent
        startActivity(yourIntent);



    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        if(AddNewProductListAdapter.getProductIds().size() > 0){
            productIds = android.text.TextUtils.join(",", AddNewProductListAdapter.getProductIds());
            JSONObject jsonObject1 = new JSONObject();
            JSONObject request = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray types=new JSONArray();
            for (int i=0; i <AddNewProductListAdapter.getProductIds().size(); i++){
                try {
                    types.put(AddNewProductListAdapter.getProductIds().get(i));
                    jsonObject1.put("productIds", types);
                    jsonArray.put(jsonObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            try {
                jsonObject1.put("status",status);
                request.put("request",jsonObject1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final String mRequestBody = request.toString();

            Log.d("req",mRequestBody);
            saveComments(mRequestBody);

        }
    }

    public void saveComments(final String mRequestBody){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"marketplace/changeProductVisibility.php";

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
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if(status.equalsIgnoreCase("true")){
                        Toast.makeText(MyProductListActivity.this,"Successfully Updated",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MyProductListActivity.this, MainDashboardActivity.class);
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
        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        MaintainRequestQueue.getInstance(MyProductListActivity.this).addToRequestQueue(req, "tag");



    }
}
