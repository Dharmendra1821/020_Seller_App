package standalone.eduqfix.qfixinfo.com.app.login.model;


import androidx.annotation.NonNull;


public class Request {

    @NonNull
    private String email;
    @NonNull
    private String user_password;
    @NonNull
    private String userType;

    @NonNull
    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(@NonNull String device_id) {
        this.device_id = device_id;
    }

    @NonNull
    private String device_id;


    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(@NonNull String user_password) {
        this.user_password = user_password;
    }

    @NonNull
    public String getUserType() {
        return userType;
    }

    public void setUserType(@NonNull String userType) {
        this.userType = userType;
    }
}
