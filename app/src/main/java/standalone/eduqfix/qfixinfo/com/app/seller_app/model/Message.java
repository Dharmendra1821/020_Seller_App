package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("collection")
    private StoreCollection collection;

    public StoreCollection getCollection() {
        return collection;
    }

    public void setCollection(StoreCollection collection) {
        this.collection = collection;
    }
}
