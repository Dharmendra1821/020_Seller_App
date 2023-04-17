package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.config;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context appContext;

    public static Context getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

}
