package com.example.mind_care.home.reminders.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mind_care.home.reminders.model.ReminderItemModel;
import com.example.mind_care.home.reminders.repository.ReminderItemRepository;

public class ReminderItemViewModel extends ViewModel {
    private MutableLiveData<ReminderItemModel> mutableReminderItem = new MutableLiveData<>();
    private ReminderItemRepository repository;

    public ReminderItemViewModel() {
        repository = new ReminderItemRepository();
    }

    public LiveData<ReminderItemModel> getReminderItemLiveData() {
        return mutableReminderItem;
    }

    public void addReminderItem(ReminderItemModel reminderItem){
        repository.addReminderItemToDatabase(reminderItem);
    }
}
