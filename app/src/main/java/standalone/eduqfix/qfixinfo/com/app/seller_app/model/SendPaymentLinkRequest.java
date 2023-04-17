package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 30/4/19.
 */

public class SendPaymentLinkRequest {

    @SerializedName("request")
    private NewPaymentLink request;

    public NewPaymentLink getRequest() {
        return request;
    }

    public void setRequest(NewPaymentLink request) {
        this.request = request;
    }
}
