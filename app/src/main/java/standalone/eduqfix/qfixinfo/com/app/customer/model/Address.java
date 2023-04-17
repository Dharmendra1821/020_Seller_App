package standalone.eduqfix.qfixinfo.com.app.customer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by darshan on 30/4/19.
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
    @SerializedName("company")
    private String company;
    @SerializedName("telephone")
    private String telephone;
    @SerializedName("fax")
    private String fax;
    @SerializedName("postcode")
    private String postcode;
    @SerializedName("city")
    private String city;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("middlename")
    private String middlename;
    @SerializedName("prefix")
    private String prefix;
    @SerializedName("suffix")
    private String suffix;
    @SerializedName("vat_id")
    private String vatId;
    @SerializedName("default_shipping")
    private Boolean defaultShipping;
    @SerializedName("default_billing")
    private Boolean defaultBilling;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getVatId() {
        return vatId;
    }

    public void setVatId(String vatId) {
        this.vatId = vatId;
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
}
