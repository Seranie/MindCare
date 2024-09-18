package com.example.mind_care.home.reminders.repository;

import android.net.Uri;
import android.util.Log;

import com.example.mind_care.home.reminders.model.ReminderItemModel;
import com.example.mind_care.home.reminders.model.RemindersGroupItem;
import com.example.mind_care.home.reminders.model.RemindersReminderItem;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GroupRepository {
    private final FirebaseFirestore db;
    private final String collection = "users";
    private final FirebaseUser user;
    List<RemindersGroupItem> groupList = new ArrayList<>();

    public GroupRepository() {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void addNewGroup(RemindersGroupItem groupItem) {
        db.collection(collection).document(user.getUid()).collection("groups").add(groupItem).addOnCompleteListener(document -> {
            if (document.isSuccessful()) {
                Log.i("INFO", "Group added");
                String groupId = document.getResult().getId();
                document.getResult().update("groupId", groupId);
            } else {
                Log.i("INFO", String.valueOf(document.getException()));
            }
        });
    }

//    public void deleteGroup(RemindersGroupItem groupItem) {
//        db.collection(collection).document(user.getUid()).update("groups", FieldValue.arrayRemove(groupItem));
//    }

    public void retrieveGroupList(OnCompleteCallback callback) {
        db.collection(collection).document(user.getUid()).collection("groups").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> docs = task.getResult().getDocuments();

                if (!docs.isEmpty()) {
                    List<RemindersGroupItem> tempList = new ArrayList<>();
                    for (DocumentSnapshot shot : docs) {
                        //Query all group under groups, and create new GroupItem. groups -> group
                        //TODO change groupitem to have a list of reminders under them.
                        Uri uri = Uri.parse(String.valueOf(shot.get("imageSource")));
                        String name = String.valueOf(shot.get("name"));
                        RemindersGroupItem groupItem = new RemindersGroupItem(uri, name);
                        groupItem.setGroupId(shot.getId());

                        //Further query group for reminders. group -> reminders
                        CollectionReference colRef = shot.getReference().collection("reminders");
                        List<ReminderItemModel> reminders = new ArrayList<>();
                        colRef.get().addOnCompleteListener(reminderTask -> {
                            if (reminderTask.isSuccessful()){
                                for (DocumentSnapshot reminderShot : reminderTask.getResult()) {
                                    String groupId = shot.getId();
                                    String title = (String) reminderShot.get("title");
                                    String note = (String) reminderShot.get("note");
                                    Timestamp timestamp = reminderShot.getTimestamp("schedule");
                                    LocalDateTime dateTime = LocalDateTime.now();
                                    if(timestamp != null){
                                        Date date = timestamp.toDate();
                                        dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                                    }


                                    //For each reminder, query for alert items reminder -> datetime
                                    CollectionReference alertRef = reminderShot.getReference().collection("alertItems");
                                    List<LocalDateTime> alertItemList = new ArrayList<>();
                                    alertRef.get().addOnCompleteListener(alertTask -> {
                                        if (alertTask.isSuccessful()){
                                            for (DocumentSnapshot alertShot : alertTask.getResult()) {
                                                Date alertDate = new Date();
                                                Timestamp alertTimestamp = alertShot.getTimestamp("dateTime");
                                                if (alertTimestamp != null){
                                                 alertDate= alertTimestamp.toDate();

                                                }
                                                LocalDateTime alertDateTime = LocalDateTime.ofInstant(alertDate.toInstant(), ZoneId.systemDefault());
                                                alertItemList.add(alertDateTime);
                                            }
                                        }
                                    });

                                    ReminderItemModel reminderItem = new ReminderItemModel(groupId, title, note, dateTime, alertItemList);
                                    reminders.add(reminderItem);
                                }
                            }
                        });

                        groupItem.setReminderList(reminders);
                        tempList.add(groupItem);
                    }
                    groupList = tempList;
                }
                callback.onComplete(groupList);
            } else {
                Log.i("INFO", String.valueOf(task.getException()));
            }
        });
    }

    public void retrieveRemindersFromGroup(String groupId, OnReminderCompleteCallback callback) {
        List<ReminderItemModel> tempList = new ArrayList<>();

        db.collection(collection).document(user.getUid()).collection("groups").document(groupId).collection("reminders").get().addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                for (DocumentSnapshot doc : task.getResult().getDocuments()){
                    //Get all alert items and make a list of it
                    List<LocalDateTime> alertItemList = new ArrayList<>();
                    doc.getReference().collection("alertItems").get().addOnCompleteListener(getAlertItemsTask -> {
                        for (DocumentSnapshot alertItemDoc : getAlertItemsTask.getResult().getDocuments()){
                            alertItemList.add(alertItemDoc.getTimestamp("dateTime").toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                        }
                    });
                    //Make new reminder item
                    Map<String, Object> map = doc.getData();
                    Timestamp timestamp = (Timestamp) map.get("schedule");
                    LocalDateTime dateTime = LocalDateTime.now();
                    Date date = timestamp.toDate();
                    dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

                    ReminderItemModel reminderItem = new ReminderItemModel(groupId, (String) map.get("title"), (String) map.get("note"), dateTime, alertItemList);
                    reminderItem.setReminderId(doc.getId());
                    tempList.add(reminderItem);
                }
                callback.onComplete(tempList);
            } else {
                Log.i("INFO", String.valueOf(task.getException()));
            }
        });
    }

    public void deleteReminder(String groupId, String reminderId){
        db.collection(collection).document(user.getUid()).collection("groups").document(groupId).collection("reminders").document(reminderId).delete();
    }


    public interface OnCompleteCallback {
        void onComplete(List<RemindersGroupItem> groupList);
    }

    public interface OnReminderCompleteCallback {
        void onComplete(List<ReminderItemModel> reminderList);
    }

}
