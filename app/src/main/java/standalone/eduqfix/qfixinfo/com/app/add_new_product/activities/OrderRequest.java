package standalone.eduqfix.qfixinfo.com.app.add_new_product.activities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderRequest {

    @SerializedName("seller_id")
    @Expose
    private String seller_id;
    @SerializedName("limit")
    @Expose
    private String limit;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("payment_status")
    @Expose
    private String payment_status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getMode_of_payment() {
        return mode_of_payment;
    }

    public void setMode_of_payment(String mode_of_payment) {
        this.mode_of_payment = mode_of_payment;
    }

    @SerializedName("mode_of_payment")
    @Expose
    private String mode_of_payment;
    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
    public String getSeller_id() {
        return seller_id;
    }
    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

}
