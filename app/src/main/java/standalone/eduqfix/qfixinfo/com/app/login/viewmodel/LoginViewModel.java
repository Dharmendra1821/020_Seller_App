package standalone.eduqfix.qfixinfo.com.app.login.viewmodel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.BR;
import standalone.eduqfix.qfixinfo.com.app.login.model.Login;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginRequest;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.login.model.Request;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSApplication;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

import static android.content.Context.MODE_PRIVATE;

public class LoginViewModel extends BaseObservable {

    public Login login;
    String successMessage = "Success";
    String errorMessage = "Fail";
    Context mContext;
    Gson gson = new Gson();
    ProgressDialog progressDialog;
    Activity activity;
    MPOSApplication application;
    SharedPreferences sharedPreferences;

    String invalidCredentials = null;
    @Bindable
    Boolean showLoginErrorMessage = false;

    @Bindable
    public String toastMessage = null;

    @Bindable
    public String showDialog = null;

    public NewLoginResponse loginResponse;

    public String getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public LoginViewModel(Context context, Activity activity,String email) {
        login = new Login(email,"","","");
        mContext = context;
        this.activity = activity;

    }

    public void afterUsernameTextChanged(CharSequence s) {
        login.setUsername(s.toString());
    }

    public void afterPasswordTextChanged(CharSequence s) {
        login.setPassword(s.toString());
    }

    public NewLoginResponse getLoginResponse() {
        return loginResponse;
    }

    public void setLoginResponse(NewLoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    public String getShowDialog() {
        return showDialog;
    }

    public void setShowDialog(String showDialog) {
        this.showDialog = showDialog;
        notifyPropertyChanged(BR.showDialog);
    }

    @Bindable
    public Boolean getShowLoginErrorMessage() {
        return showLoginErrorMessage;
    }

    public void setShowLoginErrorMessage(Boolean showLoginErrorMessage) {
        this.showLoginErrorMessage = showLoginErrorMessage;
        notifyPropertyChanged(BR.showLoginErrorMessage);
    }

    public void doLogin(){
        application = new MPOSApplication();
        if (login.isInputDataValid()){
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            setToastMessage("show");

            sharedPreferences = mContext.getSharedPreferences("StandAlone",MODE_PRIVATE);
            String shopUrl  = sharedPreferences.getString("shop_url",null);

            sharedPreferences= mContext.getSharedPreferences("StandAlone", MODE_PRIVATE);
            String firebaseToken = sharedPreferences.getString("firebase_token", null);


            MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
            Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
            MPOSServices mposServices = retrofit.create(MPOSServices.class);

            Request request = new Request();
            request.setEmail(login.getUsername());
            request.setUser_password(login.getPassword());
            request.setUserType(login.getUserType());
            request.setDevice_id(firebaseToken);

//            LoginRequest loginRequest = new LoginRequest();
//            loginRequest.setRequest(request);

            /*if(login.getUsername().equals("kapoorsports52@gmail.com") && login.getPassword().equals("Qfix@123")){
                setToastMessage("hide");
                setToastMessage(successMessage);
                setShowLoginErrorMessage(false);
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("MPOSShopping",Context.MODE_PRIVATE);
                sharedPreferences.edit().putInt("sellerId",144).apply();
            }
            else{
                setToastMessage("hide");
                setToastMessage(errorMessage);
                setShowLoginErrorMessage(true);
            }*/

            final String data = gson.toJson(request);
            Log.d("Request Data ===","Request Data ==="+data);
            mposServices.login(request).enqueue(new Callback<NewLoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<NewLoginResponse> call, @NonNull Response<NewLoginResponse> response) {
                    Log.d("styury", String.valueOf(response.body()));
                    if(response.code() == 200){

                        progressDialog.dismiss();
                        setToastMessage("hide");
                        setToastMessage(successMessage);
                        setShowLoginErrorMessage(false);
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("StandAlone", MODE_PRIVATE);

                        String responseDetails = gson.toJson(response.body());

                        final String data = gson.toJson(response.body());
                        Log.d("Request Data ===","Request Data ==="+data);

                        boolean isSeller = false ;
                        if(response.body() != null){
                            isSeller = response.body().getSellerDetails().isSeller();
                            sharedPreferences.edit().putString("customerLoginDetails",data).apply();
                        }
                        sharedPreferences.edit().putString("isSeller",String.valueOf(isSeller)).apply();
                       // sharedPreferences.edit().putString("isSeller","false").apply();
                        sharedPreferences.edit().putString("CustomerDetails", responseDetails).apply();
                        sharedPreferences.edit().putString("customerToken", "").apply();
                       // sharedPreferences.edit().putString("customerPassword",login.getPassword()).apply();
                        sharedPreferences.edit().putString("adminToken","y0085pfsyn8k9m3ro10t7hbw4rpsmqvt").apply();
                        SharedPreferences sharedPreferences1 = mContext.getSharedPreferences("StandAlone", MODE_PRIVATE);

                        // Put the json format string to SharedPreferences object.
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("customer_json", data);
                        editor.commit();

                    }else{
                        application.writeToFile(errorMessage);
                        progressDialog.dismiss();
                        setToastMessage("hide");
                        setToastMessage(errorMessage);
                        setShowLoginErrorMessage(true);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<NewLoginResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    setToastMessage("hide");
                    setToastMessage(errorMessage);
                    application.writeToFile(errorMessage);
                }
            });
        }else{
            setToastMessage("hide");
            setToastMessage(errorMessage);
            setShowLoginErrorMessage(true);
        }

    }

    public void forgotPassword(){
        setToastMessage("forgotPassword");
    }
}
