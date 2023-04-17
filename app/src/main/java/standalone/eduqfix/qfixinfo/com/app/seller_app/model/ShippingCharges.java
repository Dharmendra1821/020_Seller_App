package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

public class ShippingCharges {

    public AddressInformation getAddressInformation() {
        return addressInformation;
    }

    public void setAddressInformation(AddressInformation addressInformation) {
        this.addressInformation = addressInformation;
    }

    @SerializedName("addressInformation")
    private AddressInformation addressInformation;
}
