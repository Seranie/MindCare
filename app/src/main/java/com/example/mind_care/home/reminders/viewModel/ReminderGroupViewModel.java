package com.example.mind_care.home.reminders.viewModel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mind_care.home.reminders.model.RemindersGroupItem;
import com.example.mind_care.home.reminders.repository.GroupRepository;

import java.util.ArrayList;
import java.util.List;

public class ReminderGroupViewModel extends ViewModel{
    private final GroupRepository groupRepository = new GroupRepository();
    private final MutableLiveData<List<RemindersGroupItem>> remindersGroupLiveData = new MutableLiveData<>(new ArrayList<>());


    public LiveData<List<RemindersGroupItem>> getRemindersGroupLiveData() {
        return remindersGroupLiveData;
    }

    public void getGroupListFromRepository() {
        new Thread(() -> groupRepository.retrieveGroupList(remindersGroupLiveData::postValue)).start();
    }

    public void createGroup(Uri uri, String name) {
        RemindersGroupItem groupItem = new RemindersGroupItem(uri, name);
        groupRepository.addNewGroup(groupItem);
    }

    public int getSize() {
        return remindersGroupLiveData.getValue().size();
    }

    public void addReminderToGroup(int position){


    }
}
