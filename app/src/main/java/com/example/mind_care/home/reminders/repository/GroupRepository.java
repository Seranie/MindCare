package com.example.mind_care.home.reminders.repository;

import android.net.Uri;
import android.util.Log;

import com.example.mind_care.home.reminders.model.RemindersGroupItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupRepository {
    private final FirebaseFirestore db;
    private final String collection = "users";
    private final FirebaseUser user;
    private List<RemindersGroupItem> groupList;

    public GroupRepository() {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void addNewGroup(RemindersGroupItem groupItem) {
        db.collection(collection).document(user.getUid()).collection("groups").add(groupItem);
    }

    public void deleteGroup(RemindersGroupItem groupItem) {
        db.collection(collection).document(user.getUid()).update("groups", FieldValue.arrayRemove(groupItem));
    }

    public void retrieveGroupList(OnCompleteCallback callback) {
        db.collection(collection).document(user.getUid()).collection("groups").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> docs = task.getResult().getDocuments();
                if (!docs.isEmpty()) {
                    List<HashMap<String, Object>> groups = (List<HashMap<String, Object>>) docs.get();
                    List<RemindersGroupItem> tempList = new ArrayList<>();
                    for (HashMap<String, Object> map : groups) {
                        Uri uri = Uri.parse(String.valueOf(map.get("imageSource")));
                        String name = String.valueOf(map.get("name"));
                        RemindersGroupItem groupItem = new RemindersGroupItem(uri, name);
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

    public void addReminderToGroup(int position){
        db.collection(collection).document(user.getUid()).update("groups", );
    }

    public interface OnCompleteCallback {
        void onComplete(List<RemindersGroupItem> groupList);
    }

}
