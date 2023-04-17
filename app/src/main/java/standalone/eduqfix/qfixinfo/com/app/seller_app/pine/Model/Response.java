package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model;

import com.google.gson.annotations.SerializedName;

import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.ResponseStatus;

public class Response {
    @SerializedName("AppVersion")
    private String AppVersion;

    @SerializedName("ParameterJson")
    private String ParameterJson;

    @SerializedName("ResponseCode")
    private int ResponseCode;

    @SerializedName("ResponseMsg")
    private String ResponseMsg;

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }

    public String getParameterJson() {
        return ParameterJson;
    }

    public void setParameterJson(String parameterJson) {
        ParameterJson = parameterJson;
    }

    public int getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(int responseCode) {
        ResponseCode = responseCode;
    }

    public boolean isSuccess() {
        return ResponseCode == ResponseStatus.SUCCESS.getValue();
    }

    public String getResponseMsg() {
        return ResponseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        ResponseMsg = responseMsg;
    }
}
