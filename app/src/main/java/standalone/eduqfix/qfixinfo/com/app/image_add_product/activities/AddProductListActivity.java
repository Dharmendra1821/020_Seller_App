package standalone.eduqfix.qfixinfo.com.app.image_add_product.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.database.SubDBManager;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class AddProductListActivity extends AppCompatActivity implements AddProductListAdapter.ClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    ArrayList<AddProductListingModel> addProductListingModels;
    AddProductListAdapter addProductListAdapter;
    RecyclerView recyclerView;
    AddProductListingModel addProductListingModel;
    ProgressDialog progressDialog;
    SubDBManager subDBManager;
    ArrayList<DatabaseCategoriesModel> databaseCategoriesModels;
    ArrayList<DatabaseSubCategoriesModel> databaseSubCategoriesModels;
    AutoCompleteTextView categories,subcategories;
    String sku;
    String productName;
    String productPrice;
    String productId;
    String productImage;
    String value_id;
    String fileId;
    String entityId;
    AutoCategoryAdapter autoCategoryAdapter;
    AutoSubCategoryAdapter autoSubCategoryAdapter;
    private boolean loading = true;
    String[] options;
    private ActionBar toolbar;
    LinearLayoutManager linearLayoutManager;
    int page;

    int pastVisiblesItems, visibleItemCount, totalItemCount;

    ProgressBar progressBar;
    RelativeLayout bottomLayout;
    int flag=0;
    SharedPreferences sharedPreferences;
    Button search_filter;
    Spinner spinner,sub_spinner;
    ArrayList<String> categoryValue;
    ArrayList<String> subcategoryValue;
    Button searchBtn;
    int sellerId;
    String sub_categoryid;
    String sub_imageurl;
    String sub_parentid;
    String sub_name;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_list);

        recyclerView = findViewById(R.id.add_product_list_recyclerview);
       // categories = findViewById(R.id.auto_categories);
      //  subcategories = findViewById(R.id.auto_sub_categories);
        bottomLayout =  findViewById(R.id.loadItemsLayout_recyclerView);
        searchBtn = findViewById(R.id.search_product_list);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Product List");

        spinner =  findViewById(R.id.spinner);
        sub_spinner = findViewById(R.id.sub_spinner);

        categoryValue = new ArrayList<>();
        subcategoryValue = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(AddProductListActivity.this);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);

        page =1;
        flag =0;

        addProductListAdapter = new AddProductListAdapter();
        addProductListAdapter.setOnItemClickListener(this);

 //       sendData("3234");
        try {
            sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);

            String customerDetails = sharedPreferences.getString("CustomerDetails", null);
            LoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<LoginResponse>() {
            }.getType());
            sellerId = loginResponse != null && loginResponse.getCustomerDetails() != null ? loginResponse.getCustomerDetails().getId() : 0;

            Log.d("SellerId....", String.valueOf(sellerId));

            progressDialog = new ProgressDialog(AddProductListActivity.this);
            linearLayoutManager = new LinearLayoutManager(this);
            subDBManager = new SubDBManager(AddProductListActivity.this);
            subDBManager.open();

            getCategories(sellerId);

            //  setData();

            getCategories1(sellerId,0, page);
            sharedPreferences.edit().putString("cat_id", "null").apply();

            spinner.setOnItemSelectedListener(this);
            sub_spinner.setVisibility(View.GONE);

            sub_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                   // Toast.makeText(AddProductListActivity.this, " You select >> "+databaseSubCategoriesModels.get(i).getSub_category_id(), Toast.LENGTH_SHORT).show();
                    if(adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Sub categories")){
                        sharedPreferences.edit().putString("sub_cat_id", "null").apply();
                    }
                    else {
                        sharedPreferences.edit().putString("sub_cat_id", databaseSubCategoriesModels.get(i-1).getSub_category_id()).apply();
                    }
                    hideSoftKeyboard(AddProductListActivity.this);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    hideSoftKeyboard(AddProductListActivity.this);
                }
            });

             searchBtn.setOnClickListener(this);
         /*   categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {

                    DatabaseCategoriesModel databaseCategoriesModel = autoCategoryAdapter.getItem(position);
                    String Name = databaseCategoriesModel.getName();
                    categories.setText(Name);
                    subcategories.setText("");
                    databaseSubCategoriesModels = subDBManager.getSubCategories(Integer.parseInt(databaseCategoriesModel.getCategory_id()));
                    autoSubCategoryAdapter = new AutoSubCategoryAdapter(AddProductListActivity.this, databaseSubCategoriesModels);
                    subcategories.setThreshold(1);
                    subcategories.setAdapter(autoSubCategoryAdapter);

                    page = 1;
                    flag =1 ;
                    sharedPreferences.edit().putString("cat_id", databaseCategoriesModel.getCategory_id()).apply();
                    getfilterCategories1(Integer.parseInt(databaseCategoriesModel.getCategory_id()),page);

                }
            });




            subcategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {
                    DatabaseSubCategoriesModel databaseCategoriesModel = autoSubCategoryAdapter.getItem(position);
                    String Name = databaseCategoriesModel.getSub_name();
                    subcategories.setText(Name);
                    page = 1;
                    flag =1 ;
                    sharedPreferences.edit().putString("cat_id", databaseCategoriesModel.getSub_category_id()).apply();
                    getfilterCategories1(Integer.parseInt(databaseCategoriesModel.getSub_category_id()),page);


                }
            });
      */

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
                                Log.v("...", "Last Item Wow !");


                                page++;
                                if(sharedPreferences.getString("cat_id",null).equalsIgnoreCase("null")) {
                                    getCategories2(sellerId,0, page);
                                }
                                else {
                                    if(sharedPreferences.getString("sub_cat_id",null).equalsIgnoreCase("null")){
                                        getfiletrCategories2(sellerId,Integer.parseInt(sharedPreferences.getString("cat_id",null)),page);
                                    }
                                    else {
                                        getfiletrCategories2(sellerId,Integer.parseInt(sharedPreferences.getString("sub_cat_id",null)),page);
                                    }

                                }


                            }
                        }
                    }
                }
            });

        }catch (Exception e){

        }



    }



    public void setData(){
        addProductListingModels = new ArrayList<>();

        addProductListingModel = new AddProductListingModel();
      //  addProductListingModel.setImageurl(R.drawable.gift_icon);
        addProductListingModel.setPrice("Rs 200");
        addProductListingModel.setProductname("Golden shoe");
        addProductListingModel.setSku("Sku-001");
        addProductListingModels.add(addProductListingModel);

        addProductListingModel = new AddProductListingModel();
      //  addProductListingModel.setImageurl(R.drawable.gift_icon);
        addProductListingModel.setPrice("Rs 300");
        addProductListingModel.setProductname("T-shirt");
        addProductListingModel.setSku("Sku-002");

        addProductListingModels.add(addProductListingModel);
        addProductListAdapter = new AddProductListAdapter(AddProductListActivity.this,addProductListingModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(addProductListAdapter);

    }

    public void getCategories(final int sellerId){

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl+Constant.BASE_URL+"customapi/getStanaloneSellerCategory?sellerId="+sellerId;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);
     //   progressDialog.show();
      //  progressDialog.setTitle("Please wait...");
      //  progressDialog.setMessage("Loading Categories.......");

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response);
                        if(jsonArray.length()>0) {

                            subDBManager.delete();
                            subDBManager.delete1();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject     = jsonArray.getJSONObject(i);
                                String categoriesId       = jsonObject.getString("category_id");
                                String name               = jsonObject.getString("name");
                                String imageurl           = jsonObject.getString("imageurl");
                                String category_banner    = jsonObject.getString("category_banner");
                                String parent_id          = jsonObject.getString("parent_id");

                                subDBManager.insert(new DatabaseCategoriesModel(i,categoriesId,name,imageurl,category_banner,parent_id));
                                categoryValue.add(name);
                                JSONArray jsonArray1 = jsonObject.getJSONArray("sub_categories");

                             for (int j = 0;j< jsonArray1.length(); j++){

                                  JSONObject jsonObject1  = jsonArray1.getJSONObject(j);
                                  sub_categoryid          = jsonObject1.getString("category_id");
                                  sub_imageurl            = jsonObject1.getString("imageurl");
                                  sub_parentid            = jsonObject1.getString("parent_id");
                                  sub_name                = jsonObject1.getString("name");

                                  subDBManager.insertOption(new DatabaseSubCategoriesModel(j,sub_categoryid,sub_imageurl,sub_parentid,sub_name,name));
                              }

                            }
                            databaseCategoriesModels = subDBManager.getDataCategories();
                           /* autoCategoryAdapter = new AutoCategoryAdapter(AddProductListActivity.this, databaseCategoriesModels);
                            categories.setThreshold(1);
                            categories.setAdapter(autoCategoryAdapter);*/
                      //      progressDialog.dismiss();
                            categoryValue.add(0,"Select Categories");

                            ArrayAdapter<String> categoryAdap = new ArrayAdapter<String>(AddProductListActivity.this, R.layout.spinner_text_layout, categoryValue);
                            spinner.setAdapter(categoryAdap);
                            categoryAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(categoryAdap);
                       //     spinner.setOnItemSelectedListener(this);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                   //     progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                  //  progressDialog.dismiss();
                }
            }

        }, new Response.ErrorListener() {
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
        MaintainRequestQueue.getInstance(AddProductListActivity.this).addToRequestQueue(req, "tag");

    }

    public void getCategories1(final int sellerId,final int category,final int page){

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl+Constant.BASE_URL+"customapi/getStandaloneProductBySellerId?sellerId="+sellerId;

         url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);
        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Getting Products.......");

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                       JSONArray jsonArray = new JSONArray(response);
                       addProductListingModels = new ArrayList<>();
                       if(jsonArray.length()>0) {
                           for (int i = 0; i < jsonArray.length(); i++) {

                               JSONObject jsonObject = jsonArray.getJSONObject(i);
                               sku = jsonObject.getString("sku");
                               productName = jsonObject.getString("productName");
                               productPrice = jsonObject.getString("productPrice");
                               productId = jsonObject.getString("productId");
                               productImage = jsonObject.getString("productImage");
                               JSONObject media_gallery = jsonObject.getJSONObject("media_gallery");
                               JSONArray images = media_gallery.getJSONArray("images");
                               if(images.length()>0) {
                                   for (int ii = 0; ii < images.length(); ii++) {
                                       JSONObject jsonObject1 = images.getJSONObject(ii);
                                       value_id = jsonObject1.getString("value_id");
                                       fileId   = jsonObject1.getString("file");
                                       entityId = jsonObject1.getString("entity_id");
                                   }
                                    addProductListingModels.add(new AddProductListingModel(productName, sku, productImage, productPrice, productId, fileId, value_id, entityId));

                               }
                               else {
                                    addProductListingModels.add(new AddProductListingModel(productName, sku, productImage, productPrice, productId, "", "", ""));
                               }
                           }

                           addProductListAdapter = new AddProductListAdapter(AddProductListActivity.this, addProductListingModels);
                           recyclerView.setLayoutManager(linearLayoutManager);
                           recyclerView.setAdapter(addProductListAdapter);
                           addProductListAdapter.notifyDataSetChanged();
                           progressDialog.dismiss();
                       }
                       else {
                           Toast.makeText(AddProductListActivity.this,"No products found",Toast.LENGTH_LONG).show();
                           progressDialog.dismiss();
                       }

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

        }, new Response.ErrorListener() {
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
        MaintainRequestQueue.getInstance(AddProductListActivity.this).addToRequestQueue(req, "tag");

    }


    public void getfilterCategories1(final int sellerId,final int category,final int page){

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl+Constant.BASE_URL+"customapi/getStandaloneProductBySellerId?sellerId="+sellerId + "&categoryId=" + category+ "&pageIndex=" + page;

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);
        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading Categories.......");

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    addProductListingModels = new ArrayList<>();

                    if(jsonArray.length()>0) {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            sku = jsonObject.getString("sku");
                            productName = jsonObject.getString("productName");
                            productPrice = jsonObject.getString("productPrice");
                            productId = jsonObject.getString("productId");
                            productImage = jsonObject.getString("productImage");
                            JSONObject media_gallery = jsonObject.getJSONObject("media_gallery");
                            JSONArray images = media_gallery.getJSONArray("images");

                            for (int ii = 0; ii < images.length(); ii++) {
                                JSONObject jsonObject1 = images.getJSONObject(ii);

                                value_id = jsonObject1.getString("value_id");
                                fileId = jsonObject1.getString("file");
                                entityId = jsonObject1.getString("entity_id");
                            }

                            addProductListingModels.add(new AddProductListingModel(productName, sku, productImage, productPrice, productId, fileId, value_id, entityId));

                            hideSoftKeyboard(AddProductListActivity.this);

                        }
                        addProductListAdapter = new AddProductListAdapter(AddProductListActivity.this, addProductListingModels);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(addProductListAdapter);
                        addProductListAdapter.notifyDataSetChanged();
                        hideSoftKeyboard(AddProductListActivity.this);
                        progressDialog.dismiss();

                    }
                    else {
                        Toast.makeText(AddProductListActivity.this,"No products found",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        hideSoftKeyboard(AddProductListActivity.this);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

        }, new Response.ErrorListener() {
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
        MaintainRequestQueue.getInstance(AddProductListActivity.this).addToRequestQueue(req, "tag");

    }
    public void getfiletrCategories2(final int sellerId,final int category,final int page){

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl+Constant.BASE_URL+"customapi/getStandaloneProductBySellerId?sellerId="+sellerId + "&categoryId=" + category+ "&pageIndex=" + page;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);
        //  progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading Categories.......");
        bottomLayout.setVisibility(View.VISIBLE);
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length()>0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            sku = jsonObject.getString("sku");
                            productName = jsonObject.getString("productName");
                            productPrice = jsonObject.getString("productPrice");
                            productId = jsonObject.getString("productId");
                            productImage = jsonObject.getString("productImage");
                            JSONObject media_gallery = jsonObject.getJSONObject("media_gallery");
                            JSONArray images = media_gallery.getJSONArray("images");

                            for (int ii = 0; ii < images.length(); ii++) {
                                JSONObject jsonObject1 = images.getJSONObject(ii);
                                value_id = jsonObject1.getString("value_id");
                                fileId   = jsonObject1.getString("file");
                                entityId = jsonObject1.getString("entity_id");
                            }

                            addProductListingModels.add(new AddProductListingModel(productName, sku, productImage, productPrice, productId, fileId, value_id, entityId));
                            progressDialog.dismiss();
                            hideSoftKeyboard(AddProductListActivity.this);
                        }


                        addProductListAdapter.notifyDataSetChanged();
                        loading = true;
                        bottomLayout.setVisibility(View.GONE);
                    }
                    else {
                        addProductListAdapter.notifyDataSetChanged();
                        loading = true;
                        Toast.makeText(AddProductListActivity.this,"No more products to load",Toast.LENGTH_LONG).show();
                        bottomLayout.setVisibility(View.GONE);
                    }




                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

        }, new Response.ErrorListener() {
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
        MaintainRequestQueue.getInstance(AddProductListActivity.this).addToRequestQueue(req, "tag");

    }

    public void getCategories2(final int sellerId,final int category,final int page){

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl+Constant.BASE_URL+"customapi/getStandaloneProductBySellerId?sellerId="+sellerId + "&categoryId=" + category+ "&pageIndex=" + page;

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);
      //  progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading Categories.......");
        bottomLayout.setVisibility(View.VISIBLE);
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length()>0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            sku = jsonObject.getString("sku");
                            productName = jsonObject.getString("productName");
                            productPrice = jsonObject.getString("productPrice");
                            productId = jsonObject.getString("productId");
                            productImage = jsonObject.getString("productImage");
                            JSONObject media_gallery = jsonObject.getJSONObject("media_gallery");
                            JSONArray images = media_gallery.getJSONArray("images");
                            for (int ii = 0; ii < images.length(); ii++) {
                                JSONObject jsonObject1 = images.getJSONObject(ii);
                                value_id = jsonObject1.getString("value_id");
                                fileId = jsonObject1.getString("file");
                                entityId = jsonObject1.getString("entity_id");
                            }

                            addProductListingModels.add(new AddProductListingModel(productName, sku, productImage, productPrice, productId, fileId, value_id, entityId));
                            progressDialog.dismiss();
                            hideSoftKeyboard(AddProductListActivity.this);
                        }


                        addProductListAdapter.notifyDataSetChanged();
                        loading = true;
                        bottomLayout.setVisibility(View.GONE);
                    }
                    else {
                        addProductListAdapter.notifyDataSetChanged();
                        loading = true;
                        Toast.makeText(AddProductListActivity.this,"No more products to load",Toast.LENGTH_LONG).show();
                        bottomLayout.setVisibility(View.GONE);
                    }




                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

        }, new Response.ErrorListener() {
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
        MaintainRequestQueue.getInstance(AddProductListActivity.this).addToRequestQueue(req, "tag");

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    public void onItemClick(View v, int position) {
        addProductListingModel = addProductListAdapter.getWordAtPosition(position);
        Log.d("product...",addProductListingModel.getProductId());
        Intent intent = new Intent(AddProductListActivity.this,EditImageProductDetailActivity.class);
        intent.putExtra("product_id",addProductListingModel.getProductId());
        intent.putExtra("product_name",addProductListingModel.getProductname());
        intent.putExtra("product_price",addProductListingModel.getPrice());
        intent.putExtra("product_sku",addProductListingModel.getSku());
        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
     //   Toast.makeText(this, " You select >> "+databaseCategoriesModels.get(i).getCategory_id(), Toast.LENGTH_SHORT).show();
        subcategoryValue.clear();
        subcategoryValue.add(0,"Select Sub categories");
        if(adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Categories")){
            sharedPreferences.edit().putString("cat_id", "null").apply();
            sub_spinner.setVisibility(View.GONE);
        }
        else {
            sub_spinner.setVisibility(View.VISIBLE);
            databaseSubCategoriesModels = subDBManager.getSubCategories(Integer.parseInt(databaseCategoriesModels.get(i-1).getCategory_id()));
            sharedPreferences.edit().putString("cat_id", databaseCategoriesModels.get(i-1).getCategory_id()).apply();
            for (int j = 0; j < databaseSubCategoriesModels.size(); j++) {
                subcategoryValue.add(databaseSubCategoriesModels.get(j).getSub_name());
            }
            ArrayAdapter<String> categoryAdap = new ArrayAdapter<String>(AddProductListActivity.this, R.layout.spinner_text_layout, subcategoryValue);
            sub_spinner.setAdapter(categoryAdap);
            categoryAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sub_spinner.setAdapter(categoryAdap);

        }
        hideSoftKeyboard(AddProductListActivity.this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        hideSoftKeyboard(AddProductListActivity.this);
    }

    @Override
    public void onClick(View view) {
        if(sharedPreferences.getString("cat_id",null).equalsIgnoreCase("null") ||
                sharedPreferences.getString("cat_id",null) == null ){
            Toast.makeText(AddProductListActivity.this,"Select categories first",Toast.LENGTH_SHORT).show();
        }
        else if(sharedPreferences.getString("sub_cat_id",null).equalsIgnoreCase("null")){
            page = 1;
            getfilterCategories1(sellerId,Integer.parseInt(sharedPreferences.getString("cat_id", null)), page);
        }
        else {
            page = 1;
            getfilterCategories1(sellerId,Integer.parseInt(sharedPreferences.getString("sub_cat_id", null)), page);
        }
    }


}
