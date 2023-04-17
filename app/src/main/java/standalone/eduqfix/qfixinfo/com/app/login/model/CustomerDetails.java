package standalone.eduqfix.qfixinfo.com.app.login.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by darshan on 27/2/19.
 */

public class CustomerDetails {

    @SerializedName("id")
    private Integer id;
    @SerializedName("group_id")
    private Integer groupId;
    @SerializedName("default_billing")
    private String defaultBilling;
    @SerializedName("default_shipping")
    private String defaultShipping;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("created_in")
    private String createdIn;
    @SerializedName("email")
    private String email;
    @SerializedName("phone_no")
    private String phoneNo;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private String lastname;
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
    @SerializedName("disable_auto_group_change")
    private Integer disableAutoGroupChange;
    @SerializedName("extension_attributes")
    private ExtensionAttributes extensionAttributes;
    @SerializedName("custom_attributes")
    private List<CustomAttribute> customAttributes = null;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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

    public Integer getDisableAutoGroupChange() {
        return disableAutoGroupChange;
    }

    public void setDisableAutoGroupChange(Integer disableAutoGroupChange) {
        this.disableAutoGroupChange = disableAutoGroupChange;
    }

    public ExtensionAttributes getExtensionAttributes() {
        return extensionAttributes;
    }

    public void setExtensionAttributes(ExtensionAttributes extensionAttributes) {
        this.extensionAttributes = extensionAttributes;
    }

    public List<CustomAttribute> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(List<CustomAttribute> customAttributes) {
        this.customAttributes = customAttributes;
    }
}
