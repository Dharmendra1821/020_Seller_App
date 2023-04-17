package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class UIUtils {

    private static Toast mToast;
    private static ProgressDialog dialog;


    public static void makeToast(Context context, String msg) {
        try {
            if (mToast != null) {
                mToast.cancel();
            }

            if (msg != null && msg.length() > 0) {
                mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                mToast.show();
            }
        } catch (Exception e) {
            Log.e("toast", "" + e.getMessage());
        }
    }





    /**
     * close Keyboard from Activity
     */
    public static void closeVirtualKeyBoard(Context context) {
        Activity activity;
        if (context instanceof Activity) {
            activity = ((Activity) context);

            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
            }
            //if (imm != null && imm.isActive() && imm.isAcceptingText() && activity.getCurrentFocus() != null)

        }
    }



    public static void showDialog(Context context, String msg) {
        try {
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage(msg);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void dismissProgressbar() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
