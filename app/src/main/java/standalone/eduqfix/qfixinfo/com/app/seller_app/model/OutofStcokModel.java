package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class OutofStcokModel {
    private String sku;
    private String productName;
    private String thumbnail;
    private String productType;
    private String price;
    private String quantity;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public OutofStcokModel(String sku, String productName, String thumbnail, String productType, String price,String quantity) {
        this.sku = sku;
        this.productName = productName;
        this.thumbnail = thumbnail;
        this.productType = productType;
        this.price = price;
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
