package standalone.eduqfix.qfixinfo.com.app.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;


import standalone.eduqfix.qfixinfo.com.app.BuildConfig;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.MyOrdersListActivity;

public class MyFirebaseService extends FirebaseMessagingService {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    @Override
    public void onNewToken(String s) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.e("My Token", token);
                        SharedPreferences sharedPreferences = getSharedPreferences("StandAlone", MODE_PRIVATE);
                        sharedPreferences.edit().putString("firebase_token",token).apply();
                    }
                });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        int type = getSharedPreferences("login_info", MODE_PRIVATE).getInt("usertype", -1);

        Map<String, String> data = remoteMessage.getData();
        Log.d("data.......", String.valueOf(data));
        String body = data.get("body");
        String title = data.get("title");

        Intent intent;
        if (title.contains("Order")) {
            intent = new Intent(getApplicationContext(), MyOrdersListActivity.class);
        } else
            intent = new Intent(getApplicationContext(), MyOrdersListActivity.class);

        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 101, intent, 0);

        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri sound = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.push_notification) ;
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            channel = new NotificationChannel("222", "my_channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(sound,att);
            nm.createNotificationChannel(channel);

//            Uri sound = Uri. parse (ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.push_notification);
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(),
//                    default_notification_channel_id )
//                    .setSmallIcon(R.drawable. ic_launcher_foreground )
//                    .setContentTitle( "Test" )
//                    .setSound(sound)
//                    .setContentText( "Hello! This is my first push notification" );
//            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE ) ;
//            if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
//                AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                        .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
//                        .setUsage(AudioAttributes.USAGE_ALARM)
//                        .build() ;
//                int importance = NotificationManager. IMPORTANCE_HIGH ;
//                NotificationChannel notificationChannel = new
//                        NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
//                notificationChannel.enableLights( true ) ;
//                notificationChannel.setLightColor(Color. RED ) ;
//             //   notificationChannel.enableVibration( true ) ;
//               // notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;
//                notificationChannel.setSound(sound , audioAttributes) ;
//                mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
//                assert mNotificationManager != null;
//                mNotificationManager.createNotificationChannel(notificationChannel) ;
//            }
//            assert mNotificationManager != null;
//            mNotificationManager.notify(( int ) System. currentTimeMillis (), mBuilder.build()) ;
        }
//
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(
                            getApplicationContext(), "222")
                            .setContentTitle(title)
                            .setAutoCancel(true)
//                        .setLargeIcon(((BitmapDrawable) getDrawable(R.mipmap.ic_launcher)).getBitmap())
                            .setSmallIcon(R.mipmap.qfix_logo)
                            .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.push_notification))
                            .setContentText(body)
                            .setSmallIcon(R.mipmap.qfix_logo)
                            .setContentIntent(pi);

            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            nm.notify(m, builder.build());
        }


}

