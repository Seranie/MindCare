package com.example.mind_care.home.fences.repository;

import com.example.mind_care.home.fences.FenceObjectModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ManageFenceRepository {
    private FirebaseFirestore db;
    private FirebaseUser user;

    public ManageFenceRepository(){
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public CompletableFuture<Boolean> deleteFence(FenceObjectModel fenceObjectModel){
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        db.collection("users").document(user.getUid()).collection("fences").document(fenceObjectModel.getFenceId()).delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                completableFuture.complete(true);
            }
            else {
                completableFuture.completeExceptionally(task.getException());
            }
        });
            return completableFuture;
    }

    public CompletableFuture<ArrayList<FenceObjectModel>> getAllFences(){
        CompletableFuture<ArrayList<FenceObjectModel>> completableFuture = new CompletableFuture<>();
        db.collection("users").document(user.getUid()).collection("fences").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                ArrayList<FenceObjectModel> fenceList = new ArrayList<>();
                for(DocumentSnapshot document : task.getResult()){
                    FenceObjectModel object = document.toObject(FenceObjectModel.class);
                    object.setFenceId(document.getId());
                    fenceList.add(object);
                }
                completableFuture.complete(fenceList);
            }
            else{
                completableFuture.completeExceptionally(task.getException());
            }
        });
        return completableFuture;
    }


}
