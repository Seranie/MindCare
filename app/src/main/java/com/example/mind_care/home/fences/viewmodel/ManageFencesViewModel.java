package com.example.mind_care.home.fences.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mind_care.home.fences.FenceObjectModel;
import com.example.mind_care.home.fences.repository.ManageFenceRepository;

import java.util.ArrayList;

public class ManageFencesViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<FenceObjectModel>> manageFenceLiveData = new MutableLiveData<>(new ArrayList<>());
    private final ManageFenceRepository manageFenceRepository = new ManageFenceRepository();

    public LiveData<ArrayList<FenceObjectModel>> getManageFenceLiveData(){
        return manageFenceLiveData;
    }

    public void deleteFence(FenceObjectModel fenceObjectModel){
        new Thread(() -> manageFenceRepository.deleteFence(fenceObjectModel).thenAccept((succeed)->getAllFences())).start();
    }

    public void getAllFences(){
        new Thread(() -> {
            manageFenceRepository.getAllFences().thenAccept(manageFenceLiveData::postValue);
        }).start();
    }

}
