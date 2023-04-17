package standalone.eduqfix.qfixinfo.com.app.login.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by darshan on 27/2/19.
 */

public class Address {

    @SerializedName("id")
    private Integer id;
    @SerializedName("customer_id")
    private Integer customerId;
    @SerializedName("region")
    private Region region;
    @SerializedName("region_id")
    private Integer regionId;
    @SerializedName("country_id")
    private String countryId;
    @SerializedName("street")
    private List<String> street = null;
    @SerializedName("telephone")
    private String telephone;
    @SerializedName("postcode")
    private String postcode;
    @SerializedName("city")
    private String city;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("default_shipping")
    private Boolean defaultShipping;
    @SerializedName("default_billing")
    private Boolean defaultBilling;
    @SerializedName("address1")
    String address1;
    @SerializedName("address2")
    String address2;
    @SerializedName("state")
    String state;
    private String email;
    Boolean IsChecked=false;
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }



    public Boolean getChecked() {
        return IsChecked;
    }

    public void setChecked(Boolean checked) {
        IsChecked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public List<String> getStreet() {
        return street;
    }

    public void setStreet(List<String> street) {
        this.street = street;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Boolean getDefaultShipping() {
        return defaultShipping;
    }

    public void setDefaultShipping(Boolean defaultShipping) {
        this.defaultShipping = defaultShipping;
    }

    public Boolean getDefaultBilling() {
        return defaultBilling;
    }

    public void setDefaultBilling(Boolean defaultBilling) {
        this.defaultBilling = defaultBilling;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    public boolean IsValidDataEntered()
    {
        return (!(TextUtils.isEmpty(address1) && TextUtils.isEmpty(address2)) ||
                !TextUtils.isEmpty(state) || !TextUtils.isEmpty(postcode));
    }
}
