package standalone.eduqfix.qfixinfo.com.app.add_new_product.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.database.SubDBManager;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.AddProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.DatabaseCategoriesModel;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.DatabaseSubCategoriesModel;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.ProductDetails;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductSKU;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SKURequest;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static android.content.Context.MODE_PRIVATE;

public class BasicInfoFragment extends Fragment {
    public static View fragment;
    SearchableSpinner category,sub_category,product_stock_avail_new,product_tax_new;
    EditText p_name,p_description,p_sku,p_price,p_stock,p_availablity,p_weight;
    SharedPreferences sharedPreferences;
    SubDBManager subDBManager;
    int sellerId;
    ArrayList<DatabaseCategoriesModel> databaseCategoriesModels;
    ArrayList<DatabaseSubCategoriesModel> databaseSubCategoriesModels;
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
    int mCurCheckPosition;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            fragment = inflater.inflate(R.layout.basic_info_fragment, container, false);
            category = fragment.findViewById(R.id.add_new_product_category);
            sub_category = fragment.findViewById(R.id.add_new_product_sub_category);
            p_name = fragment.findViewById(R.id.product_name_new);
            p_description = fragment.findViewById(R.id.product_description_new);
            p_sku = fragment.findViewById(R.id.product_sku_new);
            p_price = fragment.findViewById(R.id.product_price_new);
            p_stock = fragment.findViewById(R.id.product_stock_new);
            product_stock_avail_new = fragment.findViewById(R.id.product_stock_avail_new);
            sku_exits = fragment.findViewById(R.id.sku_exist);
            product_tax_new = fragment.findViewById(R.id.product_tax_new);

            progressDialog = new ProgressDialog(getActivity());


            try {
                sharedPreferences = getActivity().getSharedPreferences("StandAlone", MODE_PRIVATE);
                categoryValue = new ArrayList<>();
                subcategoryValue = new ArrayList<>();
                stockvalue = new ArrayList<>();
                taxvalue = new ArrayList<>();

                String customerDetails = sharedPreferences.getString("CustomerDetails", null);
                LoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<LoginResponse>() {
                }.getType());
                sellerId = loginResponse != null && loginResponse.getCustomerDetails() != null ? loginResponse.getCustomerDetails().getId() : 0;

                Log.d("SellerId....", String.valueOf(sellerId));

                subDBManager = new SubDBManager(getActivity());
                subDBManager.open();

                int flag = getArguments().getInt("flag",0);
               /* if(flag==0) {
                    getCategories(sellerId);
                }
                else {
                    getCategories();
                }*/

                category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        subcategoryValue.clear();
                        subcategoryValue.add(0,"Select Sub categories");
                        if(adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Categories")){
                            sharedPreferences.edit().putString("cat_id", "null").apply();
                            sub_category.setVisibility(View.GONE);
                        }
                        else {
                            sub_category.setVisibility(View.VISIBLE);
                            databaseSubCategoriesModels = subDBManager.getSubCategories(Integer.parseInt(databaseCategoriesModels.get(i-1).getCategory_id()));
                            sharedPreferences.edit().putString("cat_id", databaseCategoriesModels.get(i-1).getCategory_id()).apply();
                            for (int j = 0; j < databaseSubCategoriesModels.size(); j++) {
                                subcategoryValue.add(databaseSubCategoriesModels.get(j).getSub_name());
                            }
                            ArrayAdapter<String> categoryAdap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text_layout, subcategoryValue);
                            sub_category.setAdapter(categoryAdap);
                            categoryAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sub_category.setAdapter(categoryAdap);

                        }
                        //     hideSoftKeyboard(getActivity());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //        hideSoftKeyboard(getActivity());
                    }
                });

            }catch (Exception e){

            }

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

            spinnervalue = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text_layout, stockvalue);
            product_stock_avail_new.setAdapter(spinnervalue);
            spinnervalue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            product_stock_avail_new.setAdapter(spinnervalue);


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

            spinnerTaxValue = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text_layout, taxvalue);
            product_tax_new.setAdapter(spinnerTaxValue);
            spinnerTaxValue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            product_tax_new.setAdapter(spinnerTaxValue);


        return fragment;

    }
    /*public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }*/
  /*  public void getCategories(final int sellerId){

        sharedPreferences = getActivity().getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl+ Constant.BASE_URL+"customapi/getStanaloneSellerCategory?sellerId="+sellerId;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);
           progressDialog.show();
           progressDialog.setTitle("Please wait...");
          progressDialog.setMessage("Loading Categories.......");

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response);
                        sharedPreferences.edit().putString("category_response",response).apply();

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

                                    subDBManager.insertOption(new DatabaseSubCategoriesModel(j,sub_categoryid,sub_imageurl,sub_parentid,sub_name));
                                }

                            }
                            databaseCategoriesModels = subDBManager.getDataCategories();
                            categoryValue.add(0,"Select Categories");

                            ArrayAdapter<String> categoryAdap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text_layout, categoryValue);
                            category.setAdapter(categoryAdap);
                            categoryAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            category.setAdapter(categoryAdap);
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
*//*
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("state_id", String.valueOf(stateid));
                return checkParams(params);
            }*//*

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
        MaintainRequestQueue.getInstance(getActivity()).addToRequestQueue(req, "tag");

    }*/

  /*  public void getCategories(){
        String response  = sharedPreferences.getString("category_response",null);

        try {
           JSONArray jsonArray = new JSONArray(response);
            sharedPreferences.edit().putString("category_response",response).apply();

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

                        subDBManager.insertOption(new DatabaseSubCategoriesModel(j,sub_categoryid,sub_imageurl,sub_parentid,sub_name));
                    }

                }
                databaseCategoriesModels = subDBManager.getDataCategories();
                categoryValue.add(0,"Select Categories");

                ArrayAdapter<String> categoryAdap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text_layout, categoryValue);
                category.setAdapter(categoryAdap);
                categoryAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                category.setAdapter(categoryAdap);
                //     spinner.setOnItemSelectedListener(this);


            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }*/

    public void checkSku(final String skuValue){
        sharedPreferences = getActivity().getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
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
        call.enqueue(new Callback<Boolean>() {
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

                    Toast.makeText(getActivity(),"Bad Request",Toast.LENGTH_LONG).show();
                }else if(response.code() == 401){

                    Toast.makeText(getActivity(),"Unauthorised",Toast.LENGTH_LONG).show();
                }else if(response.code() == 500){

                    Toast.makeText(getActivity(),"Internal server error",Toast.LENGTH_LONG).show();
                }else {

                    Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getActivity(),"FAILED",Toast.LENGTH_LONG).show();

            }
        });
    }


    }
