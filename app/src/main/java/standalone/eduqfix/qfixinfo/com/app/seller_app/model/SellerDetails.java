package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

public class SellerDetails {


    @SerializedName("seller_id")
    private Integer seller_id;
    @SerializedName("id")
    private String id;
    @SerializedName("FirstName")
    private String FirstName;
    @SerializedName("LastName")
    private String LastName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("Email")
    private String Email;
    @SerializedName("IsCommunityMicromarketOwner")
    private String IsCommunityMicromarketOwner;
    @SerializedName("TermsandConditions")
    private String TermsandConditions;
    @SerializedName("Institute_ID")
    private String Institute_ID;
    @SerializedName("BranchId")
    private String BranchId;
    @SerializedName("DeviceId")
    private String DeviceId;
    @SerializedName("Gender")
    private String Gender;
    @SerializedName("MobileNumber")
    private String MobileNumber;
    @SerializedName("shop_title")
    private String shop_title;
    @SerializedName("company_banner")
    private String company_banner;
    @SerializedName("company_logo")
    private String company_logo;
    @SerializedName("company_description")
    private String company_description;
    @SerializedName("return_policy")
    private String return_policy;
    @SerializedName("shipping_policy")
    private String shipping_policy;
    @SerializedName("isSeller")
    private boolean isSeller;

    public Integer getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(Integer seller_id) {
        this.seller_id = seller_id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIsCommunityMicromarketOwner() {
        return IsCommunityMicromarketOwner;
    }

    public void setIsCommunityMicromarketOwner(String isCommunityMicromarketOwner) {
        IsCommunityMicromarketOwner = isCommunityMicromarketOwner;
    }

    public String getTermsandConditions() {
        return TermsandConditions;
    }

    public void setTermsandConditions(String termsandConditions) {
        TermsandConditions = termsandConditions;
    }

    public String getInstitute_ID() {
        return Institute_ID;
    }

    public void setInstitute_ID(String institute_ID) {
        Institute_ID = institute_ID;
    }

    public String getBranchId() {
        return BranchId;
    }

    public void setBranchId(String branchId) {
        BranchId = branchId;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getShop_title() {
        return shop_title;
    }

    public void setShop_title(String shop_title) {
        this.shop_title = shop_title;
    }

    public String getCompany_banner() {
        return company_banner;
    }

    public void setCompany_banner(String company_banner) {
        this.company_banner = company_banner;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getCompany_description() {
        return company_description;
    }

    public void setCompany_description(String company_description) {
        this.company_description = company_description;
    }

    public String getReturn_policy() {
        return return_policy;
    }

    public void setReturn_policy(String return_policy) {
        this.return_policy = return_policy;
    }

    public String getShipping_policy() {
        return shipping_policy;
    }

    public void setShipping_policy(String shipping_policy) {
        this.shipping_policy = shipping_policy;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }
}
