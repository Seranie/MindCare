package com.example.mind_care.home.reminders.repository;

import com.example.mind_care.home.reminders.model.ReminderItemModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;

public class ReminderItemRepository {
    private final FirebaseFirestore db;
    private final String collection = "users";
    private final FirebaseUser user;
    private final CollectionReference colRef;

    public ReminderItemRepository() {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        colRef = db.collection(collection).document(user.getUid()).collection("groups");
    }


    public void addReminderItemToDatabase(ReminderItemModel reminderItem){
        //query into the reminderItem's belonging group;
        CollectionReference remindersColRef = colRef.document(reminderItem.getGroupId()).collection("reminders");

        HashMap<String, Object> map = new HashMap<>();
        map.put("title", reminderItem.getTitle());
        map.put("note", reminderItem.getNote());

        Timestamp timestamp = new Timestamp(Date.from(reminderItem.getSchedule().atZone(ZoneId.systemDefault()).toInstant()));
        map.put("schedule", timestamp);

        remindersColRef.add(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentReference reminderItemDocRef = task.getResult();
                reminderItemDocRef.update("reminderId", reminderItemDocRef.getId());
                for (LocalDateTime dateTime :reminderItem.getReminderAlertItemList()){
                    Timestamp alertItemTimestamp = new Timestamp(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
                    HashMap<String, Timestamp> alertItemMap = new HashMap<>();
                    alertItemMap.put("dateTime", alertItemTimestamp);
                    reminderItemDocRef.collection("alertItems").add(alertItemMap);
                }
            }
        });
    }

//    public void getReminderItem
}
