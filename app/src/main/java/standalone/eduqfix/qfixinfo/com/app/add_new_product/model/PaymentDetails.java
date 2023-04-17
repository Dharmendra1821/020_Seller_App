package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import com.google.gson.annotations.SerializedName;

public class PaymentDetails {
    @SerializedName("methodTitle")
    private String methodTitle;
    @SerializedName("paymentModeStatus")
    private String paymentModeStatus;

    public String getMethodTitle() {
        return methodTitle;
    }

    public void setMethodTitle(String methodTitle) {
        this.methodTitle = methodTitle;
    }

    public String getPaymentModeStatus() {
        return paymentModeStatus;
    }

    public void setPaymentModeStatus(String paymentModeStatus) {
        this.paymentModeStatus = paymentModeStatus;
    }
}
