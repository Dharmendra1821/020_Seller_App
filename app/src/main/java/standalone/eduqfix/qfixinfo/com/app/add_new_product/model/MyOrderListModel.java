package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyOrderListModel {

    public MyOrderListModel() {
    }


    @SerializedName("totalQtyOrdered")
    private String totalQtyOrdered;
    @SerializedName("grandTotal")
    private String grandTotal;
    @SerializedName("shippingAmount")
    private String shippingAmount;
    @SerializedName("taxAmount")
    private String taxAmount;
    @SerializedName("weight")
    private String weight;
    @SerializedName("increment_id")
    private String increment_id;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("subtotal")
    private String subtotal;
    @SerializedName("state")
    private String state;

    public MyOrderListModel(String totalQtyOrdered, String grandTotal, String shippingAmount, String taxAmount, String weight, String increment_id, String created_at, String subtotal, String state, String status, CustomerDetailModel customerDetails, PaymentDetails paymentDetails, BillingDetails billingDetails, ShippingDetails shippingDetails) {
        this.totalQtyOrdered = totalQtyOrdered;
        this.grandTotal = grandTotal;
        this.shippingAmount = shippingAmount;
        this.taxAmount = taxAmount;
        this.weight = weight;
        this.increment_id = increment_id;
        this.created_at = created_at;
        this.subtotal = subtotal;
        this.state = state;
        this.status = status;
        this.customerDetails = customerDetails;
        this.paymentDetails = paymentDetails;
        this.billingDetails = billingDetails;
        this.shippingDetails = shippingDetails;
    }

    @SerializedName("status")
    private String status;

    @SerializedName("customerDetails")
    private CustomerDetailModel customerDetails;
    @SerializedName("paymentDetails")
    private PaymentDetails paymentDetails;
    @SerializedName("billingDetails")
    private BillingDetails billingDetails;
    @SerializedName("shippingDetails")
    private ShippingDetails shippingDetails;

    public String getTotalQtyOrdered() {
        return totalQtyOrdered;
    }

    public void setTotalQtyOrdered(String totalQtyOrdered) {
        this.totalQtyOrdered = totalQtyOrdered;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(String shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getIncrement_id() {
        return increment_id;
    }

    public void setIncrement_id(String increment_id) {
        this.increment_id = increment_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CustomerDetailModel getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetailModel customerDetails) {
        this.customerDetails = customerDetails;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public BillingDetails getBillingDetails() {
        return billingDetails;
    }

    public void setBillingDetails(BillingDetails billingDetails) {
        this.billingDetails = billingDetails;
    }

    public ShippingDetails getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(ShippingDetails shippingDetails) {
        this.shippingDetails = shippingDetails;
    }
}
