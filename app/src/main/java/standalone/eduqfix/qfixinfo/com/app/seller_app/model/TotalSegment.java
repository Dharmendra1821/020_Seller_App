package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 29/4/19.
 */

public class TotalSegment {

    @SerializedName("code")
    private String code;
    @SerializedName("title")
    private String title;
    @SerializedName("value")
    private Double value;
    @SerializedName("extension_attributes")
    private ExtensionAttributes extensionAttributes;
    @SerializedName("area")
    private String area;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public ExtensionAttributes getExtensionAttributes() {
        return extensionAttributes;
    }

    public void setExtensionAttributes(ExtensionAttributes extensionAttributes) {
        this.extensionAttributes = extensionAttributes;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
