package standalone.eduqfix.qfixinfo.com.app.seller_app.model;

import com.google.gson.annotations.SerializedName;

public class StoreCreditDetailResponse {

    @SerializedName("status")
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public DetailMessage getMessage() {
        return message;
    }

    public void setMessage(DetailMessage message) {
        this.message = message;
    }

    @SerializedName("message")
    private DetailMessage message;

}
