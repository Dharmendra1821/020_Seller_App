package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.ExpListViewAdapterWithCheckbox;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.GroupModel;
import standalone.eduqfix.qfixinfo.com.app.customer.activity.AddCustomerAddressActivity;
import standalone.eduqfix.qfixinfo.com.app.customer.model.State;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.DatabaseCategoriesModel;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.DatabaseSubCategoriesModel;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class ExpandableListviewActivity<categoriesname> extends Activity {

    ProgressDialog progressDialog;
    ExpandableListView expandableListView;
    SharedPreferences sharedPreferences;
    List<String> listDataHeader;
    HashMap<String, List<DatabaseSubCategoriesModel>> listDataChild;
    HashMap<String, List<DatabaseCategoriesModel>> listDataGroup;
    String sub_categoryid;
    String sub_imageurl;
    String sub_parentid;
    String sub_name;
    String categoriesname;
    ExpListViewAdapterWithCheckbox expListViewAdapterWithCheckbox;
    int sellerId;
    Button product_category_submit;
    SearchableSpinner categoryLevel1,categoryLevel2;
    List<DatabaseCategoriesModel> databaseCategoriesModels = null;
    ArrayList<DatabaseSubCategoriesModel> databaseSubCategoriesModels;
    List<String> categoryNameList;
    List<String> subcategoryNameList;
    String subCategoryId;
    String parentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_expandable_listview);


        progressDialog = new ProgressDialog(ExpandableListviewActivity.this);
        expandableListView = findViewById(R.id.product_expandablelistview);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        categoryLevel1 = findViewById(R.id.category_level_1);
        categoryLevel2 = findViewById(R.id.category_level_2);



        product_category_submit = findViewById(R.id.product_category_submit);
        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails().getSeller_id() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String,List<DatabaseSubCategoriesModel>>();
        listDataGroup = new HashMap<String,List<DatabaseCategoriesModel>>();

        String size = getIntent().getStringExtra("size");
        if(size.equalsIgnoreCase("0")){
            DetectSwipeDirectionActivity.getCategoryIds().clear();
            DetectSwipeDirectionActivity.getCategoryName().clear();
        }



        categoryLevel1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    ArrayAdapter<String> stateAdapter2= new ArrayAdapter<String>(ExpandableListviewActivity.this,android.R.layout.simple_list_item_1, subcategoryNameList);
                    stateAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categoryLevel2.setAdapter(stateAdapter2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        categoryLevel2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        product_category_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("cat_name",ExpListViewAdapterWithCheckbox.getCategoryName().toString());
                Log.d("cat_id",ExpListViewAdapterWithCheckbox.getCategory().toString());
                if(parentId.equalsIgnoreCase("") || subCategoryId.equalsIgnoreCase("")){

//                    HashSet<String> hashSet = new HashSet<String>();
//                    hashSet.addAll(ExpListViewAdapterWithCheckbox.getCategoryName());
//                    ExpListViewAdapterWithCheckbox.getCategoryName().clear();
//                    ExpListViewAdapterWithCheckbox.getCategoryName().addAll(hashSet);
//
//                    HashSet<String> hashSet2 = new HashSet<String>();
//                    hashSet2.addAll(ExpListViewAdapterWithCheckbox.getCategory());
//                    ExpListViewAdapterWithCheckbox.getCategory().clear();
//                    ExpListViewAdapterWithCheckbox.getCategory().addAll(hashSet2);
//
//                    String categoryName = Arrays.toString(new String[]{ExpListViewAdapterWithCheckbox.getCategoryName().toString()});
//                    String categoryIde = Arrays.toString(new String[]{ExpListViewAdapterWithCheckbox.getCategory().toString()});
//
//                    String removeCurrency = categoryName.substring(2);
//                    String newCategoryName = removeCurrency.substring(0,removeCurrency.length() - 2);
                    Intent intent = new Intent("products-message");
                    intent.putExtra("categoryLevel1",parentId);
                    intent.putExtra("categoryLevel2",subCategoryId);
                    LocalBroadcastManager.getInstance(ExpandableListviewActivity.this).sendBroadcast(intent);
                    finish();
                }
                else {
                    Toast.makeText(ExpandableListviewActivity.this,"Select Category Levels",Toast.LENGTH_LONG).show();
                }

            }
        });


        getCategoriesNew(sellerId);
    }


    public void getCategoriesNew(final int sellerId){


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

                                Log.d("cat .............",categoriesname);
                                databaseCategoriesModels.add(new DatabaseCategoriesModel(Integer.parseInt(categoriesId),categoriesId,categoriesname,imageurl,category_banner,parent_id));

                              //  for(DatabaseCategoriesModel databaseCategoriesModel : databaseCategoriesModels){
                                //    categoryNameList.add(databaseCategoriesModels.add(categoriesname));
                               // }


                      //          databaseCategoriesModels.add(new DatabaseCategoriesModel(i,categoriesId,categoriesname,imageurl,category_banner,parent_id));
                                listDataHeader.add(categoriesname+":"+categoriesId);


                                JSONArray jsonArray1 = jsonObject.getJSONArray("sub_categories");


                                for (int j = 0;j< jsonArray1.length(); j++){

                                    JSONObject jsonObject1  = jsonArray1.getJSONObject(j);
                                    sub_categoryid          = jsonObject1.getString("category_id");
                                    sub_imageurl            = jsonObject1.getString("imageurl");
                                    sub_parentid            = jsonObject1.getString("parent_id");
                                    sub_name                = jsonObject1.getString("name");

                                    databaseSubCategoriesModels.add(new DatabaseSubCategoriesModel(j,sub_categoryid,sub_imageurl,sub_parentid,sub_name,categoriesname));

                                }




                                listDataChild.put(listDataHeader.get(i), databaseSubCategoriesModels);
                                expListViewAdapterWithCheckbox = new ExpListViewAdapterWithCheckbox(getApplicationContext(), listDataHeader, listDataChild);
                                expandableListView.setAdapter(expListViewAdapterWithCheckbox);
                            }
                            progressDialog.dismiss();
                            for(DatabaseCategoriesModel state : databaseCategoriesModels){
                                categoryNameList.add(state.getName());
                            }

                            ArrayAdapter<String> stateAdapter= new ArrayAdapter<String>(ExpandableListviewActivity.this,android.R.layout.simple_list_item_1, categoryNameList);
                            stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            categoryLevel1.setAdapter(stateAdapter);
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
        MaintainRequestQueue.getInstance(ExpandableListviewActivity.this).addToRequestQueue(req, "tag");

    }

}
