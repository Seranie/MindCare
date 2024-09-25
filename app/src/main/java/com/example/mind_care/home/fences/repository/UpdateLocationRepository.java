package com.example.mind_care.home.fences.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class UpdateLocationRepository {
    private FirebaseFirestore db;
    private FirebaseUser user;

    public UpdateLocationRepository(){
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void updateLocation(double latitude, double longitude){
        GeoPoint location = new GeoPoint(latitude, longitude);
        db.collection("users").document(user.getUid()).collection("locations").document("location").update("location", location);
    }
}
