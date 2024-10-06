package com.example.mind_care.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.mind_care.home.fences.service.CheckLocationService;
import com.example.mind_care.home.fences.service.LocationService;

public class CheckLocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, CheckLocationService.class);
        context.stopService(serviceIntent);
    }
}
