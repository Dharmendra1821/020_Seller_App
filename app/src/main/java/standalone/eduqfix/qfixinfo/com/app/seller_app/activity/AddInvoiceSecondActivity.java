package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;

import standalone.eduqfix.qfixinfo.com.app.seller_app.model.InvoiceProductList;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SellerInvoiceModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SellerInvoiceRequest;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class AddInvoiceSecondActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layout1,layout2,layout3,layout4,layout5,productListLayout;
    EditText id1,id2,id3,id4,id5;
    EditText product1,product2,product3,product4,product5;
    EditText price1,price2,price3,price4,price5;
    EditText qty1,qty2,qty3,qty4,qty5;
    EditText cgst1,cgst2,cgst3,cgst4,cgst5;
    EditText igst1,igst2,igst3,igst4,igst5;
    Button  productListBtn,addBtn,removeBtn,removeBtn2,removeBtn3,removeBtn4,removeBtn5;
    int count;
    CheckBox checkcgst1,checkcgst2,checkcgst3,checkcgst4,checkcgst5;
    CheckBox checkigst1,checkigst2,checkigst3,checkigst4,checkigst5;
    Button submit;
    int flag ;
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
    int productListVisible;
    int flagCopy;
    ViewSwitcher viewSwitcher;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    int sellerId;
    LinearLayout linear1,linear2;
    Button play;
    public static ArrayList<String> Ids = new ArrayList<>();
    public static ArrayList<String> removeIds = new ArrayList<>();

    public static ArrayList<String> getIds() {
        return Ids;
    }

    public static void setIds(ArrayList<String> ids) {
        Ids = ids;
    }

    public static ArrayList<String> getRemoveIds() {
        return removeIds;
    }

    public static void setRemoveIds(ArrayList<String> removeIds) {
        AddInvoiceSecondActivity.removeIds = removeIds;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice_second);

        Ids.clear();

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(AddInvoiceSecondActivity.this);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        LoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<LoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getCustomerDetails() != null ? loginResponse.getCustomerDetails().getId() : 0;

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


        layout1 = findViewById(R.id.add_invoice_layout1);
        layout2 = findViewById(R.id.add_invoice_layout2);
        layout3 = findViewById(R.id.add_invoice_layout3);
        layout4 = findViewById(R.id.add_invoice_layout4);
        layout5 = findViewById(R.id.add_invoice_layout5);
        //viewSwitcher = findViewById(R.id.viewSwitcher1);
      //  linear1 = findViewById(R.id.switcher_id);
     //   linear2 = findViewById(R.id.switcher_id2);
    //    play = findViewById(R.id.play);
      //  productListLayout = findViewById(R.id.product_list_layout);
        ////////////////////////////
        id1 = findViewById(R.id.add_invoice_id1);
        id2 = findViewById(R.id.add_invoice_id2);
        id3 = findViewById(R.id.add_invoice_id3);
        id4 = findViewById(R.id.add_invoice_id4);
        id5 = findViewById(R.id.add_invoice_id5);
        ////////////////////////////
        product1 = findViewById(R.id.add_invoice_product1);
        product2 = findViewById(R.id.add_invoice_product2);
        product3 = findViewById(R.id.add_invoice_product3);
        product4 = findViewById(R.id.add_invoice_product4);
        product5 = findViewById(R.id.add_invoice_product5);
        //////////////////////////
        price1 = findViewById(R.id.add_invoice_price1);
        price2 = findViewById(R.id.add_invoice_price2);
        price3 = findViewById(R.id.add_invoice_price3);
        price4 = findViewById(R.id.add_invoice_price4);
        price5 = findViewById(R.id.add_invoice_price5);
        ////////////////////////
        qty1 = findViewById(R.id.add_invoice_qty1);
        qty2 = findViewById(R.id.add_invoice_qty2);
        qty3 = findViewById(R.id.add_invoice_qty3);
        qty4 = findViewById(R.id.add_invoice_qty4);
        qty5 = findViewById(R.id.add_invoice_qty5);
        ////////////////////////
        cgst1 = findViewById(R.id.add_invoice_cgst1);
        cgst2 = findViewById(R.id.add_invoice_cgst2);
        cgst3 = findViewById(R.id.add_invoice_cgst3);
        cgst4 = findViewById(R.id.add_invoice_cgst4);
        cgst5 = findViewById(R.id.add_invoice_cgst5);
        ////////////////////////
        igst1 = findViewById(R.id.add_invoice_igst1);
        igst2 = findViewById(R.id.add_invoice_igst2);
        igst3 = findViewById(R.id.add_invoice_igst3);
        igst4 = findViewById(R.id.add_invoice_igst4);
        igst5 = findViewById(R.id.add_invoice_igst5);
        ////////////////////////
        productListBtn = findViewById(R.id.add_invoice_product_list);
        addBtn = findViewById(R.id.add_invoiceBtn);
        removeBtn = findViewById(R.id.remove_invoiceBtn);
        ////////////////////////////////////////////////////
        checkcgst1 = findViewById(R.id.checkbox_cgst1);
        checkcgst2 = findViewById(R.id.checkbox_cgst2);
        checkcgst3 = findViewById(R.id.checkbox_cgst3);
        checkcgst4 = findViewById(R.id.checkbox_cgst4);
        checkcgst5 = findViewById(R.id.checkbox_cgst5);
        //////////////////////////////////////////////////
        checkigst1 = findViewById(R.id.checkbox_igst1);
        checkigst2 = findViewById(R.id.checkbox_igst2);
        checkigst3 = findViewById(R.id.checkbox_igst3);
        checkigst4 = findViewById(R.id.checkbox_igst4);
        checkigst5 = findViewById(R.id.checkbox_igst5);
        ////////////////////////////////////////////////
        submit = findViewById(R.id.add_invoice_submit);

        count = 0;
        flag = 0;
        flagCopy = 0;
        productListVisible = 0;

        addBtn.setOnClickListener(this);
        removeBtn.setOnClickListener(this);
        productListBtn.setOnClickListener(this);

        checkcgst1.setOnClickListener(this);
        checkcgst2.setOnClickListener(this);
        checkcgst3.setOnClickListener(this);
        checkcgst4.setOnClickListener(this);
        checkcgst5.setOnClickListener(this);

        checkigst1.setOnClickListener(this);
        checkigst2.setOnClickListener(this);
        checkigst3.setOnClickListener(this);
        checkigst4.setOnClickListener(this);
        checkigst5.setOnClickListener(this);

        submit.setOnClickListener(this);

        cgst1.setEnabled(false);
        cgst2.setEnabled(false);
        cgst3.setEnabled(false);
        cgst4.setEnabled(false);
        cgst5.setEnabled(false);

        igst1.setEnabled(false);
        igst2.setEnabled(false);
        igst3.setEnabled(false);
        igst4.setEnabled(false);
        igst5.setEnabled(false);


        widgetData();

//        play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (productListVisible == 0){
//                    linear1.setVisibility(View.VISIBLE);
//                    linear2.setVisibility(View.GONE);
//                    productListVisible = 1;
//                }
//                else {
//                    linear2.setVisibility(View.VISIBLE);
//                    linear1.setVisibility(View.GONE);
//                    productListVisible = 0;
//                }
//            }
//        });


    }

    public void widgetData(){
        product1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}


            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


                        if(price1.getText().toString().length()!=0 && qty1.getText().toString().length()!=0
                                && s.length()!=0){
                            flag = Ids.size();
                        }
                        else {
                            flag = 0;
                        }

            }
        });
        price1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                    if (product1.getText().toString().length() != 0 && qty1.getText().toString().length() != 0
                            && s.length() != 0) {
                        flag = Ids.size();
                    }
                    else {
                        flag = 0;
                    }

            }
        });
        qty1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                    if (price1.getText().toString().length() != 0 && product1.getText().toString().length() != 0
                            && s.length() != 0) {
                       flag = Ids.size();;
                        Log.d("flag ..",String.valueOf(flag));
                }
                    else {
                        flag = 0;
                    }
            }
        });


        product2.addTextChangedListener(new TextWatcher() {


            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


                    if(price2.getText().toString().length()!=0 && qty2.getText().toString().length()!=0
                            && s.length()!=0){
                       flag = Ids.size();;
                    }
                    else {
                        flag =0;
                    }

            }
        });
        price2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                    if (product2.getText().toString().length() != 0 && qty2.getText().toString().length() != 0
                            && s.length() != 0) {
                       flag = Ids.size();;
                }
                    else {
                        flag = 0;
                    }
            }
        });
        qty2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                    if (price2.getText().toString().length() != 0 && product2.getText().toString().length() != 0
                            && s.length() != 0) {
                        flag = Ids.size();;
                    }
                    else {
                        flag =0;
                    }

            }
        });

        product3.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


                if(price3.getText().toString().length()!=0 && qty3.getText().toString().length()!=0
                        && s.length()!=0){
                    flag = Ids.size();;
                }
                else {
                    flag =0;
                }

            }
        });
        price3.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (product3.getText().toString().length() != 0 && qty3.getText().toString().length() != 0
                        && s.length() != 0) {
                    flag = Ids.size();;
                }
                else {
                    flag = 0;
                }
            }
        });
        qty3.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (price3.getText().toString().length() != 0 && product3.getText().toString().length() != 0
                        && s.length() != 0) {
                    flag = Ids.size();;
                }
                else {
                    flag =0;
                }

            }
        });

        product4.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


                if(price4.getText().toString().length()!=0 && qty4.getText().toString().length()!=0
                        && s.length()!=0){
                    flag = Ids.size();;
                }
                else {
                    flag =0;
                }

            }
        });
        price4.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (product4.getText().toString().length() != 0 && qty4.getText().toString().length() != 0
                        && s.length() != 0) {
                    flag = Ids.size();;
                }
                else {
                    flag = 0;
                }
            }
        });
        qty4.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (price4.getText().toString().length() != 0 && product4.getText().toString().length() != 0
                        && s.length() != 0) {
                    flag = Ids.size();;
                }
                else {
                    flag =0;
                }

            }
        });

        product5.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


                if(price5.getText().toString().length()!=0 && qty5.getText().toString().length()!=0
                        && s.length()!=0){
                    flag = Ids.size();;
                }
                else {
                    flag =0;
                }

            }
        });
        price5.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (product5.getText().toString().length() != 0 && qty5.getText().toString().length() != 0
                        && s.length() != 0) {
                    flag = Ids.size();;
                }
                else {
                    flag = 0;
                }
            }
        });
        qty5.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (price5.getText().toString().length() != 0 && product5.getText().toString().length() != 0
                        && s.length() != 0) {
                    flag = Ids.size();;
                }
                else {
                    flag =0;
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_invoiceBtn:
                if (Ids.size() < 5) {
                    count++;
                    flagCopy++;
                    Ids.add(String.valueOf(count));
                }
                Log.d("Ids....", String.valueOf(flagCopy));
                if (count == 1) {
                    layout1.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                }
                if (count == 2) {
                    layout2.setVisibility(View.VISIBLE);
                }
                if (count == 3) {
                    layout3.setVisibility(View.VISIBLE);
                }
                if (count == 4) {
                    layout4.setVisibility(View.VISIBLE);
                }
                if (count == 5) {
                    layout5.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.remove_invoiceBtn:
                if (!Ids.isEmpty()) {
                    int last = Integer.parseInt(Ids.get(Ids.size() - 1));
                    Log.d("last....", String.valueOf(last));
                    flagCopy--;
                    Ids.remove(String.valueOf(last));
                    count = last - 1;
                    if (last == 1) {
                        layout1.setVisibility(View.GONE);
                        submit.setVisibility(View.GONE);
                    }
                    if (last == 2) {
                        layout2.setVisibility(View.GONE);
                    }
                    if (last == 3) {
                        layout3.setVisibility(View.GONE);
                    }
                    if (last == 4) {
                        layout4.setVisibility(View.GONE);
                    }
                    if (last == 5) {
                        layout5.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.add_invoice_product_list:
                if (Ids.isEmpty()) {
                    count++;
                    flagCopy++;
                    Ids.add(String.valueOf(count));
                }
                submit.setVisibility(View.VISIBLE);
                addBtn.setVisibility(View.VISIBLE);
                removeBtn.setVisibility(View.VISIBLE);
                layout1.setVisibility(View.VISIBLE);

                break;
            case R.id.checkbox_cgst1:
                checkcgst1.setChecked(true);
                checkigst1.setChecked(false);
                cgst1.setEnabled(true);
                igst1.setEnabled(false);
                break;
            case R.id.checkbox_cgst2:
                checkcgst2.setChecked(true);
                checkigst2.setChecked(false);
                cgst2.setEnabled(true);
                igst2.setEnabled(false);
                break;
            case R.id.checkbox_cgst3:
                checkcgst3.setChecked(true);
                checkigst3.setChecked(false);
                cgst3.setEnabled(true);
                igst3.setEnabled(false);
                break;
            case R.id.checkbox_cgst4:
                checkcgst4.setChecked(true);
                checkigst4.setChecked(false);
                cgst4.setEnabled(true);
                igst4.setEnabled(false);
                break;
            case R.id.checkbox_cgst5:
                checkcgst5.setChecked(true);
                checkigst5.setChecked(false);
                cgst5.setEnabled(true);
                igst5.setEnabled(false);
                break;
            case R.id.checkbox_igst1:
                checkigst1.setChecked(true);
                checkcgst1.setChecked(false);
                igst1.setEnabled(true);
                cgst1.setEnabled(false);
                break;
            case R.id.checkbox_igst2:
                checkigst2.setChecked(true);
                checkcgst2.setChecked(false);
                igst2.setEnabled(true);
                cgst2.setEnabled(false);
                break;
            case R.id.checkbox_igst3:
                checkigst3.setChecked(true);
                checkcgst3.setChecked(false);
                igst3.setEnabled(true);
                cgst3.setEnabled(false);
                break;
            case R.id.checkbox_igst4:
                checkigst4.setChecked(true);
                checkcgst4.setChecked(false);
                igst4.setEnabled(true);
                cgst4.setEnabled(false);
                break;
            case R.id.checkbox_igst5:
                checkigst5.setChecked(true);
                checkcgst5.setChecked(false);
                igst5.setEnabled(true);
                cgst5.setEnabled(false);
                break;
            case R.id.add_invoice_submit:
                  Log.d("Ids", String.valueOf(Ids.size()));
                  Log.d("flag", String.valueOf(flag));
                if(Ids.size() != flag){
                    Toast.makeText(this, "All the fields against the id are required to fill", Toast.LENGTH_SHORT).show();
                }
                else {
                 //   Toast.makeText(this, "All done", Toast.LENGTH_SHORT).show();
                    getPayload();

                }
                    break;
                }

        }

        public void getPayload(){
            SellerInvoiceRequest sellerInvoiceRequest = new SellerInvoiceRequest();
            ArrayList<InvoiceProductList> invoiceProductLists = new ArrayList<>();
                InvoiceProductList invoiceProductList = new InvoiceProductList();
                invoiceProductList.setId("1");
                invoiceProductList.setProduct_name(product1.getText().toString());
                invoiceProductList.setPrice(price1.getText().toString());
                invoiceProductList.setQuantity(qty1.getText().toString());
                invoiceProductList.setCgst(cgst1.getText().toString());
                invoiceProductList.setIgst(igst1.getText().toString());
                invoiceProductLists.add(invoiceProductList);
                InvoiceProductList invoiceProductList2 = new InvoiceProductList();
                invoiceProductList2.setId("2");
                invoiceProductList2.setProduct_name(product2.getText().toString());
                invoiceProductList2.setPrice(price2.getText().toString());
                invoiceProductList2.setQuantity(qty2.getText().toString());
                invoiceProductList2.setCgst(cgst2.getText().toString());
                invoiceProductList2.setIgst(igst2.getText().toString());
                invoiceProductLists.add(invoiceProductList2);
                InvoiceProductList invoiceProductList3 = new InvoiceProductList();
                invoiceProductList3.setId("3");
                invoiceProductList3.setProduct_name(product3.getText().toString());
                invoiceProductList3.setPrice(price3.getText().toString());
                invoiceProductList3.setQuantity(qty3.getText().toString());
                invoiceProductList3.setCgst(cgst3.getText().toString());
                invoiceProductList3.setIgst(igst3.getText().toString());
                invoiceProductLists.add(invoiceProductList3);
                InvoiceProductList invoiceProductList4 = new InvoiceProductList();
                invoiceProductList4.setId("4");
                invoiceProductList4.setProduct_name(product4.getText().toString());
                invoiceProductList4.setPrice(price4.getText().toString());
                invoiceProductList4.setQuantity(qty4.getText().toString());
                invoiceProductList4.setCgst(cgst4.getText().toString());
                invoiceProductList4.setIgst(igst4.getText().toString());
                invoiceProductLists.add(invoiceProductList4);
                InvoiceProductList invoiceProductList5 = new InvoiceProductList();
                invoiceProductList5.setId("5");
                invoiceProductList5.setProduct_name(product5.getText().toString());
                invoiceProductList5.setPrice(price5.getText().toString());
                invoiceProductList5.setQuantity(qty5.getText().toString());
                invoiceProductList5.setCgst(cgst5.getText().toString());
                invoiceProductList5.setIgst(igst5.getText().toString());
                invoiceProductLists.add(invoiceProductList5);
                sellerInvoiceRequest.setProducts_list(invoiceProductLists);
                Gson gson = new Gson();
                String initiateData = gson.toJson(sellerInvoiceRequest);
              //  Log.i("InitiateData","InitiateData"+initiateData);

            JSONObject request = new JSONObject();
            JSONObject requestObj = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            JSONArray jsonArray = new JSONArray();
                    for (int i =0 ; i < Ids.size(); i++){

                try {
                    JSONObject data = new JSONObject();
                    data.put("id", invoiceProductLists.get(i).getId());
                    data.put("product_name", invoiceProductLists.get(i).getProduct_name());
                    data.put("price", invoiceProductLists.get(i).getPrice());
                    data.put("quantity", invoiceProductLists.get(i).getQuantity());
                    data.put("cgst/sgst", invoiceProductLists.get(i).getCgst());
                    data.put("igst", "0");
                    jsonArray.put(data);
                    requestObj.put("products_list", jsonArray);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            try {
                requestObj.put("seller_id",String.valueOf(sellerId));
                requestObj.put("customer_name",customerName);
                requestObj.put("company_name",companyName);
                requestObj.put("gstin",gstIn);
                requestObj.put("invoice_date",invoiceDate);
                requestObj.put("address",address);
                requestObj.put("mobile_number",mobileNumber);
                requestObj.put("email",emailId);
                requestObj.put("description",description);
                requestObj.put("expiry_date",expiryDate);
                requestObj.put("amount",amount);
                requestObj.put("amount_cgst_percent","");
                requestObj.put("amount_igst_percent","");
                requestObj.put("invoice_for_amount","1");
                request.put("request",requestObj);
                final String mRequestBody = request.toString();
                Log.d("req",mRequestBody);

               // saveData(mRequestBody);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    public void saveData(final String mRequestBody){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl + BASE_URL+"invoice/save";

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
                    int error = jsonObject.getInt("error");
                    if(error == 0){
                        Toast.makeText(AddInvoiceSecondActivity.this,"Data uploaded successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddInvoiceSecondActivity.this,SendInvoiceDashboard.class);
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
        MaintainRequestQueue.getInstance(AddInvoiceSecondActivity.this).addToRequestQueue(req, "tag");



    }

        }
