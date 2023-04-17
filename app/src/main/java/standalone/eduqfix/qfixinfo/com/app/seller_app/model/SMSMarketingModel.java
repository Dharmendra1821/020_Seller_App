package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

public class SMSMarketingModel {

    private String smsId;
    private String code;
    private String senderName;

    public String getSmsId() {
        return smsId;
    }

    public SMSMarketingModel(String smsId, String code, String senderName) {
        this.smsId = smsId;
        this.code = code;
        this.senderName = senderName;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
