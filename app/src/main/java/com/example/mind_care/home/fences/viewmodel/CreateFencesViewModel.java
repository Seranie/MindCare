package com.example.mind_care.home.fences.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mind_care.home.fences.FenceObjectModel;
import com.example.mind_care.home.fences.repository.CreateFenceRepository;

public class CreateFencesViewModel extends ViewModel {
    private final MutableLiveData<FenceObjectModel> mFenceLiveData = new MutableLiveData<>();
    private CreateFenceRepository createFenceRepository = new CreateFenceRepository();

    public LiveData<FenceObjectModel> getFenceLiveData() {
        return mFenceLiveData;
    }

    public void setFence(FenceObjectModel fence) {
        new Thread(()-> createFenceRepository.createFence(fence)).start();
    }
}
