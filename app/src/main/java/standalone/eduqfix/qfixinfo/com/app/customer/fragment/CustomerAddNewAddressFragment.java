package standalone.eduqfix.qfixinfo.com.app.customer.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.customer.viewmodel.CustomerNewAddressViewModel;
import standalone.eduqfix.qfixinfo.com.app.databinding.ActivityCustomerAddNewAddressBinding;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.SellerDashboardActivity;

import static android.content.Context.MODE_PRIVATE;

public class CustomerAddNewAddressFragment extends Fragment {

    EditText address1EditText,address2EditText,stateEditText,PincodeEditText;
    Button nextButton;
    static int CustomerId,CustomerAddressId;
    ActivityCustomerAddNewAddressBinding customerAddNewAddressBinding;
    static ProgressDialog progressDialog = null;
    static Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_customer_add_new_address, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle bundle = getArguments();
        CustomerId = bundle.getInt("CustomerId");
        context = getActivity();

        /*
        CustomerAddressId=1;
        address1EditText=view.findViewById(R.id.address1EditText);
        address2EditText=view.findViewById(R.id.address2EditText);
        stateEditText=view.findViewById(R.id.stateEditText);
        PincodeEditText=view.findViewById(R.id.PincodeEditText);
        nextButton=view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               *//*
                Address customerAddressList =null;
                try
                {
                    customerAddressList = Linq4j.asEnumerable(lCustAdd).where(new Predicate1<Address>() {
                        @Override
                        public boolean apply(Address customerAddressList) {
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
                }*//*
                Intent intent = new Intent(getActivity(), SellerDashboardActivity.class);
                intent.putExtra("CustomerId",CustomerId);
                intent.putExtra("CustomerAddressId",CustomerAddressId);
                startActivity(intent);

            }
        });*/
        customerAddNewAddressBinding = DataBindingUtil.setContentView(getActivity(),R.layout.activity_customer_add_new_address);
        customerAddNewAddressBinding.setCustomernewaddressViewModel(new CustomerNewAddressViewModel(getActivity().getApplicationContext(),CustomerId));
        customerAddNewAddressBinding.executePendingBindings();
        return view;
    }
    @BindingAdapter({"processStatus"})
    public static void ToPaymentScreen(View view,String sStatusMessage)
    {
        try
        {
            if (sStatusMessage !=null)
            {
                if(progressDialog == null){
                    progressDialog = new ProgressDialog(view.getContext());
                }
                if (sStatusMessage.toLowerCase().equals("success"))
                {
                    Intent intent = new Intent(context, SellerDashboardActivity.class);
                    intent.putExtra("CustomerId",CustomerId);
                    intent.putExtra("CustomerAddressId",CustomerAddressId);
                    SharedPreferences sharedPreferences =context.getSharedPreferences("StandAlone",MODE_PRIVATE);
                    sharedPreferences.edit().putInt("CustomerAddressId",CustomerAddressId).apply();
                    context.startActivity(intent);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
