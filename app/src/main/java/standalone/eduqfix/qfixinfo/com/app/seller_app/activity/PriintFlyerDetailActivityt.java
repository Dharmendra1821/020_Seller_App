package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DetectSwipeDirectionActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.ExpListViewAdapterWithCheckbox;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.fragments.BottomSheeetFragment;
import standalone.eduqfix.qfixinfo.com.app.seller_app.fragments.PrintFlyerProductFragment;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.FlyerProduct;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.FlyerProductModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.FlyerRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.FlyerRequestData;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class PriintFlyerDetailActivityt extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    ImageView banner;
    LinearLayout topView;
    EditText titleTxt,summaryTxt;
    ArrayList<FlyerProductModel> flyerProductModels;
    ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6;
    TextView pname1,pname2,pname3,pname4,pname5,pname6;
    TextView price1,price2,price3,price4,price5,price6;
    PrintFlyerProductFragment printFlyerProductFragment;
    BroadcastReceiver mMessageReceiver;
    String imgPosition;
    LinearLayout titleLayout,summaryLayout,flyerLyaout1,flyerLayout2;
    LinearLayout flyer_divider,flyer_footer;
    ImageView store_logo,store_code;
    TextView store_url,store_contact;
    String storeAddress;
    String campaignId;
    String bannerImg;
    String title;
    String summary;
    String logo;
    String contactDetail;
    String storeInfo;
    Button saveBtn;
    LinearLayoutCompat linearLayoutCompat;
    ImageView finalFlyer,finalFlyer1;
    LinearLayout btnLayout,weightSumView;
    Button whatsappBtn,pdfBtn,fbBtn,share_next;
    Bitmap bm;
    String path,page;
    LinearLayout emailFlyerView;
    Button shareNxt;
    int sellerId;
    String shopUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priint_flyer_detail_activityt);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(PriintFlyerDetailActivityt.this);

        try{
             page = getIntent().getStringExtra("from_page");
        }catch (Exception e){

        }

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        shopUrl  = sharedPreferences.getString("shop_url",null);

        banner     = findViewById(R.id.template_banner);
        topView    = findViewById(R.id.top_view);
        titleTxt   = findViewById(R.id.template_title);
        summaryTxt = findViewById(R.id.template_summary);
        imageView1 = findViewById(R.id.print_productImage1);
        imageView2 = findViewById(R.id.print_productImage2);
        imageView3 = findViewById(R.id.print_productImage3);
        imageView4 = findViewById(R.id.print_productImage4);
        imageView5 = findViewById(R.id.print_productImage5);
        imageView6 = findViewById(R.id.print_productImage6);

        pname1 = findViewById(R.id.print_productName);
        pname2 = findViewById(R.id.print_productName2);
        pname3 = findViewById(R.id.print_productName3);
        pname4 = findViewById(R.id.print_productName4);
        pname5 = findViewById(R.id.print_productName5);
        pname6 = findViewById(R.id.print_productName6);

        price1 = findViewById(R.id.print_productPrice);
        price2 = findViewById(R.id.print_productPrice2);
        price3 = findViewById(R.id.print_productPrice3);
        price4 = findViewById(R.id.print_productPrice4);
        price5 = findViewById(R.id.print_productPrice5);
        price6 = findViewById(R.id.print_productPrice6);

        titleLayout = findViewById(R.id.title_layout);
        summaryLayout = findViewById(R.id.summary_layout);
        flyerLyaout1 = findViewById(R.id.flyproduct_layout1);
        flyerLayout2 = findViewById(R.id.flyproduct_layout2);
        flyer_divider = findViewById(R.id.flyer_divider);
        flyer_footer  = findViewById(R.id.flyer_footer);
        store_logo = findViewById(R.id.shop_logo);
        store_contact = findViewById(R.id.flyer_contact_detail);
        store_code = findViewById(R.id.flyer_qrcode);
        store_url = findViewById(R.id.flyer_store_url);
        saveBtn = findViewById(R.id.save_template);
        linearLayoutCompat = findViewById(R.id.main_templateId);
   //     finalFlyer = findViewById(R.id.final_flyer_image);
//        btnLayout  = findViewById(R.id.btn_layout);
//        weightSumView  = findViewById(R.id.weightSumView);
//        whatsappBtn = findViewById(R.id.share_on_whatsapp);
//        pdfBtn = findViewById(R.id.save_pdf);
//        fbBtn  = findViewById(R.id.share_on_fb);
//        shareNxt = findViewById(R.id.share_next);

        getPrintFlyerDetail();

        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);

        store_url.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
//        whatsappBtn.setOnClickListener(this);
   //     pdfBtn.setOnClickListener(this);
   //     fbBtn.setOnClickListener(this);
    //    shareNxt.setOnClickListener(this);

    }

    public void getPrintFlyerDetail(){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"customapi/mobileCampaign.php";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading data.......");
        progressDialog.setCancelable(false);

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("page_type", "A4");
            jsonObject1.put("customer_id", String.valueOf(sellerId));
            jsonObject1.put("view_mode", "portrait");
            jsonObject1.put("product_id", "");
            jsonObject1.put("form_type", "print_flyer_setup");
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request.toString();

        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {
                    flyerProductModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    campaignId = jsonObject.getString("campaign_id");
                    JSONObject data     = jsonObject.getJSONObject("data");
                     bannerImg    = data.getString("banner");
                     title        = data.getString("title");
                     summary      = data.getString("summary");
                     storeAddress        = data.getString("store_address");
                     logo         = data.getString("logo");
                     contactDetail = data.getString("contact_details");
                     storeInfo      = data.getString("store_information");

                    JSONArray jsonArray = data.getJSONArray("products_array");
                    if(jsonArray.length() > 0){
                        for (int i =0; i< jsonArray.length(); i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String id = jsonObject2.getString("id");
                            String name = jsonObject2.getString("name");
                            String image = jsonObject2.getString("image");
                            String price = jsonObject2.getString("price");

                            flyerProductModels.add(new FlyerProductModel(id,name,image,price));

                        }


                    }
                //    String bannerImgage = insertString(bannerImg,shopUrl+".",7);
                    Glide.with(PriintFlyerDetailActivityt.this).load(bannerImg).into(banner);
                   // String logoImgage = insertString(logo,shopUrl+".",7);
                    Glide.with(PriintFlyerDetailActivityt.this).load(logo).into(store_logo);
                    titleTxt.setText(title);
                    summaryTxt.setText(summary);
                    topView.setVisibility(View.VISIBLE);
                    titleLayout.setVisibility(View.VISIBLE);
                    summaryLayout.setVisibility(View.VISIBLE);
                    flyerLyaout1.setVisibility(View.VISIBLE);
                    flyerLayout2.setVisibility(View.VISIBLE);
                    flyer_divider.setVisibility(View.VISIBLE);
                    flyer_footer.setVisibility(View.VISIBLE);
                    saveBtn.setVisibility(View.VISIBLE);
                    if(contactDetail!=null){
                        store_contact.setText(contactDetail);
                    }
                    else {
                        store_contact.setText("");
                    }
                    store_url.setText(storeAddress);




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
        // Adding request to request queue

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(PriintFlyerDetailActivityt.this).addToRequestQueue(req, "tag");

    }

    private void createPdf(final Bitmap bm){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        canvas.drawPaint(paint);


       Bitmap bitmap = Bitmap.createScaledBitmap(bm, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        // write the document content
        String targetPdf = "/sdcard/PrintFlyer.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
          //  btn_generate.setText("Check PDF");
            Toast.makeText(PriintFlyerDetailActivityt.this,"PDF saved on Documents",Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.print_productImage1:
                imgPosition = "1";
                printFlyerProductFragment = PrintFlyerProductFragment.newInstance();
                printFlyerProductFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");
            break;
            case R.id.print_productImage2:
                imgPosition = "2";
                printFlyerProductFragment = PrintFlyerProductFragment.newInstance();
                printFlyerProductFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");
                break;
            case R.id.print_productImage3:
                imgPosition = "3";
                printFlyerProductFragment = PrintFlyerProductFragment.newInstance();
                printFlyerProductFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");
                break;
            case R.id.print_productImage4:
                imgPosition = "4";
                printFlyerProductFragment = PrintFlyerProductFragment.newInstance();
                printFlyerProductFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");
                break;
            case R.id.print_productImage5:
                imgPosition = "5";
                printFlyerProductFragment = PrintFlyerProductFragment.newInstance();
                printFlyerProductFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");
                break;
            case R.id.print_productImage6:
                imgPosition = "6";
                printFlyerProductFragment = PrintFlyerProductFragment.newInstance();
                printFlyerProductFragment.show(getSupportFragmentManager(), "add_photo_dialog_fragment");
                break;
            case R.id.flyer_store_url:
                showAlert("Generate QR Code");
                break;
            case R.id.save_template:
                saveTemplate();
                break;
//            case R.id.share_on_whatsapp:
//                onClickApp("com.whatsapp",bm);
//                break;
//            case R.id.share_on_fb:
//                onClickApp("com.facebook.katana",bm);
//                break;
//            case R.id.save_pdf:
//                createPdf(bm);
//                break;
//            case R.id.share_next:
//                    Intent intent = new Intent(PriintFlyerDetailActivityt.this,SMSContactListActivity.class);
//                    intent.putExtra("from_page",page);
//                    intent.putExtra("path",path);
//                    startActivity(intent);
//                 break;

        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
       // ByteArrayOutputStream bytes = new ByteArrayOutputStream();
       // inImage.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        Uri uriPath = null;

        saveToInternalStorage(inImage);

        path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        uriPath =  Uri.fromFile(new File(path));
        Intent intent = new Intent(PriintFlyerDetailActivityt.this,TemplateDetailsFinalActivity.class);
        intent.putExtra("uri",path);
        intent.putExtra("filePath",uriPath.getPath());
        intent.putExtra("from_page",page);
        startActivity(intent);
        return Uri.parse(path);
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"template.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sharedPreferences.edit().putString("template_path",directory.getAbsolutePath()).apply();
        return directory.getAbsolutePath();
    }


    public ArrayList<FlyerProductModel> getMyData() {
        return flyerProductModels;
    }

    public String getPosition(){
        return imgPosition;
    }

    public void showAlert(final String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PriintFlyerDetailActivityt.this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        generateQRCode();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void generateQRCode(){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl + BASE_URL+"customapi/createQRcodeTemplate";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading data.......");
        progressDialog.setCancelable(false);

        JSONObject jsonObject1 = new JSONObject();
        JSONObject request = new JSONObject();
        try {
            jsonObject1.put("qr_code_url", storeAddress);
            jsonObject1.put("campaign_id", campaignId);
            request.put("request",jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String mRequestBody = request.toString();

        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("success");
                    if(status){
                        String filePath = jsonObject.getString("filepath");
                        String Image = insertString(filePath,shopUrl+".",7);
                        Glide.with(PriintFlyerDetailActivityt.this).load(filePath).into(store_code);
                        store_url.setVisibility(View.GONE);
                        store_code.setVisibility(View.VISIBLE);
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
        // Adding request to request queue

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(PriintFlyerDetailActivityt.this).addToRequestQueue(req, "tag");

    }

    public void saveTemplate(){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"customapi/updateMobileTemplate.php";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Saving data.......");
        progressDialog.setCancelable(false);

            FlyerRequest request1 = new FlyerRequest();
            FlyerRequestData product_data = new FlyerRequestData();
            product_data.setId(campaignId);
            product_data.setTitle(titleTxt.getText().toString());
            product_data.setLogo(logo);
            product_data.setPage_type("A4");
            product_data.setView_mode("portrait");
            product_data.setBanner(bannerImg);
            product_data.setSummary(summaryTxt.getText().toString());
            product_data.setContact_details(contactDetail);
            product_data.setStore_address(storeAddress);
            product_data.setStore_information(storeInfo);
            product_data.setIs_modified(1);
            product_data.setSeller_id(sellerId);
            ArrayList<FlyerProduct> flyerProducts = new ArrayList<>();
            FlyerProduct flyerProduct1 = new FlyerProduct();
            flyerProduct1.setId(sharedPreferences.getString("flyer_id1",null));
            flyerProducts.add(flyerProduct1);
            FlyerProduct flyerProduct2 = new FlyerProduct();
            flyerProduct2.setId(sharedPreferences.getString("flyer_id2",null));
            flyerProducts.add(flyerProduct2);
            FlyerProduct flyerProduct3 = new FlyerProduct();
            flyerProduct3.setId(sharedPreferences.getString("flyer_id3",null));
            flyerProducts.add(flyerProduct3);
            FlyerProduct flyerProduct4 = new FlyerProduct();
            flyerProduct4.setId(sharedPreferences.getString("flyer_id4",null));
            flyerProducts.add(flyerProduct4);
            FlyerProduct flyerProduct5 = new FlyerProduct();
            flyerProduct5.setId(sharedPreferences.getString("flyer_id5",null));
            flyerProducts.add(flyerProduct5);
            FlyerProduct flyerProduct6 = new FlyerProduct();
            flyerProduct6.setId(sharedPreferences.getString("flyer_id6",null));
            flyerProducts.add(flyerProduct6);
            product_data.setProduct_data(flyerProducts);
            request1.setRequest(product_data);

            Gson gson = new Gson();
            String initiateData = gson.toJson(request1);

        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String message =jsonObject.getString("message");
                    Toast.makeText(PriintFlyerDetailActivityt.this,message,Toast.LENGTH_LONG).show();
                    saveBtn.setVisibility(View.GONE);
                    LinearLayoutCompat view = (LinearLayoutCompat) findViewById(R.id.main_templateId);
                    view.setDrawingCacheEnabled(true);
                    view.buildDrawingCache();
                     bm = view.getDrawingCache();
                    topView.setVisibility(View.GONE);
                    titleLayout.setVisibility(View.GONE);
                    summaryLayout.setVisibility(View.GONE);
                    flyerLyaout1.setVisibility(View.GONE);
                    flyerLayout2.setVisibility(View.GONE);
                    flyer_divider.setVisibility(View.GONE);
                    flyer_footer.setVisibility(View.GONE);


//                    if(page.equalsIgnoreCase("email")){
//                        finalFlyer.setImageBitmap(bm);
//                        finalFlyer.setVisibility(View.VISIBLE);
//                        btnLayout.setVisibility(View.VISIBLE);
//                        weightSumView.setVisibility(View.VISIBLE);
//                        whatsappBtn.setVisibility(View.GONE);
//                        pdfBtn.setVisibility(View.GONE);
//                        fbBtn.setVisibility(View.GONE);
//                    }
//                    if(page.equalsIgnoreCase("print")){
//                        finalFlyer.setImageBitmap(bm);
//                        finalFlyer.setVisibility(View.VISIBLE);
//                        btnLayout.setVisibility(View.VISIBLE);
//                        weightSumView.setVisibility(View.VISIBLE);
//                        shareNxt.setVisibility(View.GONE);
//                    }


                    getImageUri(PriintFlyerDetailActivityt.this,bm);






//                    if(page.equalsIgnoreCase("email")){
//                        Intent intent = new Intent(PriintFlyerDetailActivityt.this,SMSContactListActivity.class);
//                        intent.putExtra("from_page",page);
//                        intent.putExtra("path",path);
//                        startActivity(intent);
//                    }

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
                    Log.e("Url...", "" + initiateData);
                    return initiateData == null ? null : initiateData.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", initiateData, "utf-8");
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

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(PriintFlyerDetailActivityt.this).addToRequestQueue(req, "tag");

    }

    public void onClickApp(String pack, Bitmap bitmap) {
        PackageManager pm = getPackageManager();
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Template", null);
            Uri imageUri = Uri.parse(path);

            @SuppressWarnings("unused")
            PackageInfo info = pm.getPackageInfo(pack, PackageManager.GET_META_DATA);

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("image/*");
            waIntent.setPackage(pack);
            waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
            waIntent.putExtra(Intent.EXTRA_TEXT, pack);
            startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
            Toast.makeText(PriintFlyerDetailActivityt.this, "App not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String position = intent.getStringExtra("position");
                    String image = intent.getStringExtra("image");
                    String id = intent.getStringExtra("id");
                    String name = intent.getStringExtra("name");
                    String price = intent.getStringExtra("price");

                    setUpView(position,image,id,name,price);
                    printFlyerProductFragment.dismiss();

                }catch (Exception e){

                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver , new IntentFilter("flyer-message"));

    }

    public void setUpView(final String position,final String image,final String id,final String name,final String price){
        if(position.equalsIgnoreCase("1")){
            String Image = insertString(image,shopUrl+".",7);
            Glide.with(PriintFlyerDetailActivityt.this).load(image).into(imageView1);
            pname1.setText(name);
            price1.setText(price);
            sharedPreferences.edit().putString("flyer_id1",id).apply();
        }
        if(position.equalsIgnoreCase("2")){
            String Image = insertString(image,shopUrl+".",7);
            Glide.with(PriintFlyerDetailActivityt.this).load(image).into(imageView2);
            pname2.setText(name);
            price2.setText(price);
            sharedPreferences.edit().putString("flyer_id2",id).apply();
        }
        if(position.equalsIgnoreCase("3")){
            String Image = insertString(image,shopUrl+".",7);
            Glide.with(PriintFlyerDetailActivityt.this).load(image).into(imageView3);
            pname3.setText(name);
            price3.setText(price);
            sharedPreferences.edit().putString("flyer_id3",id).apply();
        }
        if(position.equalsIgnoreCase("4")){
            String Image = insertString(image,shopUrl+".",7);
            Glide.with(PriintFlyerDetailActivityt.this).load(image).into(imageView4);
            pname4.setText(name);
            price4.setText(price);
            sharedPreferences.edit().putString("flyer_id4",id).apply();
        }
        if(position.equalsIgnoreCase("5")){
            String Image = insertString(image,shopUrl+".",7);
            Glide.with(PriintFlyerDetailActivityt.this).load(image).into(imageView5);
            pname5.setText(name);
            price5.setText(price);
            sharedPreferences.edit().putString("flyer_id5",id).apply();
        }
        if(position.equalsIgnoreCase("6")){
            String Image = insertString(image,shopUrl+".",7);
            Glide.with(PriintFlyerDetailActivityt.this).load(image).into(imageView6);
            pname6.setText(name);
            price6.setText(price);
            sharedPreferences.edit().putString("flyer_id6",id).apply();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        }catch (Exception e){

        }
    }

    public static String insertString(
            String originalString,
            String stringToBeInserted,
            int index)
    {

        // Create a new string
        String newString = originalString.substring(0, index + 1)
                + stringToBeInserted
                + originalString.substring(index + 1);

        // return the modified String

        Log.d("new string",newString);
        return newString;
    }
}
