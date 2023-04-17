package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model;

import com.google.gson.annotations.SerializedName;

public class UpdatePaymentRequest {

    @SerializedName("accessCode")
    private String access_code;
    @SerializedName("paymentGatewayMode")
    private String paymentGatewayMode;
    @SerializedName("paymentOptionCode")
    private String paymentOptionCode;
    @SerializedName("totalAmount")
    private String totalAmount;

    public String getPaymentOptionCode() {
        return paymentOptionCode;
    }

    public void setPaymentOptionCode(String paymentOptionCode) {
        this.paymentOptionCode = paymentOptionCode;
    }

    @SerializedName("requestPayload")
    private String requestPayload;

    public String getAccess_code() {
        return access_code;
    }

    public void setAccess_code(String access_code) {
        this.access_code = access_code;
    }

    public String getPaymentGatewayMode() {
        return paymentGatewayMode;
    }

    public void setPaymentGatewayMode(String paymentGatewayMode) {
        this.paymentGatewayMode = paymentGatewayMode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRequestPayload() {
        return requestPayload;
    }

    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }

    @SerializedName("requestParameters")
    private String requestParameters;


}
