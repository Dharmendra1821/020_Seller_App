package standalone.eduqfix.qfixinfo.com.app.login.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 27/2/19.
 */

public class CustomAttribute {

    @SerializedName("attribute_code")
    private String attributeCode;
    @SerializedName("value")
    private String value;

    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
