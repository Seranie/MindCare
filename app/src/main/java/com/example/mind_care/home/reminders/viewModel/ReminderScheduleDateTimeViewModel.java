package com.example.mind_care.home.reminders.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReminderScheduleDateTimeViewModel extends ViewModel {
    private MutableLiveData<LocalDateTime> mutableDateTime = new MutableLiveData<>();

    public ReminderScheduleDateTimeViewModel() {
        mutableDateTime.setValue(LocalDateTime.now());
    }

    public LiveData<LocalDateTime> getDateTimeLiveData() {
        return mutableDateTime;
    }


    public void changeDateTime(LocalDateTime dateTime){
        mutableDateTime.setValue(dateTime);
    }

    public LocalDateTime getDateTime(){
        return mutableDateTime.getValue();
    }

    public String getDateTimeString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = mutableDateTime.getValue();
        return dateTime.format(formatter);
    }
}
