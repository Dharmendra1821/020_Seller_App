package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model;

import com.google.gson.annotations.SerializedName;

public class AcknowledgeRequest {

    @SerializedName("accessCode")
    private String accessCode;
    @SerializedName("paymentGatewayRequest")
    private PaymentGatewayRequest paymentGatewayRequest;
    @SerializedName("paymentGatewayResponse")
    private PaymentGatewayResponse paymentGatewayResponse;
    @SerializedName("transactionErrorCode")
    private String transactionErrorCode;
    @SerializedName("transactionStatusCode")
    private String transactionStatusCode;
    @SerializedName("updatedBy")
    private Integer updatedBy;

    public String getPaymentOptionCode() {
        return paymentOptionCode;
    }

    public void setPaymentOptionCode(String paymentOptionCode) {
        this.paymentOptionCode = paymentOptionCode;
    }

    @SerializedName("channel")
    private String channel;
    private String environment;
    private String paymentOptionCode;

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public PaymentGatewayRequest getPaymentGatewayRequest() {
        return paymentGatewayRequest;
    }

    public void setPaymentGatewayRequest(PaymentGatewayRequest paymentGatewayRequest) {
        this.paymentGatewayRequest = paymentGatewayRequest;
    }

    public PaymentGatewayResponse getPaymentGatewayResponse() {
        return paymentGatewayResponse;
    }

    public void setPaymentGatewayResponse(PaymentGatewayResponse paymentGatewayResponse) {
        this.paymentGatewayResponse = paymentGatewayResponse;
    }

    public String getTransactionErrorCode() {
        return transactionErrorCode;
    }

    public void setTransactionErrorCode(String transactionErrorCode) {
        this.transactionErrorCode = transactionErrorCode;
    }

    public String getTransactionStatusCode() {
        return transactionStatusCode;
    }

    public void setTransactionStatusCode(String transactionStatusCode) {
        this.transactionStatusCode = transactionStatusCode;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
