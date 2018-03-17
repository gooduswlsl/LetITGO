package com.sook.cs.letitgo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sook.cs.letitgo.shared.IndexActivity;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = this.getClass().getSimpleName();
    private String from, contents;
    Intent notificationIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived() 호출됨.");

        from = remoteMessage.getFrom();
        Map<String, String> data = remoteMessage.getData();
        contents = data.get("contents");
        Log.d(TAG, "from : " + from + ", contents : " + contents);

        notificationIntent = new Intent(this, IndexActivity.class);

        if (contents.equals("주문이 도착하였습니다.")) { //customer가 보내는 메시지
            goToSeller_order();
        }
        else { //seller가 보내는 메세지
            goToCustomer_my();
        }
    }

    /*
    * 푸시메시지를 클릭했을 때 보여주는 화면을 다르게하기 위해 메소드를 나누어 구현하였다.
    * 두 메소드 모두 intent에 각자의 값을 넣어 IndexActivity.class에 보낸다
    * customer가 보내는 메세지 -> IndexActivity -> seller_main -> seller_order 순
    * seller가 보내는 메세지 -> IndexActivity -> customer_main -> customer_my 순
    * */

    private void goToSeller_order(){
        notificationIntent.putExtra("particularFragment", "goToSeller_order");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("테이크잇")
                .setContentIntent(contentIntent)
                .setContentText(contents)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1, 1000});

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager notificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mBuilder.build());
    }

    private void goToCustomer_my(){
        notificationIntent.putExtra("particularFragment", "goToCustomer_my");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Take EAT")
                .setContentIntent(contentIntent)
                .setContentText(contents)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1, 1000});

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager notificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mBuilder.build());
    }
}
