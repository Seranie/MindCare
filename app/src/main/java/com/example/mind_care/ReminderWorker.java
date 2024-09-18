package com.example.mind_care;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ReminderWorker extends Worker {
    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Timestamp now = Timestamp.now();

        // Access Firestore and check for any reminders matching the current time
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("users").document(user.getUid()).collection("groups").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot groupDoc : task.getResult()) {
                    String groupID = groupDoc.getId();

                    db.collection("users").document(user.getUid()).collection("groups").document(groupID).collection("reminders").whereLessThanOrEqualTo("schedule", now).get().addOnCompleteListener(reminderTask -> {
                        if (reminderTask.isSuccessful()) {
                            for (DocumentSnapshot reminderDoc : reminderTask.getResult()) {
                                triggerNotification(reminderDoc.getString("title"), reminderDoc.getId());
                            }
                        }
                    });
                }
            }
        });
        return Result.success();
    }

    private void triggerNotification(String reminderTitle, String reminderId) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String channelID = "reminder_channel";
        Log.i("INFO", String.valueOf(notificationManager.areNotificationsEnabled()));
        Log.i("INFO", reminderTitle);

        NotificationChannel channel = new NotificationChannel(
                channelID, "Reminder Notifications",
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_image_icon) //TODO chng later
                .setContentTitle("Mind Care")
                .setContentText(reminderTitle + " is due now!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(1, notificationBuilder.build());
    }
}
