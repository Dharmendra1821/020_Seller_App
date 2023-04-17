package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by darshan on 29/4/19.
 */

public class Collection {

    @SerializedName("shipping_address")
    private ShippingAddress shippingAddress;
    @SerializedName("email")
    private String email;
    @SerializedName("identifier_flag")
    private String identifierFlag;
    @SerializedName("channel")
    private String channel;
    @SerializedName("customerId")
    private Integer customerId;
    @SerializedName("items")
    private List<Item> items = null;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    @SerializedName("billing_address")
    private BillingAddress billingAddress;
    @SerializedName("cartId")
    private Integer cartId;
    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentifierFlag() {
        return identifierFlag;
    }

    public void setIdentifierFlag(String identifierFlag) {
        this.identifierFlag = identifierFlag;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }
}
