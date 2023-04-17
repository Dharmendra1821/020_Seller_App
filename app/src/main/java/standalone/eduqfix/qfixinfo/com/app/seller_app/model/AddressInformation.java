package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

public class AddressInformation {

    @SerializedName("shippingAddress")
    private ShippingAddress shippingAddress;
    @SerializedName("billingAddress")
    private BillingAddress billingAddress;

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getShippingCarrierCode() {
        return shippingCarrierCode;
    }

    public void setShippingCarrierCode(String shippingCarrierCode) {
        this.shippingCarrierCode = shippingCarrierCode;
    }

    public String getShippingMethodCode() {
        return shippingMethodCode;
    }

    public void setShippingMethodCode(String shippingMethodCode) {
        this.shippingMethodCode = shippingMethodCode;
    }

    @SerializedName("shippingCarrierCode")
    private String shippingCarrierCode;
    @SerializedName("shippingMethodCode")
    private String shippingMethodCode;
}
