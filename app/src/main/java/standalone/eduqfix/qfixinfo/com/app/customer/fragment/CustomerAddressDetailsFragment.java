package standalone.eduqfix.qfixinfo.com.app.customer.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.hydromatic.linq4j.Linq4j;
import net.hydromatic.linq4j.function.Predicate1;

import java.util.ArrayList;
import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.customer.activity.AddCustomerAddressActivity;
import standalone.eduqfix.qfixinfo.com.app.customer.adaptor.CustomerAddressAdaptor;
import standalone.eduqfix.qfixinfo.com.app.customer.model.Addresses;
import standalone.eduqfix.qfixinfo.com.app.customer.model.CustomerDetails;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.SellerDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;

import static android.content.Context.MODE_PRIVATE;

public class CustomerAddressDetailsFragment extends Fragment {

    public static LinearLayout CustomerAddressLinearLayout;
    RecyclerView customerAddressRecyclerView;
    Button addNewAddressButton,proceedToProductsButton;
    ProgressDialog progressDialog;
    int CustomerId = 0;
    SharedPreferences sharedPreferences;
    Double Amount = 0.0;
    List<Addresses> lCustAdd =new ArrayList<>();
//2652
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_customer_address_details, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        customerAddressRecyclerView = view.findViewById(R.id.customerAddressRecyclerView);
        addNewAddressButton = view.findViewById(R.id.addNewAddressButton);
        proceedToProductsButton = view.findViewById(R.id.proceedToProductsButton);
        Bundle bundle = getArguments();
        addNewAddressButton.setEnabled(true);
        proceedToProductsButton.setEnabled(true);

        sharedPreferences = getActivity().getApplication().getSharedPreferences("StandAlone",MODE_PRIVATE);
        String details = sharedPreferences.getString("CustomerDetails",null);
        NewLoginResponse customerDetails = new Gson().fromJson(details, new TypeToken<NewLoginResponse>() {}.getType());
        if (bundle==null)
        {
            if (sharedPreferences.getInt("CustomerId",0)==0)
            {
                proceedToProductsButton.setEnabled(false);
            }
            else
            {
                CustomerId = sharedPreferences.getInt("CustomerId",0);
                CustomerId = customerDetails.getSellerDetails().getSeller_id();
                FillCustomerDetails();
            }
            }
            else
            {
                   CustomerId =bundle.getInt("CustomerId");
                   FillCustomerDetails();
            }
        addNewAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getActivity(), AddCustomerAddressActivity.class);
               intent.putExtra("isFromSearchFragment",true);
               startActivity(intent);
             }
            });

        proceedToProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Addresses customerAddressList =null;
                try
                {
                    customerAddressList = Linq4j.asEnumerable(lCustAdd).where(new Predicate1<Addresses>() {
                        @Override
                        public boolean apply(Addresses customerAddressList) {
                            return customerAddressList.getChecked()==true;
                        }
                    }).first();
                }
                catch (Exception ex)
                {}
                if (customerAddressList==null)
                {
                    Toast.makeText(getActivity().getBaseContext(),"Please select address to deliver your package.",Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(getActivity(),SellerDashboardActivity.class);
                intent.putExtra("CustomerId",sharedPreferences.getInt("CustomerId",0));
                intent.putExtra("CustomerAddressId",customerAddressList.getId());
                startActivity(intent);

                /*FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                  PaymentMethodActivity paymentMethodFragment = new SellerDashboardActivity();
                  paymentMethodFragment.setArguments(bundle);
                  fragmentTransaction.replace(parentLinearLayout[0].getId(), paymentMethodFragment,"Payment");
                  fragmentTransaction.commit();*/


            }
        });
        return view;
    }
    public void FillCustomerDetails()
    {
        try {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            addNewAddressButton.setEnabled(true);
            proceedToProductsButton.setEnabled(true);
            String sCustomerDetails = sharedPreferences.getString("selectedCustomerDetails", "");
            if (!sCustomerDetails.isEmpty()) {
                CustomerDetails customerDetails = new Gson().fromJson(sCustomerDetails, new TypeToken<CustomerDetails>() {
                }.getType());
                lCustAdd = customerDetails.getItems().get(0).getAddresses();
                if (lCustAdd.size() == 0) {
                    Addresses addresses = new Addresses();
                    addresses.setCity(getString(R.string.Customer_Address_not_found));
                    lCustAdd.add(addresses);
                    //addNewAddressButton.setEnabled(false);
                    proceedToProductsButton.setEnabled(false);
                }
            } else {
                //addNewAddressButton.setEnabled(false);
                proceedToProductsButton.setEnabled(false);
            }
            customerAddressRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
            CustomerAddressAdaptor adapter = new CustomerAddressAdaptor(getActivity().getBaseContext(), lCustAdd);
            customerAddressRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();

        }catch (Exception e){
           progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
}
