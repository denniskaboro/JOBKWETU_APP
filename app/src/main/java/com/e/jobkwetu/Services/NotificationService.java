package com.e.jobkwetu.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.e.jobkwetu.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {
    String TAG = "NotificationService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String id = "MY_CH_ID";
            CharSequence name = "Smart Home";
            //String desc = "Notifications regarding our products";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build();
            mChannel.setDescription(remoteMessage.getNotification().getBody());
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            //mChannel.setSound(sounduri, attributes);
            notificationManager.createNotificationChannel(mChannel);
        }
        Uri notimelody = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //Ringtone r = RingtoneManager.getRingtone(context,notimelody);
        //String desc = "Notifications regarding our products";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MY_CH_ID");
        builder.setContentTitle("Smart Home Notify");
        builder.setContentText(remoteMessage.getNotification().getBody());
        //builder.setContentIntent(operation);
        builder.setSmallIcon(R.drawable.ic_notifications_active_black_24dp);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setSound(notimelody);
        builder.setAutoCancel(true);
        // builder.addAction(R.drawable.ic_cancel_black_24dp,"CANCEL",operation3);
        // builder.addAction(R.drawable.ic_snooze_black_24dp,"SNOOZE",operation2);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setChannelId("MY_CH_ID");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: ");
    }
}
