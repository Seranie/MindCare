package com.example.mind_care.home.reminders.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReminderAlertDateTimeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<LocalDateTime>> mutableDateTimeArray = new MutableLiveData<>();

    public ReminderAlertDateTimeViewModel() {
        mutableDateTimeArray.setValue(new ArrayList<>());
    }

    public LiveData<ArrayList<LocalDateTime>> getMutableDateTimeLiveData() {
        return mutableDateTimeArray;
    }

    public boolean replaceDateTimeObject(int position, LocalDateTime dateTime){
        ArrayList<LocalDateTime> arrayList = mutableDateTimeArray.getValue();
        LocalDateTime previousItem = arrayList.set(position, dateTime);
        mutableDateTimeArray.setValue(arrayList);
        return previousItem != null;
    }

    public boolean deleteDateTimeObject(int position){
        ArrayList<LocalDateTime> arrayList = mutableDateTimeArray.getValue();
        LocalDateTime previousItem = arrayList.remove(position);
        mutableDateTimeArray.setValue(arrayList);
        return previousItem != null;
    }

    public void addDateTimeToArray(LocalDateTime dateTime){
        ArrayList<LocalDateTime> array = mutableDateTimeArray.getValue();
        array.add(dateTime);
        mutableDateTimeArray.setValue(array);
    }

    public int getSize(){
        return mutableDateTimeArray.getValue().size();
    }

    public LocalDateTime getDateTimeObject(int position){
        return mutableDateTimeArray.getValue().get(position);
    }

    public String getDateTimeString(int position){
        ArrayList<LocalDateTime> arrayList = mutableDateTimeArray.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = arrayList.get(position);
        return dateTime.format(formatter);
    }

    public List<LocalDateTime> getDateTimeList(){
        return mutableDateTimeArray.getValue();
    }
}
