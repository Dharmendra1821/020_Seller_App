package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import java.io.Serializable;

public class ComplaintsModel implements Serializable {
    private String entity_id;
    private String product_id;
    private String vendor_id;
    private String customer_comments;
    private String customer_id;
    private String request_status;
    private String vendor_comments;
    private String seller_name;
    private String product_name;
    private String customer_name;
    private String seller_response_date;
    private String orderid;

    public ComplaintsModel(String entity_id, String product_id, String vendor_id, String customer_comments, String customer_id, String request_status, String vendor_comments, String seller_name, String product_name, String customer_name, String seller_response_date, String orderid, String itemid) {
        this.entity_id = entity_id;
        this.product_id = product_id;
        this.vendor_id = vendor_id;
        this.customer_comments = customer_comments;
        this.customer_id = customer_id;
        this.request_status = request_status;
        this.vendor_comments = vendor_comments;
        this.seller_name = seller_name;
        this.product_name = product_name;
        this.customer_name = customer_name;
        this.seller_response_date = seller_response_date;
        this.orderid = orderid;
        this.itemid = itemid;
    }

    private String itemid;

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getCustomer_comments() {
        return customer_comments;
    }

    public void setCustomer_comments(String customer_comments) {
        this.customer_comments = customer_comments;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }

    public String getVendor_comments() {
        return vendor_comments;
    }

    public void setVendor_comments(String vendor_comments) {
        this.vendor_comments = vendor_comments;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getSeller_response_date() {
        return seller_response_date;
    }

    public void setSeller_response_date(String seller_response_date) {
        this.seller_response_date = seller_response_date;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }
}
