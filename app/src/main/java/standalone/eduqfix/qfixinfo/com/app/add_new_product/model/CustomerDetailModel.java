package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import com.google.gson.annotations.SerializedName;

public class CustomerDetailModel {
    @SerializedName("customer_email")
    private String customer_email;
    @SerializedName("customer_firstname")
    private String customer_firstname;
    @SerializedName("customer_lastname")
    private String customer_lastname;
    @SerializedName("customer_middlename")
    private String customer_middlename;

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_firstname() {
        return customer_firstname;
    }

    public void setCustomer_firstname(String customer_firstname) {
        this.customer_firstname = customer_firstname;
    }

    public String getCustomer_lastname() {
        return customer_lastname;
    }

    public void setCustomer_lastname(String customer_lastname) {
        this.customer_lastname = customer_lastname;
    }

    public String getCustomer_middlename() {
        return customer_middlename;
    }

    public void setCustomer_middlename(String customer_middlename) {
        this.customer_middlename = customer_middlename;
    }
}
