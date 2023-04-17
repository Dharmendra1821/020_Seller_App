package standalone.eduqfix.qfixinfo.com.app.customer.viewmodel;

import android.content.Context;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import androidx.databinding.library.baseAdapters.BR;
import standalone.eduqfix.qfixinfo.com.app.customer.model.State;
import standalone.eduqfix.qfixinfo.com.app.login.model.Address;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

public class CustomerNewAddressViewModel extends BaseObservable {
    Address customerAddress;
    boolean isSuccess=false;
    boolean isFailed=false;
    private Integer selectedItemPosition;
    List<State> stateList = null;

    @Bindable
    Boolean showAddressErrorMessage = false;
    @Bindable
    Boolean showStateErrorMessage = false;
    @Bindable
    Boolean showPostCodeErrorMessage = false;//showLoginErrorMessage
    @Bindable
    String processStatus;//toastmessage
    @Bindable
    public String CustomerNewAddressStatusMessage = null;
    @Bindable
    public String showDialog = null;

    Context context;
    int CustomerId,CustomerAddressId;
    public CustomerNewAddressViewModel(Context cont, int customerId)
    {
        context=cont;
        CustomerId=customerId;
        customerAddress=new Address();
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
        notifyPropertyChanged(BR.processStatus);
    }

    public Boolean getShowAddressErrorMessage() {
        return showAddressErrorMessage;
    }

    public void setShowAddressErrorMessage(Boolean showAddressErrorMessage) {
        this.showAddressErrorMessage = showAddressErrorMessage;
    }

    public Boolean getShowStateErrorMessage() {
        return showStateErrorMessage;
    }

    public void setShowStateErrorMessage(Boolean showStateErrorMessage) {
        this.showStateErrorMessage = showStateErrorMessage;
    }

    public Boolean getShowPostCodeErrorMessage() {
        return showPostCodeErrorMessage;
    }

    public void setShowPostCodeErrorMessage(Boolean showPostCodeErrorMessage) {
        this.showPostCodeErrorMessage = showPostCodeErrorMessage;
    }


    public Address getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(Address customerAddress) {
        this.customerAddress = customerAddress;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public void setFailed(boolean failed) {
        isFailed = failed;
    }



    public String getShowDialog() {
        return showDialog;
    }

    public void setShowDialog(String showDialog) {
        this.showDialog = showDialog;
    }

    public String getCustomerNewAddressStatusMessage() {
        return CustomerNewAddressStatusMessage;
    }

    public void setCustomerNewAddressStatusMessage(String customerNewAddressStatusMessage) {
        CustomerNewAddressStatusMessage = customerNewAddressStatusMessage;
        notifyPropertyChanged(BR.CustomerNewAddressStatusMessage);
    }

    public void afterAddress1TextChanged(CharSequence s) {
        customerAddress.setAddress1(s.toString());
    }

    public void afterAddress2TextChanged(CharSequence s) {
        customerAddress.setAddress2(s.toString());
    }
    public void afterPostCodeTextChanged(CharSequence s) {
        customerAddress.setPostcode(s.toString());
    }

    public void afterStateTextChanged(CharSequence s) {

        customerAddress.setState(s.toString());
    }

    public void onNextClick()
    {
        try
        {
            setCustomerNewAddressStatusMessage("Show");
            if (customerAddress.IsValidDataEntered())
            {
                //if saved record output
                setCustomerNewAddressStatusMessage("Hide");
                setProcessStatus("Success");
                setShowAddressErrorMessage(false);
                setShowStateErrorMessage(false);
                setShowPostCodeErrorMessage(false);

            }
            else
            {
                setCustomerNewAddressStatusMessage("Hide");
                setProcessStatus("Failed");
                ShowValidationError();
                Toast.makeText(context,"Please fill address details", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    void ShowValidationError()
    {
        if (TextUtils.isEmpty(customerAddress.getAddress1()) &&  TextUtils.isEmpty(customerAddress.getAddress2()))
        {
            setShowAddressErrorMessage(true);

            notifyPropertyChanged(BR.showAddressErrorMessage);
        }
        if(TextUtils.isEmpty(customerAddress.getState()))
        {
            setShowStateErrorMessage(true);

            notifyPropertyChanged(BR.showStateErrorMessage);
        }
        if(TextUtils.isEmpty(customerAddress.getPostcode()))
        {
            setShowPostCodeErrorMessage(true);
            notifyPropertyChanged(BR.showPostCodeErrorMessage);
        }

    }

    @Bindable
    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
        notifyPropertyChanged(BR.selectedItemPosition);
    }



    public List<State> getStateList(){

        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.BASE_URL);
        MPOSServices services = retrofit.create(MPOSServices.class);

        services.getStateList().enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                if(response.code() == 200 || response.isSuccessful()){
                    stateList = response.body();
                }else{
                    Log.d("Error=====","Error");
                }
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                Log.d("Error=====","Error");
            }
        });

        return stateList;
    }
}
