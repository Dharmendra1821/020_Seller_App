package standalone.eduqfix.qfixinfo.com.app.seller_app.community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DeliveryBoyListingActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.ProfileActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.DeliveryboyAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyList;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.MainDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.community.model.ImageModel;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;

public class EditMicroMarketActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    EditText label,title,contactDetails;
    ImageView logo,thumbnail;
    TextView readMorePrivacy,readLessPrivacy,readMoreTerms,readLessTerms,readMoreShipping,readLessShipping,readMoreAboutUs,readLessAboutUs,readMoreReturn,readLessReturn;
    EditText privacyPolicy,termsCondition,aboutUs,shippingPolicy,returnPolicy;
    RecyclerView recyclerView;
    ArrayList<ImageModel> imageModels;
    ImageModel imageModel;
    ImageAdapter imageAdapter;
    LinearLayoutManager linearLayoutManager;
    int PERMISSION_ALL = 1;
    boolean flagPermissions = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_micro_market);

        getSupportActionBar().setTitle("Edit MarketPlace");

        progressDialog = new ProgressDialog(EditMicroMarketActivity.this);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String microMarketId = sharedPreferences.getString("microMarketId",null);

        label = findViewById(R.id.microMarket_label);
        title = findViewById(R.id.microMarket_title);
        contactDetails = findViewById(R.id.microMarket_contact);
        privacyPolicy = findViewById(R.id.microMarket_privacyPolicy);
        termsCondition = findViewById(R.id.microMarket_termsCondition);
        aboutUs = findViewById(R.id.microMarket_aboutUs);
        shippingPolicy = findViewById(R.id.microMarket_shippingPolicy);
        returnPolicy = findViewById(R.id.microMarket_returnPolicy);
        logo = findViewById(R.id.microMarket_logo);
        thumbnail = findViewById(R.id.microMarket_thumbnail);
        readMorePrivacy = findViewById(R.id.read_more_privacy);
        readLessPrivacy = findViewById(R.id.read_less_privacy);
        readMoreTerms = findViewById(R.id.read_more_terms);
        readLessTerms = findViewById(R.id.read_less_terms);
        readMoreAboutUs = findViewById(R.id.read_more_aboutUs);
        readLessAboutUs = findViewById(R.id.read_less_aboutUs);
        readMoreShipping = findViewById(R.id.read_more_shippingPolicy);
        readLessShipping = findViewById(R.id.read_less_shippingPolicy);
        readMoreReturn = findViewById(R.id.read_more_returnPolicy);
        readLessReturn = findViewById(R.id.read_less_returnPolicy);
        recyclerView   = findViewById(R.id.microMarket_banner_images);


        getMicroMarket(microMarketId);
        readMorePrivacy.setOnClickListener(this);
        readLessPrivacy.setOnClickListener(this);
        readMoreTerms.setOnClickListener(this);
        readLessTerms.setOnClickListener(this);
        readMoreAboutUs.setOnClickListener(this);
        readLessAboutUs.setOnClickListener(this);
        readMoreShipping.setOnClickListener(this);
        readLessShipping.setOnClickListener(this);
        readMoreReturn.setOnClickListener(this);
        readLessReturn.setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(EditMicroMarketActivity.this);
    }

    public void getMicroMarket(final String microMarketId){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl+ BASE_URL+"customapi/getmicromarket?micromarket_id="+microMarketId;

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Getting data.......");
        progressDialog.show();

        StringRequest req = new StringRequest(com.android.volley.Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    imageModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equalsIgnoreCase("success")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        String microMarketLabel = data.getString("label");
                        String microMarketTitle = data.getString("title");
                        String microMarketContactDetails = data.getString("contact_details");
                        String microMarketPolicy = data.getString("policies");
                        String microMarketTermsCondition = data.getString("terms_and_condition");
                        String microMarketAboutUs = data.getString("about_us");
                        String microMarketShippingPolicy= data.getString("shipping_policy");
                        String microMarketReturnPolicy= data.getString("return_policy");
                        String microMarketLogo = data.getString("logo");
                        String microMarketThumbnail = data.getString("thumbnail");
                        JSONObject bannerImages = data.getJSONObject("banner_images");
                        String baseUrl = bannerImages.getString("base_url");
                        JSONArray jsonArray = bannerImages.getJSONArray("banner");
                        if(jsonArray.length() > 0){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                imageModels.add(new ImageModel(String.valueOf(jsonArray.get(i)),baseUrl));
                            }
                        }
                        imageAdapter = new ImageAdapter(EditMicroMarketActivity.this,imageModels);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(imageAdapter);

                        if(!microMarketLabel.equalsIgnoreCase("null")){
                            label.setText(microMarketLabel);
                        }
                        if(!microMarketTitle.equalsIgnoreCase("null")) {
                            title.setText(microMarketTitle);
                        }
                        if(!microMarketContactDetails.equalsIgnoreCase("null")) {
                            contactDetails.setText(microMarketContactDetails);
                        }
                        if(!microMarketPolicy.equalsIgnoreCase("null")) {
                            privacyPolicy.setText(HtmlCompat.fromHtml(microMarketPolicy,0));
                        }
                        if(!microMarketTermsCondition.equalsIgnoreCase("null")) {
                            termsCondition.setText(HtmlCompat.fromHtml(microMarketTermsCondition,0));
                        }
                        if(!microMarketAboutUs.equalsIgnoreCase("null")) {
                            aboutUs.setText(HtmlCompat.fromHtml(microMarketAboutUs,0));
                        }
                        if(!microMarketShippingPolicy.equalsIgnoreCase("null")) {
                            shippingPolicy.setText(HtmlCompat.fromHtml(microMarketShippingPolicy,0));
                        }
                        if(!microMarketReturnPolicy.equalsIgnoreCase("null")) {
                            returnPolicy.setText(HtmlCompat.fromHtml(microMarketReturnPolicy,0));
                        }
                        if(!microMarketLogo.equalsIgnoreCase("null")) {
                            Glide.with(EditMicroMarketActivity.this).load(microMarketLogo).error(R.drawable.download).into(logo);
                        }
                        if(!microMarketThumbnail.equalsIgnoreCase("null")) {
                            Glide.with(EditMicroMarketActivity.this).load(microMarketThumbnail).error(R.drawable.download).into(thumbnail);
                        }


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
            public com.android.volley.Request.Priority getPriority() {
                return com.android.volley.Request.Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
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
        MaintainRequestQueue.getInstance(EditMicroMarketActivity.this).addToRequestQueue(req, "tag");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.read_more_privacy:
                readMorePrivacy.setVisibility(View.GONE);
                readLessPrivacy.setVisibility(View.VISIBLE);
                privacyPolicy.setMaxLines(Integer.MAX_VALUE);
                break;
            case R.id.read_less_privacy:
                readMorePrivacy.setVisibility(View.VISIBLE);
                readLessPrivacy.setVisibility(View.GONE);
                privacyPolicy.setMaxLines(3);
                break;
            case R.id.read_more_terms:
                readMoreTerms.setVisibility(View.GONE);
                readLessTerms.setVisibility(View.VISIBLE);
                termsCondition.setMaxLines(Integer.MAX_VALUE);
                break;
            case R.id.read_less_terms:
                readMoreTerms.setVisibility(View.VISIBLE);
                readLessTerms.setVisibility(View.GONE);
                termsCondition.setMaxLines(3);
                break;
            case R.id.read_more_aboutUs:
                readMoreAboutUs.setVisibility(View.GONE);
                readLessAboutUs.setVisibility(View.VISIBLE);
                aboutUs.setMaxLines(Integer.MAX_VALUE);
                break;
            case R.id.read_less_aboutUs:
                readMoreAboutUs.setVisibility(View.VISIBLE);
                readLessAboutUs.setVisibility(View.GONE);
                aboutUs.setMaxLines(3);
                break;
            case R.id.read_more_shippingPolicy:
                readMoreShipping.setVisibility(View.GONE);
                readLessShipping.setVisibility(View.VISIBLE);
                shippingPolicy.setMaxLines(Integer.MAX_VALUE);
                break;
            case R.id.read_less_shippingPolicy:
                readMoreShipping.setVisibility(View.VISIBLE);
                readLessShipping.setVisibility(View.GONE);
                shippingPolicy.setMaxLines(3);
                break;
            case R.id.read_more_returnPolicy:
                readMoreReturn.setVisibility(View.GONE);
                readLessReturn.setVisibility(View.VISIBLE);
                returnPolicy.setMaxLines(Integer.MAX_VALUE);
                break;
            case R.id.read_less_returnPolicy:
                readMoreReturn.setVisibility(View.VISIBLE);
                readLessReturn.setVisibility(View.GONE);
                returnPolicy.setMaxLines(3);
                break;
        }
    }
}
