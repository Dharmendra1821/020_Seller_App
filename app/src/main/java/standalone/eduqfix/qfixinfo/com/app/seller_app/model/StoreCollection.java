package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreCollection {

    @SerializedName("total_store_refunds")
    private String total_store_refunds;

    @SerializedName("items")
    public List<StoreItems> items;

    public String getTotal_store_refunds() {
        return total_store_refunds;
    }

    public void setTotal_store_refunds(String total_store_refunds) {
        this.total_store_refunds = total_store_refunds;
    }

    public List<StoreItems> getItems() {
        return items;
    }

    public void setItems(List<StoreItems> items) {
        this.items = items;
    }
}
