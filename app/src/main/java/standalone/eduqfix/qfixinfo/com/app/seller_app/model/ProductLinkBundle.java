
package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductLinkBundle {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("option_id")
    @Expose
    private Integer optionId;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("is_default")
    @Expose
    private Boolean isDefault;
    @SerializedName("price")
    @Expose
    private Object price;
    @SerializedName("price_type")
    @Expose
    private Object priceType;
    @SerializedName("can_change_quantity")
    @Expose
    private Integer canChangeQuantity;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productPrice")
    @Expose
    private String productPrice;
    @SerializedName("productImage")
    @Expose
    private String productImage;
    @SerializedName("productSmallImage")
    @Expose
    private String productSmallImage;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    public Object getPriceType() {
        return priceType;
    }

    public void setPriceType(Object priceType) {
        this.priceType = priceType;
    }

    public Integer getCanChangeQuantity() {
        return canChangeQuantity;
    }

    public void setCanChangeQuantity(Integer canChangeQuantity) {
        this.canChangeQuantity = canChangeQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductSmallImage() {
        return productSmallImage;
    }

    public void setProductSmallImage(String productSmallImage) {
        this.productSmallImage = productSmallImage;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
