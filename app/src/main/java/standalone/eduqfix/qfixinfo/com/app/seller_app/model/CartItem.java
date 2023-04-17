package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    String sku;
    Integer qty;
    String user_id;
    @SerializedName("item_id")
    Integer itemId;
    String name;
    String productType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    Double price;
    ProductOption product_option;
    String type;

    public ProductOption getProduct_option() {
        return product_option;
    }

    public void setProduct_option(ProductOption product_option) {
        this.product_option = product_option;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
