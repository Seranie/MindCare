package com.example.mind_care.home.fences.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mind_care.home.fences.FenceObjectModel;
import com.example.mind_care.home.fences.repository.FencesRepository;

import java.util.List;

public class FencesViewModel extends ViewModel {
    private LiveData<List<FenceObjectModel>> fencesLiveData;
    private FencesRepository repo;

    public FencesViewModel() {
        repo = new FencesRepository();
        fencesLiveData = repo.getAllFences();
    }

    public LiveData<List<FenceObjectModel>> getFencesLiveData(){
        return fencesLiveData;
    }

    public void getAllFences(){
        repo.getFences();
    }


}
