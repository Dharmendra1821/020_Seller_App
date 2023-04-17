package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class CustomerListModel {

    private String name;
    private String email;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private String mobile;
    private String is_credit_available;
    private boolean isSelected;

    public CustomerListModel(String name, String email, String mobile, String is_credit_available, String customerId) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.is_credit_available = is_credit_available;
        this.customerId = customerId;
    }

    private String customerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIs_credit_available() {
        return is_credit_available;
    }

    public void setIs_credit_available(String is_credit_available) {
        this.is_credit_available = is_credit_available;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
