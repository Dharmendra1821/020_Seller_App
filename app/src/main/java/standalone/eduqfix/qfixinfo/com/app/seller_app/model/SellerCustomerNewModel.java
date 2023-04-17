package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class SellerCustomerNewModel {

     private String customerId;
     private String customerName;
     private String customerEmail;
     private String customerMobile;
     private String status;
     private String gender;
     private String addOnDate;

    public SellerCustomerNewModel(String customerId, String customerName, String customerEmail, String customerMobile, String status, String gender, String addOnDate) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerMobile = customerMobile;
        this.status = status;
        this.gender = gender;
        this.addOnDate = addOnDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddOnDate() {
        return addOnDate;
    }

    public void setAddOnDate(String addOnDate) {
        this.addOnDate = addOnDate;
    }
}
