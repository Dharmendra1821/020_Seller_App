package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

public class InvoiceProductList {

    @SerializedName("id")
    private String id;

    @SerializedName("product_name")
    private String product_name;

    @SerializedName("price")
    private String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("cgst/sgst")
    private String cgst;

    @SerializedName("igst")
    private String igst;


}
