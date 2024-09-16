package com.example.mind_care.home.reminders.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mind_care.home.reminders.model.DateModel;
import com.example.mind_care.home.reminders.model.DateTimeModel;
import com.example.mind_care.home.reminders.model.TimeModel;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class ReminderAlertDateTimeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<DateTimeModel>> mutableDateTimeArray = new MutableLiveData<>();

    public MutableLiveData<ArrayList<DateTimeModel>> getMutableDateTimeLiveData() {
        return mutableDateTimeArray;
    }

    public ArrayList<DateTimeModel> getDateTimeArray(){
        return mutableDateTimeArray.getValue();
    }

    public boolean replaceDateTimeObject(int position, Calendar calendar){
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        ArrayList<DateTimeModel> arrayList = mutableDateTimeArray.getValue();
        DateTimeModel previousItem = arrayList.set(position, new DateTimeModel(new DateModel(dayOfMonth, month, year), new TimeModel(hour, minute)));
        return previousItem != null;
    }

    public boolean deleteDateTimeObject(int position){
        ArrayList<DateTimeModel> arrayList = mutableDateTimeArray.getValue();
        DateTimeModel previousItem = arrayList.remove(position);
        return previousItem != null;
    }

    public void addDateTimeToArray(Calendar calendar){
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        ArrayList<DateTimeModel> array = mutableDateTimeArray.getValue();
        array.add(new DateTimeModel(new DateModel(dayOfMonth, month, year), new TimeModel(hour, minute)));
        mutableDateTimeArray.setValue(array);
    }

    public int getSize(){
        return mutableDateTimeArray.getValue().size();
    }

    public DateTimeModel getDateTimeObject(int position){
        return mutableDateTimeArray.getValue().get(position);
    }

    public String getDateTimeString(int position){
        ArrayList<DateTimeModel> arrayList = mutableDateTimeArray.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    }
}
