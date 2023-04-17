package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;



public class NewLoginResponse {

    @SerializedName("sellerDetails")
    public SellerDetails sellerDetails;

    public SellerDetails getSellerDetails() {
        return sellerDetails;
    }

    public void setSellerDetails(SellerDetails sellerDetails) {
        this.sellerDetails = sellerDetails;
    }
}
