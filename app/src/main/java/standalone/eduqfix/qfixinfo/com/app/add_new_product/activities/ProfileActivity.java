package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

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
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.AddNewProductListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductAttributes;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductImages;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductMediaGallery;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductModel;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.MainDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText contact,shoptitle,company_locality,gstin,pan,company_description;
    ImageView banner,logo;
    TextView shipping_policy,read_more,read_less,retutn_policy,retutn_more,retutn_less,privacy_policy;
    TextView privacy_more,privacy_less;
    EditText retutn_policy_edit,shipping_policy_edit,privacy_policy_edit;
    ProgressDialog progressDialog;
    String contactNumber,shopTitle,companyLocality,GSTIN,PanNumber,returnPolicy,shippingPolicy,companyBannner,companyLogo;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, SELECT_FILE1 = 2;
    private String userChoosenTask;

    String entityId;
    String isSeller;
    String seller_Id;
    String storeId;
    String otherInfo;
    String pan_no;
    String gst_no;
    String companyDescription;
    String privacyPolicy;
    String logoBanner;
    ImageView edit,save;

    SharedPreferences sharedPreferences;
    int sellerId;
    String result;
  /*  var optionid = standardObj.option_id;
				if($scope.bundleData.indexOf(optionid) !== -1) {
        console.log("already exist!!")
        	$scope.bundleData.splice(scope.bundleData.pop(standardObj.sku));
    }
				$scope.bundleData.push(standardObj.option_id);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("My Profile");

        progressDialog = new ProgressDialog(ProfileActivity.this);

        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails().getSeller_id() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

//        for(int i=0;i < loginResponse.getCustomerDetails().getCustomAttributes().size();i++){
//
//            String attribute = loginResponse.getCustomerDetails().getCustomAttributes().get(i).getAttributeCode();
//            if(attribute.equalsIgnoreCase("shipping_type")){
//                String value = loginResponse.getCustomerDetails().getCustomAttributes().get(i).getValue();
//                Log.d("value......",value);//qshipping mpfixrate
//            }
//        }

        contact = findViewById(R.id.myprofile_contact);
        shoptitle = findViewById(R.id.myprofile_shoptitle);
        company_locality = findViewById(R.id.myprofile_company_locality);
        gstin = findViewById(R.id.myprofile_company_gstin);
        pan = findViewById(R.id.myprofile_company_pan);

        retutn_policy = findViewById(R.id.myprofile_return_policy);
        retutn_policy_edit = findViewById(R.id.myprofile_return_policy_edit);
        retutn_more = findViewById(R.id.read_more_return);
        retutn_less = findViewById(R.id.read_less_retutn);
        shipping_policy = findViewById(R.id.myprofile_company_policy);
        shipping_policy_edit = findViewById(R.id.myprofile_company_policy_edit);
        read_more = findViewById(R.id.read_more);
        read_less = findViewById(R.id.read_less);
        company_description = findViewById(R.id.myprofile_company_description);
        privacy_policy = findViewById(R.id.myprofile_privacy_policy);
        privacy_policy_edit = findViewById(R.id.myprofile_privacy_policy_edit);
        privacy_more = findViewById(R.id.read_more_privacy);
        privacy_less = findViewById(R.id.read_less_privacy);

        banner = findViewById(R.id.myprofile_banner);
        logo = findViewById(R.id.myprofile_company_logo);
        edit = findViewById(R.id.myprofile_edit_btn);
        save = findViewById(R.id.myprofile_save_btn);


        contact.setEnabled(false);
        shoptitle.setEnabled(false);
        company_locality.setEnabled(false);
        gstin.setEnabled(false);
        pan.setEnabled(false);
        retutn_policy.setEnabled(false);
        shipping_policy.setEnabled(false);
        company_description.setEnabled(false);
        privacy_policy.setEnabled(false);
        banner.setEnabled(false);
        logo.setEnabled(false);


        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent();
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent1();
            }
        });

        read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read_more.setVisibility(View.GONE);
                read_less.setVisibility(View.VISIBLE);
                shipping_policy.setMaxLines(Integer.MAX_VALUE);
            }
        });

        read_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read_more.setVisibility(View.VISIBLE);
                read_less.setVisibility(View.GONE);
                shipping_policy.setMaxLines(3);
            }
        });

        retutn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retutn_more.setVisibility(View.GONE);
                retutn_less.setVisibility(View.VISIBLE);
                retutn_policy.setMaxLines(Integer.MAX_VALUE);
            }
        });

        retutn_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retutn_more.setVisibility(View.VISIBLE);
                retutn_less.setVisibility(View.GONE);
                retutn_policy.setMaxLines(3);
            }
        });

        privacy_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacy_more.setVisibility(View.GONE);
                privacy_less.setVisibility(View.VISIBLE);
                privacy_policy.setMaxLines(Integer.MAX_VALUE);
            }
        });

        privacy_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacy_more.setVisibility(View.VISIBLE);
                privacy_less.setVisibility(View.GONE);
                privacy_policy.setMaxLines(3);
            }
        });

        edit.setOnClickListener(this);
        save.setOnClickListener(this);



        myProfile(sellerId);

    }

    public void editProfile(){

     //   String url = "https://shoppingtest.eduqfix.com/rest/V1/customapi/editSellerProfileDetails";
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"customapi/editSellerProfileDetails.php";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Editing profile.......");

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("entity_id", entityId);
            jsonObject1.put("is_seller", isSeller);
            jsonObject1.put("seller_id", String.valueOf(sellerId));
            jsonObject1.put("contact_number", contactNumber);
            jsonObject1.put("shop_title", shopTitle);
            jsonObject1.put("company_locality", companyLocality);
            jsonObject1.put("return_policy", returnPolicy);
            jsonObject1.put("shipping_policy", shippingPolicy);
          /*  jsonObject1.put("banner_pic", companyBannner);
            jsonObject1.put("logo_pic", logoBanner);*/
            jsonObject1.put("pan_no", pan_no);
            jsonObject1.put("gst_no", gst_no);
            jsonObject1.put("store_id", storeId);
            jsonObject1.put("meta_description", "");
            jsonObject1.put("company_description", companyDescription);
            jsonObject1.put("privacy_policy", privacyPolicy);
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request.toString();

        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    Toast.makeText(ProfileActivity.this,"Successfully updated your profile",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ProfileActivity.this, MainDashboardActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
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
        MaintainRequestQueue.getInstance(ProfileActivity.this).addToRequestQueue(req, "tag");



    }


    public class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
                String imageDataBytes = sharedPreferences.getString("image1",null);
                ByteArrayInputStream imageStream = null;
                if(logoBanner != null){
                    imageStream  = new ByteArrayInputStream(Base64.decode(logoBanner.getBytes(), Base64.DEFAULT));
                }

                HttpClient client = new DefaultHttpClient();
                String adminToken = "Bearer " + sharedPreferences.getString("adminToken", null);
                String shopUrl = sharedPreferences.getString("shop_url", null);

                //   String url = "https://" + shopUrl + Constant.BASE_URL + "products/" + sku + "/media";
                String url = Constant.REVAMP_URL1+"customapi/editSellerProfileDetails.php";
                HttpPost post = new HttpPost(url);
                post.setHeader("Authorization", adminToken);

                Log.d("admin",adminToken);

                Log.d("url.....", contactNumber);
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                entityBuilder.addTextBody("entity_id", entityId);
                entityBuilder.addTextBody("is_seller", isSeller);
                entityBuilder.addTextBody("seller_id", String.valueOf(sellerId));
                entityBuilder.addTextBody("contact_number", contactNumber);
                entityBuilder.addTextBody("shop_title", shopTitle);
                entityBuilder.addTextBody("company_locality", companyLocality);
                entityBuilder.addTextBody("return_policy", returnPolicy);
                entityBuilder.addTextBody("shipping_policy", shippingPolicy);
                  /*  jsonObject1.put("banner_pic", companyBannner);
                    jsonObject1.put("logo_pic", logoBanner);*/
                entityBuilder.addTextBody("pan_no", pan_no);
                entityBuilder.addTextBody("gst_no", gst_no);
                entityBuilder.addTextBody("store_id", storeId);
                entityBuilder.addTextBody("meta_description", "");
                entityBuilder.addTextBody("company_description", companyDescription);
                entityBuilder.addTextBody("privacy_policy", privacyPolicy);
                String boundary = "---------------" + UUID.randomUUID().toString();
                entityBuilder.setBoundary(boundary);
                Log.d(".....",UUID.randomUUID().toString());

                if(logoBanner != null){
                    entityBuilder.addBinaryBody("logo_pic", imageStream, ContentType.create("image/png"), UUID.randomUUID().toString()+".jpg");
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

                            Handler handler = new Handler(getMainLooper());
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(ProfileActivity.this, "profile updated successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ProfileActivity.this, MainDashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressDialog.dismiss();
                                }
                            });

                } else {
                    Handler handler =  new Handler(getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(ProfileActivity.this, "Please try again!", Toast.LENGTH_LONG).show();
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
                        progressDialog.setMessage("Saving data ....");
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

    public void myProfile(final int sellerId){

      //  String url = Constant.TEST_URL+"customapi/getSellerDetails?seller_id="+sellerId;
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"customapi/getSellerDetails.php?seller_id="+sellerId;

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);
        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading profile.......");

        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                         entityId           = jsonObject.getString("entity_id");
                         isSeller           = jsonObject.optString("is_seller");
                         seller_Id          = jsonObject.optString("seller_id");
                         contactNumber      = jsonObject.optString("contact_number");
                         shopTitle          = jsonObject.optString("shop_title");
                         companyLocality    = jsonObject.optString("company_locality");
                         otherInfo          = jsonObject.optString("others_info");
                         returnPolicy       = jsonObject.optString("return_policy");
                         shippingPolicy     = jsonObject.optString("shipping_policy");
                         companyBannner     = jsonObject.optString("banner_pic");
                         companyLogo        = jsonObject.optString("logo_pic");
                         storeId            = jsonObject.optString("store_id");
                         companyDescription = jsonObject.optString("company_description");
                         privacyPolicy      = jsonObject.optString("privacy_policy");
                         pan_no             = jsonObject.optString("pan_no");
                         gst_no             = jsonObject.optString("gst_no");
                         returnPolicy       = returnPolicy.replaceAll("[^a-zA-Z0-9\\s+]", "");
                         returnPolicy       = returnPolicy.replace("ol","");
                         returnPolicy       = returnPolicy.replace("li","");

                        shippingPolicy = shippingPolicy.replaceAll("[^a-zA-Z0-9\\s+]", "");
                        shippingPolicy = shippingPolicy.replace("ol","");
                        shippingPolicy = shippingPolicy.replace("li","");

                        privacyPolicy = privacyPolicy.replaceAll("[^a-zA-Z0-9\\s+]", "");
                        privacyPolicy = privacyPolicy.replace("ol","");
                        privacyPolicy = privacyPolicy.replace("li","");

                        contact.setText(contactNumber);
                        shoptitle.setText(shopTitle);
                        company_locality.setText(companyLocality);
                        retutn_policy.setText(returnPolicy);
                        retutn_policy_edit.setText(returnPolicy);
                        shipping_policy.setText(shippingPolicy);
                        shipping_policy_edit.setText(shippingPolicy);
                        company_description.setText(companyDescription);
                        privacy_policy.setText(privacyPolicy);
                        privacy_policy_edit.setText(privacyPolicy);
                        gstin.setText(gst_no);
                        pan.setText(pan_no);

                        Glide.with(ProfileActivity.this).load(companyLogo).error(R.drawable.download).into(logo);
           //         Glide.with(ProfileActivity.this).load("https://shoppingtest.eduqfix.com/pub/media/avatar/"+companyBannner).error(R.drawable.download).into(banner);

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
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(ProfileActivity.this).addToRequestQueue(req, "tag");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(ProfileActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    private void galleryIntent1()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE1);
    }


    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if(requestCode ==  SELECT_FILE1){
                onSelectFromGalleryResult1(data);
            }
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        banner.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Bitmap newBitmap = getResizedBitmap(bm,400);
                BitMapToString(newBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("logo", String.valueOf(bm));

        banner.setImageBitmap(bm);
    }

    private void onSelectFromGalleryResult1(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                Bitmap newBitmap = getResizedBitmap(bm,400);
                BitMapToString1(newBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logo.setImageBitmap(bm);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,90, baos);
        byte [] b=baos.toByteArray();
        companyBannner = Base64.encodeToString(b, Base64.DEFAULT);
        return companyBannner;
    }
    public String BitMapToString1(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,90, baos);
        byte [] b=baos.toByteArray();
        logoBanner = Base64.encodeToString(b, Base64.DEFAULT);
        return logoBanner;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.myprofile_edit_btn:
                contact.setEnabled(true);
                shoptitle.setEnabled(true);
                company_locality.setEnabled(true);
                gstin.setEnabled(true);
                pan.setEnabled(true);
                company_description.setEnabled(true);
                privacy_policy.setEnabled(true);
                retutn_policy_edit.setEnabled(true);

                retutn_policy_edit.setVisibility(View.VISIBLE);
                retutn_policy.setVisibility(View.GONE);
                retutn_more.setVisibility(View.GONE);
                retutn_less.setVisibility(View.GONE);
                shipping_policy.setEnabled(true);

                shipping_policy_edit.setVisibility(View.VISIBLE);
                shipping_policy.setVisibility(View.GONE);
                read_more.setVisibility(View.GONE);
                read_less.setVisibility(View.GONE);

                privacy_policy_edit.setVisibility(View.VISIBLE);
                privacy_policy.setVisibility(View.GONE);
                privacy_more.setVisibility(View.GONE);
                privacy_less.setVisibility(View.GONE);
                banner.setEnabled(true);
                logo.setEnabled(true);
                edit.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);

                break;

            case R.id.myprofile_save_btn:

                contactNumber = contact.getText().toString();
                shopTitle = shoptitle.getText().toString();
                companyLocality = company_locality.getText().toString();
                gst_no = gstin.getText().toString();
                pan_no = pan.getText().toString();
                returnPolicy = retutn_policy_edit.getText().toString();
                shippingPolicy = shipping_policy_edit.getText().toString();
                companyDescription = company_description.getText().toString();
                privacyPolicy = privacy_policy_edit.getText().toString();

              //  editProfile();
                new RequestTask().execute();

                break;
        }
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
}
