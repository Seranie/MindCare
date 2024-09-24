package com.example.mind_care.home.fences.worker;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

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
        checkPatientLocation();
        return Result.success();
    }

    public void checkPatientLocation(){
        db.collection("users").document(user.getUid()).collection("linked_patient").document("patient").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String patientId = task.getResult().getString("id");
                db.collection("users").document(patientId).collection("locations").document("location").get().addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        currentLocation = task1.getResult().getGeoPoint("location");
                        checkGeoFence(currentLocation);
                    }
                });
            }
        });
    }

    public void checkGeoFence(GeoPoint currentLocation){
        db.collection("users").document(user.getUid()).collection("fences").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (DocumentSnapshot document : task.getResult()){
                    GeoPoint fenceLocation = document.getGeoPoint("location");
                    int radius = document.getLong("radius").intValue();
                    if (currentLocation.getLatitude() - fenceLocation.getLatitude() >= radius || currentLocation.getLongitude() - fenceLocation.getLongitude() >= radius){
                        sendNotification(document.getString("name"));
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
