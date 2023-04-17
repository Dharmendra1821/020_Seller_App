package standalone.eduqfix.qfixinfo.com.app.invoices.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.invoices.adapters.InvoiceAdapter;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.InvoiceList;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.SearchInvoice;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.SearchInvoiceRequest;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.SearchInvoiceResponse;
import standalone.eduqfix.qfixinfo.com.app.login.activity.LoginActivity;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

public class SearchInvoiceActivity extends AppCompatActivity {

    RecyclerView orderListRecyclerView;
    SharedPreferences sharedPreferences;
    InvoiceAdapter adapter;
    List<SearchInvoiceResponse> responses = null;
    Button searchOrderButton;
    EditText firstNameEditText,lastNameEditText,invoiceNoEditText;
    String firstName,lastName,invoiceNo;
    TextView errorMessageTextView;
    LinearLayout noInvoiceFoundLinearLayout;
    ProgressDialog progressDialog = null;
    MPOSRetrofitClient mposRetrofitClient;
    Integer userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_invoice);

        orderListRecyclerView = findViewById(R.id.orderListingRecyclerView);
//        firstNameEditText = findViewById(R.id.firstNameEditText);
//        lastNameEditText = findViewById(R.id.lastNameEditText);
//        invoiceNoEditText = findViewById(R.id.invoiceNoEditText);
//        searchOrderButton = findViewById(R.id.searchOrderButton);
        errorMessageTextView = findViewById(R.id.errorMessageTextView);

        noInvoiceFoundLinearLayout = findViewById(R.id.noInvoiceFoundLinearLayout);

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
//        String details = sharedPreferences.getString("CustomerDetails",null);
//        Gson gson = new Gson();
//        LoginResponse response = gson.fromJson(details,LoginResponse.class);
//
//        String token = response.getCustomerToken();
//        Log.d("token,,",token);



//        searchOrderButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                firstName = firstNameEditText.getText().toString();
//                lastName  = lastNameEditText.getText().toString();
//                invoiceNo = invoiceNoEditText.getText().toString();
//                if(isValidSearchCriteria() || userId != null){
//                    fetchInvoices();
//                }
//            }
//        });

        fetchInvoices();
        orderListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        MenuItem item = menu.findItem(R.id.menu_item_cart);
        MenuItem refresh = menu.findItem(R.id.menu_item_refresh);
        refresh.setVisible(true);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i){
            case R.id.menu_item_logout:
                sharedPreferences.edit().clear().apply();
                Intent intent = new Intent(SearchInvoiceActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.menu_item_refresh:
                  fetchInvoices();
                return true;
            default:
                return false;
        }
    }

    public Boolean isValidSearchCriteria(){

        Boolean flag = false;

        if(!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(invoiceNo)){
            flag = true;
            errorMessageTextView.setVisibility(View.GONE);
            errorMessageTextView.setText("");
        }else if((!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)) && TextUtils.isEmpty(invoiceNo)){
            flag = true;
            errorMessageTextView.setVisibility(View.GONE);
            errorMessageTextView.setText("");
        }else if((TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName)) && !TextUtils.isEmpty(invoiceNo)){
            flag = true;
            errorMessageTextView.setVisibility(View.GONE);
            errorMessageTextView.setText("");
        }else if((!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(invoiceNo)) && TextUtils.isEmpty(lastName)){
            flag = false;
            errorMessageTextView.setVisibility(View.VISIBLE);
            errorMessageTextView.setText("*Last Name required");
        }
        return flag;
    }

    public void fetchInvoices(){
        InvoiceList invoiceList = new InvoiceList();
        invoiceList.setInvoiceId("");
        invoiceList.setFirstName("");
        invoiceList.setLastName("");
        invoiceList.setInvoiceId("");
        progressDialog = new ProgressDialog(SearchInvoiceActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String details = sharedPreferences.getString("CustomerDetails",null);
        Gson gson = new Gson();
        NewLoginResponse response = gson.fromJson(details,NewLoginResponse.class);
        userId = Integer.valueOf(response.getSellerDetails().getId());
        mposRetrofitClient = new MPOSRetrofitClient();
        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices services = retrofit.create(MPOSServices.class);
        SearchInvoiceRequest request = new SearchInvoiceRequest();
        request.setUserId(userId);
        request.setFirstName("");

        String invoiceListRequest = gson.toJson(request);
        Log.i("InvoiceJson","InvoiceJson"+invoiceListRequest);

      //  String token = response.getCustomerToken();
  
      //  Log.i("Token","Token"+token);

        services.searchInvoiceList(request).enqueue(new Callback<List<SearchInvoiceResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<SearchInvoiceResponse>> call, @NonNull Response<List<SearchInvoiceResponse>> response) {
                progressDialog.dismiss();

                Gson gson = new Gson();
                String data = gson.toJson(response.body());
                Log.d("Response body....",data);

                if(response.code() == 200){
                    responses = response.body();
                    if(responses.size() > 0){
                        orderListRecyclerView.setVisibility(View.VISIBLE);
                    }else{
                        noInvoiceFoundLinearLayout.setVisibility(View.VISIBLE);
                    }
                    adapter = new InvoiceAdapter(getApplicationContext(),responses);
                    orderListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else{
                    progressDialog.dismiss();
                    /*Intent intent = new Intent(SearchInvoiceActivity.this, LoginActivity.class);
                      startActivity(intent);
                      finish();*/
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SearchInvoiceResponse>> call, @NonNull Throwable t) {
                Log.d("Failure","Failure"+t.getMessage());
                progressDialog.dismiss();
            }
        });


    }


}
