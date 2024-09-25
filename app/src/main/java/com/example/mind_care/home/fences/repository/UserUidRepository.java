package com.example.mind_care.home.fences.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserUidRepository {
    private final FirebaseFirestore db;
    private final FirebaseUser user;

    public UserUidRepository() {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void fetchUserUid() {
        //TODO
    }

    public void validateUid(String Uid, validated callback){
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    if (document.getId().equals(Uid)) {
                        callback.isValid(true);
                    }
                }
            }
        });
        callback.isValid(false);
    }

    public void setLinkedAccount(String uid){
        HashMap<String, String> map = new HashMap<>();
        map.put("id", uid);
        db.collection("users").document(user.getUid()).collection("linked_patient").document("patient").set(map);
    }

    public interface validated{
        void isValid(boolean isValid);
    }
}
