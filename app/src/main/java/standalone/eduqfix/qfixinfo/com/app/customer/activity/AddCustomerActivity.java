package standalone.eduqfix.qfixinfo.com.app.customer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.customer.model.Customers;
import standalone.eduqfix.qfixinfo.com.app.customer.model.CustomerResponse;
import standalone.eduqfix.qfixinfo.com.app.customer.model.Data;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

import static android.content.Context.MODE_PRIVATE;

public class AddCustomerActivity extends AppCompatActivity {

    EditText firstNameEditText,lastNameEditText,emailEditText,passwordEditText,mobileEdittext;
    Button addCustomerButton;
    SharedPreferences sharedPreferences;
    LoginResponse customerDetails;
    Boolean isFromSearchFragment = null;
    String institudeId;
    String branchId;
    int sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String details = sharedPreferences.getString("CustomerDetails",null);
        customerDetails = new Gson().fromJson(details, new TypeToken<LoginResponse>() {}.getType());
        isFromSearchFragment = getIntent().getBooleanExtra("isFromSearchFragment",false);

        firstNameEditText =   findViewById(R.id.firstNameEditText);
        lastNameEditText  =   findViewById(R.id.lastNameEditText);
        emailEditText     =   findViewById(R.id.emailEditText);
        mobileEdittext    =   findViewById(R.id.mobileNumber);
        passwordEditText  =   findViewById(R.id.passwordEditText);
        addCustomerButton =   findViewById(R.id.addCustomerButton);

        String customerDetails = sharedPreferences.getString("CustomerDetails", null);
        NewLoginResponse loginResponse = new Gson().fromJson(customerDetails, new TypeToken<NewLoginResponse>() {
        }.getType());
        sellerId = loginResponse != null && loginResponse.getSellerDetails().getSeller_id() != null ? loginResponse.getSellerDetails().getSeller_id() : 0;

//        String customerDetailJson = sharedPreferences.getString("customer_json", "");
//        try {
//            JSONObject jsonObject = new JSONObject(customerDetailJson);
//            JSONObject jsonObject1 = jsonObject.getJSONObject("customerDetails");
//            int id = jsonObject1.getInt("id");
//            org.json.JSONArray jsonArray = jsonObject1.getJSONArray("custom_attributes");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
//                String attribute_code = jsonObject2.getString("attribute_code");
//                if(attribute_code.equalsIgnoreCase("institute_code")){
//                     institudeId = jsonObject2.getString("value");
//                }
//                if(attribute_code.equalsIgnoreCase("branch_code")){
//                     branchId = jsonObject2.getString("value");
//                }
//            }
//        }catch (Exception e){
//        }
            addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomer();
            }
        });
    }

    public void addCustomer(){
        String firstName =  firstNameEditText.getText().toString();
        String lastName  =  lastNameEditText.getText().toString();
        String email     =  emailEditText.getText().toString().trim();
        String mobile    =  mobileEdittext.getText().toString().trim();
        String password  =  "Pass@123";
       // Integer sellerId =  customerDetails.getCustomerDetails().getId();

        if(isValidCustomerData(firstName,lastName,email,mobile)){
            Data data = new Data();
            data.setFirst_name(firstName);
            data.setLast_name(lastName);
            data.setEmail(email);
            data.setMobileno(mobile);
            data.setPassword(password);
            data.setConfirm_password(password);
            data.setInstituteId("1670");
            data.setBranchId("4203");
            data.setSellerId(String.valueOf(sellerId));
            createCustomer(data);
        }else{
            Toast.makeText(AddCustomerActivity.this,"All fields are required",Toast.LENGTH_LONG).show();
            addCustomerButton.setEnabled(false);
        }
    }

    public Boolean isValidCustomerData(String firstName,String lastName,String email,String  mobile){
        Boolean flag = false;
        if(!TextUtils.isEmpty(firstName)){
            flag = true;
        }
        if(!TextUtils.isEmpty(lastName)){
            flag = true;
        }
        if(!TextUtils.isEmpty(email)){
            flag = true;
        }
        if(!TextUtils.isEmpty(mobile)){
            flag = true;
        }
        return flag;
    }

    public void createCustomer(Data data){
        try{
            final ProgressDialog progressDialog = new ProgressDialog(AddCustomerActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
            MPOSServices mposServices = retrofit.create(MPOSServices.class);
            Customers customer = new Customers();
            customer.setData(data);
            Gson gson = new Gson();
            String gsonData = gson.toJson(data);
            Log.d("GSONData=====","GSONData====="+gsonData);

            mposServices.createCustomer(data).enqueue(new Callback<CustomerResponse>() {
                @Override
                public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                    if(response.code() == 200 || response.isSuccessful()){
                        progressDialog.dismiss();
                        CustomerResponse customerResponse = response.body();
                        Gson gson = new Gson();
                        String newCustomer = gson.toJson(customerResponse);
                        Log.d("new customer....",newCustomer);
                        try {
                            JSONObject jsonObject = new JSONObject(newCustomer);
                            String message = jsonObject.getString("message");
                            if(message.equalsIgnoreCase("Duplicate Data")){
                                Toast.makeText(AddCustomerActivity.this,"A customer with the same detail already exists.",Toast.LENGTH_LONG).show();
                            }
                            else {
                                sharedPreferences.edit().putInt("CustomerId", customerResponse.getEntityId()).apply();
                                sharedPreferences.edit().putString("newCustomer",newCustomer).apply();
                                Intent intent = new Intent(AddCustomerActivity.this,AddCustomerAddressActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else if(response.code() == 400){
                        progressDialog.dismiss();
                        Toast.makeText(AddCustomerActivity.this,"A customer with the same email already exists in an associated website.",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 401){
                        progressDialog.dismiss();
                        Toast.makeText(AddCustomerActivity.this,"Unauthorized",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        progressDialog.dismiss();
                        Toast.makeText(AddCustomerActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(AddCustomerActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<CustomerResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(AddCustomerActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

   /* @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity,menu);
        MenuItem item = menu.findItem(R.id.menu_item_cart);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i){
            case R.id.menu_item_logout:
                sharedPreferences.edit().clear().apply();
                Intent intent = new Intent(AddCustomerActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }*/

}
