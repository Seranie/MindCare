package com.example.mind_care;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShowcaseChangeViewModel extends ViewModel {
    private MutableLiveData<Integer> mShowcaseChangeId = new MutableLiveData<>();

    public void setShowcaseChangeId(int id){
        mShowcaseChangeId.setValue(id);
    }

    public MutableLiveData<Integer> getShowcaseChangeId(){return mShowcaseChangeId;}

}
