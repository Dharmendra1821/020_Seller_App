package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DeliveryBoyOrderActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.adapters.DeliveryboyListAdapter;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyList;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyOrder;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.CreateInvoiceActivity;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.model.InvoiceMerchantIdModel;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

public class AddInvoiceActivity extends AppCompatActivity implements View.OnClickListener {

    EditText customer_name,company_name,gstin,address,mobile_number,email_id,amount,description;
    LinearLayout invoice_layout,expiry_layout;
    TextView invoice_date_txt,expiry_date_txt;
    Button invoice_nextBtn;
    int  mYear,mMonth,mDay;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String regex = "^[0-9]{2}[A-Z]{5}[0-9]{4}"
            + "[A-Z]{1}[1-9A-Z]{1}"
            + "Z[0-9A-Z]{1}$";

    Pattern pattern;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    int sellerId;
    SearchableSpinner merchantId;
    ArrayList<String> merchantIdValue;
    String merchantIdData;
    ArrayList<InvoiceMerchantIdModel> invoiceMerchantIdModels;
    Button help_document;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);
        sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;
        merchantIdValue = new ArrayList<>();


        customer_name      = findViewById(R.id.invoice_customer_name);
        company_name       = findViewById(R.id.invoice_company_name);
        gstin              = findViewById(R.id.invoice_gstin);
        address            = findViewById(R.id.invoice_address);
        mobile_number      = findViewById(R.id.invoice_mpbile_no);
        email_id           = findViewById(R.id.invoice_email_id);
        amount             = findViewById(R.id.invoice_amount);
        description        = findViewById(R.id.invoice_description);
        merchantId         = findViewById(R.id.invoice_merchant_id);
        help_document = findViewById(R.id.help_document);

        invoice_layout = findViewById(R.id.invoice_date_layout);
        expiry_layout = findViewById(R.id.expiry_date_layout);
        invoice_date_txt = findViewById(R.id.invoice_date);
        expiry_date_txt = findViewById(R.id.invoice_expiry_date);
        invoice_nextBtn = findViewById(R.id.invoice_next_Btn);

        invoice_nextBtn.setOnClickListener(this);
        invoice_layout.setOnClickListener(this);
        expiry_layout.setOnClickListener(this);
        pattern = Pattern.compile(regex);
        progressDialog = new ProgressDialog(AddInvoiceActivity.this);
        gstin.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        gstin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable arg) {

                String gstIN = arg.toString();
                if(!gstIN.equals(gstIN.toUpperCase())){
                    gstIN = gstIN.toUpperCase();


                }

            }
        });

        getTIds(String.valueOf(sellerId));

        merchantIdData = "";

        merchantId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                for(InvoiceMerchantIdModel invoiceMerchantIdModel : invoiceMerchantIdModels){
                    if (!adapterView.getSelectedItem().toString().equalsIgnoreCase("Select Merchant TID'S")) {
                        merchantIdData = invoiceMerchantIdModels.get(i-1).getValue();
                        Log.d("merchabtId ",merchantIdData);
                    }

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        help_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cdn.eduqfix.com/shop/static/pub/media/Invoice_Based_Payment_flow.pdf"));
                startActivity(browserIntent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.invoice_next_Btn:
                Matcher m = pattern.matcher(gstin.getText().toString());
                if(customer_name.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(AddInvoiceActivity.this, "Please enter only alphabets or spaces", Toast.LENGTH_SHORT).show();
                }
//                else if(!m.matches() && gstin.getText().toString().length() > 0){
//                    Toast.makeText(AddInvoiceActivity.this, "Enter valid GSTIN number", Toast.LENGTH_SHORT).show();
//                }
                else if(invoice_date_txt.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(AddInvoiceActivity.this, "Please enter a valid date", Toast.LENGTH_SHORT).show();
                }
                else if(mobile_number.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(AddInvoiceActivity.this, "Mobile number should be of 10 digits only", Toast.LENGTH_SHORT).show();
                }
                else if(mobile_number.getText().toString().length() < 10){
                    Toast.makeText(AddInvoiceActivity.this, "Mobile number should be of 10 digits only", Toast.LENGTH_SHORT).show();
                }
                else if(email_id.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(AddInvoiceActivity.this, "Please enter valid email ID", Toast.LENGTH_SHORT).show();
                }
                else if(!email_id.getText().toString().matches(emailPattern)){
                    Toast.makeText(AddInvoiceActivity.this, "Please enter valid email ID", Toast.LENGTH_SHORT).show();
                }
                else if(amount.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(AddInvoiceActivity.this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                }
                else if(expiry_date_txt.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(AddInvoiceActivity.this, "Please enter expiry date", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("merchabtId ",merchantIdData);
                    Intent intent = new Intent(AddInvoiceActivity.this, CreateInvoiceActivity.class);
                    intent.putExtra("customer_name",customer_name.getText().toString());
                    intent.putExtra("company_name",company_name.getText().toString());
                    intent.putExtra("gstin",gstin.getText().toString());
                    intent.putExtra("address",address.getText().toString());
                    intent.putExtra("mobile_number",mobile_number.getText().toString());
                    intent.putExtra("email_id",email_id.getText().toString());
                    intent.putExtra("amount",amount.getText().toString());
                    intent.putExtra("description",description.getText().toString());
                    intent.putExtra("invoice_date",invoice_date_txt.getText().toString());
                    intent.putExtra("merchant_id",merchantIdData);
                    intent.putExtra("expiry_date",expiry_date_txt.getText().toString());
                    startActivity(intent);

                }
                break;
            case R.id.expiry_date_layout:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddInvoiceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String monthString = String.valueOf((monthOfYear + 1));
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }
                                String dayMonth = String.valueOf(dayOfMonth);
                                if (dayMonth.length() == 1) {
                                    dayMonth = "0" + dayMonth;
                                }
                                expiry_date_txt.setText(dayMonth + "-" + monthString + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setMinDate(c.getTimeInMillis());
                break;
            case R.id.invoice_date_layout:
                final Calendar c1 = Calendar.getInstance();
                mYear = c1.get(Calendar.YEAR);
                mMonth = c1.get(Calendar.MONTH);
                mDay = c1.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(AddInvoiceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String monthString = String.valueOf((monthOfYear + 1));
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }
                                String dayMonth = String.valueOf(dayOfMonth);
                                if (dayMonth.length() == 1) {
                                    dayMonth = "0" + dayMonth;
                                }
                                invoice_date_txt.setText(dayMonth + "-" + monthString + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog1.show();
                DatePicker datePicker1 = datePickerDialog1.getDatePicker();
                datePicker1.setMinDate(c1.getTimeInMillis());
                break;
        }
    }

    public void getTIds(final String sellerId){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"customapi/getMerchantTids.php?seller_id="+sellerId;

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
                            String name = jsonObject.getString("option_name");
                            String value = jsonObject.getString("option_value");

                            merchantIdValue.add(value);
                            invoiceMerchantIdModels.add(new InvoiceMerchantIdModel(name,value));
                        }
                        merchantIdValue.add(0, "Select Merchant TID'S");

                        final ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(AddInvoiceActivity.this, R.layout.spinner_text_layout, merchantIdValue);
                        merchantId.setAdapter(statusAdapter);

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
        MaintainRequestQueue.getInstance(AddInvoiceActivity.this).addToRequestQueue(req, "tag");



    }


}
