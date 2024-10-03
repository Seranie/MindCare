package com.example.mind_care.home.fences.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mind_care.home.fences.FenceObjectModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FencesRepository {
    private LiveData<List<FenceObjectModel>> fencesLiveData = new MutableLiveData<>(new ArrayList<>());
    private FirebaseFirestore db;
    private FirebaseUser user;

    public FencesRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        getFences();
    }

    public LiveData<List<FenceObjectModel>> getAllFences() {
        return fencesLiveData;
    }

    public void getFences() {
        new Thread(()-> {
            db.collection("users").document(user.getUid()).collection("fences").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<FenceObjectModel> fenceList = new ArrayList<>();
                    for(DocumentSnapshot fenceDoc: task.getResult()){
                        FenceObjectModel fence = fenceDoc.toObject(FenceObjectModel.class);
                        fenceList.add(fence);
                    }
                    ((MutableLiveData<List<FenceObjectModel>>) fencesLiveData).postValue(fenceList);
                }
            });
        }).start();
    }

}
