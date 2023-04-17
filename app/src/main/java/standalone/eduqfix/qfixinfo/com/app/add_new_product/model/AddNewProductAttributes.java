package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddNewProductAttributes implements Serializable {

    private String description;
    private String short_description;
    private String url_key;
    private String tax_class_id;
    private String type;

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    private String size;
    private String bag_brand;
    private String hsn_code;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl_key() {
        return url_key;
    }

    public void setUrl_key(String url_key) {
        this.url_key = url_key;
    }

    public String getTax_class_id() {
        return tax_class_id;
    }

    public void setTax_class_id(String tax_class_id) {
        this.tax_class_id = tax_class_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBag_brand() {
        return bag_brand;
    }

    public void setBag_brand(String bag_brand) {
        this.bag_brand = bag_brand;
    }

    public String getHsn_code() {
        return hsn_code;
    }

    public void setHsn_code(String hsn_code) {
        this.hsn_code = hsn_code;
    }
}
