package standalone.eduqfix.qfixinfo.com.app.invoices.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 27/2/19.
 */

public class MarkAsPaidRequest {

    @SerializedName("entityId")
    private String entityId;
    private String token;
    @SerializedName("deliveryBoyId")
    private Integer deliveryBoyId;
    @SerializedName("paymenyChannel")
    private Integer paymenyChannel;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getDeliveryBoyId() {
        return deliveryBoyId;
    }

    public void setDeliveryBoyId(Integer deliveryBoyId) {
        this.deliveryBoyId = deliveryBoyId;
    }

    public Integer getPaymenyChannel() {
        return paymenyChannel;
    }

    public void setPaymenyChannel(Integer paymenyChannel) {
        this.paymenyChannel = paymenyChannel;
    }
}
