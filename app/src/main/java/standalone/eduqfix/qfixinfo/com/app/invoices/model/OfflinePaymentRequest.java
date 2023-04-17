package standalone.eduqfix.qfixinfo.com.app.invoices.model;

/**
 * Created by darshan on 27/2/19.
 */

public class OfflinePaymentRequest {

    private MarkAsPaidRequest request;

    public MarkAsPaidRequest getRequest() {
        return request;
    }

    public void setRequest(MarkAsPaidRequest request) {
        this.request = request;
    }
}
