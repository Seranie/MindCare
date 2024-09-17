package com.example.mind_care.home.reminders.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.example.mind_care.home.reminders.model.ReminderItemModel;
import com.example.mind_care.home.reminders.repository.ReminderItemRepository;

public class ReminderItemViewModel {
    private MutableLiveData<ReminderItemModel> mutableReminderItem = new MutableLiveData<>();
    private ReminderItemRepository repository;

    public ReminderItemViewModel() {
        repository = new ReminderItemRepository();
    }

    public MutableLiveData<ReminderItemModel> getReminderItemLiveData() {

    public void addReminderItem(ReminderItemModel reminderItem){
        repository.addReminderItem(reminderItem);
    }
}
