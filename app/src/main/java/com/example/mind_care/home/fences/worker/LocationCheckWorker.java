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
                    Log.i("INFO", patientId);
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
        Log.i("INFO", String.valueOf(currentLocation));
        db.collection("users").document(user.getUid()).collection("fences").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (DocumentSnapshot document : task.getResult()){
                    GeoPoint fenceLocation = document.getGeoPoint("location");
                    Double radius = document.getDouble("radius");
                    if (radius != null && fenceLocation != null){
                        if (currentLocation.getLatitude() - fenceLocation.getLatitude() >= radius || currentLocation.getLongitude() - fenceLocation.getLongitude() >= radius){
                            sendNotification(document.getString("fenceName"));
                        }
                    }
                }
            }
        });
    }

    public void sendNotification(String fenceName){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Fence_Alerts")
                .setSmallIcon(R.mipmap.ic_launcher_official)
                .setContentTitle("Geofence Alert")
                .setContentText("Patient has left area" + fenceName)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Default", "Geofence Alerts", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(fenceName.hashCode(), builder.build());
    }
}
