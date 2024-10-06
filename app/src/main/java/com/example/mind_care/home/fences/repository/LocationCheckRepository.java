package com.example.mind_care.home.fences.repository;

import androidx.work.Data;
import androidx.work.ListenableWorker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.concurrent.CompletableFuture;

public class LocationCheckRepository {
    private GeoPoint currentLocation;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public CompletableFuture<Boolean> checkIfLinkedToPatient() {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        // Fetch the linked patient document
        db.collection("users").document(user.getUid())
                .collection("linked_patient").document("patient").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Check if the linked patient document exists
                        boolean isLinked = task.getResult().exists();
                        completableFuture.complete(isLinked);
                    } else {
                        // Complete exceptionally if there was an error
                        completableFuture.completeExceptionally(new Exception("Failed to check linked patient"));
                    }
                });

        return completableFuture;
    }


}
