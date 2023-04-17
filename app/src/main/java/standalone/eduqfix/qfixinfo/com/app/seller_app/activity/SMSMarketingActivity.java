package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.seller_app.fragments.BottomSheeetFragment;
import standalone.eduqfix.qfixinfo.com.app.seller_app.fragments.SMSContactListFragment;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSContactModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSMarketingModel;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class SMSMarketingActivity extends AppCompatActivity implements View.OnClickListener, SMSContactListFragment.ClickListener, SMSContactListFragment.closeClickListener {

    SearchableSpinner template;
    ArrayList<String> templateValue;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    ArrayList<SMSMarketingModel> smsMarketingModels;
    String smsTemplate;
    TextView tagline;
    String shopUrl;
    String location,productName,discount;
    EditText locationEdit,messageEdit,discountEdit,shopUrlEdit,productEdit;
    LinearLayout date_layout,time_layout;
    TextView dateText,timeText;
    int mYear,mMonth,mDay,mHour,mMinute;
    View sms_separator;
    Button submit;
    SMSContactListFragment smsContactListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_m_s_marketing);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        shopUrl  = sharedPreferences.getString("shop_url",null);
        progressDialog = new ProgressDialog(SMSMarketingActivity.this);
        template = findViewById(R.id.sms_template);
        tagline = findViewById(R.id.sms_marketing_tag);
        locationEdit = findViewById(R.id.sms_marketing_location);
        messageEdit  = findViewById(R.id.sms_marketing_message);
        date_layout = findViewById(R.id.sms_marketing_datelayout);
        time_layout = findViewById(R.id.sms_marketing_timelayout);
        timeText    = findViewById(R.id.sms_marketing_time);
        dateText    = findViewById(R.id.sms_marketing_date);
        discountEdit = findViewById(R.id.sms_marketing_discount);
        productEdit = findViewById(R.id.sms_marketing_productname);
        shopUrlEdit = findViewById(R.id.sms_marketing_shopurl);
        sms_separator = findViewById(R.id.sms_separator);
        submit = findViewById(R.id.sms_template_submit);

        messageEdit.setEnabled(false);
        shopUrlEdit.setEnabled(false);

        shopUrlEdit.setText(shopUrl);

        templateValue = new ArrayList<>();

        getTemplate("1");

        template.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("get Item",smsMarketingModels.get(position).getSmsId());
                smsTemplate = parent.getSelectedItem().toString();
                if(smsTemplate.equalsIgnoreCase("offer")){
                    tagline.setText("");
                    messageEdit.setText("");
                    tagline.setText("Hey you Want to buy "+productEdit.getText().toString()+" , are now online. Stay home and get your orders. "+shopUrl+" .eduqfix.com shop now");
                    messageEdit.setText("Hey you Want to buy "+productEdit.getText().toString()+" , are now online. Stay home and get your orders. "+shopUrl+" .eduqfix.com shop now");
                    locationEdit.setVisibility(View.GONE);
                    discountEdit.setVisibility(View.GONE);
                    date_layout.setVisibility(View.GONE);
                    time_layout.setVisibility(View.GONE);
                    sms_separator.setVisibility(View.GONE);
                    productEdit.setVisibility(View.VISIBLE);
                }
                else {
                    tagline.setText(shopUrl+" "+locationEdit.getText().toString()+" Join us for our grand opening this"+dateText.getText().toString()+" at "+ timeText.getText().toString()+" for "+discountEdit.getText().toString()+" off.");
                    messageEdit.setText(shopUrl+" "+locationEdit.getText().toString()+" Join us for our grand opening this"+dateText.getText().toString()+" at "+ timeText.getText().toString()+" for "+discountEdit.getText().toString()+" off.");
                    locationEdit.setVisibility(View.VISIBLE);
                    discountEdit.setVisibility(View.VISIBLE);
                    date_layout.setVisibility(View.VISIBLE);
                    time_layout.setVisibility(View.VISIBLE);
                    sms_separator.setVisibility(View.VISIBLE);
                    productEdit.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tagline.setText(shopUrl+" Join us for our grand opening this {Date} at {Time} for { Percentage} off.");
        messageEdit.setText(shopUrl+" Join us for our grand opening this {Date} at {Time} for { Percentage} off.");

        locationEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    tagline.setText(shopUrl+" "+location+" Join us for our grand opening this {Date} at {Time} for { Percentage} off.");
                    messageEdit.setText(shopUrl+" "+location+" Join us for our grand opening this {Date} at {Time} for { Percentage} off.");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                location = String.valueOf(s);
                tagline.setText(shopUrl+" "+locationEdit.getText().toString()+" Join us for our grand opening this"+dateText.getText().toString()+" at "+ timeText.getText().toString()+" for "+discountEdit.getText().toString()+" off.");
                messageEdit.setText(shopUrl+" "+locationEdit.getText().toString()+" Join us for our grand opening this"+dateText.getText().toString()+" at "+ timeText.getText().toString()+" for "+discountEdit.getText().toString()+" off.");

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        discountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tagline.setText(shopUrl+" "+location+" Join us for our grand opening this {Date} at {Time} for { Percentage} off.");
                messageEdit.setText(shopUrl+" "+location+" Join us for our grand opening this {Date} at {Time} for { Percentage} off.");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                discount = String.valueOf(s);
                tagline.setText(shopUrl+" "+locationEdit.getText().toString()+" Join us for our grand opening this"+dateText.getText().toString()+" at "+ timeText.getText().toString()+" for "+discountEdit.getText().toString()+" off.");
                messageEdit.setText(shopUrl+" "+locationEdit.getText().toString()+" Join us for our grand opening this"+dateText.getText().toString()+" at "+ timeText.getText().toString()+" for "+discountEdit.getText().toString()+" off.");

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        productEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tagline.setText("Hey you Want to buy "+productEdit.getText().toString()+" , are now online. Stay home and get your orders. "+shopUrl+" .eduqfix.com shop now");
                messageEdit.setText("Hey you Want to buy "+productEdit.getText().toString()+" , are now online. Stay home and get your orders. "+shopUrl+" .eduqfix.com shop now");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productName = String.valueOf(s);
                tagline.setText("Hey you Want to buy "+productEdit.getText().toString()+" , are now online. Stay home and get your orders. "+shopUrl+" .eduqfix.com shop now");
                messageEdit.setText("Hey you Want to buy "+productEdit.getText().toString()+" , are now online. Stay home and get your orders. "+shopUrl+" .eduqfix.com shop now");

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        date_layout.setOnClickListener(this);
        time_layout.setOnClickListener(this);
        submit.setOnClickListener(this);

        SMSContactListFragment.newInstance().setOnItemClickListener(this);
        SMSContactListFragment.newInstance().setOnCloseClickListener(this);

    }

    public void getTemplate(final String value){

        shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = "https://"+shopUrl + SKU_URL+"qfix-sms/sms/search?searchCriteria="+value;

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Template.......");

        String adminToken = sharedPreferences.getString("adminToken",null);
        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try{
                    smsMarketingModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray items = jsonObject.getJSONArray("items");
                    if(items.length() > 0){
                        for (int i=0; i < items.length(); i++){
                            JSONObject jsonObject1 = items.getJSONObject(i);
                            String smsId = jsonObject1.getString("sms_id");
                            String code = jsonObject1.getString("code");
                            String senderName = jsonObject1.getString("sender_name");

                            templateValue.add(code);
                            smsMarketingModels.add(new SMSMarketingModel(smsId,code,senderName));

                        }
                        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(SMSMarketingActivity.this, R.layout.spinner_text_layout, templateValue);
                        template.setAdapter(statusAdapter);

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","Bearer "+adminToken);
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
        MaintainRequestQueue.getInstance(SMSMarketingActivity.this).addToRequestQueue(req, "tag");



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sms_marketing_datelayout:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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
                                dateText.setText(dayMonth + "-" + monthString + "-" + year);
                                tagline.setText(shopUrl+" "+locationEdit.getText().toString()+" Join us for our grand opening this"+dateText.getText().toString()+" at "+ timeText.getText().toString()+" for "+discountEdit.getText().toString()+" off.");
                                messageEdit.setText(shopUrl+" "+locationEdit.getText().toString()+" Join us for our grand opening this"+dateText.getText().toString()+" at "+ timeText.getText().toString()+" for "+discountEdit.getText().toString()+" off.");
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setMinDate(c.getTimeInMillis());
                break;
            case R.id.sms_marketing_timelayout:
                final Calendar c1 = Calendar.getInstance();
                mHour = c1.get(Calendar.HOUR_OF_DAY);
                mMinute = c1.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String hourString = String.valueOf((hourOfDay));
                                if (hourString.length() == 1) {
                                    hourString = "0" + hourString;
                                }
                                String minString = String.valueOf((minute));
                                if (minString.length() == 1) {
                                    minString = "0" + minString;
                                }
                                timeText.setText(hourString + ":" + minString);
                                tagline.setText(shopUrl+" "+locationEdit.getText().toString()+" Join us for our grand opening this"+dateText.getText().toString()+" at "+ timeText.getText().toString()+" for "+discountEdit.getText().toString()+" off.");
                                messageEdit.setText(shopUrl+" "+locationEdit.getText().toString()+" Join us for our grand opening this"+dateText.getText().toString()+" at "+ timeText.getText().toString()+" for "+discountEdit.getText().toString()+" off.");

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.sms_template_submit:

                Intent intent = new Intent(SMSMarketingActivity.this,SMSContactListActivity.class);
                intent.putExtra("message",messageEdit.getText().toString());
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onItemClick(View v, String fromDate, String toDate) {

    }

    @Override
    public void onCloseClick(View v) {

    }
}
