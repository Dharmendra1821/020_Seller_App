
package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BundleProductOption {

    @SerializedName("option_id")
    @Expose
    private Integer optionId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("required")
    @Expose
    private Boolean required;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("product_links")
    @Expose
    private List<ProductLink> productLinks = null;

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<ProductLink> getProductLinks() {
        return productLinks;
    }

    public void setProductLinks(List<ProductLink> productLinks) {
        this.productLinks = productLinks;
    }

}
