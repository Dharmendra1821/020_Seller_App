package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfigurableItemOption {
    @SerializedName("option_id")
    @Expose
    private String option_id;

    @SerializedName("extension_attributes")
    @Expose
    private ExtAttributes extension_attributes;

    public ExtAttributes getExtension_attributes() {
        return extension_attributes;
    }

    public void setExtension_attributes(ExtAttributes extension_attributes) {
        this.extension_attributes = extension_attributes;
    }

    @SerializedName("option_value")
    @Expose
    private String option_value;

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public String getOption_value() {
        return option_value;
    }

    public void setOption_value(String option_value) {
        this.option_value = option_value;
    }
}
