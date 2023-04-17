package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import java.io.Serializable;

public class ProductDetailModel implements Serializable {

    private String sku;
    private String productName;
    private String productPrice;
    private String productType;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public ProductDetailModel(String sku, String productName, String productPrice, String productType) {
        this.sku = sku;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productType = productType;
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
}
