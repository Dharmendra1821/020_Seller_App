package standalone.eduqfix.qfixinfo.com.app.image_add_product.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class EditImageProductDetailActivity extends AppCompatActivity implements ProductImageAdapter.ClickListener{

    ImageView imageView1,imageView2,imageView3;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    ImageProductModel imageProductModel;
    ArrayList<ImageProductModel> imageProductModels;
    ProductImageAdapter productImageAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    TextView name_txt,price_txt,sku_txt;
    LinearLayout edit_layout;
    String productId;
    String productName;
    String productPrice;
    String productSku;
    int sellerId;
    int PERMISSION_ALL = 1;
    boolean flagPermissions = false;
    private ActionBar toolbar;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image_product_detail);

        try {
             progressDialog     = new ProgressDialog(EditImageProductDetailActivity.this);

             productId          = getIntent().getStringExtra("product_id");
             productName        = getIntent().getStringExtra("product_name");
             productPrice       = getIntent().getStringExtra("product_price");
             productSku         = getIntent().getStringExtra("product_sku");
             sharedPreferences  = getSharedPreferences("StandAlone",MODE_PRIVATE);

            recyclerView = findViewById(R.id.image_recyclerview);
            edit_layout  = findViewById(R.id.image_edit_layout);
            name_txt     = findViewById(R.id.edit_product_name);
            price_txt    = findViewById(R.id.edit_price_detail);
            sku_txt      = findViewById(R.id.edit_sku_detail);

            toolbar = getSupportActionBar();
            toolbar.setTitle("Product Images");

            try {
                if (productPrice.equalsIgnoreCase("null")) {
                    price_txt.setText("0");
                } else if (productPrice.equalsIgnoreCase("0")) {
                    price_txt.setText("0");
                } else {
                    StringTokenizer tokens = new StringTokenizer(productPrice, ".");
                    String first = tokens.nextToken();
                    String second = tokens.nextToken();
                    Log.d("....", first);
                    Log.d("....", second);
                    price_txt.setText(first);
                }
            }catch (Exception e){

            }
            name_txt.setText(productName);
            sku_txt.setText(productSku);



            sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
            String customerDetails = sharedPreferences.getString("CustomerDetails", null);
            LoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<LoginResponse>() {
            }.getType());
            sellerId = loginResponse != null && loginResponse.getCustomerDetails() != null ? loginResponse.getCustomerDetails().getId() : 0;

            getProductImage(Integer.parseInt(productId));


            productImageAdapter = new ProductImageAdapter();
            productImageAdapter.setOnItemClickListener(this);

        }catch (Exception e){

        }
    }

    public Bitmap StringToBitMap1(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    public Bitmap StringToBitMap2(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    public Bitmap StringToBitMap3(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void getProductImage(final int productId){
      //  String url = "https://shoppingtest.eduqfix.com/index.php/rest/V1/customapi/getStandaloneProductBySellerId?sellerId=" + sellerId + "&productId=" + productId ;

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl+Constant.BASE_URL+"customapi/getStandaloneProductImages?productId="+productId ;

     //   String url = "https://shoppingtest.eduqfix.com/index.php/rest/V1/customapi/getStandaloneProductImages?productId="+productId;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading product images.......");

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    imageProductModels = new ArrayList<>();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                if (jsonArray.length() >= 3) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String valueId = jsonObject1.getString("value_id");
                                    String file = jsonObject1.getString("file");
                                    String entityId = jsonObject1.getString("entity_id");

                                    imageProductModels.add(new ImageProductModel(valueId, file, entityId, "3"));
                                } else if (jsonArray.length() == 2) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String valueId = jsonObject1.getString("value_id");
                                    String file = jsonObject1.getString("file");
                                    String entityId = jsonObject1.getString("entity_id");

                                    imageProductModels.add(new ImageProductModel(valueId, file, entityId, "2"));
                                    if (i == 1) {
                                        imageProductModels.add(2, new ImageProductModel("", "", "", "2"));
                                    }
                                } else if (jsonArray.length() == 1) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String valueId = jsonObject1.getString("value_id");
                                    String file = jsonObject1.getString("file");
                                    String entityId = jsonObject1.getString("entity_id");

                                    imageProductModels.add(new ImageProductModel(valueId, file, entityId, "2"));
                                    if (i == 0) {
                                        imageProductModels.add(1, new ImageProductModel("", "", "", "2"));
                                        imageProductModels.add(2, new ImageProductModel("", "", "", "2"));
                                    }


                                }
                            }


                            }
                        else {
                            imageProductModels.add(0, new ImageProductModel("", "", "", "2"));
                            imageProductModels.add(1, new ImageProductModel("", "", "", "2"));
                            imageProductModels.add(2, new ImageProductModel("", "", "", "2"));
                        }


                        productImageAdapter = new ProductImageAdapter(EditImageProductDetailActivity.this, imageProductModels);
                        recyclerView.setLayoutManager(new LinearLayoutManager(EditImageProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        recyclerView.setAdapter(productImageAdapter);
                        productImageAdapter.notifyDataSetChanged();
                        edit_layout.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }catch (Exception e){

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
            MaintainRequestQueue.getInstance(EditImageProductDetailActivity.this).addToRequestQueue(req, "tag");

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onItemClick(View v, int position,int flag) {
         imageProductModel = productImageAdapter.getWordAtPosition(position);
         if(flag==0){
             if(!imageProductModel.getFile().equalsIgnoreCase("")){
                 AlertDialog myQuittingDialogBox =new AlertDialog.Builder(v.getContext())
                         //set message, title, and icon
                         .setTitle("Delete")
                         .setMessage("Are you sure you wish to delete this image?")
                         .setIcon(R.drawable.trash)
                         .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int whichButton) {
                                deleteImages(productSku, Integer.parseInt(imageProductModel.getValueId()));
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
         }
         else if(flag==1) {
             if (!flagPermissions) {
                 checkPermissions(position, imageProductModel.getValueId());
                 return;
             } else {
                 imageProductModel = productImageAdapter.getWordAtPosition(position);
                 Intent intent = new Intent(EditImageProductDetailActivity.this, EditProductListActivity.class);
                 intent.putExtra("position", position);
                 intent.putExtra("product_id", productId);
                 intent.putExtra("product_sku", productSku);
                 intent.putExtra("value_id", imageProductModel.getValueId());
                 startActivity(intent);
             }
         }
         else {
             if (!flagPermissions) {
                 checkPermissions(position, imageProductModel.getValueId());
                 return;
             } else {
                 imageProductModel = productImageAdapter.getWordAtPosition(position);
                 Intent intent = new Intent(EditImageProductDetailActivity.this, EditProductListActivity.class);
                 intent.putExtra("position", position);
                 intent.putExtra("product_id", productId);
                 intent.putExtra("product_sku", productSku);
                 intent.putExtra("value_id", imageProductModel.getValueId());
                 startActivity(intent);
             }
         }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void checkPermissions(final int position,final String valueId) {
        if (!hasPermissions(this, PERMISSIONS)) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
            flagPermissions = false;
        }
        else {
            flagPermissions = true;
            imageProductModel = productImageAdapter.getWordAtPosition(position);
            Intent intent = new Intent(EditImageProductDetailActivity.this, EditProductListActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("product_id", productId);
            intent.putExtra("product_sku",productSku);
            intent.putExtra("value_id", imageProductModel.getValueId());
            startActivity(intent);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
                else {

                }
            }
        }
        return true;
    }

    public void deleteImages(final String skuId,final int valueId){

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);

        progressDialog = new ProgressDialog(EditImageProductDetailActivity.this);
        progressDialog.setMessage("Deleting product image....");
        progressDialog.show();
        final String authToken = "Bearer " + sharedPreferences.getString("adminToken",null);
        Log.d("Auth Token.....",authToken);
        mposServices.deleteImages(valueId,0,0).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                //    Log.d("Response.....",response.body().string());
                    if(response.code()==200){
                        progressDialog.dismiss();
                        Toast.makeText(EditImageProductDetailActivity.this,"Product image deleted successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditImageProductDetailActivity.this,AddProductListActivity.class);
                        startActivity(intent);

                    }
                    else if(response.code() == 400){
                        progressDialog.dismiss();
                        Toast.makeText(EditImageProductDetailActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 401){
                        progressDialog.dismiss();
                        Toast.makeText(EditImageProductDetailActivity.this,"Unauthorised",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        progressDialog.dismiss();
                        Toast.makeText(EditImageProductDetailActivity.this,"Internal Server error",Toast.LENGTH_LONG).show();
                    }else {
                        progressDialog.dismiss();
                        Log.d("failure...", String.valueOf(response.body()));
                        Toast.makeText(EditImageProductDetailActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
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
                Toast.makeText(EditImageProductDetailActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }


        });


    }


}
