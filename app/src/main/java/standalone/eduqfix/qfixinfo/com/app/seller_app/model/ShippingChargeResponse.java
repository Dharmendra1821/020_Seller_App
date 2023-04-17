package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShippingChargeResponse {
    public List<PaymentMethods> getPayment_methods() {
        return payment_methods;
    }

    public void setPayment_methods(List<PaymentMethods> payment_methods) {
        this.payment_methods = payment_methods;
    }

    @SerializedName("payment_methods")
    private List<PaymentMethods> payment_methods;
}
