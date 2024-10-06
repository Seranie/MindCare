package com.example.mind_care.home.fences.worker;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mind_care.R;
import com.example.mind_care.home.fences.repository.UpdateLocationRepository;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Deprecated
public class ShareLocationWorker extends Worker {
    private final int LOCATION_REQUEST_INTERVAL = 15000; // 15 seconds

    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;
    private final UpdateLocationRepository updateLocationRepository;

    private static final String CHANNEL_ID = "location_channel";
    private static final int NOTIFICATION_ID = 1;

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            for (Location location : locationResult.getLocations()) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                updateLocationInDatabase(latitude, longitude);
            }
        }
    };

    public ShareLocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        updateLocationRepository = new UpdateLocationRepository();
        createNotificationChannel();
    }

    @NonNull
    @Override
    public Result doWork() {
        setForegroundAsync(createForegroundInfo());

        CompletableFuture<Result> completableFuture = new CompletableFuture<>();
        Task<Void> task = getLocationAndUpdateDatabase();

        if(task != null){
            task.addOnSuccessListener(aVoid -> completableFuture.complete(Result.success())).addOnFailureListener(e -> completableFuture.complete(Result.failure()));
            try{
                return completableFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                return Result.failure();
            }
        }
        return Result.failure();
    }

    private ForegroundInfo createForegroundInfo(){
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Location Sharing")
                .setContentText("Sharing your location in the background")
                .setSmallIcon(R.drawable.baseline_location_on_24)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        // Return a ForegroundInfo object
        return new ForegroundInfo(NOTIFICATION_ID, notification);
    }

    private Task<Void> getLocationAndUpdateDatabase() {
        //check location accuracy permission
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show();
            return null;
        }
        return fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper());
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Location Sharing",
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    private LocationRequest createLocationRequest() {
        return new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_REQUEST_INTERVAL).build();
    }

    private void updateLocationInDatabase(double latitude, double longitude) {
        new Thread(() -> updateLocationRepository.updateLocation(latitude, longitude)).start();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onStopped() {
        super.onStopped();
        stopLocationUpdates();
    }
}
