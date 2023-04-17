package standalone.eduqfix.qfixinfo.com.app.qpos_login.model;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Created by darshan on 27/2/19.
 */

public class QPOSLogin extends BaseObservable {

    @Bindable
    public String mosambeeId;
    @Bindable
    public String mosambeePassword;

    public String getMosambeeId() {
        return mosambeeId;
    }

    public void setMosambeeId(String mosambeeId) {
        this.mosambeeId = mosambeeId;
    }

    public String getMosambeePassword() {
        return mosambeePassword;
    }

    public void setMosambeePassword(String mosambeePassword) {
        this.mosambeePassword = mosambeePassword;
    }
}
