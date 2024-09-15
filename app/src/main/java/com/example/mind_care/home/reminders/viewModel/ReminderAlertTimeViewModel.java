package com.example.mind_care.home.reminders.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mind_care.home.reminders.model.TimeModel;

import java.util.ArrayList;

public class ReminderAlertTimeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TimeModel>> mutableTimeArray = new MutableLiveData<>();

    public MutableLiveData<ArrayList<TimeModel>> getMutableTimeArray() {
        return mutableTimeArray;
    }

    public void setMutableTimeArray(TimeModel item){
        ArrayList<TimeModel> array = mutableTimeArray.getValue();
        array.add(item);
        mutableTimeArray.setValue(array);
    }
}
