package standalone.eduqfix.qfixinfo.com.app.invoices.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.legacy.widget.Space;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.invoices.adapters.ProductAdapter;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.Item;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.MPOSResponse;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.MarkAsPaidRequest;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.OfflinePaymentRequest;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.SearchInvoiceResponse;
import standalone.eduqfix.qfixinfo.com.app.login.activity.LoginActivity;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.transaction_response.activity.TransactionSuccessActivity;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

public class InvoiceDetailsActivity extends AppCompatActivity {

    TextView customerNameTextView,invoiceTextView,amountTextView,somethingWentWrongTextView,addressTextView,contactTextView;
    RecyclerView productRecyclerView;
    ProductAdapter adapter;
    Button markAsPaidButton,onlinePaymentButton;
    SearchInvoiceResponse response = null;
    Double amount = null;
    Integer userId = null;
    Space errorSpace;
    SharedPreferences sharedPreferences;
    String token = null;
    MPOSRetrofitClient mposRetrofitClient;
    Map<String,Item> itemMap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);
        itemMap = new TreeMap<>();
        customerNameTextView = findViewById(R.id.customerNameTextView);
        invoiceTextView = findViewById(R.id.invoiceTextView);
        amountTextView = findViewById(R.id.amountTextView);
        markAsPaidButton = findViewById(R.id.markAsPaidButton);
        onlinePaymentButton = findViewById(R.id.onlinePaymentButton);
        somethingWentWrongTextView = findViewById(R.id.somethingWentWrongTextView);
        errorSpace = findViewById(R.id.errorSpace);
        contactTextView =  findViewById(R.id.contactTextView);
        addressTextView = findViewById(R.id.addressTextView);
        final Gson gson = new Gson();
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String details = sharedPreferences.getString("CustomerDetails",null);
        NewLoginResponse loginResponse = gson.fromJson(details,NewLoginResponse.class);
        userId = Integer.valueOf(loginResponse.getSellerDetails().getId());
    //    token = loginResponse.getCustomerToken();

        productRecyclerView = findViewById(R.id.productRecyclerView);

//        if(sharedPreferences.getString("mpos_id",null).equalsIgnoreCase("0")){
//            onlinePaymentButton.setVisibility(View.GONE);
//        }
//        else {
//            onlinePaymentButton.setVisibility(View.VISIBLE);
//        }

        final Bundle bundle = getIntent().getExtras();
        String selectedInvoice = null;
        if(bundle != null){
            selectedInvoice = bundle.getString("SelectedInvoice");
        }
        if(TextUtils.isEmpty(selectedInvoice)){
            selectedInvoice = sharedPreferences.getString("SelectedInvoice",null);
        }
        response = gson.fromJson(selectedInvoice,SearchInvoiceResponse.class);
        sharedPreferences.edit().putString("SelectedInvoice",selectedInvoice).apply();

        productRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Gson gson2 = new Gson();
        String invoiceListRequest = gson2.toJson(response);
        Log.d("fhfhf", String.valueOf(invoiceListRequest));

        if(response.getOrder_status().equalsIgnoreCase("6")){
            markAsPaidButton.setVisibility(View.GONE);
        }
        else {
            if(response.getPayment_status().equalsIgnoreCase("0")){
                markAsPaidButton.setVisibility(View.VISIBLE);
                markAsPaidButton.setText("Cash on Delivery");
            }
            if(response.getPayment_status().equalsIgnoreCase("1")){
                markAsPaidButton.setVisibility(View.VISIBLE);
                markAsPaidButton.setText("Delivered");
            }
        }

      //  Log.d("Resss...",response.getPayment_status());
        amount = Double.valueOf(response.getGrand_total() != null ? response.getGrand_total() : "0.0");

        customerNameTextView.setText(response.getCustomer_name() != null ? response.getCustomer_name() : "");
        invoiceTextView.setText(response.getInvoice_number());
        double invoiceAmount = amount != null ? Math.round(amount * 100)/100.00:0.0;
        amountTextView.setText(String.valueOf(invoiceAmount));
        contactTextView.setText(String.valueOf(response.getContact_number()));
        addressTextView.setText(response.getAddress());

        if(response.getProducts().size() > 0){
            List<Item> items = response.getProducts();

            for(int i=0;i<items.size();i++){
                Item item = items.get(i);
                if(itemMap.containsKey(String.valueOf(item.getProductId()))){
                    Item mapItem = itemMap.get(String.valueOf(item.getProductId()));
                    mapItem.setName(item.getName());
                }else{
                    itemMap.put(String.valueOf(item.getProductId()),item);
                }
            }

        }

        ArrayList<Item> itemList = new ArrayList<Item>(itemMap.values());
        String newItemList = gson.toJson(itemList);
        Log.d("ItemList ========== ","ItemList"+newItemList);
        adapter = new ProductAdapter(getApplicationContext(),itemList);
        productRecyclerView.setAdapter(adapter);

        markAsPaidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAsPaid();
            }
        });

        onlinePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // for poynt device
              /*  ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

                // Check for network connections
                if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                        connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

                    // if connected with internet
                    Intent intentpoynt = new Intent(InvoiceDetailsActivity.this, poynt.class);
                    intentpoynt.putExtra("app","delivery");
                    intentpoynt.putExtra("AMOUNT",amount);
                    startActivity(intentpoynt);


                } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

                    Toast.makeText(InvoiceDetailsActivity.this, " You Are Not Connected To internet ", Toast.LENGTH_LONG).show();
                }*/

              // for mosambee device
//                Intent intent = new Intent(InvoiceDetailsActivity.this, MosambeePaymentProcessingActivity.class);
//                Bundle bundles = new Bundle();
//                bundles.putDouble("Amount",amount);
//                List<Item> itemList = response.getItems();
//                bundles.putInt("OrderId",response.getOrderId());
//                String itemsList = gson.toJson(itemList);
//                sharedPreferences.edit().putString("Items", itemsList).apply();
//                intent.putExtras(bundles);
//                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
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
                Intent intent = new Intent(InvoiceDetailsActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }

    public void markAsPaid(){
        mposRetrofitClient = new MPOSRetrofitClient();
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices services = retrofit.create(MPOSServices.class);
        OfflinePaymentRequest offlinePaymentRequest = new OfflinePaymentRequest();
        MarkAsPaidRequest markAsPaidRequest = new MarkAsPaidRequest();
        markAsPaidRequest.setDeliveryBoyId(userId);//pass userId here
        markAsPaidRequest.setPaymenyChannel(1224);
        markAsPaidRequest.setToken("qhbahdbhbddjbnk");
        markAsPaidRequest.setEntityId(response.getInvoice_number());
        amount = response.getGrand_total() != null ? Double.valueOf(response.getGrand_total()) : 0.0 ;
        offlinePaymentRequest.setRequest(markAsPaidRequest);//000000197

        Gson gson = new Gson();
        String invoiceListRequest = gson.toJson(offlinePaymentRequest);
        Log.i("InvoiceJson","InvoiceJson"+invoiceListRequest);

    //    Log.d("request..", token);
        services.markAsPaid(offlinePaymentRequest).enqueue(new Callback<MPOSResponse>() {
            @Override
            public void onResponse(@NonNull Call<MPOSResponse> call, @NonNull Response<MPOSResponse> response) {
                if(response.code() == 200 && response.body().getStatus().equalsIgnoreCase("success")){
                    Log.d("Success","Success");
                    Intent intent = new Intent(InvoiceDetailsActivity.this, TransactionSuccessActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("Amount",amount);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MPOSResponse> call, @NonNull Throwable t) {
                Log.d("Failure","Failure");
                showErrorMessage();
            }
        });


    }

    public void showErrorMessage(){
        somethingWentWrongTextView.setVisibility(View.VISIBLE);
        errorSpace.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                somethingWentWrongTextView.setVisibility(View.GONE);
                errorSpace.setVisibility(View.GONE);
            }
        },3000);
    }
}
