package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 30/4/19.
 */

public class PaymentRequest {

    @SerializedName("send_payment_link_email")
    private String sendPaymentLinkEmail;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("send_payment_link_mobile_number")
    private String sendPaymentLinkMobileNumber;
    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSendPaymentLinkEmail() {
        return sendPaymentLinkEmail;
    }

    public void setSendPaymentLinkEmail(String sendPaymentLinkEmail) {
        this.sendPaymentLinkEmail = sendPaymentLinkEmail;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSendPaymentLinkMobileNumber() {
        return sendPaymentLinkMobileNumber;
    }

    public void setSendPaymentLinkMobileNumber(String sendPaymentLinkMobileNumber) {
        this.sendPaymentLinkMobileNumber = sendPaymentLinkMobileNumber;
    }
}
