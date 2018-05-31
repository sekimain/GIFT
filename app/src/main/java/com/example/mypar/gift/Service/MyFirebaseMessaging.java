package com.example.mypar.gift.Service;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.mypar.gift.Activity.Splash_Activity;
import com.example.mypar.gift.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seki on 2018-03-29.
 */

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification());
    }

    private void showNotification(RemoteMessage.Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            Intent i = new Intent(this, Splash_Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            String channelId = "channel1";
            CharSequence channelName = "22segi_channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);

            String groupId = "22segi";
            CharSequence groupName = "22segiro";

            notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(groupId, groupName));

            List<NotificationChannelGroup> notificationChannelGroups = new ArrayList();
            notificationChannelGroups.add(new NotificationChannelGroup(groupId, groupName));

            notificationManager.createNotificationChannelGroup(notificationChannelGroups.get(0));


            int notifyId = 1;


            android.app.Notification notification2 = new android.app.Notification.Builder(this)
                    .setColor(Color.WHITE)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                    .setSmallIcon(R.mipmap.ic_launcher_round)

                    .setContentTitle(notification.getBody())
                    .setContentText(notification.getTitle())
                    .setChannelId(channelId)
                    .setContentIntent(pendingIntent)
                    .build();

            notificationManager.notify(notifyId, notification2);

        } else {
            Intent intent = new Intent(this, Splash_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setColor(Color.WHITE)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLights(Color.BLACK,1000,3000)
                    .setContentTitle(notification.getTitle())
                    .setContentText(notification.getBody())
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);


            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
        }


    }
}