package com.example.mind_care;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String reminderTitle = intent.getStringExtra("reminderTitle");
        String reminderNote = intent.getStringExtra("reminderNote");
        String reminderId = intent.getStringExtra("reminderId");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "reminder_channel";

        NotificationChannel channel = new NotificationChannel(channelId, "Reminder Notifications", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId).setSmallIcon(R.mipmap.ic_launcher_official_round)
                .setContentTitle(reminderTitle + " is due now!").setContentText(reminderNote).setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true);

        // Show the notification
        notificationManager.notify(reminderId.hashCode(), notificationBuilder.build());
    }
}
