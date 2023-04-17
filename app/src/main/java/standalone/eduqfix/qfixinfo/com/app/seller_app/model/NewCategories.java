package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewCategories {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("parent_id")
    @Expose
    private String parent_id;

    @SerializedName("children_data")
    @Expose
    private List<ChildrenData> children_data = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public List<ChildrenData> getChildren_data() {
        return children_data;
    }

    public void setChildren_data(List<ChildrenData> children_data) {
        this.children_data = children_data;
    }
}
