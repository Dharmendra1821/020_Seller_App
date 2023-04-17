
package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtensionAttributes_ {

    @SerializedName("qty")
    @Expose
    private Integer qty;

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

}
