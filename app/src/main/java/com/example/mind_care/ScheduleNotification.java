package com.example.mind_care;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.Timestamp;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class ScheduleNotification {
    public static void scheduleNotification(Context context, Timestamp reminderDateTime, String reminderTitle, String reminderNote, String reminderId){
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("reminderTitle", reminderTitle);
        intent.putExtra("reminderNote", reminderNote);
        intent.putExtra("reminderId", reminderId);

//        Log.i("INFO", String.valueOf(LocalDateTime.ofInstant(reminderDateTime.toDate().toInstant(), ZoneId.systemDefault())));

        // Create a PendingIntent for the broadcast
        int requestCode = reminderId.hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the AlarmManager system service
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Schedule the alarm to trigger at the reminder's timestamp
        if (alarmManager != null) {
            long reminderTimestamp = reminderDateTime.toDate().getTime();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimestamp, pendingIntent);
            }
            else{
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimestamp, pendingIntent);
            }
        }
    }

    public static void scheduleNotification(Context context, Timestamp reminderDateTime, String reminderTitle, String reminderNote, String reminderId, String alertItemId){
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("reminderTitle", reminderTitle);
        intent.putExtra("reminderNote", reminderNote);
        intent.putExtra("reminderId", reminderId);

        // Create a PendingIntent for the broadcast
        int requestCode = alertItemId.hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the AlarmManager system service
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Schedule the alarm to trigger at the reminder's timestamp
        if (alarmManager != null) {
            long reminderTimestamp = reminderDateTime.toDate().getTime();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimestamp, pendingIntent);
            }
            else{
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimestamp, pendingIntent);
            }
        }
    }

    public static void cancelNotification(Context context, String reminderId, List<String> alertItemIds){
        Intent intent = new Intent(context, NotificationReceiver.class);
        int requestCode = reminderId.hashCode();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        for (String alertItemId : alertItemIds){
            pendingIntent = PendingIntent.getBroadcast(
                    context, alertItemId.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
    }
}
