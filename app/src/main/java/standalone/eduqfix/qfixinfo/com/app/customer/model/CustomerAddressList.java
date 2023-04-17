package standalone.eduqfix.qfixinfo.com.app.customer.model;

public class CustomerAddressList  {
    Integer CustomerId;
    Integer CustomerAddressId;
    String Address1;
    String Address2;
    String State;
    String ZipCode;
    Boolean isChecked;
    public Integer getCustomerAddressId() {
        return CustomerAddressId;
    }

    public void setCustomerAddressId(Integer customerAddressId) {
        CustomerAddressId = customerAddressId;
    }



    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Integer getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(Integer customerId) {
        CustomerId = customerId;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }
}
