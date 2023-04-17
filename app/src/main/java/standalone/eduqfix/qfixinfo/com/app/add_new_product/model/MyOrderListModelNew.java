package standalone.eduqfix.qfixinfo.com.app.add_new_product.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MyOrderListModelNew implements Serializable {

    private String totalQtyOrdered;
    private String grandTotal;
    private String shippingAmount;
    private String taxAmount;
    private String weight;
    private String increment_id;
    private String created_at;
    private String subtotal;
    private String state;
    private String status;
    private String customer_firstname;
    private String customer_lastname;
    private String customer_email;
    private String methodTitle;
    private String paymentModeStatus;
    private String billingcity;
    private String billingpostcode;
    private String billingtelephone;
    private String billingstate_code;
    private String billingstreet;
    private String shippingcity;
    private String shippingpostcode;
    private String shippingtelephone;
    private String shippingstate_code;
    private String shippingDescription;
    private String shippingMethod;
    ArrayList<ProductDetailModel> productDetailModels;

    public String getShippingDescription() {
        return shippingDescription;
    }

    public void setShippingDescription(String shippingDescription) {
        this.shippingDescription = shippingDescription;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public ArrayList<ProductDetailModel> getProductDetailModels() {
        return productDetailModels;
    }

    public void setProductDetailModels(ArrayList<ProductDetailModel> productDetailModels) {
        this.productDetailModels = productDetailModels;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    private String invoiceId;

    public String getBillingstreet() {
        return billingstreet;
    }

    public void setBillingstreet(String billingstreet) {
        this.billingstreet = billingstreet;
    }

    public String getShippingstreet() {
        return shippingstreet;
    }

    public void setShippingstreet(String shippingstreet) {
        this.shippingstreet = shippingstreet;
    }

    private String shippingstreet;
    private String orderId;
    private String entityId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    private String shipment_id;
    private String carrier_name;

    public String getShipment_id() {
        return shipment_id;
    }

    public void setShipment_id(String shipment_id) {
        this.shipment_id = shipment_id;
    }

    public String getCarrier_name() {
        return carrier_name;
    }

    public void setCarrier_name(String carrier_name) {
        this.carrier_name = carrier_name;
    }

    public String getTracking_number() {
        return tracking_number;
    }

    public void setTracking_number(String tracking_number) {
        this.tracking_number = tracking_number;
    }

    private String tracking_number;

    public MyOrderListModelNew(String orderId,String entityId,String totalQtyOrdered, String grandTotal, String shippingAmount, String taxAmount, String weight, String increment_id, String created_at, String subtotal, String state, String status, String customer_firstname, String customer_lastname, String customer_email, String methodTitle, String paymentModeStatus, String billingcity, String billingpostcode, String billingtelephone, String billingstate_code, String shippingcity, String shippingpostcode, String shippingtelephone, String shippingstate_code
                               , String billingstreet, String shippingstreet, String invoiceId,
                               String shippingDesc, String shippingMethod, ArrayList<ProductDetailModel> productDetailModels,
                               String shipment_id,String carrier_name,String tracking_number) {
        this.orderId = orderId;
        this.entityId = entityId;
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
        this.customer_firstname = customer_firstname;
        this.customer_lastname = customer_lastname;
        this.customer_email = customer_email;
        this.methodTitle = methodTitle;
        this.paymentModeStatus = paymentModeStatus;
        this.billingcity = billingcity;
        this.billingpostcode = billingpostcode;
        this.billingtelephone = billingtelephone;
        this.billingstate_code = billingstate_code;
        this.shippingcity = shippingcity;
        this.shippingpostcode = shippingpostcode;
        this.shippingtelephone = shippingtelephone;
        this.shippingstate_code = shippingstate_code;
        this.billingstreet = billingstreet;
        this.shippingstreet = shippingstreet;
        this.invoiceId = invoiceId;
        this.shippingDescription = shippingDesc;
        this.shippingMethod = shippingMethod;
        this.productDetailModels = productDetailModels;
        this.shipment_id = shipment_id;
        this.carrier_name = carrier_name;
        this.tracking_number = tracking_number;
    }

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

    public String getCustomer_firstname() {
        return customer_firstname;
    }

    public void setCustomer_firstname(String customer_firstname) {
        this.customer_firstname = customer_firstname;
    }

    public String getCustomer_lastname() {
        return customer_lastname;
    }

    public void setCustomer_lastname(String customer_lastname) {
        this.customer_lastname = customer_lastname;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getMethodTitle() {
        return methodTitle;
    }

    public void setMethodTitle(String methodTitle) {
        this.methodTitle = methodTitle;
    }

    public String getPaymentModeStatus() {
        return paymentModeStatus;
    }

    public void setPaymentModeStatus(String paymentModeStatus) {
        this.paymentModeStatus = paymentModeStatus;
    }

    public String getBillingcity() {
        return billingcity;
    }

    public void setBillingcity(String billingcity) {
        this.billingcity = billingcity;
    }

    public String getBillingpostcode() {
        return billingpostcode;
    }

    public void setBillingpostcode(String billingpostcode) {
        this.billingpostcode = billingpostcode;
    }

    public String getBillingtelephone() {
        return billingtelephone;
    }

    public void setBillingtelephone(String billingtelephone) {
        this.billingtelephone = billingtelephone;
    }

    public String getBillingstate_code() {
        return billingstate_code;
    }

    public void setBillingstate_code(String billingstate_code) {
        this.billingstate_code = billingstate_code;
    }

    public String getShippingcity() {
        return shippingcity;
    }

    public void setShippingcity(String shippingcity) {
        this.shippingcity = shippingcity;
    }

    public String getShippingpostcode() {
        return shippingpostcode;
    }

    public void setShippingpostcode(String shippingpostcode) {
        this.shippingpostcode = shippingpostcode;
    }

    public String getShippingtelephone() {
        return shippingtelephone;
    }

    public void setShippingtelephone(String shippingtelephone) {
        this.shippingtelephone = shippingtelephone;
    }

    public String getShippingstate_code() {
        return shippingstate_code;
    }

    public void setShippingstate_code(String shippingstate_code) {
        this.shippingstate_code = shippingstate_code;
    }
}
