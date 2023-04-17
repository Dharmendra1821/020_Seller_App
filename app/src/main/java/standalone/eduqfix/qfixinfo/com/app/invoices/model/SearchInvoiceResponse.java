package standalone.eduqfix.qfixinfo.com.app.invoices.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by darshan on 27/2/19.
 */

public class SearchInvoiceResponse {

    @SerializedName("customer_name")
    private String customer_name;
    @SerializedName("invoice_number")
    private String invoice_number;
    @SerializedName("products")
    private List<Item> products = null;
    @SerializedName("amount")
    private String amount;
    @SerializedName("contact_number")
    private String contact_number;
    private String payment_status;
    private String order_status;

    public String getIncrement_id() {
        return increment_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public void setIncrement_id(String increment_id) {
        this.increment_id = increment_id;
    }

    private String increment_id;

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public List<Item> getProducts() {
        return products;
    }

    public void setProducts(List<Item> products) {
        this.products = products;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @SerializedName("grand_total")
    private String grand_total;
    @SerializedName("address")
    private String address;


}
