package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BillingDetails {
    @SerializedName("billingcity")
    private String billingcity;
    @SerializedName("billingpostcode")
    private String billingpostcode;
    @SerializedName("billingtelephone")
    private String billingtelephone;
    @SerializedName("billingstate_code")
    private String billingstate_code;
    @SerializedName("billingstreet")
    private ArrayList<String> billingstreet;

    public String getBillingcity() {
        return billingcity;
    }

    public void setBillingcity(String billingcity) {
        this.billingcity = billingcity;
    }

    public String getBillingpostcode() {
        return billingpostcode;
    }

    public void setBillingpostcode(String billingpostcode) {
        this.billingpostcode = billingpostcode;
    }

    public String getBillingtelephone() {
        return billingtelephone;
    }

    public void setBillingtelephone(String billingtelephone) {
        this.billingtelephone = billingtelephone;
    }

    public String getBillingstate_code() {
        return billingstate_code;
    }

    public void setBillingstate_code(String billingstate_code) {
        this.billingstate_code = billingstate_code;
    }

    public ArrayList<String> getBillingstreet() {
        return billingstreet;
    }

    public void setBillingstreet(ArrayList<String> billingstreet) {
        this.billingstreet = billingstreet;
    }
}
