package standalone.eduqfix.qfixinfo.com.app.invoice_seller;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.MyProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.adapter.SellerProductAdaptor;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.database.AppDatabase;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.database.AppExecutors;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.model.InvoiceMerchantIdModel;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.model.InvoicePostModel;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.model.InvoiceProductModel;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.AddInvoiceActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.AddInvoiceSecondActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.SendInvoiceDashboard;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ComplaintsAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;

public class CreateInvoiceActivity extends AppCompatActivity implements SellerProductAdaptor.ClickListener, View.OnClickListener {

    EditText id,product,price,qty,cgst,igst;
    Button add;
    private AppDatabase mDb;
    Intent intent;
    int mProductId;
    private InvoicePostModel postViewModel;
    RecyclerView recylerview;
    SellerProductAdaptor sellerProductAdaptor;
    InvoiceProductModel invoiceProductModel;
    int invoiceId;
    RadioGroup radioGroup;
    RadioButton radioButton;
    LinearLayout product_layout,gst_layout;
    SearchableSpinner gst_tax_selection,gst_tax_value;
    CheckBox check_cgst,check_igst;
    CheckBox check_cgst_new,check_igst_new;
    ArrayList<String> taxArray;
    SearchableSpinner status, create_cgst_tax,create_igst_tax,create_main_tax;
    String taxValue;
    TextInputLayout cgstLayout,igstLayout;
    ArrayAdapter<String> mainAdapter,statusAdapter,cgstAdapter,igstAdapter,gstTaxAdapter,gstTaxValue;
    Button submit;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    int sellerId;
    String customerName;
    String companyName;
    String address;
    String mobileNumber;
    String emailId;
    String amount;
    String description;
    String invoiceDate;
    String expiryDate;
    String gstIn;
    String gstTaxData;
    RadioButton rb;
    int count;
    String merchantIdData;
    ArrayList<String> merchantIdValue;
    ArrayList<InvoiceMerchantIdModel> invoiceMerchantIdModels;
    String cgstSelectedValue,igstSelectedValue;
    String cgstValue,igstValue,mainTaxValue;
    String gstTaxSelected;
    String gstTaxSelectedValue;
    int sum = 0;
    Button help_document;
    String radioBtnValue;
    int flag ;
    CheckBox cgstCheckbox,igstCheckbox;
    String cgstChecked,igstChecked;
    int isAdded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);



        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(CreateInvoiceActivity.this);

        merchantIdValue = new ArrayList<>();

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

        customerName     = getIntent().getStringExtra("customer_name");
        companyName      = getIntent().getStringExtra("company_name");
        gstIn            = getIntent().getStringExtra("gstin");
        address          = getIntent().getStringExtra("address");
        mobileNumber     = getIntent().getStringExtra("mobile_number");
        emailId          = getIntent().getStringExtra("email_id");
        amount           = getIntent().getStringExtra("amount");
        description      = getIntent().getStringExtra("description");
        invoiceDate      = getIntent().getStringExtra("invoice_date");
        expiryDate       = getIntent().getStringExtra("expiry_date");
        merchantIdData = getIntent().getStringExtra("merchant_id");

        //  Log.d("merchanId..",merchantIdData);
      //  merchantIdData = "";

        id = findViewById(R.id.create_invoice_id_new);
        product = findViewById(R.id.create_invoice_product_new);
        price = findViewById(R.id.create_invoice_price_new);
        qty = findViewById(R.id.create_invoice_qty_new);


        add = findViewById(R.id.create_invoice);
        recylerview = findViewById(R.id.create_recyclerview);

        product_layout = findViewById(R.id.layout_product_list);
        gst_layout = findViewById(R.id.layout_gst);
        gst_tax_selection = findViewById(R.id.gst_tax_selection);
        gst_tax_value = findViewById(R.id.gst_tax_value);

     //   create_cgst_tax = findViewById(R.id.create_cgst_tax);
        create_main_tax = findViewById(R.id.create_main_tax);
     //   create_igst_tax = findViewById(R.id.create_igst_tax);
        cgstCheckbox = findViewById(R.id.cgst_checkbox);
        igstCheckbox = findViewById(R.id.igst_checkbox);

    //    status = findViewById(R.id.tax_status);
        submit = findViewById(R.id.create_invoice_submit);
        count = 0;
        isAdded = 0;

        mDb = AppDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        sellerProductAdaptor = new SellerProductAdaptor(this);
        setAdapter();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.personDao().deleteAll();
            }
        });

        taxArray   = new ArrayList<>();

        postViewModel = ViewModelProviders.of(this).get(InvoicePostModel.class);
        // Create the observer which updates the UI.
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        postViewModel.getAllPosts().observe(this, posts -> sellerProductAdaptor.setData(posts));


        sellerProductAdaptor.setOnItemClickListener(this);


        radioGroup = (RadioGroup) findViewById(R.id.radioInvoice);
        radioGroup.clearCheck();


        radioBtnValue = "";

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    if(rb.getText().toString().equalsIgnoreCase("Product List")){
                        radioBtnValue = "Product List";
                        product_layout.setVisibility(View.VISIBLE);
                        recylerview.setVisibility(View.VISIBLE);
                        gst_layout.setVisibility(View.GONE);
                        gstTaxSelected = "Select Tax";
                        //  gst_cgst.setText("");
                        //  gst_igst.setText("");
                    }
                    else {
                        radioBtnValue = "GST";
                        product_layout.setVisibility(View.GONE);
                        gst_layout.setVisibility(View.VISIBLE);
                        recylerview.setVisibility(View.GONE);

                    }
                }

            }
        });

        taxArray.add("Select Tax");
        taxArray.add("CGST/SGST");
        taxArray.add("IGST");
//        statusAdapter = new ArrayAdapter<String>(CreateInvoiceActivity.this, R.layout.spinner_text_layout, taxArray);
//        status.setAdapter(statusAdapter);

        gstTaxAdapter = new ArrayAdapter<String>(CreateInvoiceActivity.this, R.layout.spinner_text_layout, taxArray);
        gst_tax_selection.setAdapter(gstTaxAdapter);

      //  create_cgst_tax.setVisibility(View.GONE);
        create_main_tax.setVisibility(View.GONE);
       // create_igst_tax.setVisibility(View.GONE);

        taxValue = "";
        cgstChecked = "0";
        igstChecked = "0";

        cgstCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cgstCheckbox.isChecked()){
                    igstCheckbox.setChecked(false);
                    create_main_tax.setVisibility(View.VISIBLE);
                    taxValue =  "CGST/SGST";
                    igstCheckbox.setEnabled(false);
                    cgstChecked = "1";

                }
                else {
                    igstCheckbox.setEnabled(true);
                    cgstChecked = "0";
                }
            }
        });

        igstCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(igstCheckbox.isChecked()){
                    cgstCheckbox.setChecked(false);
                    cgstCheckbox.setEnabled(false);
                    create_main_tax.setVisibility(View.VISIBLE);
                        taxValue =  "IGST";
                        igstChecked = "2";

                }
                else {
                    cgstCheckbox.setEnabled(true);
                    igstChecked = "0";
                }
            }
        });

//        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(parent.getSelectedItem().toString().equalsIgnoreCase("Select Tax")){
//                    taxValue  = "";
//                    create_cgst_tax.setVisibility(View.GONE);
//                    create_igst_tax.setVisibility(View.GONE);
//
//                }
//                if(parent.getSelectedItem().toString().equalsIgnoreCase("CGST/SGST")){
//                    taxValue =  "CGST/SGST";
//
//                    create_cgst_tax.setVisibility(View.VISIBLE);
//                    create_igst_tax.setVisibility(View.GONE);
//                    int spinnerPosition = igstAdapter.getPosition("None");
//                    create_igst_tax.setSelection(spinnerPosition);
//                }
//                if(parent.getSelectedItem().toString().equalsIgnoreCase("IGST")){
//                    taxValue =  "IGST";
//                    //    igstLayout.setVisibility(View.VISIBLE);
//                    int spinnerPosition = cgstAdapter.getPosition("None");
//                    create_cgst_tax.setSelection(spinnerPosition);
//                    create_cgst_tax.setVisibility(View.GONE);
//                    create_igst_tax.setVisibility(View.VISIBLE);
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        gst_tax_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItem().toString().equalsIgnoreCase("Select Tax")){
                    gst_tax_value.setVisibility(View.GONE);
                }
                else {
                    gst_tax_value.setVisibility(View.VISIBLE);
                    gstTaxSelected = parent.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gst_tax_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gstTaxSelectedValue = invoiceMerchantIdModels.get(position).getValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        count++;
        id.setText(String.valueOf(count));

        getTaxes();

        cgstSelectedValue = "";
        igstSelectedValue = "";
        cgstValue = "0";
        igstValue = "0";
        mainTaxValue = "0";

        create_main_tax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cgstSelectedValue = parent.getSelectedItem().toString();
                Log.d("value...",invoiceMerchantIdModels.get(position).getValue());
                mainTaxValue = invoiceMerchantIdModels.get(position).getValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    public  void setAdapter() {
        recylerview.setLayoutManager(new LinearLayoutManager(this));
        recylerview.setHasFixedSize(true);
        recylerview.addItemDecoration(new DividerItemDecoration(recylerview.getContext(), DividerItemDecoration.VERTICAL));
        recylerview.setItemAnimator(new DefaultItemAnimator());
        recylerview.setAdapter(sellerProductAdaptor);
    }


    public void onSaveButtonClicked() {

        if(id.getText().toString().length()== 0 || product.getText().toString().length()== 0 ||
                price.getText().toString().length() == 0 || qty.getText().toString().length() ==0){
            Toast.makeText(CreateInvoiceActivity.this,"All the fields are required to fill",Toast.LENGTH_LONG).show();
        }
        else {

            final InvoiceProductModel invoiceProductModel = new InvoiceProductModel(
                    id.getText().toString(),
                    product.getText().toString(),
                    price.getText().toString(),
                    qty.getText().toString(),
                    taxValue,
                    cgstSelectedValue,
                    mainTaxValue);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if(add.getText().toString().equalsIgnoreCase("add")){
                        mDb.personDao().insertPerson(invoiceProductModel);
                        clearFields("add");
                        isAdded = 1;
                    }
                    else {
                        sum = 0;
                        flag = 0;
                        invoiceProductModel.setId(invoiceId);
                        mDb.personDao().updatePerson(invoiceProductModel);
                        runOnUiThread(() -> {
                            add.setText("add");
                        });
                        clearFields("edit");
                    }
                }
            });
        }

    }

    public void clearFields(final String flag){

        runOnUiThread(() -> {
            if(flag.equalsIgnoreCase("add")){
                count++;
                id.setText(String.valueOf(count));
            }
            else {
                count++;
                count--;
                id.setText(String.valueOf(count));
            }
            product.setText("");
            price.setText("");
            qty.setText("");
            cgstSelectedValue = "";
            igstSelectedValue = "";
            //  create_cgst_tax.setText("");
            // create_igst_tax.setText("");
            int spinnerPosition1 = mainAdapter.getPosition("None");
            create_main_tax.setSelection(spinnerPosition1);

        });



    }

    @Override
    public void onItemClick(View v, int position,String flag) {
        invoiceProductModel = sellerProductAdaptor.getWordAtPosition(position);
        if(flag.equalsIgnoreCase("edit")){
            id.setText(invoiceProductModel.getInvoiceId());
            product.setText(invoiceProductModel.getName());
            price.setText(invoiceProductModel.getPrice());
            qty.setText(invoiceProductModel.getQty());
            if(invoiceProductModel.getTax() != null){
                int spinnerPosition1 = mainAdapter.getPosition(invoiceProductModel.getCgstValue());
                create_main_tax.setSelection(spinnerPosition1);
                mainTaxValue = invoiceProductModel.getCgst();
                create_main_tax.setVisibility(View.VISIBLE);
            }
            invoiceId = invoiceProductModel.getId();
            add.setText("Update");
        }
        else {
            postViewModel.deletePost(invoiceProductModel);

        }



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_invoice_submit:
                sum = 0;
                flag = 1;
                postViewModel.getAllPosts().observe(this,data->{
                    Log.d("add",add.getText().toString());

                        JSONObject request = new JSONObject();
                        JSONObject requestObj = new JSONObject();
                        JSONObject jsonObject1 = new JSONObject();
                        JSONArray jsonArray = new JSONArray();

                        try {
                            requestObj.put("seller_id",String.valueOf(sellerId));
                            requestObj.put("customer_name",customerName);
                            requestObj.put("company_name",companyName);
                            requestObj.put("gstin",gstIn);
                            requestObj.put("invoice_date",invoiceDate);
                            requestObj.put("address",address);
                            requestObj.put("mobile_number",mobileNumber);
                            requestObj.put("email",emailId);
                            requestObj.put("merchant_id",merchantIdData);
                            requestObj.put("description",description);
                            requestObj.put("expiry_date",expiryDate);
                            requestObj.put("amount",amount);
                            if(gstTaxSelected!=null){
                                if(gstTaxSelected.equalsIgnoreCase("CGST/SGST")){
                                    requestObj.put("amount_cgst_percent",gstTaxSelectedValue);
                                }
                                else {
                                    requestObj.put("amount_cgst_percent","0");
                                }
                                if(gstTaxSelected.equalsIgnoreCase("IGST")){
                                    requestObj.put("amount_igst_percent",gstTaxSelectedValue);
                                }
                                else {
                                    requestObj.put("amount_igst_percent","0");
                                }
                            }

                            if(radioBtnValue.equalsIgnoreCase("Product List")){
                                requestObj.put("invoice_for_amount","0");
                            }
                            else if(radioBtnValue.equalsIgnoreCase("GST")){
                                requestObj.put("invoice_for_amount","1");
                            }
                            else {
                                requestObj.put("invoice_for_amount","");
                            }

                            if(!data.isEmpty()){
                                sum = 0;
                                for(int i = 0;i < data.size(); i++){
                                    sum = sum + Integer.parseInt(data.get(i).getPrice());
                                    JSONObject data1 = new JSONObject();
                                    data1.put("id", data.get(i).getInvoiceId());
                                    data1.put("product_name", data.get(i).getName());
                                    data1.put("price", data.get(i).getPrice());
                                    data1.put("quantity", data.get(i).getQty());
                                    if(taxValue.equalsIgnoreCase("CGST/SGST")){
                                        if(cgstChecked.equalsIgnoreCase("0") && igstChecked.equalsIgnoreCase("0")){
                                            data1.put("cgst/sgst","0");
                                            data1.put("igst", "0");
                                        }
                                        else {
                                            data1.put("cgst/sgst", data.get(i).getMainTextValue());
                                            data1.put("igst", "0");
                                        }

                                    }
                                    else if(taxValue.equalsIgnoreCase("IGST")){
                                        if(cgstChecked.equalsIgnoreCase("0") && igstChecked.equalsIgnoreCase("0")){
                                            data1.put("cgst/sgst","0");
                                            data1.put("igst", "0");
                                        }
                                        else {
                                            data1.put("cgst/sgst","0");
                                            data1.put("igst",  data.get(i).getMainTextValue());
                                        }

                                    }
                                    else {
                                        data1.put("cgst/sgst","0");
                                        data1.put("igst", "0");
                                    }

                                    jsonArray.put(data1);
                                    requestObj.put("products_list", jsonArray);
                                }
                            }
                            else {
                                if(radioBtnValue.equalsIgnoreCase("Product List")){
                                    sum = Integer.parseInt(price.getText().toString());
                                    JSONObject data1 = new JSONObject();
                                    data1.put("id", id.getText().toString());
                                    data1.put("product_name", product.getText().toString());
                                    data1.put("price", price.getText().toString());
                                    data1.put("quantity", qty.getText().toString());
                                    if(taxValue.equalsIgnoreCase("CGST/SGST")){
                                        if(cgstChecked.equalsIgnoreCase("0") && igstChecked.equalsIgnoreCase("0")){
                                            data1.put("cgst/sgst","0");
                                            data1.put("igst", "0");
                                        }
                                        else {
                                            data1.put("cgst/sgst", mainTaxValue);
                                            data1.put("igst", "0");
                                        }

                                    }
                                    else if(taxValue.equalsIgnoreCase("IGST")){
                                        if(cgstChecked.equalsIgnoreCase("0") && igstChecked.equalsIgnoreCase("0")){
                                            data1.put("cgst/sgst","0");
                                            data1.put("igst", "0");
                                        }
                                        else {
                                            data1.put("cgst/sgst","0");
                                            data1.put("igst",  mainTaxValue);
                                        }

                                    }
                                    else {
                                        data1.put("cgst/sgst","0");
                                        data1.put("igst", "0");
                                    }

                                    jsonArray.put(data1);
                                    requestObj.put("products_list", jsonArray);
                                }
                                else {
                                    requestObj.put("products_list", jsonArray);
                                }

                            }

                            Log.d("summm", String.valueOf(sum));
                            request.put("request",requestObj);
                            final String mRequestBody = request.toString();
                            Log.d("req",mRequestBody);

                            if(!data.isEmpty() && rb.getText().toString().equalsIgnoreCase("Product List")){
                                if(flag == 1){
                                    if(sum > Integer.parseInt(amount) || sum < Integer.parseInt(amount)){
                                        Toast.makeText(CreateInvoiceActivity.this,"Product list total must be equal to total amount.",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        saveData(mRequestBody);
                                    }
                                }
                            }
                            else {
                                saveData(mRequestBody);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                });
                break;
            case R.id.create_invoice:
                onSaveButtonClicked();
                break;
            default:
                break;
        }


    }

    public void saveData(final String mRequestBody){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
    //    String url = "https://"+shopUrl + BASE_URL+"invoice/save";

        String url = Constant.REVAMP_URL1+ "customapi/invoicesave.php";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Saving Invoice.......");


        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        Toast.makeText(CreateInvoiceActivity.this,"Data uploaded successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreateInvoiceActivity.this, SendInvoiceDashboard.class);
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
        // Adding request to request queue
        MaintainRequestQueue.getInstance(CreateInvoiceActivity.this).addToRequestQueue(req, "tag");



    }


    public void getTaxes(){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"marketplace/getAllTaxClassess.php";

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);
        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading .......");
        progressDialog.setCancelable(false);


        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    invoiceMerchantIdModels = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length() > 0){
                        for (int i = 0; i <jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("label");
                            String value = jsonObject.getString("value");

                            merchantIdValue.add(name);
                            invoiceMerchantIdModels.add(new InvoiceMerchantIdModel(name,value));
                        }

                        mainAdapter = new ArrayAdapter<String>(CreateInvoiceActivity.this, R.layout.spinner_text_layout, merchantIdValue);
                        create_main_tax.setAdapter(mainAdapter);

                        gstTaxValue = new ArrayAdapter<String>(CreateInvoiceActivity.this, R.layout.spinner_text_layout, merchantIdValue);
                        gst_tax_value.setAdapter(gstTaxValue);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
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
        MaintainRequestQueue.getInstance(CreateInvoiceActivity.this).addToRequestQueue(req, "tag");



    }
}
