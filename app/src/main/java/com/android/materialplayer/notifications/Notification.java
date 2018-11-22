package com.android.materialplayer.notifications;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.android.materialplayer.R;
import com.android.materialplayer.Settings;
import com.android.materialplayer.activities.MainActivity;
import com.android.materialplayer.models.ExtendedSong;

/**
 * Created by dl1998 on 03.02.18.
 */

public class Notification {

    private Context context;
    private String chanelId;
    private String chanelName;
    private ExtendedSong song;
    private NotificationManager notificationManager;

    public Notification(Context context, String chanelId) {
        this.context = context;
        this.chanelId = chanelId;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChanel(NotificationManager notificationManager) {
        NotificationChannel notificationChannel = new NotificationChannel(chanelId, chanelName, NotificationManager.IMPORTANCE_HIGH);

        notificationChannel.setDescription("Chanel Description");
        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(false);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private android.app.Notification setNotification() {

        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews remoteViews = getRemoteViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChanel(this.notificationManager);
        }

        android.app.Notification notification = new NotificationCompat.Builder(context, chanelId)
                .setCategory(android.app.Notification.CATEGORY_SERVICE)
                .setCustomContentView(remoteViews)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true)
                .setVisibility(android.app.Notification.VISIBILITY_PUBLIC)
                .setOngoing(false)
                .setSmallIcon(R.mipmap.ic_stat_onesignal_default)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setColorized(true)
                .setColor(Color.argb(255, 86, 58, 58))
                .build();

        //notificationManager.notify(Settings.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

        return notification;
    }

    private RemoteViews getRemoteViews() {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
        remoteViews.setImageViewBitmap(R.id.notificationCover, BitmapFactory.decodeFile(song.getArtPath()));
        remoteViews.setTextViewText(R.id.notificationTitle, song.getSongName());
        remoteViews.setTextViewText(R.id.notificationArtistAlbum, song.getArtistName() + " - " + song.getAlbumName());

        return remoteViews;
    }

    private PendingIntent getPendingIntent() {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setAction(Settings.Action.MAIN_ACTION);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    public android.app.Notification create() {
        return setNotification();
    }

    public void setChanelName(String chanelName) {
        this.chanelName = chanelName;
    }

    public void setSong(ExtendedSong song) {
        this.song = song;
    }

}
