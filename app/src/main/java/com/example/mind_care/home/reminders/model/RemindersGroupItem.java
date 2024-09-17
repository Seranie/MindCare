package com.example.mind_care.home.reminders.model;

import android.net.Uri;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class RemindersGroupItem {
    //TODO incomplete class, carrys data of each group in the reminders tab
    private final Uri mImageSource;
    private final String mName;
    @PropertyName("reminderList")
    private List<ReminderItemModel> reminderList;
    private String groupId;

    public RemindersGroupItem(Uri imageSource, String name) {
        this.mImageSource = imageSource;
        this.mName = name;
    }

    public Uri getImageSource() {
        return mImageSource;
    }

    public String getName() {
        return mName;
    }

    public List<ReminderItemModel> getReminderList() {
        return reminderList;
    }

    public void setReminderList(List<ReminderItemModel> reminders) {
        this.reminderList = reminders;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }
}
