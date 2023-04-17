package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class SellerInvoiceRequest {

    @SerializedName("products_list")
    private List<InvoiceProductList> products_list = null;

    public List<InvoiceProductList> getProducts_list() {
        return products_list;
    }

    public void setProducts_list(List<InvoiceProductList> products_list) {
        this.products_list = products_list;
    }
}
