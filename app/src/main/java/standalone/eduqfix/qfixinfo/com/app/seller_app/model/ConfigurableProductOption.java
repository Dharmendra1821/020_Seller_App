package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfigurableProductOption {

    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttribute_id() {
        return attribute_id;
    }

    public void setAttribute_id(String attribute_id) {
        this.attribute_id = attribute_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @SerializedName("attribute_id")
    @Expose
    private String attribute_id;
    @SerializedName("label")
    @Expose
    private String label;

    public List<ConfigValue> getValues() {
        return values;
    }

    public void setValues(List<ConfigValue> values) {
        this.values = values;
    }

    @SerializedName("position")
    @Expose
    private int position;
    @SerializedName("values")
    @Expose
    private List<ConfigValue> values = null;

}
