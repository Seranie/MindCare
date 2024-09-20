package com.example.mind_care.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.example.mind_care.home.reminders.repository.GroupRepository;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Log.i("INFO", "BOOTED");
            new Thread(()->{
               GroupRepository groupRepo = new GroupRepository();
               groupRepo.getAllRemindersAndSetNotifications(context);
            }).start();
        }
        Log.i("INFO", "RECEIVED");
    }

}
