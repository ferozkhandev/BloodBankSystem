package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.app.NotificationManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMsgService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.enough);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Blood Donation Request")
                .setSound(sound)
                .setContentText("Hey! I need blood here. Will you help me?")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        int mNotificationID = (int) System.currentTimeMillis();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(mNotificationID,mBuilder.build());
    }
}
