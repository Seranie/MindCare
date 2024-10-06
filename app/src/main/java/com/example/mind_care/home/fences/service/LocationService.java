package com.example.mind_care.home.fences.service;

import static androidx.core.location.LocationManagerCompat.requestLocationUpdates;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.mind_care.R;
import com.example.mind_care.home.fences.repository.UpdateLocationRepository;
import com.example.mind_care.notification.LocationShareReceiver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationService extends Service {
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private static final int LOCATION_NOTIFICATION_ID = 10;
    private UpdateLocationRepository updateLocationRepository;


    @Override
    public void onCreate() {
        super.onCreate();

        updateLocationRepository = new UpdateLocationRepository();

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Define the location callback to handle location updates
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        // Here you can update the location to your server/database TODO
                        updateLocationInDatabase(latitude, longitude);
                    }
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start the foreground service with a notification
        Notification notification = createNotification();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        startForeground(LOCATION_NOTIFICATION_ID, notification);

        // Request location updates
        requestLocationUpdates();

        return START_STICKY; // Ensures the service keeps running unless explicitly stopped
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createNotification() {
        String channelId = "location_channel";

        NotificationChannel channel = new NotificationChannel(
                channelId,
                "Location Service",
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }

        Intent dismissIntent = new Intent(this, LocationShareReceiver.class);
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                dismissIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );


        return new NotificationCompat.Builder(this, channelId)
                .setOngoing(true)
                .setContentTitle("Location Service")
                .setContentText("Tracking your location in the background")
                .setSmallIcon(R.drawable.baseline_location_on_24)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDeleteIntent(dismissPendingIntent)
                .build();
    }


    // Request location updates
    private void requestLocationUpdates() {
        com.google.android.gms.location.LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)  // 10 seconds
                .setFastestInterval(5000)  // 5 seconds
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; // Handle missing location permissions (ask user if necessary)
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private void updateLocationInDatabase(double latitude, double longitude){
        new Thread(() -> updateLocationRepository.updateLocation(latitude, longitude)).start();
    }
}
