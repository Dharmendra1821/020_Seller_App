package standalone.eduqfix.qfixinfo.com.app.customer.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.customer.activity.AddCustomerActivity;
import standalone.eduqfix.qfixinfo.com.app.customer.adaptor.CustomerSearchAdaptor;
import standalone.eduqfix.qfixinfo.com.app.customer.model.CustomerDetails;
import standalone.eduqfix.qfixinfo.com.app.customer.model.SearchCustomerRequest;
import standalone.eduqfix.qfixinfo.com.app.customer.model.SearchRequest;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class SearchCustomerDetailsFragment extends Fragment {
    public static LinearLayout ProfileLinearLayout;
    public static TabLayout CustomerTabLayout;
    EditText emailIdOrMobileNoEditText;
    RecyclerView customerdetailsRecyclerView;
    Button searchCustomerButton,addCustomerButton;
    ProgressDialog progressDialog;
    Context context;
    List<CustomerDetails> lCustDetail= new ArrayList<>();
    String sSearchType;
    String email,phone;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_search_customer_details, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context=getActivity();
        UpdateShared(null,false);
        emailIdOrMobileNoEditText=view.findViewById(R.id.emailIdOrMobileNoEditText);
        customerdetailsRecyclerView=view.findViewById(R.id.customerdetailsRecyclerView);
        searchCustomerButton=view.findViewById(R.id.searchCustomerButton);
        addCustomerButton = view.findViewById(R.id.addCustomerButton);
        searchCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 sSearchType="email";
                if (emailIdOrMobileNoEditText.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getActivity(),"Please enter search criteria.", Toast.LENGTH_LONG).show();
                }
                else if( TextUtils.isDigitsOnly(emailIdOrMobileNoEditText.getText().toString()))
                {
                    sSearchType = "phone";
                    FillCustomerDetails(sSearchType);
                }
                else {
                    FillCustomerDetails(sSearchType);
                }
        //        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService((INPUT_METHOD_SERVICE));
         //       imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });

        addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), AddCustomerActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
    public void FillCustomerDetails(final String sSearchType)
    {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        sharedPreferences =  getActivity().getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);

        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices services = retrofit.create(MPOSServices.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("StandAlone", MODE_PRIVATE);
        final String adminToken = sharedPreferences.getString("adminToken",null);

        if(sSearchType.equalsIgnoreCase("email")){
                email = emailIdOrMobileNoEditText.getText().toString();
                phone = "";
        }
         if(sSearchType.equalsIgnoreCase("phone")){
             phone = emailIdOrMobileNoEditText.getText().toString();
             email = "";
        }

        SearchRequest searchRequest = new SearchRequest();
      //  searchRequest.setAdminToken(adminToken);
        searchRequest.setEmail(email);
        searchRequest.setMobile_no(phone);
        SearchCustomerRequest searchCustomerRequest = new SearchCustomerRequest();
        searchCustomerRequest.setRequest(searchRequest);

       // Log.d("SEarch customer....",adminToken);

        Gson gson = new Gson();
        final String data = gson.toJson(searchCustomerRequest);
        Log.d("Request Data ===","Request Data ==="+data);

        services.SearchCustomerByMobile(searchCustomerRequest).enqueue(new Callback<CustomerDetails>() {
            @Override
            public void onResponse(Call<CustomerDetails> call, Response<CustomerDetails> response) {
                if(response.code() == 200 ||response.isSuccessful()){

                    lCustDetail.clear();
                    CustomerDetails customerDetails = null;
                    customerDetails=response.body();
                    Gson gson = new Gson();
                    String gsonData = gson.toJson(response.body());
                    Log.d("GSONData=====","GSONData====="+gsonData);


                           lCustDetail.add(customerDetails);
                           Log.d("Array...", lCustDetail.toString());
                           UpdateShared(customerDetails, true);
                           customerdetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                           CustomerSearchAdaptor adapter = new CustomerSearchAdaptor(context, lCustDetail, ProfileLinearLayout, getActivity());
                           customerdetailsRecyclerView.setAdapter(adapter);
                           adapter.notifyDataSetChanged();
                           progressDialog.dismiss();

                           try {
                               InputMethodManager imm = (InputMethodManager) getActivity().getSystemService((INPUT_METHOD_SERVICE));
                               imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                              } catch (Exception e) {
                             }

                }else {
                    Gson gson = new Gson();
                    String gsonData = gson.toJson(response.body());
                    Log.d("GSONData=====","GSONData====="+gsonData);
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"Customers not found.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerDetails> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("......",t.getMessage());
                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void UpdateShared(CustomerDetails customerDetails,boolean IsAdding)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("StandAlone", MODE_PRIVATE);
        Gson gson = new Gson();
        if (IsAdding)
        {
            String selectedProducts = gson.toJson(customerDetails);
            sharedPreferences.edit().putString("selectedCustomerDetails", selectedProducts).apply();
        }
        else
        {
            sharedPreferences.edit().remove("selectedCustomerDetails").apply();
        }
    }
}
