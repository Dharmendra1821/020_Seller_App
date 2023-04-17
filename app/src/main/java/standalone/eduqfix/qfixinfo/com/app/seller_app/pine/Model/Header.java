package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model;

import com.google.gson.annotations.SerializedName;

public class Header {

    @SerializedName("ApplicationId")
    private String ApplicationId;

    @SerializedName("MethodId")
    private String MethodId;

    @SerializedName("UserId")
    private String UserId;

    @SerializedName("VersionNo")
    private String VersionNo;

    public Header() {
    }

    public Header(String applicationId, String methodId, String userId, String versionNo) {
        this.ApplicationId = applicationId;
        this.MethodId = methodId;
        this.UserId = userId;
        this.VersionNo = versionNo;
    }

    public String getApplicationId() {
        return ApplicationId;
    }

    public void setApplicationId(String applicationId) {
        ApplicationId = applicationId;
    }

    public String getMethodId() {
        return MethodId;
    }

    public void setMethodId(String methodId) {
        MethodId = methodId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getVersionNo() {
        return VersionNo;
    }

    public void setVersionNo(String versionNo) {
        VersionNo = versionNo;
    }
}
