package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.activities;

import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.DetailResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Utility.UIUtils;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.helper.PineServiceHelper;

public abstract class BasePineActivity extends BaseActivity implements PineServiceHelper.PineCallBack {
    @Override
    protected void onRestart() {
        super.onRestart();
        PineServiceHelper.getInstance().connect(this);
    }

    @Override
    public void showToast(String msg) {
        UIUtils.dismissProgressbar();
        UIUtils.makeToast(this, msg);
    }

    @Override
    public void connectAgain() {
        UIUtils.dismissProgressbar();
        PineServiceHelper.getInstance().connect(this);
    }

    @Override
    public void sendResult(DetailResponse detailResponse) {
        UIUtils.dismissProgressbar();
    }

    @Override
    public void showWaitingDialog() {
        UIUtils.showDialog(this, "Please wait .......");
    }

}
