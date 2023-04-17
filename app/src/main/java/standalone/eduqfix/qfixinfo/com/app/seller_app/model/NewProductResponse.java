package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;

public class NewProductResponse {

    List<ProductCartResponse> product;
    private String shipping_charges;

    public List<ProductCartResponse> getProduct() {
        return product;
    }

    public void setProduct(List<ProductCartResponse> product) {
        this.product = product;
    }

    public String getShipping_charges() {
        return shipping_charges;
    }

    public void setShipping_charges(String shipping_charges) {
        this.shipping_charges = shipping_charges;
    }
}
