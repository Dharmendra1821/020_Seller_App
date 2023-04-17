package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ComplaintsAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SubCategoryAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ComplaintsModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Items;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SubCategories;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SubCategoryModel;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class SellerSubCategoriesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SubCategoryAdapter adapter;
    ImageView subCategorysearchButton,subCategorycancelButton;
    EditText subCategorysearchEditText;
    ProgressDialog progressDialog;
    SubCategories subcategoriesList = null;
    int CustomerId,CustomerAddressId;
    CardView noSubCategoriesCardView;
    String displayCategories = null;
    SubCategoryModel items;
    List<SubCategoryModel> subCategoryModels;
    SharedPreferences sharedPreferences;
    int sellerId;
    int CategoryId= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_sub_categories);
        progressDialog = new ProgressDialog(SellerSubCategoriesActivity.this);
        recyclerView = findViewById(R.id.subCategoryListRecyclerView);
        subCategorysearchEditText = findViewById(R.id.subCategorysearchEditText);
        noSubCategoriesCardView = findViewById(R.id.noSubCategoriesCardView);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        LoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<LoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getCustomerDetails() != null ? loginResponse.getCustomerDetails().getId() : 0;

        //getSubCategories();
        getSubCat(String.valueOf(sellerId));
        if (getCurrentFocus() !=null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService((INPUT_METHOD_SERVICE));
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        ArrayList<String> listdata = null;


        subCategorysearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
            }
        });

        if (getIntent().getExtras() ==null)
        {
            CategoryId= sharedPreferences.getInt("CategoryId",0);
            CustomerId= sharedPreferences.getInt("CustomerId",0);
            CustomerAddressId= sharedPreferences.getInt("CustomerAddressId",0);
        }
        else
        {
            CategoryId= getIntent().getExtras().getInt("CategoryId");
            CustomerId= getIntent().getExtras().getInt("CustomerId");
            CustomerAddressId= getIntent().getExtras().getInt("CustomerAddressId");
            sharedPreferences.edit().putInt("CategoryId",CategoryId).apply();
            sharedPreferences.edit().putInt("CustomerId",getIntent().getExtras().getInt("CustomerId")).apply();
            sharedPreferences.edit().putInt("CustomerAddressId",getIntent().getExtras().getInt("CustomerAddressId")).apply();
        }

        adapter = new SubCategoryAdapter();
        adapter.setOnItemClickListener(new SubCategoryAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                items = adapter.getWordAtPosition(position);
                Intent intent = new Intent(SellerSubCategoriesActivity.this, ProductListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("SubCategoryId",Integer.parseInt(items.getCategoryId()));
                bundle.putInt("CategoryId",Integer.parseInt(items.getParent_id()));
                bundle.putInt("CustomerId",CustomerId);
                bundle.putInt("CustomerAddressId",CustomerAddressId);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

         public void getSubCategories(){
              sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
              displayCategories = sharedPreferences.getString("displayCategories",null);
              final String []displayCategoryArray = new Gson().fromJson(displayCategories,new TypeToken<String[]>(){}.getType());

              Log.d("DisplayCategories","DisplayCategories"+ Arrays.toString(displayCategoryArray));

              ArrayList<String> listdata = null;
              int CategoryId= 0;

              subCategorysearchEditText.addTextChangedListener(new TextWatcher() {
                 @Override
                 public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 }
                 @Override
                 public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                     adapter.getFilter().filter(charSequence);
                 }
                 @Override
                 public void afterTextChanged(Editable editable) {
                     //after the change calling the method and passing the search input
                 }
             });

             if (getIntent().getExtras() ==null)
             {
                 CategoryId= sharedPreferences.getInt("CategoryId",0);
                 CustomerId= sharedPreferences.getInt("CustomerId",0);
                 CustomerAddressId= sharedPreferences.getInt("CustomerAddressId",0);
             }
             else
             {
                 CategoryId= getIntent().getExtras().getInt("CategoryId");
                 CustomerId= getIntent().getExtras().getInt("CustomerId");
                 CustomerAddressId= getIntent().getExtras().getInt("CustomerAddressId");
                 sharedPreferences.edit().putInt("CategoryId",CategoryId).apply();
                 sharedPreferences.edit().putInt("CustomerId",getIntent().getExtras().getInt("CustomerId")).apply();
                 sharedPreferences.edit().putInt("CustomerAddressId",getIntent().getExtras().getInt("CustomerAddressId")).apply();
             }

             progressDialog = new ProgressDialog(SellerSubCategoriesActivity.this);
             progressDialog.setMessage("Loading.....");
             progressDialog.show();


             sharedPreferences  = getSharedPreferences("StandAlone",MODE_PRIVATE);
             String shopUrl     = sharedPreferences.getString("shop_url",null);
             MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
             Retrofit retrofit  = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
             MPOSServices services = retrofit.create(MPOSServices.class);
             String adminToken  = "Bearer "+ sharedPreferences.getString("adminToken",null);

             services.getSellerSubcategories(CategoryId,adminToken).enqueue(new Callback<SubCategories>() {
                 @Override
                 public void onResponse(Call<SubCategories> call, Response<SubCategories> response) {
                     Log.d("url...", String.valueOf(call.request().url()));
                     if(response.code() == 200 ||response.isSuccessful()){
                         progressDialog.dismiss();
                         List<Items> newSubCategoryList = new ArrayList<>();
                         ArrayList<Items> newArrayList = new ArrayList<>();
                         subcategoriesList = response.body();

                         Gson gson = new Gson();
                         String productGson = gson.toJson(response.body());
                         Log.d("Product JSON","Product Json == "+productGson);
                         if(subcategoriesList.getItems().size() > 0){
                             recyclerView.setVisibility(View.VISIBLE);
                             noSubCategoriesCardView.setVisibility(View.GONE);
                             newSubCategoryList = subcategoriesList.getItems();
//
//                             for(int j = 0; j < newSubCategoryList.size(); j++){
//                                 int catId = subcategoriesList.getItems().get(j).getId();
//                                 for(int i = 0 ; i < displayCategoryArray.length ; i++){
//                                     Integer d = Integer.parseInt(displayCategoryArray[i]);
//                                     if(d == catId){
//                                         newArrayList.add(subcategoriesList.getItems().get(j));
//                                     }
//                                  }
//                                 }
//                             recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
//                             adapter = new SubCategoryAdapter(getApplicationContext(),newSubCategoryList,CustomerId,CustomerAddressId);
//                             recyclerView.setAdapter(adapter);
//                             adapter.notifyDataSetChanged();
                         }else {
                             recyclerView.setVisibility(View.GONE);
                             noSubCategoriesCardView.setVisibility(View.VISIBLE);
                         }
                     }else {
                         progressDialog.dismiss();
                         Toast.makeText(SellerSubCategoriesActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                     }
                 }
                 @Override
                 public void onFailure(Call<SubCategories> call, Throwable t) {
                     progressDialog.dismiss();
                     Toast.makeText(SellerSubCategoriesActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                 }
             });
         }

    public void getSubCat(final String sellerId){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
       // String url = "https://"+shopUrl + SKU_URL+"marketplace/getStanaloneSellerCategory?sellerId="+sellerId;
        String url = Constant.REVAMP_URL1+"customapi/getStanaloneSellerCategory.php?sellerId=74";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading data.......");
        progressDialog.setCancelable(false);

        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {
                    subCategoryModels = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length() > 0){
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String catId = jsonObject.getString("category_id");
                            Log.d("categoryId", String.valueOf(CategoryId));
                          //  if(catId.equalsIgnoreCase(String.valueOf(CategoryId))){
                                JSONArray subArray = jsonObject.getJSONArray("sub_categories");
                                if(subArray.length() > 0){
                                    for(int j = 0; j < subArray.length(); j++){
                                        JSONObject jsonObject1 = subArray.getJSONObject(j);
                                        String categoryId  = jsonObject1.getString("category_id");
                                        String name        = jsonObject1.getString("name");
                                        String imageurl    = jsonObject1.getString("imageurl");
                                        String parent_id   = jsonObject1.getString("parent_id");


                                        subCategoryModels.add(new SubCategoryModel(categoryId,name,imageurl,parent_id));
                                    }
                                }
                            }
                       // }

                             recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                             adapter = new SubCategoryAdapter(getApplicationContext(),subCategoryModels);
                             recyclerView.setAdapter(adapter);
                             adapter.notifyDataSetChanged();
                    }




                    progressDialog.dismiss();
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
        MaintainRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(req, "tag");

    }

}
