package com.example.mind_care.home.reminders.viewModel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mind_care.home.reminders.model.RemindersGroupItem;

import java.util.ArrayList;

public class ReminderGroupViewModel extends ViewModel {
    private MutableLiveData<ArrayList<RemindersGroupItem>> remindersGroupLiveData = new MutableLiveData<>();

    public ReminderGroupViewModel() {
        remindersGroupLiveData.setValue(new ArrayList<>());
    }

    public MutableLiveData<ArrayList<RemindersGroupItem>> getRemindersGroupLiveData() {
        return remindersGroupLiveData;
    }

    public ArrayList<RemindersGroupItem> getGroupList(){
        return remindersGroupLiveData.getValue();
    }

    public void createGroup(Uri uri, String name){
        ArrayList<RemindersGroupItem> temp = remindersGroupLiveData.getValue();
        temp.add(new RemindersGroupItem(uri, name));
        remindersGroupLiveData.setValue(temp);
    }

    public int getSize(){
        return remindersGroupLiveData.getValue().size();
    }

}
