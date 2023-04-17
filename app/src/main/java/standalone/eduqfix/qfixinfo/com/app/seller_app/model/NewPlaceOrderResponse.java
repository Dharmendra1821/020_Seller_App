package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

public class NewPlaceOrderResponse {

    @SerializedName("data")
    PlacedOrderResponse data;

    public PlacedOrderResponse getData() {
        return data;
    }

    public void setData(PlacedOrderResponse data) {
        this.data = data;
    }
}
