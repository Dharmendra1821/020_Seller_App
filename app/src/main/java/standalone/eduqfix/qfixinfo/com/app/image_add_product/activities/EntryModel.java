package standalone.eduqfix.qfixinfo.com.app.image_add_product.activities;

import com.google.gson.annotations.SerializedName;

public class EntryModel {

    @SerializedName("entry")
    Entry entry;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }
}
