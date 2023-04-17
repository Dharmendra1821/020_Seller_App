package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model;

import com.google.gson.annotations.SerializedName;

public class PaymentGatewayRequest {

    @SerializedName("amount")
    private Double amount;
    @SerializedName("container")
    private String container;
    @SerializedName("qfixReferenceNumber")
    private String qfixReferenceNumber;
    @SerializedName("transType")
    private String transType;
    private String mosambeeId;
    private String mosambeePassword;
    private String customerMobile;
    private String customerEmail;
    private String date;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getQfixReferenceNumber() {
        return qfixReferenceNumber;
    }

    public void setQfixReferenceNumber(String qfixReferenceNumber) {
        this.qfixReferenceNumber = qfixReferenceNumber;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getMosambeeId() {
        return mosambeeId;
    }

    public void setMosambeeId(String mosambeeId) {
        this.mosambeeId = mosambeeId;
    }

    public String getMosambeePassword() {
        return mosambeePassword;
    }

    public void setMosambeePassword(String mosambeePassword) {
        this.mosambeePassword = mosambeePassword;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
