
package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomAttribute {

    @SerializedName("attribute_code")
    @Expose
    private String attributeCode;

    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }

    @SerializedName("value")
    @Expose
    private Object value;

    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }


}
