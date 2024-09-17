package com.example.mind_care.home.reminders.repository;

import com.example.mind_care.home.reminders.model.ReminderItemModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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


    public void addReminderItem(ReminderItemModel reminderItem){
        //query into the reminderItem's belonging group
        DocumentReference docRef = colRef.document(reminderItem.getGroupId());
        docRef.update("reminders", FieldValue.arrayUnion(reminderItem));
    }

    public void getReminderItem
}
