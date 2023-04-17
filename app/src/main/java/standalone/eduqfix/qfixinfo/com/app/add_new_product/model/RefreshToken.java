package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import com.google.gson.annotations.SerializedName;

public class RefreshToken {
    @SerializedName("admin_token")
    public String admin_token;

    public String getAdmin_token() {
        return admin_token;
    }

    public void setAdmin_token(String admin_token) {
        this.admin_token = admin_token;
    }

    public String getCustomer_token() {
        return customer_token;
    }

    public void setCustomer_token(String customer_token) {
        this.customer_token = customer_token;
    }

    @SerializedName("customer_token")
    public String customer_token;


}
