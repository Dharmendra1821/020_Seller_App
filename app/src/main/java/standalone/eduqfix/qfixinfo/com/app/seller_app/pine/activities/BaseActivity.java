package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.activities;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Utility.UIUtils;

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Hide Soft Keyboard automatically for Dialogs.
     */
    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && imm.isActive()) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        UIUtils.closeVirtualKeyBoard(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    protected void setBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}