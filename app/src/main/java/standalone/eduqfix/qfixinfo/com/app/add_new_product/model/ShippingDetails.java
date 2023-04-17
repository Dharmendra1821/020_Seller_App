package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ShippingDetails {

    @SerializedName("shippingcity")
    private String shippingcity;
    @SerializedName("shippingpostcode")
    private String shippingpostcode;
    @SerializedName("shippingtelephone")
    private String shippingtelephone;
    @SerializedName("shippingstate_code")
    private String shippingstate_code;
    @SerializedName("shippingstreet")
    private ArrayList<String> shippingstreet;

    public String getShippingcity() {
        return shippingcity;
    }

    public void setShippingcity(String shippingcity) {
        this.shippingcity = shippingcity;
    }

    public String getShippingpostcode() {
        return shippingpostcode;
    }

    public void setShippingpostcode(String shippingpostcode) {
        this.shippingpostcode = shippingpostcode;
    }

    public String getShippingtelephone() {
        return shippingtelephone;
    }

    public void setShippingtelephone(String shippingtelephone) {
        this.shippingtelephone = shippingtelephone;
    }

    public String getShippingstate_code() {
        return shippingstate_code;
    }

    public void setShippingstate_code(String shippingstate_code) {
        this.shippingstate_code = shippingstate_code;
    }

    public ArrayList<String> getShippingstreet() {
        return shippingstreet;
    }

    public void setShippingstreet(ArrayList<String> shippingstreet) {
        this.shippingstreet = shippingstreet;
    }
}
