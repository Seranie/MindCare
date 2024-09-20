package com.example.mind_care.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.Timestamp;

import java.util.List;

public class ScheduleNotification {
    public static void scheduleNotification(Context context, Timestamp reminderDateTime, String reminderTitle, String reminderNote, String reminderId){
        //To make sure that reminder date time has not already passed
        if(Timestamp.now().compareTo(reminderDateTime) <= 0){
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("reminderTitle", reminderTitle);
            intent.putExtra("reminderNote", reminderNote);
            intent.putExtra("reminderId", reminderId);


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
                    Log.i("INFO", reminderDateTime.toDate() + "alarm set |" + Timestamp.now().toDate());
                }
                else{
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimestamp, pendingIntent);
                    Log.i("INFO", reminderDateTime.toDate() + "alarm set |" + Timestamp.now().toDate());
                }
            }
        }
    }

    public static void scheduleNotification(Context context, Timestamp reminderDateTime, String reminderTitle, String reminderNote, String reminderId, String alertItemId){
        if (Timestamp.now().compareTo(reminderDateTime) <= 0){
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
                    Log.i("INFO", reminderDateTime.toDate() + "alarm set |" + Timestamp.now().toDate());
                }
                else{
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimestamp, pendingIntent);
                    Log.i("INFO", reminderDateTime.toDate() + "alarm set |" + Timestamp.now().toDate());
                }
            }
        }
    }

    public static void cancelNotification(Context context, String reminderId, List<String> alertItemIds){
        Intent intent = new Intent(context, NotificationReceiver.class);
        int requestCode = reminderId.hashCode();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Log.i("INFO","reminder id = " + reminderId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        for (String alertItemId : alertItemIds){
            Log.i("INFO","Alert item id = " + alertItemId);
            pendingIntent = PendingIntent.getBroadcast(
                    context, alertItemId.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
    }
}
