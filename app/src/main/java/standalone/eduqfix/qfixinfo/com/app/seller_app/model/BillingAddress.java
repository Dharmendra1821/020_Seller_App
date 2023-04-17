package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 29/4/19.
 */

public class BillingAddress {

    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("street")
    private String street;
    @SerializedName("city")
    private String city;
    @SerializedName("country_id")
    private String countryId;
    @SerializedName("region")
    private String region;
    @SerializedName("postcode")
    private String postcode;
    @SerializedName("telephone")
    private String telephone;
    @SerializedName("fax")
    private String fax;
    @SerializedName("regionId")
    private Integer regionId;
    @SerializedName("save_in_address_book")
    private Integer saveInAddressBook;
    @SerializedName("customer_address_id")
    private Integer customer_address_id;

    public Integer getCustomer_address_id() {
        return customer_address_id;
    }

    public void setCustomer_address_id(Integer customer_address_id) {
        this.customer_address_id = customer_address_id;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public Integer getSaveInAddressBook() {
        return saveInAddressBook;
    }

    public void setSaveInAddressBook(Integer saveInAddressBook) {
        this.saveInAddressBook = saveInAddressBook;
    }
}
