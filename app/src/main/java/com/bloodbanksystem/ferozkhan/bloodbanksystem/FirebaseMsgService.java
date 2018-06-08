package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMsgService extends FirebaseMessagingService {
    private String longitude,latitude;
    private boolean longi = false;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        String messageTitle = remoteMessage.getNotification().getTitle();
//        String messageBody = remoteMessage.getNotification().getBody();
        longitude = "";
        latitude = "";
        String messageTitle = remoteMessage.getData().get("title");
        for(int i = 0; i<messageTitle.length(); i++)
        {
            if(messageTitle.charAt(i) == ',')
            {
                longi = true;
            }
            if(!longi && messageTitle.charAt(i) != ',')
            {
                latitude = latitude+messageTitle.charAt(i);
            }
            if(longi && messageTitle.charAt(i) != ',')
            {
                longitude = longitude+messageTitle.charAt(i);
            }
        }
        String messageBody = remoteMessage.getData().get("body");
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("longitude",longitude);
        intent.putExtra("latitude",latitude);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.enough);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Blood Donation Request")
                .setContentIntent(pendingIntent)
                .setSound(sound)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        int mNotificationID = (int) System.currentTimeMillis();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(mNotificationID,mBuilder.build());
    }
}
