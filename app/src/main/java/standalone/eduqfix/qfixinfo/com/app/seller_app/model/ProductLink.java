
package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductLink {

    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("link_type")
    @Expose
    private String linkType;
    @SerializedName("linked_product_sku")
    @Expose
    private String linkedProductSku;
    @SerializedName("linked_product_type")
    @Expose
    private String linkedProductType;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("extension_attributes")
    @Expose
    private ExtAttributes extensionAttributes;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productPrice")
    @Expose
    private String productPrice;
    @SerializedName("productImage")
    @Expose
    private Object productImage;
    @SerializedName("productSmallImage")
    @Expose
    private Object productSmallImage;
    @SerializedName("thumbnail")
    @Expose
    private Object thumbnail;
    @SerializedName("productId")
    @Expose
    private String productId;


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("option_id")
    @Expose
    private Integer optionId;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("price")
    @Expose
    private Object price;
    @SerializedName("price_type")
    @Expose
    private Object priceType;





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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





    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkedProductSku() {
        return linkedProductSku;
    }

    public void setLinkedProductSku(String linkedProductSku) {
        this.linkedProductSku = linkedProductSku;
    }

    public String getLinkedProductType() {
        return linkedProductType;
    }

    public void setLinkedProductType(String linkedProductType) {
        this.linkedProductType = linkedProductType;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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

    public Object getProductImage() {
        return productImage;
    }

    public void setProductImage(Object productImage) {
        this.productImage = productImage;
    }

    public Object getProductSmallImage() {
        return productSmallImage;
    }

    public void setProductSmallImage(Object productSmallImage) {
        this.productSmallImage = productSmallImage;
    }

    public Object getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Object thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}
