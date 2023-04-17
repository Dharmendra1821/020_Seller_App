package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DetailMessage {

    @SerializedName("collection")
    public ArrayList<DetailCollection> collection;

    public ArrayList<DetailCollection> getCollection() {
        return collection;
    }

    public void setCollection(ArrayList<DetailCollection> collection) {
        this.collection = collection;
    }
}
