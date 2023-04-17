package standalone.eduqfix.qfixinfo.com.app.login.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.PaymentMethod;

public class NewProductCartResponse {

    @SerializedName("data")
    private List<ProductCartResponse> data = null;

    public List<ProductCartResponse> getData() {
        return data;
    }

    public void setData(List<ProductCartResponse> data) {
        this.data = data;
    }
}
