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
        db.collection(collection).document(user.getUid()).collection("groups").add(groupItem).addOnCompleteListener(document -> {
            if (document.isSuccessful()) {
                Log.i("INFO", "Group added");
            } else {
                Log.i("INFO", String.valueOf(document.getException()));
            }
        });
    }

    public void deleteGroup(RemindersGroupItem groupItem) {
        db.collection(collection).document(user.getUid()).update("groups", FieldValue.arrayRemove(groupItem));
    }

    public void retrieveGroupList(OnCompleteCallback callback) {
        db.collection(collection).document(user.getUid()).collection("groups").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> docs = task.getResult().getDocuments();

                if (!docs.isEmpty()) {
                    List<RemindersGroupItem> tempList = new ArrayList<>();
                    for (DocumentSnapshot shot : docs) {
                        //Query all group under groups, and create new GroupItem and return.
                        //TODO change groupitem to have a list of reminders under them.
                        Uri uri = Uri.parse(String.valueOf(shot.get("imageSource")));
                        String name = String.valueOf(shot.get("name"));
                        RemindersGroupItem groupItem = new RemindersGroupItem(uri, name);
                        groupItem.setGroupId(shot.getId());
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

//    public void addReminderToGroup(int position){
//        db.collection(collection).document(user.getUid()).update("groups", );
//    }

    public interface OnCompleteCallback {
        void onComplete(List<RemindersGroupItem> groupList);
    }

}
