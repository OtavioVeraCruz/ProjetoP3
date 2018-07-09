package com.example.otavio.newshowup.notificacao;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.example.otavio.newshowup.R;

public class NotificationReceiver {

    public static void createNotificationMessage(Context context, String title, String text){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_profile_image);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int num = (int) System.currentTimeMillis();
        assert mNotificationManager != null;
        mNotificationManager.notify(num, mBuilder.build());
    }

}
