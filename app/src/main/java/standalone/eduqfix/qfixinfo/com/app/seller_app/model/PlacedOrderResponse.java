package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 30/4/19.
 */

public class PlacedOrderResponse {

    @SerializedName("order_id")
    private String orderId;
    @SerializedName("status")
    private Integer status;


    public String getSend_pay_link_orderid() {
        return send_pay_link_orderid;
    }

    public void setSend_pay_link_orderid(String send_pay_link_orderid) {
        this.send_pay_link_orderid = send_pay_link_orderid;
    }

    @SerializedName("send_pay_link_orderid")
    private String send_pay_link_orderid;
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
