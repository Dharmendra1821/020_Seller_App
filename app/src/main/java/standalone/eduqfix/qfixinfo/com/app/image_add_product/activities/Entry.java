package standalone.eduqfix.qfixinfo.com.app.image_add_product.activities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Entry {

    @SerializedName("media_type")
    private String media_type;
    @SerializedName("label")
    private String label;
    @SerializedName("position")
    private int position;

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    @SerializedName("disabled")
    private boolean disabled;
    private ArrayList<String> types;

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }
    @SerializedName("content")
    Content content;



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

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
