package standalone.eduqfix.qfixinfo.com.app.customer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by darshan on 30/4/19.
 */

public class Customer {

    @SerializedName("id")
    private Integer id;
    @SerializedName("group_id")
    private Integer groupId;
    @SerializedName("default_billing")
    private String defaultBilling;
    @SerializedName("default_shipping")
    private String defaultShipping;
    @SerializedName("confirmation")
    private String confirmation;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("created_in")
    private String createdIn;
    @SerializedName("dob")
    private String dob;
    @SerializedName("email")
    private String email;
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
    @SerializedName("gender")
    private Integer gender;
    @SerializedName("store_id")
    private Integer storeId;
    @SerializedName("taxvat")
    private String taxvat;
    @SerializedName("website_id")
    private Integer websiteId;
    @SerializedName("addresses")
    private List<Address> addresses = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getDefaultBilling() {
        return defaultBilling;
    }

    public void setDefaultBilling(String defaultBilling) {
        this.defaultBilling = defaultBilling;
    }

    public String getDefaultShipping() {
        return defaultShipping;
    }

    public void setDefaultShipping(String defaultShipping) {
        this.defaultShipping = defaultShipping;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedIn() {
        return createdIn;
    }

    public void setCreatedIn(String createdIn) {
        this.createdIn = createdIn;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getTaxvat() {
        return taxvat;
    }

    public void setTaxvat(String taxvat) {
        this.taxvat = taxvat;
    }

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
