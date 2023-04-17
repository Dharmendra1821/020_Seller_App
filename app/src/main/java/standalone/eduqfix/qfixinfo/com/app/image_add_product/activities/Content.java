package standalone.eduqfix.qfixinfo.com.app.image_add_product.activities;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class Content {
    @SerializedName("base64_encoded_data")
    private File base64_encoded_data;
    @SerializedName("type")
    private String type;

    public File getBase64_encoded_data() {
        return base64_encoded_data;
    }

    public void setBase64_encoded_data(File base64_encoded_data) {
        this.base64_encoded_data = base64_encoded_data;
    }

    @SerializedName("name")
    private String name;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
