package com.xjysx.xiaochetong.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.xjysx.xiaochetong.R;


/**
 * 通知
 * Created by dan on 16/9/4.
 */
public class NotifyUtil {

    public static void notify(Context context, PendingIntent pendingIntent, String title, CharSequence contentText){

        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);

        Notification notification=builder.setContentTitle(title)
                .setContentText(contentText).setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(1, notification);
    }
}
