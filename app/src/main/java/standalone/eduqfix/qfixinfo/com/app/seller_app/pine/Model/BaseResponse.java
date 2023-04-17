package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Utility.GsonUtils;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.config.AppConfig;

public class BaseResponse {

    @SerializedName("OperationType")
    private int operationType;

    @SerializedName("ResponseCode")
    private int responseCode;

    @SerializedName("ResponseMessage")
    private String responseMessage;

    public Bundle getBundle() {

        Bundle bundle = new Bundle();
        bundle.putString(AppConfig.RESPONSE_KEY, new Gson().toJson(this));

        return bundle;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public String toString() {
        return GsonUtils.fromJsonToString(this);
    }
}
