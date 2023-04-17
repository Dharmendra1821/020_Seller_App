package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.hydromatic.linq4j.Linq4j;
import net.hydromatic.linq4j.function.Predicate1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DetectSwipeDirectionActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.RefreshToken;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.Request;
import standalone.eduqfix.qfixinfo.com.app.customer.activity.CustomerTabActivity;
import standalone.eduqfix.qfixinfo.com.app.customer.model.Addresses;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.AddProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.login.activity.LoginActivity;
import standalone.eduqfix.qfixinfo.com.app.login.activity.WelcomeActivity;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.CategoryAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Address;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Categories;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigDetails;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.EstimateShippingCharge;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.EstimateShippingResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

public class SellerDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CategoryAdapter.ClickListener {

    RecyclerView recyclerView;
    CategoryAdapter adapter;
    ProgressDialog progressDialog;
    List<Categories> categoriesList = new ArrayList<>();
    int CustomerId,CustomerAddressId;
    TextView loggedInUsernameTextView,emailTextView;
    CardView noCategoriesCardView;
    SharedPreferences sharedPreferences;
    EditText searchEdittext;
    ImageButton cancelButton;
    Categories categories;
    List<EstimateShippingResponse> estimateShippingResponses;
    EstimateShippingResponse estimateShippingResponse;
    int textlength = 0;
    public static ArrayList<Categories> array_sort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        emailTextView = view.findViewById(R.id.emailTextView);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        recyclerView = findViewById(R.id.categoryListRecyclerView);
        loggedInUsernameTextView = view.findViewById(R.id.loggedInUsernameTextView);

        noCategoriesCardView = findViewById(R.id.noCategoriesCardView);
        searchEdittext = findViewById(R.id.searchEditText);


        //getCartItemCount();

       // estimateShippingCharges();
        //getCustomerToken();
        getCategories();
        array_sort = new ArrayList<>();


        adapter = new CategoryAdapter();
        adapter.setOnItemClickListener(this);

        searchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                textlength = searchEdittext.getText().length();
                array_sort.clear();
                for (int j = 0; j < categoriesList.size(); j++) {
                    if (textlength <= categoriesList.get(j).getName().length()) {
                        if (categoriesList.get(j).getName().toLowerCase().trim().contains(
                                searchEdittext.getText().toString().toLowerCase().trim())) {
                                array_sort.add(categoriesList.get(j));
                        }
                    }
                }
                adapter = new CategoryAdapter(SellerDashboardActivity.this, array_sort);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ArrayList<String> listdata = null;

        navigationView.setNavigationItemSelectedListener(this);
        if (getCurrentFocus() !=null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService((INPUT_METHOD_SERVICE));
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = SellerDashboardActivity.this.getAssets().open("categories.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.d("JSON Array ===","JSON Array"+json);
        return json;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.seller_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(SellerDashboardActivity.this, MainDashboardActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_add_product){
            Intent addintent = new Intent(SellerDashboardActivity.this, DetectSwipeDirectionActivity.class);
            addintent.putExtra("position",0);
            startActivity(addintent);
            finish();
        }
        else if (id==R.id.nav_place_order){
            Intent intent = new Intent(SellerDashboardActivity.this, CustomerTabActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.nav_logout){
            sharedPreferences.edit().putString("CustomerDetails","").apply();
           // sharedPreferences.edit().putString("CustomerDetails",null).apply();
           // sharedPreferences.edit().clear().apply();
            Intent intent = new Intent(SellerDashboardActivity.this,EmailActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
   /* public void estimateShippingCharges(){
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String adminToken = sharedPreferences.getString("adminToken",null);
        String custToken =  sharedPreferences.getString("customerToken",null);


        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
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
                      sharedPreferences.edit().putString("shipping_rate_service", String.valueOf(amount)).apply();
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
    }*/



    public void getCategories(){
        progressDialog = new ProgressDialog(SellerDashboardActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String customerDetails = sharedPreferences.getString("CustomerDetails",null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails,new TypeToken<NewLoginResponse>(){}.getType());
        String username = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getEmail() : "NA";
        String firstName = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getFirstName(): "NA";
        String lastName = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getLastName(): "NA";
        emailTextView.setText(username);
        loggedInUsernameTextView.setText(String.format("%s %s", firstName, lastName));
        if (getIntent().getExtras()!=null)
        {
            CustomerAddressId = getIntent().getExtras().getInt("CustomerAddressId");
            CustomerId = getIntent().getExtras().getInt("CustomerId");
        }
        else
        {
            CustomerId = sharedPreferences.getInt("CustomerId",0);
            CustomerAddressId = sharedPreferences.getInt("CustomerAddressId",0);
        }

        Integer sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices services = retrofit.create(MPOSServices.class);
        Log.d("seller id...",String.valueOf(sellerId));

        String adminToken = sharedPreferences.getString("adminToken",null);

        if(sellerId != 0){
            services.getSellercategories(sellerId).enqueue(new Callback<List<Categories>>() {
                @Override
                public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
                    Log.d("url...", String.valueOf(call.request().url()));
                    if(response.code() == 200 ||response.isSuccessful()){
                        progressDialog.dismiss();
                    //    categoriesList = response.body();
                        Gson gson = new Gson();
                        String gsonData = gson.toJson(response.body());
                        Log.d("GSONData=====","GSONData====="+gsonData);

                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(gsonData);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String parentId = jsonObject.getString("parent_id");
                              //  if(parentId.equalsIgnoreCase("2")){

                                    String categoryBanner = jsonObject.optString("category_banner");
                                    int categoryId = jsonObject.getInt("category_id");
                                    String imageUrl = jsonObject.optString("imageurl");
                                    String name = jsonObject.getString("name");

                                     categories = new Categories();
                                     categories.setCategory_banner(categoryBanner);
                                     categories.setCategory_id(categoryId);
                                     categories.setImageurl(imageUrl);
                                     categories.setName(name);
                                     categories.setParent_id(parentId);
                                     categoriesList.add(categories);
                               // }
                            }
                            if(categoriesList != null && categoriesList.size() > 0){
                                noCategoriesCardView.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setHasFixedSize(true);

                                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SellerDashboardActivity.this, 2);
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                                adapter = new CategoryAdapter(getApplicationContext(),categoriesList,CustomerId,CustomerAddressId);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                            else {
                                recyclerView.setVisibility(View.GONE);
                                noCategoriesCardView.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                      /*  for(int i=0;i<categoriesList.size();i++){
                            if(categoriesList.get(i).getParent_id().equalsIgnoreCase("2")){
                                Categories categories = new Categories();
                                categories.setCategory_banner(categoriesList.get(i).getCategory_banner());
                                categories.setCategory_id(categoriesList.get(i).getCategory_id());
                                categories.setImageurl(categoriesList.get(i).getImageurl());
                                categories.setName(categoriesList.get(i).getName());
                                categories.setParent_id(categoriesList.get(i).getParent_id());
                                categoriesList.add(categories);
                            }
                        }*/

                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(SellerDashboardActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Categories>> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(SellerDashboardActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            });
        }else {
            progressDialog.dismiss();
            Toast.makeText(SellerDashboardActivity.this,"Unable to find seller",Toast.LENGTH_LONG).show();
        }
    }

   /* @Override
    public void onClick(View view) {
        searchEdittext.setText("");
      //  getCategories();
    }*/

    @Override
    public void onItemClick(View v, int position) {

        categories = adapter.getWordAtPosition(position);
        if(categories.getCategory_banner()==null){
            Toast.makeText(SellerDashboardActivity.this,"Sorry Sub categories not available!",Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(SellerDashboardActivity.this, ProductListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("CategoryId", categories.getCategory_id());
            bundle.putInt("CustomerId", CustomerId);
            bundle.putInt("CustomerAddressId", CustomerAddressId);
            intent.putExtras(bundle);
         //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

//            progressDialog = new ProgressDialog(SellerDashboardActivity.this);
//            progressDialog.setMessage("Loading...");
//            progressDialog.show();
//
//            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
//            String shopUrl  = sharedPreferences.getString("shop_url",null);
//            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
//            Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
//            MPOSServices mposServices = retrofit.create(MPOSServices.class);
//            final SharedPreferences sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
//            String token = "Bearer " + sharedPreferences.getString("customerToken", null);
//            Log.d("admin...........",token);
//            mposServices.getDisplayCategories(2, 2, categories.getCategory_id(), token).enqueue(new Callback<String[]>() {
//                @Override
//                public void onResponse(@NonNull Call<String[]> call, @NonNull Response<String[]> response) {
//                    if (response.code() == 200 || response.isSuccessful()) {
//                        String[] stringCategories = response.body();
//                        Log.d("String.....", Arrays.toString(response.body()));
//
//                        Gson gson = new Gson();
//                        String gsonData = gson.toJson(response.body());
//                        Log.d("GSONData=====", "GSONData=====" + gsonData);
//
//                        sharedPreferences.edit().putString("displayCategories", Arrays.toString(stringCategories)).apply();
//                        progressDialog.dismiss();
//                        Intent intent = new Intent(SellerDashboardActivity.this, SellerSubCategoriesActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("CategoryId", categories.getCategory_id());
//                        bundle.putInt("CustomerId", CustomerId);
//                        bundle.putInt("CustomerAddressId", CustomerAddressId);
//                        intent.putExtras(bundle);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    } else if (response.code() == 400) {
//                        progressDialog.dismiss();
//                        Toast.makeText(SellerDashboardActivity.this, "Bad Request", Toast.LENGTH_LONG).show();
//                    } else if (response.code() == 401) {
//                        progressDialog.dismiss();
//                        Toast.makeText(SellerDashboardActivity.this, "Unauthorised", Toast.LENGTH_LONG).show();
//                    } else if (response.code() == 403) {
//                        progressDialog.dismiss();
//                        Toast.makeText(SellerDashboardActivity.this, "Forbidden", Toast.LENGTH_LONG).show();
//                    } else if (response.code() == 500) {
//                        progressDialog.dismiss();
//                        Toast.makeText(SellerDashboardActivity.this, "Internal Server Error", Toast.LENGTH_LONG).show();
//                    } else {
//                        progressDialog.dismiss();
//                        Toast.makeText(SellerDashboardActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<String[]> call, @NonNull Throwable t) {
//                    progressDialog.dismiss();
//                    Toast.makeText(SellerDashboardActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
//                }
//            });
//        }

    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<RefreshToken> call, Throwable t) {
                //progressDialog.hide();
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });

    }
}
