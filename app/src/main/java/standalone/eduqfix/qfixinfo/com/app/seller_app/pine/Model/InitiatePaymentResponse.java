package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model;

import com.google.gson.annotations.SerializedName;

public class InitiatePaymentResponse {

    @SerializedName("access_code")
    private String accessCode;
    @SerializedName("qfix_reference_number")
    private String qfixReferenceNumber;

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getQfixReferenceNumber() {
        return qfixReferenceNumber;
    }

    public void setQfixReferenceNumber(String qfixReferenceNumber) {
        this.qfixReferenceNumber = qfixReferenceNumber;
    }
}
