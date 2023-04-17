package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by darshan on 29/4/19.
 */

public class OrderSummary {

    @SerializedName("payment_methods")
    private List<PaymentMethod> paymentMethods = null;
    @SerializedName("totals")
    private Totals totals;

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public Totals getTotals() {
        return totals;
    }

    public void setTotals(Totals totals) {
        this.totals = totals;
    }
}
