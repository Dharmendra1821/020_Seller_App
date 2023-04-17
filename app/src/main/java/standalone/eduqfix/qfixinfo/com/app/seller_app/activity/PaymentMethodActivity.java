package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.OrderListActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MainRequest;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.Request;
import standalone.eduqfix.qfixinfo.com.app.customer.model.AddAddressResponse;
import standalone.eduqfix.qfixinfo.com.app.login.model.Address;
import standalone.eduqfix.qfixinfo.com.app.customer.model.CustomerDetails;
import standalone.eduqfix.qfixinfo.com.app.customer.model.CustomerResponse;
import standalone.eduqfix.qfixinfo.com.app.customer.model.Items;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.AddressInformation;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.BillingAddress;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Collection;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigDetails;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Item;


import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewPaymentLink;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewPlaceOrderResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.PaymentRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.PlaceOrderRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.PlacedOrderResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SendPaymentLinkRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SendPaymentLinkResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ShippingAddress;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ShippingChargeResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ShippingCharges;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ShippingRateRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.AcknowledgeRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.AcknowledgeResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.DetailResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.DoTransactionRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.FeeInitiateToken;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.HeaderRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.InitiatePaymentRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.InitiatePaymentResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.Payload;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.PaymentGatewayRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.PaymentGatewayResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.PaymentModeEnum;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.TokenResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.UpdatePaymentRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Utility.GsonUtils;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Utility.UIUtils;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.activities.BasePineActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.helper.PineServiceHelper;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.transaction_response.activity.TransactionSuccessActivity;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;


public class PaymentMethodActivity extends BasePineActivity {

    Button makePaymentButton;
    private CheckBox sendPaymentCheckBox,sendQFixCheckBox,acceptterms,collectnow;
    private RadioButton paymentRadioButton;
    String amount= "0";
    Boolean isSeller =false;
    int CustomerId,CustomerAddressId;
    List<ProductCartResponse> productList = null;
    SharedPreferences sharedPreferences;
    String customerAddressDetails;
    EditText emailAddressEditText,mobileNoEditText;
    ProgressDialog progressDialog = null;
    String orderId = null;
    Integer orderid2;
    String newCustomerDetails,newAddressDetails;
    CustomerResponse customerResponseDetails;
    AddAddressResponse addAddressResponse;
    LinearLayout friendsDetailsLinearLayout;
    Items items;
    List<Item> items1;
    String email;
    String shippingType;
    String cardHolderName;
    String cardType;
    String cardNumber;
    String transactionId;
    Address addAddressRequest;
    String editedAddressRequest;
    Boolean isSendPaymentLink = false;
    MPOSRetrofitClient mposRetrofitClient;
    String qfixReferenceNumber;
    static String  accessCode, markPaidToken, uniqueDeviceId,itemList;
    List<Address> addressList;
    View collect_now_view;
    LinearLayout collect_now_layout;
    CheckBox collect_now_card,collect_now_upi;

    ////////////////////////////////////////
    String PLUTUS_SMART_PACKAGE = "com.pinelabs.masterapp";
    String PLUTUS_SMART_ACTION = "com.pinelabs.masterapp.SERVER";
    int MESSAGE_CODE = 1001;
    String BILLING_REQUEST_TAG = "MASTERAPPREQUEST";
    String BILLING_RESPONSE_TAG = "MASTERAPPRESPONSE";
    Messenger mServerMessenger;
    private boolean isBound;
    private static PaymentMethodActivity instance;
    static String newMarkPaidToken;
    @NonNull
    private PineServiceHelper pineSH;
    String newAccessCode;
    String posAcknowledgeResponse;
    String posAcknowledgeRequest;
    int transactionType;
    String transactionStatus;
    String errorCode = "";
    String errorMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        sendPaymentCheckBox = findViewById(R.id.sendPaymentCheckBox);
        sendQFixCheckBox = findViewById(R.id.sendQFixCheckBox);
        makePaymentButton = findViewById(R.id.makePaymentButton);
        emailAddressEditText = findViewById(R.id.emailAddressEditText);
        mobileNoEditText = findViewById(R.id.mobileNoEditText);
        friendsDetailsLinearLayout = findViewById(R.id.friendsDetailsLinearLayout);
      //  acceptterms = findViewById(R.id.ACCPEPT);
        collectnow =findViewById(R.id.Collectnow);
        collect_now_view = findViewById(R.id.collectnow_view);
        collect_now_layout = findViewById(R.id.collectnow_layout);
        collect_now_upi = findViewById(R.id.collect_now_upi);
        collect_now_card = findViewById(R.id.collect_now_card);
        //checkDeviceConnected();
        pineSH = PineServiceHelper.getInstance();
        setData();
        instance = this;
        progressDialog = new ProgressDialog(PaymentMethodActivity.this);
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        final String cartList = sharedPreferences.getString("cartList",null);
        productList = new Gson().fromJson(cartList, new TypeToken<List<ProductCartResponse>>() {}.getType());



        customerAddressDetails = sharedPreferences.getString("selectedCustomerDetails","");

        newCustomerDetails = sharedPreferences.getString("newCustomer",null);
        customerResponseDetails = new Gson().fromJson(newCustomerDetails, new TypeToken<CustomerResponse>() {}.getType());

        newAddressDetails = sharedPreferences.getString("newAddress",null);
        addAddressResponse = new Gson().fromJson(newAddressDetails, new TypeToken<AddAddressResponse>() {}.getType());

        editedAddressRequest = sharedPreferences.getString("editCustomerAddress",null);
        addAddressRequest = new Gson().fromJson(editedAddressRequest,new TypeToken<Address>(){}.getType());

  /*      sendPaymentCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   sendPaymentCheckBox.setChecked(true);
                if (((CheckBox)view).isChecked() && sendPaymentCheckBox.isChecked())
                {
                    sendQFixCheckBox.setChecked(false);
                    isSendPaymentLink = true;
                    friendsDetailsLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });*/

        CustomerDetails customerDetails = new Gson().fromJson(customerAddressDetails, new TypeToken<CustomerDetails>() {}.getType());

         email = customerDetails.getItems().get(0).getEmail();

         emailAddressEditText.setText(email);

        sendPaymentCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (sendPaymentCheckBox.isChecked())
                {
                    sendQFixCheckBox.setChecked(false);
                    collectnow.setChecked(false);
                    collect_now_upi.setChecked(false);
                    collect_now_card.setChecked(false);
                    isSendPaymentLink = true;
                    friendsDetailsLinearLayout.setVisibility(View.VISIBLE);
                    collect_now_layout.setVisibility(View.GONE);
                    collect_now_view.setVisibility(View.GONE);
                }

            }
        });



        sendQFixCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sendQFixCheckBox.isChecked())
                {
                    sendPaymentCheckBox.setChecked(false);
                    collectnow.setChecked(false);
                    collect_now_upi.setChecked(false);
                    collect_now_card.setChecked(false);
                    friendsDetailsLinearLayout.setVisibility(View.GONE);
                    collect_now_layout.setVisibility(View.GONE);
                    collect_now_view.setVisibility(View.GONE);
                }

            }
        });

        collectnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (collectnow.isChecked())
                {
                    sendPaymentCheckBox.setChecked(false);
                    sendQFixCheckBox.setChecked(false);
                    collect_now_upi.setChecked(false);
                    collect_now_card.setChecked(false);
                    friendsDetailsLinearLayout.setVisibility(View.GONE);
                    collect_now_layout.setVisibility(View.VISIBLE);
                    collect_now_view.setVisibility(View.VISIBLE);
                }

            }
        });

        collect_now_upi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collect_now_upi.isChecked()){
                    sendPaymentCheckBox.setChecked(false);
                    sendQFixCheckBox.setChecked(false);
                    collect_now_card.setChecked(false);
                }
            }
        });

        collect_now_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collect_now_card.isChecked()){
                    sendPaymentCheckBox.setChecked(false);
                    sendQFixCheckBox.setChecked(false);
                    collect_now_upi.setChecked(false);
                }
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle !=null)
        {
            amount = bundle.getString("Amount");
            CustomerId = bundle.getInt("CustomerId");
            CustomerAddressId = bundle.getInt("CustomerAddressId");
            isSeller=bundle.getBoolean("isSeller");
        }




        makePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (sendPaymentCheckBox.isChecked()) {
                    String email = emailAddressEditText.getText().toString();
                    if (isSendPaymentLink && !TextUtils.isEmpty(email)) {
                        placeOrder();
                    } else {
                        Toast.makeText(PaymentMethodActivity.this, " Fields Are left blank ", Toast.LENGTH_SHORT).show();
                    }
                } else if (sendQFixCheckBox.isChecked()) {
                    if (sendQFixCheckBox.isChecked()) {

                        // get Connectivity Manager object to check connection
                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

                        // Check for network connections
                        if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                                connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                                connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

                            placeOrder();

                        } else if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                                connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

                            Toast.makeText(PaymentMethodActivity.this, " You Are Not Connected To internet ", Toast.LENGTH_LONG).show();
                        }


                    }
                }

             /*   if(sendPaymentCheckBox.isChecked() || sendQFixCheckBox.isChecked()){
                    String email = emailAddressEditText.getText().toString();
                    if(isSendPaymentLink && !TextUtils.isEmpty(email) ){
                        placeOrder();
                    }
                    else {
                           Intent intent = new Intent(PaymentMethodActivity.this, poynt.class);
                            startActivity(intent);
                        //  launchPoyntPayment();
                   //     Toast.makeText(PaymentMethodActivity.this,"Please provide email to send payment link",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(PaymentMethodActivity.this,"Please select payment method",Toast.LENGTH_LONG).show();
                }*/


                else if (collect_now_card.isChecked()) {

                    if (collect_now_card.isChecked()) {

                        // get Connectivity Manager object to check connection
                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

                        // Check for network connections
                        if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                                connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                                connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


                            placeOrder();
                            // if connected with internet
                           /* Intent intent = new Intent(PaymentMethodActivity.this, poynt.class);
                            intent.putExtra("app","seller");
                            intent.putExtra("AMOUNT",amount);
                            startActivity(intent);*/

//                            Intent intent = new Intent(PaymentMethodActivity.this, MosambeePaymentProcessingActivity.class);
//                            Bundle bundles = new Bundle();
//                            bundles.putDouble("Amount",amount);
//                            List<standalone.eduqfix.qfixinfo.com.app.invoices.model.Item> itemList = response.getItems();
//                            bundles.putInt("OrderId",response.getOrderId());
//                            String itemsList = gson.toJson(itemList);
//                            sharedPreferences.edit().putString("Items", itemsList).apply();
//                            intent.putExtras(bundles);
//                            startActivity(intent);


                        } else if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                                connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

                            Toast.makeText(PaymentMethodActivity.this, " You Are Not Connected To internet ", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                else if(collect_now_upi.isChecked()){
                    if (collect_now_upi.isChecked()) {

                        // get Connectivity Manager object to check connection
                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

                        // Check for network connections
                        if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                                connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                                connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

                            placeOrder();

                        } else if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                                connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

                            Toast.makeText(PaymentMethodActivity.this, " You Are Not Connected To internet ", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                else {
                    Toast.makeText(PaymentMethodActivity.this, "Please select payment method", Toast.LENGTH_LONG).show();
                }

            }
        });

       // setShippingCharges();

    }



    public void placeOrder(){

        try{
            progressDialog = new ProgressDialog(PaymentMethodActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
            MPOSServices services = retrofit.create(MPOSServices.class);
            PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
            Collection collection = new Collection();
            ShippingAddress shippingAddress = new ShippingAddress();
            BillingAddress billingAddress = new BillingAddress();
            Integer customerId = sharedPreferences.getInt("CustomerId",0);
            CustomerDetails customerDetails = new Gson().fromJson(customerAddressDetails, new TypeToken<CustomerDetails>() {}.getType());
            Integer customersId = customerDetails != null ? customerDetails.getItems().get(0).getAddresses().get(0).getCustomer_id():0;

            if(customerId == 0){
                customerId = Integer.valueOf(String.valueOf(customerResponseDetails != null && customerResponseDetails.getEntityId() != null ? customerResponseDetails.getEntityId() : customersId));
            }

            if(customerDetails != null && customerDetails.getItems() != null){
                items =  customerDetails.getItems().get(0);
            }

            Integer count = items.getAddresses().size();
            String firstName = items != null && items.getFirstname() != null ? items.getFirstname() : customerResponseDetails.getFirstName();
            String lastName = items != null && items.getLastname() != null ? items.getFirstname() : customerResponseDetails.getLastName();
            String street = addAddressRequest != null ? String.valueOf(addAddressRequest.getStreet()) : String.valueOf(items != null && items.getAddresses().get(0).getStreet() != null ? items.getAddresses().get(0).getStreet() : addAddressResponse.getAddresses().get(0).getStreet());
            String city = addAddressRequest != null ? addAddressRequest.getCity() : String.valueOf(items != null && items.getAddresses().get(0).getCity() != null ? items.getAddresses().get(0).getCity() : addAddressResponse.getAddresses().get(0).getCity());
            Integer regionId = items != null && items.getAddresses().get(0).getRegion().getRegionId() != null ? items.getAddresses().get(0).getRegion().getRegionId() : addAddressResponse.getAddresses().get(0).getRegion().getRegionId();
            String region = items != null && items.getAddresses().get(0).getRegion().getRegion() != null ? items.getAddresses().get(0).getRegion().getRegion() : addAddressResponse.getAddresses().get(0).getRegion().getRegion();
            String postCode = items != null && items.getAddresses().get(0).getPostcode() != null ? items.getAddresses().get(0).getPostcode() : addAddressResponse.getAddresses().get(0).getPostcode();
            String telephone = items != null && items.getAddresses().get(0).getTelephone() != null ? items.getAddresses().get(0).getTelephone() : addAddressResponse.getAddresses().get(0).getTelephone();
            String email = items != null && items.getEmail() != null ? items.getEmail() : customerResponseDetails.getEmail();

            shippingAddress.setFirstname(firstName);
            shippingAddress.setLastname(lastName);
            shippingAddress.setStreet(street);
            shippingAddress.setCity(city.equalsIgnoreCase("null") ? "Mumbai" : city);
            shippingAddress.setRegionId(regionId);
            shippingAddress.setRegion(region);
            shippingAddress.setPostcode(postCode);
            shippingAddress.setTelephone(telephone);
            shippingAddress.setFax("");
            shippingAddress.setSaveInAddressBook(1);
            shippingAddress.setCountryId("IN");
            shippingAddress.setCustomer_address_id(items.getAddresses().get(0).getId());



            billingAddress.setFirstname(firstName);
            billingAddress.setLastname(lastName);
            billingAddress.setStreet(street);
            billingAddress.setCity(city.equalsIgnoreCase("null") ? "Mumbai" : city);
            billingAddress.setRegionId(regionId);
            billingAddress.setRegion(region);
            billingAddress.setPostcode(postCode);
            billingAddress.setTelephone(telephone);
            billingAddress.setFax("");
            billingAddress.setSaveInAddressBook(1);
            billingAddress.setCountryId("IN");
            billingAddress.setCustomer_address_id(items.getAddresses().get(0).getId());

            placeOrderRequest.setEmail(email);
            placeOrderRequest.setIdentifierFlag("placeorder");
            placeOrderRequest.setChannel("SHOPPING-MPOS");
            placeOrderRequest.setCustomer_id(customerId);
          //  collection.setCartId(sharedPreferences.getInt("ProductCartId",0));
            placeOrderRequest.setCartId(sharedPreferences.getInt("ProductCartId",0));
            List<Item> cartListItem = new ArrayList<>();
            for(ProductCartResponse product : productList){
                Item item = new Item();
                item.setProductId(Integer.valueOf(product.getProductId()));
                item.setQty(product.getQty());
                cartListItem.add(item);
            }

            placeOrderRequest.setItems(cartListItem);
            placeOrderRequest.setShippingAddress(shippingAddress);
            placeOrderRequest.setBillingAddress(billingAddress);
            placeOrderRequest.setCurrencyId("INR");
         //   placeOrderRequest.setCollection(collection);

            Gson gson = new Gson();
            String placeOrderJson = gson.toJson(placeOrderRequest);
            String custToken = sharedPreferences.getString("adminToken",null);
            Log.d("PlaceOrder===","PlaceOrder==="+placeOrderJson);
            Log.d("PlaceOrder===","PlaceOrder==="+custToken);


            services.placeOrder(placeOrderRequest,"Bearer "+custToken).enqueue(new Callback<NewPlaceOrderResponse>() {
                @Override
                public void onResponse(@NonNull Call<NewPlaceOrderResponse> call, @NonNull Response<NewPlaceOrderResponse> response) {
                    if(response.code() == 200 || response.isSuccessful()){
                        progressDialog.dismiss();
                        orderId = response.body().getData().getSend_pay_link_orderid();

                        Log.d("Orderid........", String.valueOf(response.body()));
                        Toast.makeText(PaymentMethodActivity.this,"Order Placed Successfully",Toast.LENGTH_LONG).show();

                        if (sendQFixCheckBox.isChecked()){
                            Intent intent = new Intent(PaymentMethodActivity.this, TransactionSuccessActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("CustomerId",CustomerId);
                            bundle.putString("PaymentType","QFix");
                            bundle.putInt("CustomerAddressId",CustomerAddressId);
                            bundle.putString("Amount",amount);
                            bundle.putBoolean("isSeller",true);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            // startPayentProcess();
                          //  paymentN();
                            progressDialog.dismiss();


                           // getOrderAddress();

                        }
                         if(collect_now_card.isChecked() || collect_now_upi.isChecked()){
                            startPayentProcess();
                            progressDialog.dismiss();
                        }
                        else{
                            //bundle.putString("PaymentType","SendPayment");
                            sendPaymentLink();
                            //getPaymentToken();
                            progressDialog.dismiss();
                        }

                    }else if(response.code() == 401){
                        progressDialog.dismiss();
                        Toast.makeText(PaymentMethodActivity.this, "Unauthorized customer", Toast.LENGTH_SHORT).show();
                    }else if(response.code() == 400){
                        progressDialog.dismiss();
                        Toast.makeText(PaymentMethodActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        progressDialog.dismiss();
                        Toast.makeText(PaymentMethodActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                    }else{

                        progressDialog.dismiss();
                        Toast.makeText(PaymentMethodActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewPlaceOrderResponse> call, @NonNull Throwable t) {
                    progressDialog.hide();
                    Toast.makeText(PaymentMethodActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception ex){
            progressDialog.hide();
            Toast.makeText(PaymentMethodActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void initiateMPOSPayment(){
        progressDialog = new ProgressDialog(PaymentMethodActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        mposRetrofitClient = new MPOSRetrofitClient();
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String returnUrl =  Constant.REVAMP_URL1+"ccavenuepay/ccavenuepay/returnurl";
        Log.d("return url..",returnUrl);
        //  Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.QFIX_BASE_URL1);
        Integer customerId = sharedPreferences.getInt("CustomerId",0);
        CustomerDetails customerDetails = new Gson().fromJson(customerAddressDetails, new TypeToken<CustomerDetails>() {}.getType());
        Integer customersId = customerDetails != null ? customerDetails.getItems().get(0).getAddresses().get(0).getCustomer_id():0;

        if(customerId == 0){
            customerId = Integer.valueOf(String.valueOf(customerResponseDetails != null && customerResponseDetails.getEntityId() != null ? customerResponseDetails.getEntityId() : customersId));
        }

        if(customerDetails != null && customerDetails.getItems() != null){
            items =  customerDetails.getItems().get(0);
        }
        String firstName = items != null && items.getFirstname() != null ? items.getFirstname() : customerResponseDetails.getFirstName();

        String street = addAddressRequest != null ? String.valueOf(addAddressRequest.getStreet()) : String.valueOf(items != null && items.getAddresses().get(0).getStreet() != null ? items.getAddresses().get(0).getStreet() : addAddressResponse.getAddresses().get(0).getStreet());
        String city = addAddressRequest != null ? addAddressRequest.getCity() : String.valueOf(items != null && items.getAddresses().get(0).getCity() != null ? items.getAddresses().get(0).getCity() : addAddressResponse.getAddresses().get(0).getCity());
        Integer regionId = items != null && items.getAddresses().get(0).getRegion().getRegionId() != null ? items.getAddresses().get(0).getRegion().getRegionId() : addAddressResponse.getAddresses().get(0).getRegion().getRegionId();
        String region = items != null && items.getAddresses().get(0).getRegion().getRegion() != null ? items.getAddresses().get(0).getRegion().getRegion() : addAddressResponse.getAddresses().get(0).getRegion().getRegion();
        String postCode = items != null && items.getAddresses().get(0).getPostcode() != null ? items.getAddresses().get(0).getPostcode() : addAddressResponse.getAddresses().get(0).getPostcode();
        String telephone = items != null && items.getAddresses().get(0).getTelephone() != null ? items.getAddresses().get(0).getTelephone() : addAddressResponse.getAddresses().get(0).getTelephone();
        String email = items != null && items.getEmail() != null ? items.getEmail() : customerResponseDetails.getEmail();


        Retrofit retrofit = mposRetrofitClient.getClient(Constant.QFIX_BASE_URL1);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);
        final InitiatePaymentRequest initiatePaymentRequest = new InitiatePaymentRequest();
        String amountNum = amount.replace("\u20B9","");
        double amtNum = Double.parseDouble(amountNum);
        double d = amtNum;
        initiatePaymentRequest.setAmount(d);
        initiatePaymentRequest.setChannel("SHOPPING-MPOS");
        initiatePaymentRequest.setCurrencyCode("INR");
        initiatePaymentRequest.setCustomerCity(city);
        initiatePaymentRequest.setCustomerCountry("IND");
        initiatePaymentRequest.setCustomerEmail(email);
        initiatePaymentRequest.setCustomerAddress(street);
        initiatePaymentRequest.setCustomerState(region);
        initiatePaymentRequest.setCustomerPincode(postCode);
        initiatePaymentRequest.setCustomerPhone(telephone);
        initiatePaymentRequest.setOrderId(String.valueOf(orderId));
        initiatePaymentRequest.setIdentifier(email);
        String customerName = firstName;
        initiatePaymentRequest.setCustomerName(customerName);
        initiatePaymentRequest.setReturnUrl(returnUrl);
        initiatePaymentRequest.setEnvironment("STANDALONE");
        ArrayList<Payload> payloadArrayList = new ArrayList<>();//QPOS6010811065
        for (int i =0;i <productList.size();i++) {
            Payload payload = new Payload();
            payload.setProductName(productList.get(i).getName());
            payload.setProductCode(productList.get(i).getSku());
            payload.setAmount(productList.get(i).getPrice());
            payloadArrayList.add(payload);
        }
        initiatePaymentRequest.setPayload(payloadArrayList);
        Gson gson = new Gson();
        String initiateData = gson.toJson(initiatePaymentRequest);
        Log.i("InitiateData","InitiateData"+initiateData);
        Log.i("Token","Token "+markPaidToken);
        mposServices.initiatePayment(initiatePaymentRequest,markPaidToken).enqueue(new Callback<InitiatePaymentResponse>() {
            @Override
            public void onResponse(Call<InitiatePaymentResponse> call, Response<InitiatePaymentResponse> response) {
                if(response.code() == 200 ||response.isSuccessful()){
                    progressDialog.dismiss();
                    InitiatePaymentResponse initiatePaymentResponse = response.body();
                    Gson gson = new Gson();
                    String initiateData = gson.toJson(response.body());
                    Log.i("InitiateData","InitiateData"+initiateData);
                    accessCode = initiatePaymentResponse.getAccessCode();
                    qfixReferenceNumber = initiatePaymentResponse.getQfixReferenceNumber();
                    sharedPreferences.edit().putString("ReferenceNumber", String.valueOf(qfixReferenceNumber)).apply();
                    updatePayment();
               //     Toast.makeText(getApplicationContext(), "Initiate Success", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Payment initiation failed, Please try again", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PaymentMethodActivity.this, MainDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<InitiatePaymentResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Payment initiation failed, Please try again", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PaymentMethodActivity.this, MainDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    public void updatePayment(){
        progressDialog = new ProgressDialog(PaymentMethodActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        mposRetrofitClient = new MPOSRetrofitClient();
        String amountNum = amount.replace("\u20B9","");
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.QFIX_BASE_URL1);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);
        UpdatePaymentRequest updatePaymentRequest = new UpdatePaymentRequest();
        updatePaymentRequest.setAccess_code(accessCode);
        updatePaymentRequest.setPaymentGatewayMode("O");
        updatePaymentRequest.setTotalAmount(amountNum);
        updatePaymentRequest.setPaymentOptionCode("PINELABS_POS_CARD");
        updatePaymentRequest.setRequestParameters("parameters");
        updatePaymentRequest.setRequestPayload("payload");
        Gson gson = new Gson();
        String initiateData = gson.toJson(updatePaymentRequest);
        Log.i("InitiateData","InitiateData"+initiateData);
        Log.i("Token","Token "+markPaidToken);
        mposServices.updatePayment(updatePaymentRequest,markPaidToken).enqueue(new Callback<InitiatePaymentResponse>() {
            @Override
            public void onResponse(Call<InitiatePaymentResponse> call, Response<InitiatePaymentResponse> response) {
                if(response.code() == 200 ||response.isSuccessful()){
                    progressDialog.dismiss();
                    InitiatePaymentResponse initiatePaymentResponse = response.body();
                    Gson gson = new Gson();
                    String initiateData = gson.toJson(response.body());
                    Log.i("InitiateData","InitiateData"+initiateData);
                    newAccessCode =          initiatePaymentResponse.getAccessCode();
                    qfixReferenceNumber =    initiatePaymentResponse.getQfixReferenceNumber();
                    //  sharedPreferences.edit().putString("ReferenceNumber", qfixReferenceNumber).apply();
                    Log.d("new access code.",newAccessCode);
                 //   Toast.makeText(getApplicationContext(), "Initiate Success", Toast.LENGTH_SHORT).show();
                    if(collect_now_upi.isChecked()){
                        HeaderRequest<DoTransactionRequest> headerRequest = PaymentModeEnum.UPI.getRequest();
                        callPineService(headerRequest);
                    }
                    if(collect_now_card.isChecked()){
                        HeaderRequest<DoTransactionRequest> headerRequest = PaymentModeEnum.Sale.getRequest();
                        callPineService(headerRequest);
                    }

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Initiate Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InitiatePaymentResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Initiate Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void startPayentProcess(){

        progressDialog = new ProgressDialog(PaymentMethodActivity.this);
        progressDialog.setMessage("Payment Loading...");
        progressDialog.show();
        mposRetrofitClient = new MPOSRetrofitClient();
      /*  sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
          String shopUrl  = sharedPreferences.getString("shop_url",null);
          Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.QFIX_BASE_URL1);
      */
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.QFIX_BASE_URL1);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);
        FeeInitiateToken feeInitiateToken = new FeeInitiateToken();
        feeInitiateToken.setLogin("QF!X-Payment#1spatcher@dm!n");
        feeInitiateToken.setPassword("QF!X-Payment#!spatcher@dm!n@8090");
        Gson gson = new Gson();
        String placeOrderJson = gson.toJson(feeInitiateToken);
        Log.d("request...",placeOrderJson);
        mposServices.getUserToken(feeInitiateToken).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                if(response.code() == 200){
                    progressDialog.dismiss();
                    TokenResponse tokenResponse = response.body();
                    markPaidToken = tokenResponse.getToken();
                    Log.d("token....",markPaidToken);
                    if(markPaidToken != null){
                        initiateMPOSPayment();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                Log.i("Token Resposnse","Token Response"+t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    public void startPayentProcess1(){

        progressDialog = new ProgressDialog(PaymentMethodActivity.this);
        progressDialog.setMessage("Payment Loading...");
        progressDialog.show();
        mposRetrofitClient = new MPOSRetrofitClient();
      /*  sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
          String shopUrl  = sharedPreferences.getString("shop_url",null);
          Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.QFIX_BASE_URL1);
      */
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.QFIX_BASE_URL1);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);
        FeeInitiateToken feeInitiateToken = new FeeInitiateToken();
        feeInitiateToken.setLogin("QF!X-Payment#1spatcher@dm!n");
        feeInitiateToken.setPassword("QF!X-Payment#!spatcher@dm!n@8090");
        Gson gson = new Gson();
        String placeOrderJson = gson.toJson(feeInitiateToken);
        Log.d("request...",placeOrderJson);
        mposServices.getUserToken(feeInitiateToken).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                if(response.code() == 200){
                    progressDialog.dismiss();
                    TokenResponse tokenResponse = response.body();
                    newMarkPaidToken = tokenResponse.getToken();
                    Log.d("token....",newMarkPaidToken);
                    if(newMarkPaidToken != null){
                        acknowledgePaymentRequest();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                Log.i("Token Resposnse","Token Response"+t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    public void setShippingCharges(){

        try{

            progressDialog = new ProgressDialog(PaymentMethodActivity.this);
            progressDialog.setMessage("Getting payment method....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
            MPOSServices services = retrofit.create(MPOSServices.class);
            ShippingCharges shippingCharges = new ShippingCharges();
            AddressInformation addressInformation = new AddressInformation();
            ShippingAddress shippingAddress = new ShippingAddress();
            BillingAddress billingAddress = new BillingAddress();
            Integer customerId = sharedPreferences.getInt("CustomerId",0);
            CustomerDetails customerDetails = new Gson().fromJson(customerAddressDetails, new TypeToken<CustomerDetails>() {}.getType());
            Integer customersId = customerDetails != null ? customerDetails.getItems().get(0).getAddresses().get(0).getCustomer_id():0;

            if(customerId == 0){
                customerId = Integer.valueOf(String.valueOf(customerResponseDetails != null && customerResponseDetails.getEntityId() != null ? customerResponseDetails.getEntityId() : customersId));
            }

            if(customerDetails != null && customerDetails.getItems() != null){
                items =  customerDetails.getItems().get(0);
            }
            String customerDetails1 = sharedPreferences.getString("CustomerDetails", null);
            LoginResponse loginResponse = new Gson().fromJson(customerDetails1, new TypeToken<LoginResponse>() {
            }.getType());

            for(int i=0;i < loginResponse.getCustomerDetails().getCustomAttributes().size();i++){

                String attribute = loginResponse.getCustomerDetails().getCustomAttributes().get(i).getAttributeCode();
                if(attribute.equalsIgnoreCase("shipping_type")){
                    shippingType = loginResponse.getCustomerDetails().getCustomAttributes().get(i).getValue();

                }
            }

            Integer count = items.getAddresses().size();
            String firstName = items != null && items.getFirstname() != null ? items.getFirstname() : customerResponseDetails.getFirstName();
            String lastName = items != null && items.getLastname() != null ? items.getFirstname() : customerResponseDetails.getLastName();
            String street = addAddressRequest != null ? String.valueOf(addAddressRequest.getStreet()) : String.valueOf(items != null && items.getAddresses().get(0).getStreet() != null ? items.getAddresses().get(0).getStreet() : addAddressResponse.getAddresses().get(0).getStreet());
            String city = addAddressRequest != null ? addAddressRequest.getCity() : String.valueOf(items != null && items.getAddresses().get(0).getCity() != null ? items.getAddresses().get(0).getCity() : addAddressResponse.getAddresses().get(0).getCity());
            Integer regionId = items != null && items.getAddresses().get(0).getRegion().getRegionId() != null ? items.getAddresses().get(0).getRegion().getRegionId() : addAddressResponse.getAddresses().get(0).getRegion().getRegionId();
            String region = items != null && items.getAddresses().get(0).getRegion().getRegion() != null ? items.getAddresses().get(0).getRegion().getRegion() : addAddressResponse.getAddresses().get(0).getRegion().getRegion();
            String postCode = items != null && items.getAddresses().get(0).getPostcode() != null ? items.getAddresses().get(0).getPostcode() : addAddressResponse.getAddresses().get(0).getPostcode();
            String telephone = items != null && items.getAddresses().get(0).getTelephone() != null ? items.getAddresses().get(0).getTelephone() : addAddressResponse.getAddresses().get(0).getTelephone();
            String email = items != null && items.getEmail() != null ? items.getEmail() : customerResponseDetails.getEmail();

            String shippedStreet = sharedPreferences.getString("shipping_street",null);
            String shippedCity = sharedPreferences.getString("shipping_city",null);
            String shippedPostCode = sharedPreferences.getString("shipping_postcode",null);
            String shippedRegion = sharedPreferences.getString("shipping_region",null);
            int regionid = sharedPreferences.getInt("shipping_regionId",0);

            shippingAddress.setFirstname(firstName);
            shippingAddress.setLastname(lastName);
          //  shippingAddress.setStreet(shippedStreet);
            shippingAddress.setCity(shippedCity);
            shippingAddress.setRegionId(regionid);
            shippingAddress.setRegion(shippedRegion);
            shippingAddress.setPostcode(shippedPostCode);
            shippingAddress.setTelephone(telephone);
            shippingAddress.setFax("");
            shippingAddress.setSaveInAddressBook(1);
            shippingAddress.setCountryId("IN");



            billingAddress.setFirstname(firstName);
            billingAddress.setLastname(lastName);
          //  billingAddress.setStreet(street);
            billingAddress.setCity(city);
            billingAddress.setRegionId(regionid);
            billingAddress.setRegion(region);
            billingAddress.setPostcode(postCode);
            billingAddress.setTelephone(telephone);
            billingAddress.setFax("");
            billingAddress.setSaveInAddressBook(1);
            billingAddress.setCountryId("IN");


            addressInformation.setBillingAddress(billingAddress);
            addressInformation.setShippingAddress(shippingAddress);
            addressInformation.setShippingCarrierCode(shippingType);
            addressInformation.setShippingMethodCode(shippingType);
            shippingCharges.setAddressInformation(addressInformation);

            Gson gson = new Gson();
            String placeOrderJson = gson.toJson(shippingCharges);
            String custToken = sharedPreferences.getString("customerToken",null);
            Log.d("PlaceOrder===","PlaceOrder==="+placeOrderJson);
            Log.d("PlaceOrder===","PlaceOrder==="+custToken);


            services.setShippingCharges(shippingCharges,"Bearer "+custToken).enqueue(new Callback<ShippingChargeResponse>() {
                @Override
                public void onResponse(@NonNull Call<ShippingChargeResponse> call, @NonNull Response<ShippingChargeResponse> response) {
                    if(response.code() == 200 || response.isSuccessful()){
                        progressDialog.dismiss();
                    }else if(response.code() == 401){
                        progressDialog.dismiss();
                        Toast.makeText(PaymentMethodActivity.this, "Unauthorized customer", Toast.LENGTH_SHORT).show();
                    }else if(response.code() == 400){
                        progressDialog.dismiss();
                        Toast.makeText(PaymentMethodActivity.this,"Bad Request",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        progressDialog.dismiss();
                        Toast.makeText(PaymentMethodActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(PaymentMethodActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ShippingChargeResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(PaymentMethodActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception ex){
            progressDialog.dismiss();
            Toast.makeText(PaymentMethodActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void getOrderAddress(){
        progressDialog = new ProgressDialog(PaymentMethodActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        addressList = new ArrayList<>();
        mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.BASE_URL);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);
        orderid2 = Integer.valueOf(orderId);
        mposServices.getShippingAddressByOrderId(orderid2).enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(@NonNull Call<List<Address>> call, @NonNull Response<List<Address>> response) {
                if(response.code() == 200 || response.isSuccessful()){
                    progressDialog.hide();
                    addressList = response.body();
                    if(addressList.size() > 0){
                      //  startPayentProcess();
                    }else{
                        Toast.makeText(PaymentMethodActivity.this,"Customers address not found",Toast.LENGTH_LONG).show();
                      //  Intent intent = new Intent(PaymentMethodActivity.this, InvoiceDetailsActivity.class);
                      //  startActivity(intent);
                      //  finish();
                    }
                }else {
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Address>> call, @NonNull Throwable t) {
                Log.d("Failure Log","Failure Log"+t.getMessage());
                progressDialog.hide();
            }
        });

    }

    public void getPaymentToken(){

        final ProgressDialog progressDialog = new ProgressDialog(PaymentMethodActivity.this);
        progressDialog.setMessage("Sending payment link...");
        progressDialog.show();

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);
//qwe123@@

        Request request = new Request();
        ConfigDetails configDetails = new ConfigDetails();
        configDetails.setUsername("admin123");
        configDetails.setPassword("qwe123@@");
        request.setData(configDetails);
        Gson gson = new Gson();
        final String data = gson.toJson(request);
        Log.d("Request Data ===","Request Data ==="+data);

        mposServices.getPaymentToken(request).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 200 || response.isSuccessful()){
                    progressDialog.dismiss();
                    String paymentToken = response.body();
                    Log.d("payment..",paymentToken);
                    sendPaymentLink();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });

    }



    public void sendPaymentLink(){
        try{

            final ProgressDialog progressDialog = new ProgressDialog(PaymentMethodActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            String email = emailAddressEditText.getText().toString();
            String mobile = mobileNoEditText.getText().toString();

            SendPaymentLinkRequest sendPaymentLinkRequest = new SendPaymentLinkRequest();
            NewPaymentLink newPaymentLink = new NewPaymentLink();
            newPaymentLink.setOrderId(orderId);
            newPaymentLink.setSendPaymentLinkEmail(email);
            newPaymentLink.setSendPaymentLinkMobileNumber(mobile);
          //  paymentRequest.setToken(paymentToken)
            sendPaymentLinkRequest.setRequest(newPaymentLink);

            sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);
            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
            MPOSServices mposServices = retrofit.create(MPOSServices.class);
            Gson gson = new Gson();
            final String data = gson.toJson(sendPaymentLinkRequest);
            Log.d("Request Data ===","Request Data ==="+data);

            mposServices.sendPaymentLinkForForOrder(sendPaymentLinkRequest).enqueue(new Callback<SendPaymentLinkResponse>() {
                @Override
                public void onResponse(@NonNull Call<SendPaymentLinkResponse> call, @NonNull Response<SendPaymentLinkResponse> response) {
                    if(response.code() == 200 || response.isSuccessful()){

                        SendPaymentLinkResponse paymentLinkResponse = response.body();
                        if(paymentLinkResponse.getStatus().equalsIgnoreCase("success")){
                            Toast.makeText(PaymentMethodActivity.this,"Email sent successfully",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(PaymentMethodActivity.this, MainDashboardActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }else if(response.code() == 400){
                        progressDialog.dismiss();
                        Toast.makeText(PaymentMethodActivity.this,"Bad request",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 401){
                        progressDialog.dismiss();
                        Toast.makeText(PaymentMethodActivity.this,"Unauthorized",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 500){
                        progressDialog.dismiss();
                        Toast.makeText(PaymentMethodActivity.this,"Internal server error",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<SendPaymentLinkResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(PaymentMethodActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            });
        }catch(Exception ex){
            progressDialog.hide();
            ex.printStackTrace();
        }
    }
    private boolean isPoyntTerminal() {
        return "POYNT".equals(Build.MANUFACTURER);
    }

    @Override
    public void onBackPressed() {
    }

    ////////////////////////////////////

    public static PaymentMethodActivity getInstance() {
        return instance;
    }
    private void setData() {
        //Connecting to the Pine Service Helper
        pineSH.connect(this);
    }

    //Connect Pine payment payment app for a particular mode of trasaction
    private void callPineService(final HeaderRequest<DoTransactionRequest> headerRequest) {
        String qfixReferenceNumber = sharedPreferences.getString("ReferenceNumber",null);
        String amountNum = amount.replace("\u20B9","");
        float amt = Float.parseFloat(amountNum);
        DoTransactionRequest request = headerRequest.getDetail();
        request.setBillingRefNo(qfixReferenceNumber);
        request.setPaymentAmount((long) (amt*100));
        pineSH.callPineService(headerRequest);
        Gson gson = new Gson();
        posAcknowledgeRequest = gson.toJson(headerRequest);
        Log.d("request....",posAcknowledgeRequest);
    }


    //Handle the action if transaction is successful or not
    @Override
    public void sendResult(DetailResponse detailResponse) {
        super.sendResult( detailResponse);
        if (detailResponse != null) {
            //Check if the transaction is successful or not
            if (detailResponse.getResponse().isSuccess()) {

                try {
                    Gson gson = new Gson();
                     try {
                         posAcknowledgeResponse = gson.toJson(detailResponse);
                         transactionStatus = "SUCCESS";
                        startPayentProcess1();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.d("response....", posAcknowledgeResponse);
                }catch(NullPointerException e) {
                    e.printStackTrace();
                }


            } else {

                Gson gson = new Gson();
                posAcknowledgeResponse = gson.toJson(detailResponse.getResponse());
                transactionStatus = "ERROR";
                errorMsg = detailResponse.getResponse().getResponseMsg();
                startPayentProcess1();

                Log.d("errrrrrrr...",posAcknowledgeResponse);
                Log.d("errrrrrrr...",detailResponse.getResponse().getResponseMsg());

            }

        } else {
            UIUtils.makeToast(this, "Error Occurred");
        }
    }


    public void acknowledgePaymentRequest(){
        Gson gson = new Gson();
        String details = sharedPreferences.getString("CustomerDetails",null);
        NewLoginResponse loginResponse = gson.fromJson(details,NewLoginResponse.class);
        progressDialog = new ProgressDialog(PaymentMethodActivity.this);
        progressDialog.setMessage("Verifying payment .....");
        progressDialog.show();
        String selectedInvoice = sharedPreferences.getString("SelectedInvoice",null);

        String qfixReferenceNumber = sharedPreferences.getString("ReferenceNumber",null);
        Integer customerId = sharedPreferences.getInt("CustomerId",0);
        CustomerDetails customerDetails = new Gson().fromJson(customerAddressDetails, new TypeToken<CustomerDetails>() {}.getType());
        Integer customersId = customerDetails != null ? customerDetails.getItems().get(0).getAddresses().get(0).getCustomer_id():0;

        if(customerId == 0){
            customerId = Integer.valueOf(String.valueOf(customerResponseDetails != null && customerResponseDetails.getEntityId() != null ? customerResponseDetails.getEntityId() : customersId));
        }

        if(customerDetails != null && customerDetails.getItems() != null){
            items =  customerDetails.getItems().get(0);
        }

        AcknowledgeRequest acknowledgeRequest = new AcknowledgeRequest();
        PaymentGatewayRequest paymentGatewayRequest = new PaymentGatewayRequest();
        PaymentGatewayResponse paymentGatewayResponse = new PaymentGatewayResponse();


       // String acknowledgeJson = gson.toJson(posAcknowledgeResponse);


        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String amountNum = amount.replace("\u20B9","");
        double amtNum = Double.parseDouble(amountNum);
        double d = amtNum;
        String transType = "";
        String paymentOptionCode = "";
        if(collect_now_upi.isChecked()){
            transType = "Upi";
            paymentOptionCode = "PINELABS_POS_UPI";
        }
        if(collect_now_card.isChecked()){
            transType = "Sale";
            paymentOptionCode = "PINELABS_POS_CARD";
        }
        paymentGatewayRequest.setTransType(transType);
        paymentGatewayRequest.setContainer("");
        paymentGatewayRequest.setAmount(d);
        paymentGatewayRequest.setQfixReferenceNumber(qfixReferenceNumber);

        paymentGatewayRequest.setMosambeeId("3253545645");
        paymentGatewayRequest.setMosambeePassword("12344");
        paymentGatewayRequest.setCustomerEmail(items.getEmail());
        paymentGatewayRequest.setCustomerMobile(items.getAddresses().get(0).getTelephone());

        paymentGatewayResponse.setResponse(posAcknowledgeResponse);

        acknowledgeRequest.setTransactionStatusCode(transactionStatus);
        acknowledgeRequest.setPaymentOptionCode(paymentOptionCode);
        acknowledgeRequest.setChannel("SHOPPING-MPOS");
        acknowledgeRequest.setEnvironment("STANDALONE");
        acknowledgeRequest.setAccessCode(newAccessCode);
        acknowledgeRequest.setUpdatedBy(customersId);
        acknowledgeRequest.setTransactionErrorCode(errorCode);

        acknowledgeRequest.setPaymentGatewayRequest(paymentGatewayRequest);
        acknowledgeRequest.setPaymentGatewayResponse(paymentGatewayResponse);

        mposRetrofitClient = new MPOSRetrofitClient();
      /*sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.QFIX_BASE_URL1);
      */
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.QFIX_BASE_URL1);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);
        Log.d("Token....",newMarkPaidToken);
        String acknowledgeJson = gson.toJson(acknowledgeRequest);
        Log.i("AcknowledgeRequest===","AcknowledgeRequest=="+acknowledgeJson);

        mposServices.acknowledgePaymentRequest(acknowledgeRequest,newMarkPaidToken).enqueue(new Callback<AcknowledgeResponse>() {
            @Override
            public void onResponse(Call<AcknowledgeResponse> call, Response<AcknowledgeResponse> response) {
                if(response.code() == 200 || response.isSuccessful()){
                    AcknowledgeResponse acknowledgeResponse = response.body();
                    if(acknowledgeResponse.getStatus().equalsIgnoreCase("success")){
                   //     String amountNum = amount.replace("\u20B9","");
                        Intent intent = new Intent(PaymentMethodActivity.this, TransactionSuccessActivity.class );
                        Bundle bundle = new Bundle();
                        bundle.putString("Amount",amount);
                        bundle.putInt("CustomerId",CustomerId);
                        bundle.putBoolean("isSeller",true);
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    }
                    else {
                            Intent intent = new Intent(PaymentMethodActivity.this, TransactionErrorActivity.class );
                            Bundle bundle = new Bundle();
                            bundle.putString("billing_number",qfixReferenceNumber);
                            bundle.putString("amount",amountNum);
                            bundle.putInt("customerId",CustomerId);
                            bundle.putString("fail_reason",errorMsg);
                            intent.putExtras(bundle);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();
                    }

                }else{
                    Log.i("Error","Error");
                    Toast.makeText(PaymentMethodActivity.this, "Something went Wrong, Try again", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PaymentMethodActivity.this, TransactionErrorActivity.class );
                    Bundle bundle = new Bundle();
                    bundle.putString("billing_number",qfixReferenceNumber);
                    bundle.putString("amount",amountNum);
                    bundle.putInt("customerId",CustomerId);
                    bundle.putString("fail_reason","");
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();

                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<AcknowledgeResponse> call, Throwable t) {
                //progressDialog.hide();
                Toast.makeText(PaymentMethodActivity.this, "Something went Wrong, Try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
