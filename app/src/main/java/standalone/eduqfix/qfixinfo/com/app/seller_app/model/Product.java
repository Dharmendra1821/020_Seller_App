package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {

    private boolean isSelected;
    @SerializedName("sku")
    private String sku;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @SerializedName("productName")
    private String productName;
    @SerializedName("productPrice")
    private String productPrice;
    @SerializedName("productId")
    private String productId;
    @SerializedName("productImage")
    private String productImage;
    @SerializedName("productSmallImage")
    private String productSmallImage;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("producturl")
    private String producturl;

    public String getProducturl() {
        return producturl;
    }

    public void setProducturl(String producturl) {
        this.producturl = producturl;
    }

    @SerializedName("createdOn")
    private String createdOn;
    boolean IsChecked=false;
    private String selectedQuantity="0";
    private String productType;
    @SerializedName("isProductInStock")
    private Integer isProductInStock;
    @SerializedName("option_collection")
    @Expose
    private List<OpenCollection> openCollections = null;

    public List<OpenCollection> getOpenCollections() {
        return openCollections;
    }

    public void setOpenCollections(List<OpenCollection> openCollections) {
        this.openCollections = openCollections;
    }

    @SerializedName("min_price")
    @Expose
    private String minPrice;
    @SerializedName("max_price")
    @Expose
    private String maxPrice;



    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(String selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public void setChecked(boolean checked) { IsChecked = checked; }
    public boolean getChecked() { return IsChecked; }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getIsProductInStock() {
        return isProductInStock;
    }

    public void setIsProductInStock(Integer isProductInStock) {
        this.isProductInStock = isProductInStock;
    }
}
