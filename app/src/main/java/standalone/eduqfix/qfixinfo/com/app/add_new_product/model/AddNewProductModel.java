package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddNewProductModel implements Serializable {

    private String sku;
    private String productName;
    private String productPrice;
    private String categoryId;
    private String categoryName;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    private String subCategoryId;
    private String subCategoryName;

    public int getVisibilityStatus() {
        return visibilityStatus;
    }

    public void setVisibilityStatus(int visibilityStatus) {
        this.visibilityStatus = visibilityStatus;
    }

    private int visibilityStatus;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private String productId;
    private boolean isSelected;

    public boolean isStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(boolean stockStatus) {
        this.stockStatus = stockStatus;
    }

    private boolean stockStatus;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    private String productImage;
    private String productSmallImage;
    private String stock;
    private String thumbnail;

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    private String weight;


    public ArrayList<CategoryIdsModel> categoryIds;

    public ArrayList<CategoryIdsModel> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(ArrayList<CategoryIdsModel> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public AddNewProductMediaGallery media_gallery;
    public AddNewProductAttributes attributes;

    public AddNewProductAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(AddNewProductAttributes attributes) {
        this.attributes = attributes;
    }

    public AddNewProductMediaGallery getMedia_gallery() {
        return media_gallery;
    }

    public void setMedia_gallery(AddNewProductMediaGallery media_gallery) {
        this.media_gallery = media_gallery;
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


}
