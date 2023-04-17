package standalone.eduqfix.qfixinfo.com.app.seller_app.pine.helper;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.BaseResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.DetailResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.HeaderRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Utility.GsonUtils;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.config.AppConfig;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.config.MyApplication;

public class PineServiceHelper {
    private static final int MASTER_APP = 2;
    private static final int BILLING_APP = 1001;

    private static final String TAG = PineServiceHelper.class.getSimpleName();
    private static PineServiceHelper INSTANCE;
    private Messenger mServerMessenger;
    private Messenger mClientMessenger;
    private boolean isBound;
    private PineCallBack pineCallBack;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServerMessenger = new Messenger(service);
            isBound = true;
            try {
                linkToDeath(service);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServerMessenger = null;
            isBound = false;
            if (pineCallBack != null) {
                pineCallBack.connectAgain();
            }
        }

        private void linkToDeath(IBinder service) throws RemoteException {
            service.linkToDeath(new IBinder.DeathRecipient() {
                @Override
                public void binderDied() {
                    Log.d(TAG, "Device service is dead. Reconnecting...");
                }
            }, 0);
        }

    };

    private PineServiceHelper() {
        HandlerThread thread = new HandlerThread("ClientThread");
        thread.start();

        mClientMessenger = new Messenger(new IncomingHandler(thread));
    }

    public static PineServiceHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (PineServiceHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PineServiceHelper();
                }
            }
        }
        return INSTANCE;
    }

    public void connect(PineCallBack pineCallBack) {
        this.pineCallBack = pineCallBack;
        if (!isBound) {
            Intent intent = new Intent();
            intent.setAction(AppConfig.PINE_ACTION);
            intent.setPackage(AppConfig.PINE_PACKAGE);
            MyApplication.getAppContext().bindService(intent, connection, BIND_AUTO_CREATE);
        }
    }

    public void callPineService(final HeaderRequest request) {
        sendMessage(BILLING_APP, GsonUtils.fromJsonToString(request));
    }

    private void sendMessage(final int what, final String value) {
        if (isBound && mServerMessenger != null) {
            Message message = Message.obtain(null, what);
            Bundle data = new Bundle();
            data.putString(AppConfig.REQUEST_KEY, value);
            message.setData(data);
            try {
                message.replyTo = mClientMessenger;
                mServerMessenger.send(message);
                if (pineCallBack != null) {
                    pineCallBack.showWaitingDialog();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            if (pineCallBack != null) {
                pineCallBack.showToast("Service is not connected");
                pineCallBack.connectAgain();
            }
        }
    }

    private void showDialog(final DetailResponse detailResponse) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                pineCallBack.sendResult(detailResponse);
            }
        });
    }


    public interface PineCallBack {
        void showToast(String msg);

        void connectAgain();

        void sendResult(DetailResponse detailResponse);

        void showWaitingDialog();
    }

    /**
     * Handler of incoming messages from service.
     */
    private class IncomingHandler extends Handler {

        IncomingHandler(HandlerThread thr) {
            super(thr.getLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle;
            String value;
            switch (msg.what) {
                case MASTER_APP:
                    bundle = msg.getData();
                    value = bundle.getString(AppConfig.RESPONSE_KEY);

                    BaseResponse response = GsonUtils.fromStringToJson(value, BaseResponse.class);
                    if (response != null) {
                        value = response.getResponseMessage();
                    }

                    Log.d("byte value", value + "");
                    showDialog(null);
                    break;
                case BILLING_APP:
                    bundle = msg.getData();
                    value = bundle.getString(AppConfig.RESPONSE_KEY);
                    DetailResponse detailResponse = GsonUtils.fromStringToJson(value, DetailResponse.class);
                    showDialog(detailResponse);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}