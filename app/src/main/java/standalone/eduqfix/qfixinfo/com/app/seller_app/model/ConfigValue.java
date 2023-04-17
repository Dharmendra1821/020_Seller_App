package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfigValue {

    public int getValueindex() {
        return valueindex;
    }

    public void setValueindex(int valueindex) {
        this.valueindex = valueindex;
    }

    @SerializedName("value_index")
    @Expose
    private int valueindex;

}
