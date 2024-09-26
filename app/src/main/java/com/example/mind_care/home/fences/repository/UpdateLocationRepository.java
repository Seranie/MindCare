package com.example.mind_care.home.fences.repository;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class UpdateLocationRepository {
    private FirebaseFirestore db;
    private FirebaseUser user;

    public UpdateLocationRepository(){
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void updateLocation(double latitude, double longitude){
        GeoPoint location = new GeoPoint(latitude, longitude);
        HashMap<String, GeoPoint> map = new HashMap<>();
        map.put("location", location);
        db.collection("users").document(user.getUid()).collection("locations").document("location").set(map, SetOptions.merge());
    }
}
