package standalone.eduqfix.qfixinfo.com.app.login.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by darshan on 27/2/19.
 */

public class ExtensionAttributes {

    @SerializedName("is_subscribed")
    private Boolean isSubscribed;

    public Boolean getSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        isSubscribed = subscribed;
    }
}
