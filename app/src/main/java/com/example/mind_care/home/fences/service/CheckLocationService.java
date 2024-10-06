package com.example.mind_care.home.fences.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.mind_care.R;
import com.example.mind_care.notification.CheckLocationReceiver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class CheckLocationService extends Service {
    private static final String CHANNEL_ID = "Fence_Alerts";
    private static final double EARTH_RADIUS = 6371000; // in meters
    private static final int LOCATION_UPDATE_INTERVAL = 60000;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Handler handler;
    private Runnable locationCheckRunnable;


    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler(Looper.getMainLooper());
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(11, createNotification());
        startLocationUpdates(); // Start requesting location updates
        return START_STICKY; // Keep the service running until explicitly stopped
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates(); // Stop requesting location updates
    }

    private void startLocationUpdates() {
        locationCheckRunnable = new Runnable() {
            @Override
            public void run() {
                checkPatientLocation();
                handler.postDelayed(this, LOCATION_UPDATE_INTERVAL);
            }
        };
        handler.post(locationCheckRunnable);
    }

    private void stopLocationUpdates() {
        if (locationCheckRunnable != null) {
            handler.removeCallbacks(locationCheckRunnable);
        }
    }

    private Notification createNotification() {
        // Create an intent to stop the service
        Intent stopIntent = new Intent(this, CheckLocationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 3, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Service")
                .setContentText("Tracking patient's location in the background")
                .setSmallIcon(R.drawable.baseline_location_searching_24) // Change as needed
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setDeleteIntent(pendingIntent)
                .build();
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Geofence Alerts",
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    private void checkPatientLocation() {
        db.collection("users").document(user.getUid()).collection("linked_patient").document("patient").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            String patientId = snapshot.getString("id");
                            db.collection("users").document(patientId).collection("locations").document("location").get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            DocumentSnapshot patientLocation = task1.getResult();
                                            if (patientLocation.exists()) {
                                                GeoPoint currentLocation = patientLocation.getGeoPoint("location");
                                                checkGeoFence(currentLocation);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void checkGeoFence(GeoPoint currentLocation) {
        Log.i("INFO", "CURRENT LOCATION: " + currentLocation);
        db.collection("users").document(user.getUid()).collection("fences").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    GeoPoint fenceLocation = document.getGeoPoint("location");
                    Double radius = document.getDouble("radius");
                    if (radius != null && fenceLocation != null) {
                        Log.i("INFO", "FENCE LOCATION: " + fenceLocation);
                        if (calculateDistance(currentLocation, fenceLocation) >= radius) {
                            String fenceName = document.getString("fenceName");
                            if (fenceName != null) {
                                sendNotification(fenceName);
                            }
                        }
                    }
                }
            }
        });
    }

    private double calculateDistance(GeoPoint currentLocation, GeoPoint fenceLocation) {
        double lat1 = currentLocation.getLatitude();
        double lon1 = currentLocation.getLongitude();
        double lat2 = fenceLocation.getLatitude();
        double lon2 = fenceLocation.getLongitude();

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // returns the distance in meters
    }

    private void sendNotification(String fenceName) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_official_round) // Change as needed
                .setContentTitle("Geofence Alert")
                .setContentText("Patient has left the area: " + fenceName)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(fenceName.hashCode(), builder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
