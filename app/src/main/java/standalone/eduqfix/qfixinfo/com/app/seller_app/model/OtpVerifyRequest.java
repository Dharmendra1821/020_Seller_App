package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import androidx.annotation.NonNull;

public class OtpVerifyRequest {

    @NonNull
    private String mobile;
    @NonNull
    private String otp;

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
    public String getMobile() {
        return mobile;
    }

    public void setMobile(@NonNull String mobile) {
        this.mobile = mobile;
    }

    @NonNull
    public String getOtp() {
        return otp;
    }

    public void setOtp(@NonNull String otp) {
        this.otp = otp;
    }
}