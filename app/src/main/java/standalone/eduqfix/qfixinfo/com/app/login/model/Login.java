package standalone.eduqfix.qfixinfo.com.app.login.model;


import android.text.TextUtils;

import androidx.annotation.NonNull;

/**
 * Created by darshan on 27/2/19.
 */

public class Login {

    @NonNull
    private String username;
    @NonNull
    private String password;
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

    Request request;

    public Login(@NonNull String username, @NonNull String password,@NonNull String userType,@NonNull String device_id) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.device_id = device_id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @NonNull
    public String getUserType() {
        return userType;
    }

    public void setUserType(@NonNull String userType) {
        this.userType = userType;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
    public boolean isInputDataValid() {
        return (!TextUtils.isEmpty(getUsername()) && !TextUtils.isEmpty(getPassword()));
    }
}
