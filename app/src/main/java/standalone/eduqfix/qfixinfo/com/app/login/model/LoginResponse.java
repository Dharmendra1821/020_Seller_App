package standalone.eduqfix.qfixinfo.com.app.login.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 27/2/19.
 */

public class LoginResponse {

    @SerializedName("customerDetails")
    private CustomerDetails customerDetails;
    @SerializedName("customerToken")
    private String customerToken;
    private String message;
    private String isSeller;

    public String getSellerAdminToken() {
        return sellerAdminToken;
    }

    public void setSellerAdminToken(String sellerAdminToken) {
        this.sellerAdminToken = sellerAdminToken;
    }

    @SerializedName("sellerAdminToken")
    private String sellerAdminToken;

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public String getCustomerToken() {
        return customerToken;
    }

    public void setCustomerToken(String customerToken) {
        this.customerToken = customerToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsSeller() {
        return isSeller;
    }

    public void setIsSeller(String isSeller) {
        this.isSeller = isSeller;
    }
}
