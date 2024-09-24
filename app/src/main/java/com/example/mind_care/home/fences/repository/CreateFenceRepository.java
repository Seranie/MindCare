package com.example.mind_care.home.fences.repository;

import com.example.mind_care.home.fences.FenceObjectModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateFenceRepository {
    private FirebaseFirestore db;
    private FirebaseUser user;

    public CreateFenceRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void createFence(FenceObjectModel fence) {
        db.collection("users").document(user.getUid()).collection("fences").add(fence);
    }
}
