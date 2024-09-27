package com.example.mind_care.home.fences.worker;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mind_care.R;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class LocationCheckWorker extends Worker {
    private GeoPoint currentLocation;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static final double EARTH_RADIUS = 6371000;

    public LocationCheckWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        return checkPatientLocation();
    }

    public Result checkPatientLocation(){
        CompletableFuture<Result> completableFuture = new CompletableFuture<>();
        db.collection("users").document(user.getUid()).collection("linked_patient").document("patient").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot snapshot = task.getResult();
                if(snapshot.exists()){
                    String patientId = snapshot.getString("id");
                    db.collection("users").document(patientId).collection("locations").document("location").get().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            DocumentSnapshot patientLocation = task1.getResult();
                            if(patientLocation.exists()){
                                currentLocation = patientLocation.getGeoPoint("location");
                                checkGeoFence(currentLocation);
                                completableFuture.complete(Result.success());
                            } else {completableFuture.complete(Result.failure(new Data.Builder().putString("message", "Patient has no current location").build()));}
                        }else {completableFuture.complete(Result.failure());}
                    });
                }
                else{
                    //No patient linked
                    Data data = new Data.Builder().putString("message", "No patient linked").build();
                    completableFuture.complete(Result.failure(data));
                }
            }
            else{
                completableFuture.complete(Result.failure());
            }
        });
        try{
            return completableFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            return Result.failure();
        }
    }

    public void checkGeoFence(GeoPoint currentLocation){
        db.collection("users").document(user.getUid()).collection("fences").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (DocumentSnapshot document : task.getResult()){
                    GeoPoint fenceLocation = document.getGeoPoint("location");
                    Double radius = document.getDouble("radius");
                    if (fenceLocation != null) {
                        Log.i("INFO", calculateDistance(currentLocation, fenceLocation) + "with radius: " + radius);
                    }
                    if (radius != null && fenceLocation != null){
                        if (calculateDistance(currentLocation, fenceLocation) >= radius){
                            String fenceName = document.getString("fenceName");
                            if(fenceName != null){
                                sendNotification(fenceName);
                            }
                        }
                    }
                }
            }
        });
    }

    private double calculateDistance(GeoPoint currentLocation, GeoPoint fenceLocation){
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

    public void sendNotification(String fenceName){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Fence_Alerts")
                .setSmallIcon(R.mipmap.ic_launcher_official)
                .setContentTitle("Geofence Alert")
                .setContentText("Patient has left area" + fenceName)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("Fence_Alerts", "Geofence Alerts", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(fenceName.hashCode(), builder.build());
    }
}
