package standalone.eduqfix.qfixinfo.com.app.customer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.customer.model.AddAddressRequest;
import standalone.eduqfix.qfixinfo.com.app.customer.model.AddAddressResponse;
import standalone.eduqfix.qfixinfo.com.app.customer.model.Address;
import standalone.eduqfix.qfixinfo.com.app.customer.model.Addresses;
import standalone.eduqfix.qfixinfo.com.app.customer.model.Customer;
import standalone.eduqfix.qfixinfo.com.app.customer.model.CustomerDetails;
import standalone.eduqfix.qfixinfo.com.app.customer.model.CustomerResponse;
import standalone.eduqfix.qfixinfo.com.app.customer.model.Region;
import standalone.eduqfix.qfixinfo.com.app.customer.model.State;

import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.SellerDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

import static android.content.Context.MODE_PRIVATE;

public class AddCustomerAddressActivity extends AppCompatActivity {

    EditText address1EditText,address2EditText,cityEditText,pincodeEditText,telephoneEditText;
    SearchableSpinner stateSpinner;
    Button nextButton;
    List<State> stateList = null;
    String stateName,code,region_id;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog = null;
    Boolean isFromSearchFragment = null;
    Gson gson = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_address);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);

        address1EditText = findViewById(R.id.address1EditText);
        address2EditText = findViewById(R.id.address2EditText);
        cityEditText = findViewById(R.id.cityEditText);
        pincodeEditText = findViewById(R.id.pincodeEditText);
        telephoneEditText = findViewById(R.id.telephoneEditText);
        stateSpinner = findViewById(R.id.stateSpinner);
        nextButton = findViewById(R.id.nextButton);

        getStateList();

        stateSpinner.setTitle("Select State");

        isFromSearchFragment = getIntent().getBooleanExtra("isFromSearchFragment",false);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateName = parent.getSelectedItem().toString();
                for(State state : stateList){
                    if(state.getName().equalsIgnoreCase(stateName)){

                        region_id = state.getRegionId();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCustomer = sharedPreferences.getString("selectedCustomerDetails",null);
                String newCustomer = sharedPreferences.getString("newCustomer",null);

                if(selectedCustomer==null&& newCustomer==null){
                    Toast.makeText(AddCustomerAddressActivity.this,"Please search customer first",Toast.LENGTH_SHORT).show();
                }
                else if(newCustomer!=null){
                    saveAddressForNewCustomer();
                }
                else {
                    saveAddressForCustomer();
                }
            }
        });
    }

    public void getStateList(){
        try{

            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
            MPOSServices mposServices = retrofit.create(MPOSServices.class);

            mposServices.getStateList().enqueue(new Callback<List<State>>() {
                @Override
                public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                    if(response.code() == 200 || response.isSuccessful()){
                        stateList = response.body();
                        List<String> stateNameList = new ArrayList<>();
                        for(State state : stateList){
                            stateNameList.add(state.getName());
                        }
                        ArrayAdapter<String> stateAdapter= new ArrayAdapter<String>(AddCustomerAddressActivity.this,android.R.layout.simple_list_item_1, stateNameList);
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        stateSpinner.setAdapter(stateAdapter);
                    }else if(response.code() == 400){
                        Toast.makeText(AddCustomerAddressActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 401){
                        Toast.makeText(AddCustomerAddressActivity.this,"Unauthorized",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        Toast.makeText(AddCustomerAddressActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<State>> call, Throwable t) {

                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    public void saveAddressForCustomer(){
        try{
            progressDialog = new ProgressDialog(AddCustomerAddressActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();


            String newCustomerDetails = sharedPreferences.getString("newCustomer",null);
            CustomerResponse customerDetails = new Gson().fromJson(newCustomerDetails, new TypeToken<CustomerResponse>() {}.getType());

            String selectedCustomer = sharedPreferences.getString("selectedCustomerDetails",null);
            CustomerDetails custDetails = new Gson().fromJson(selectedCustomer,new TypeToken<CustomerDetails>(){}.getType());

            List<Addresses> custAddress = custDetails != null ? custDetails.getItems().get(0).getAddresses() : null;

            Customer customer = new Customer();
            Address address = new Address();
            Region region = new Region();
            final List<Address> addresses = new ArrayList<>();
            List<String> streetList = new ArrayList<>();
            AddAddressRequest addAddressRequest = new AddAddressRequest();

            String addressLine1 = address1EditText.getText().toString();
            String addressLine2 = address2EditText.getText().toString();
            String city = cityEditText.getText().toString();
            String pincode = pincodeEditText.getText().toString();
            String telephone = telephoneEditText.getText().toString();

            customer.setId(customerDetails != null ? customerDetails.getEntityId() : custDetails.getItems().get(0).getId());
            customer.setGroupId(0);
            customer.setDefaultBilling("");
            customer.setDefaultShipping("");
            customer.setConfirmation("");
            customer.setCreatedAt("");
            customer.setUpdatedAt("");
            customer.setCreatedIn("");
            customer.setDob("");
            customer.setEmail(customerDetails != null ? customerDetails.getEmail() : custDetails.getItems().get(0).getEmail());
            customer.setFirstname(customerDetails != null ? customerDetails.getFirstName() : custDetails.getItems().get(0).getFirstname());
            customer.setLastname(customerDetails != null ? customerDetails.getLastName() : custDetails.getItems().get(0).getLastname());
            customer.setMiddlename("");
            customer.setPrefix("");
            customer.setSuffix("");
            customer.setGender(1);
            customer.setStoreId(0);
            customer.setTaxvat("");
            customer.setWebsiteId(1);

            address.setId(0);
            address.setCustomerId(Integer.valueOf(customerDetails != null ? customerDetails.getEntityId() : custDetails.getItems().get(0).getId()));

            region.setRegion(stateName != null ? stateName : custDetails != null ? custDetails.getItems().get(0).getAddresses().get(0).getRegion().getRegion() : "NA");
            region.setRegionCode(code != null ? code : custDetails != null ? custDetails.getItems().get(0).getAddresses().get(0).getRegion().getRegionCode() : "NA");
            region.setRegionId(Integer.valueOf(region_id != null ? region_id : String.valueOf(custDetails != null ? custDetails.getItems().get(0).getAddresses().get(0).getRegion().getRegionId() : 0)));

            address.setRegion(region);
            address.setRegionId(Integer.valueOf(region_id != null ? region_id : String.valueOf(custDetails != null ? custDetails.getItems().get(0).getAddresses().get(0).getRegion().getRegionId() : 0)));
            address.setCountryId("IN");

            streetList.add(addressLine1);
            streetList.add(addressLine2);
            address.setStreet(streetList);

            address.setCompany("");
            address.setTelephone(telephone);
            address.setFax("");
            address.setPostcode(pincode);
            address.setCity(city);
            address.setFirstname(customerDetails != null ? customerDetails.getFirstName() : custDetails.getItems().get(0).getFirstname());
            address.setLastname(customerDetails != null ? customerDetails.getLastName() : custDetails.getItems().get(0).getLastname());
            address.setMiddlename("");
            address.setPrefix("");
            address.setSuffix("");
            address.setVatId("");
            address.setDefaultBilling(true);
            address.setDefaultShipping(true);

            if(custAddress !=null ){
                for(Addresses add : custAddress){
                    Address newAddress = new Address();
                    newAddress.setId(add.getId());
                    newAddress.setCustomerId(add.getCustomer_id());

                    Region newRegion = new Region();
                    newRegion.setRegionCode(add.getRegion().getRegionCode());
                    newRegion.setRegionId(add.getRegion().getRegionId());
                    newRegion.setRegion(add.getRegion().getRegion());

                    newAddress.setRegion(newRegion);
                    newAddress.setStreet(add.getStreet());

                    newAddress.setCompany("");
                    newAddress.setTelephone(add.getTelephone());
                    newAddress.setFax("");
                    newAddress.setPostcode(add.getPostcode());
                    newAddress.setCity(city);
                    newAddress.setFirstname(custDetails.getItems().get(0).getFirstname());
                    newAddress.setLastname(custDetails.getItems().get(0).getLastname());
                    newAddress.setMiddlename("");
                    newAddress.setPrefix("");
                    newAddress.setSuffix("");
                    newAddress.setVatId("");
                    newAddress.setDefaultBilling(true);
                    newAddress.setDefaultShipping(true);
                    addresses.add(newAddress);
                }
            }

            addresses.add(address);
            customer.setAddresses(addresses);
            addAddressRequest.setCustomer(customer);

            gson = new Gson();
            final String addressRequest = gson.toJson(addAddressRequest);

            Log.d("Add Address","Add Address" + addressRequest);

            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
            MPOSServices mposServices = retrofit.create(MPOSServices.class);

            final String authToken = "Bearer " + sharedPreferences.getString("customerToken",null);
            Integer id = Integer.valueOf(customerDetails != null ? customerDetails.getEntityId() : custDetails.getItems().get(0).getId());
            Log.d("Auth Token.....",authToken);
            mposServices.addAddressForCustomer(addAddressRequest,authToken).enqueue(new Callback<AddAddressResponse>() {
                @Override
                public void onResponse(Call<AddAddressResponse> call, Response<AddAddressResponse> response) {
                    if(response.code() == 200 || response.isSuccessful()){

                        AddAddressResponse addAddressResponse = response.body();
                        gson = new Gson();
                        final String addressResponse = gson.toJson(response.body());
                        Log.d("Add Address Response",addressResponse);

                        if(isFromSearchFragment != null  && isFromSearchFragment){
                            Integer count = addresses.size();
                            Address addr = null;
                            if(count > 0){
                                addr = addresses.get(count-1);
                            }
                            Gson gson = new Gson();
                            String newAddress = gson.toJson(addr);
                            sharedPreferences.edit().putString("editCustomerAddress",newAddress).apply();
                        }
                        String newAddress = gson.toJson(addAddressResponse);
                        sharedPreferences.edit().putString("newAddress",newAddress).apply();
                        progressDialog.dismiss();
                        Intent intent = new Intent(AddCustomerAddressActivity.this, CustomerTabActivity.class);
                        startActivity(intent);

                    }else if(response.code() == 400){
                        progressDialog.dismiss();
                        Toast.makeText(AddCustomerAddressActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 401){
                        progressDialog.dismiss();
                        Toast.makeText(AddCustomerAddressActivity.this,"Unauthorised",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        progressDialog.dismiss();
                        Toast.makeText(AddCustomerAddressActivity.this,"Internal Server error",Toast.LENGTH_LONG).show();
                    }else {
                        progressDialog.dismiss();
                        Log.d("failure...", String.valueOf(response.body()));
                        Toast.makeText(AddCustomerAddressActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<AddAddressResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("failure...",t.getMessage());
                    Toast.makeText(AddCustomerAddressActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            });


        }catch(Exception ex){
            progressDialog.hide();
            ex.printStackTrace();
        }
    }


    public void saveAddressForNewCustomer(){
        try{
            progressDialog = new ProgressDialog(AddCustomerAddressActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();


            String newCustomerDetails = sharedPreferences.getString("newCustomer",null);
            CustomerResponse customerDetails = new Gson().fromJson(newCustomerDetails, new TypeToken<CustomerResponse>() {}.getType());

            Customer customer = new Customer();
            Address address = new Address();
            Region region = new Region();
            final List<Address> addresses = new ArrayList<>();
            List<String> streetList = new ArrayList<>();
            AddAddressRequest addAddressRequest = new AddAddressRequest();

            String addressLine1 = address1EditText.getText().toString();
            String addressLine2 = address2EditText.getText().toString();
            String city = cityEditText.getText().toString();
            String pincode = pincodeEditText.getText().toString();
            String telephone = telephoneEditText.getText().toString();

            customer.setId(customerDetails.getEntityId()) ;
            customer.setGroupId(0);
            customer.setDefaultBilling("");
            customer.setDefaultShipping("");
            customer.setConfirmation("");
            customer.setCreatedAt("");
            customer.setUpdatedAt("");
            customer.setCreatedIn("");
            customer.setDob("");
            customer.setEmail(customerDetails.getEmail());
            customer.setFirstname(customerDetails.getFirstName());
            customer.setLastname(customerDetails.getLastName());
            customer.setMiddlename("");
            customer.setPrefix("");
            customer.setSuffix("");
            customer.setGender(1);
            customer.setStoreId(0);
            customer.setTaxvat("");
            customer.setWebsiteId(1);

            address.setId(0);
            address.setCustomerId(customerDetails.getEntityId());

            region.setRegion(stateName != null ? stateName :"NA");
            region.setRegionCode(code != null ? code :  "NA");
            region.setRegionId(Integer.valueOf(region_id != null ? region_id : "0"));

            address.setRegion(region);
            address.setRegionId(Integer.valueOf(region_id != null ? region_id : "0"));
            address.setCountryId("IN");

            streetList.add(addressLine1);
            streetList.add(addressLine2);
            address.setStreet(streetList);

            address.setCompany("");
            address.setTelephone(telephone);
            address.setFax("");
            address.setPostcode(pincode);
            address.setCity(city);
            address.setFirstname(customerDetails.getFirstName());
            address.setLastname(customerDetails.getLastName());
            address.setMiddlename("");
            address.setPrefix("");
            address.setSuffix("");
            address.setVatId("");
            address.setDefaultBilling(true);
            address.setDefaultShipping(true);


            addresses.add(address);
            customer.setAddresses(addresses);
            addAddressRequest.setCustomer(customer);

            gson = new Gson();
            final String addressRequest = gson.toJson(addAddressRequest);

            Log.d("Add Address","Add Address" + addressRequest);

            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
            MPOSServices mposServices = retrofit.create(MPOSServices.class);

            final String authToken = "Bearer " + sharedPreferences.getString("customerToken",null);
            mposServices.addAddressForCustomer(addAddressRequest,authToken).enqueue(new Callback<AddAddressResponse>() {
                @Override
                public void onResponse(Call<AddAddressResponse> call, Response<AddAddressResponse> response) {
                    if(response.code() == 200 || response.isSuccessful()){

                        AddAddressResponse addAddressResponse = response.body();
                        gson = new Gson();
                        final String addressResponse = gson.toJson(response.body());
                        Log.d("Add Address Response",addressResponse);

                        if(isFromSearchFragment != null  && isFromSearchFragment){
                            Integer count = addresses.size();
                            Address addr = null;
                            if(count > 0){
                                addr = addresses.get(count-1);
                            }
                            Gson gson = new Gson();
                            String newAddress = gson.toJson(addr);
                            sharedPreferences.edit().putString("editCustomerAddress",newAddress).apply();
                        }
                        String newAddress = gson.toJson(addAddressResponse);
                        sharedPreferences.edit().putString("newAddress",newAddress).apply();
                        progressDialog.dismiss();
                        Intent intent = new Intent(AddCustomerAddressActivity.this, CustomerTabActivity.class);
                        startActivity(intent);

                    }else if(response.code() == 400){
                        progressDialog.dismiss();
                        Toast.makeText(AddCustomerAddressActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 401){
                        progressDialog.dismiss();
                        Toast.makeText(AddCustomerAddressActivity.this,"Unauthorised",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        progressDialog.dismiss();
                        Toast.makeText(AddCustomerAddressActivity.this,"Internal Server error",Toast.LENGTH_LONG).show();
                    }else {
                        progressDialog.dismiss();
                        Log.d("failure...", String.valueOf(response.body()));
                        Toast.makeText(AddCustomerAddressActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<AddAddressResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("failure...",t.getMessage());
                    Toast.makeText(AddCustomerAddressActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            });


        }catch(Exception ex){
            progressDialog.hide();
            ex.printStackTrace();
        }
    }

  /*  @Override
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
                Intent intent = new Intent(AddCustomerAddressActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }*/
}
