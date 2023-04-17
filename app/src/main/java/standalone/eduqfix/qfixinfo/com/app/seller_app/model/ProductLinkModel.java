package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class ProductLinkModel {

    public String id;
    public int option_Id;
    public String productImage;
    public String productName;
    public String productPrice;
    public String productSmallImage;
    public String sku1;
    public int qty;

    public ProductLinkModel(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOption_Id() {
        return option_Id;
    }

    public void setOption_Id(int option_Id) {
        this.option_Id = option_Id;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
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

    public String getProductSmallImage() {
        return productSmallImage;
    }

    public void setProductSmallImage(String productSmallImage) {
        this.productSmallImage = productSmallImage;
    }

    public String getSku1() {
        return sku1;
    }

    public void setSku1(String sku1) {
        this.sku1 = sku1;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
