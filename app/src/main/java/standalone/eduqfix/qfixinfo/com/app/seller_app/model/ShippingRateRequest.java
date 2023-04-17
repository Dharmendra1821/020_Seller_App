package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 29/4/19.
 */

public class ShippingRateRequest {

    @SerializedName("request")
    private Request request;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
