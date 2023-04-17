package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

public class ProductOption {
    @SerializedName("extension_attributes")
    ExtentionAttributes extension_attributes;

    public ExtentionAttributes getExtension_attributes() {
        return extension_attributes;
    }

    public void setExtension_attributes(ExtentionAttributes extension_attributes) {
        this.extension_attributes = extension_attributes;
    }
}
