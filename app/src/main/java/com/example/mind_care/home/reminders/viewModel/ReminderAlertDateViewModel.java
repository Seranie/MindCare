package com.example.mind_care.home.reminders.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mind_care.home.reminders.model.DateModel;

import java.util.ArrayList;

public class ReminderAlertDateViewModel extends ViewModel {
    private MutableLiveData<ArrayList<DateModel>> mutableDateArray = new MutableLiveData<>();

    public MutableLiveData<ArrayList<DateModel>> getMutableDateArray() {
        return mutableDateArray;
    }

    public void addDateToArray(int dayOfMonth, int month, int year){
        ArrayList<DateModel> array = mutableDateArray.getValue();
        array.add(new DateModel(dayOfMonth, month, year));
        mutableDateArray.setValue(array);
    }
}
