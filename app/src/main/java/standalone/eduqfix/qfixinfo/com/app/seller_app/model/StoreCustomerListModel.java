package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class StoreCustomerListModel {
    private String customerName;
    private String balance;
    private String customer_mobile;
    private String customer_id;

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public StoreCustomerListModel(String customerName, String balance,String customer_mobile,String customer_id) {
        this.customerName = customerName;
        this.balance = balance;
        this.customer_mobile = customer_mobile;
        this.customer_id = customer_id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
