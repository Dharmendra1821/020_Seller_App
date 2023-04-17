package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenCollection {

    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("product_id")
    @Expose
    private String productid;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getAttributeid() {
        return attributeid;
    }

    public void setAttributeid(String attributeid) {
        this.attributeid = attributeid;
    }

    public String getDefaulttitle() {
        return defaulttitle;
    }

    public void setDefaulttitle(String defaulttitle) {
        this.defaulttitle = defaulttitle;
    }

    public String getValueindex() {
        return valueindex;
    }

    public void setValueindex(String valueindex) {
        this.valueindex = valueindex;
    }

    public String getDisplaylabel() {
        return displaylabel;
    }

    public void setDisplaylabel(String displaylabel) {
        this.displaylabel = displaylabel;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @SerializedName("attribute_id")
    @Expose
    private String attributeid;
    @SerializedName("default_title")
    @Expose
    private String defaulttitle;
    @SerializedName("value_index")
    @Expose
    private String valueindex;
    @SerializedName("display_label")
    @Expose
    private String displaylabel;
    @SerializedName("price")
    @Expose
    private String price;
}
