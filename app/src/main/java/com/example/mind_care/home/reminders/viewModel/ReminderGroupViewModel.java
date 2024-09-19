package com.example.mind_care.home.reminders.viewModel;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mind_care.home.reminders.model.ReminderItemModel;
import com.example.mind_care.home.reminders.model.RemindersGroupItem;
import com.example.mind_care.home.reminders.repository.GroupRepository;

import java.util.ArrayList;
import java.util.List;

public class ReminderGroupViewModel extends ViewModel{
    private final GroupRepository groupRepository = new GroupRepository();
    private final MutableLiveData<List<RemindersGroupItem>> remindersGroupLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<ReminderItemModel>> remindersLiveData = new MutableLiveData<>(new ArrayList<>());

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
        try{
            return remindersGroupLiveData.getValue().size();
        } catch (NullPointerException e){
            return 0;
        }
    }

    public LiveData<List<ReminderItemModel>> getRemindersLiveData() {
        return remindersLiveData;
    }

    public void getRemindersFromGroup(String groupId){
        if (groupId.isEmpty()){
            remindersLiveData.setValue(new ArrayList<>());
        }else{
            new Thread(() -> groupRepository.retrieveRemindersFromGroup(groupId, remindersLiveData::postValue)).start();
        }
    }

    public void deleteReminder(String groupId, String reminderId){
        //delete then update livedata
        groupRepository.deleteReminder(groupId, reminderId);
        getRemindersFromGroup(groupId);
    }

    public void scheduleAllNotifications(Context context){
        new Thread(() -> groupRepository.getAllRemindersAndSetNotifications(context)).start();
    }


    public void getAllAlertItemIdsFromReminder(String groupId, String reminderId, OnAlertItemIdQueryComplete callback){
        groupRepository.getAllAlertItemIds(groupId, reminderId, callback);
    }

    public interface OnAlertItemIdQueryComplete {
        void onComplete(List<String> alertItemIds);
    }
}
