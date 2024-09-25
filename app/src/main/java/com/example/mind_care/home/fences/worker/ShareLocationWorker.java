package com.example.mind_care.home.fences.worker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mind_care.home.fences.repository.UpdateLocationRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class ShareLocationWorker extends Worker {
    private final int LOCATION_REQUEST_INTERVAL = 15000; // 15 seconds

    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;
    private final UpdateLocationRepository updateLocationRepository;

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult == null) {
            } else {
                for (Location location : locationResult.getLocations()) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    updateLocationInDatabase(latitude, longitude);
                }
            }
        }
    };

    public ShareLocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        updateLocationRepository = new UpdateLocationRepository();
    }

    @NonNull
    @Override
    public Result doWork() {
        getLocationAndUpdateDatabase();
        return null;
    }

    private void getLocationAndUpdateDatabase() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper());
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

    public void stopWorker() {
        super.onStopped();
        stopLocationUpdates();
    }

}
