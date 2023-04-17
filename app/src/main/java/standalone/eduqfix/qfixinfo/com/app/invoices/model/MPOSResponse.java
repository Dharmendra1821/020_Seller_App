package standalone.eduqfix.qfixinfo.com.app.invoices.model;

/**
 * Created by darshan on 27/2/19.
 */

public class MPOSResponse {

    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
