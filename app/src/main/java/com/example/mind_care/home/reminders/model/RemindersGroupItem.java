package com.example.mind_care.home.reminders.model;

import android.net.Uri;

import java.util.List;

public class RemindersGroupItem {
    //TODO incomplete class, carrys data of each group in the reminders tab
    private final Uri mImageSource;
    private final String mName;
    private List<RemindersReminderItem> mRemindersReminderItems;

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

    public List<RemindersReminderItem> getRemindersReminderItems() {
        return mRemindersReminderItems;
    }

    public void setRemindersReminderItems(RemindersReminderItem remindersReminderItems) {
        this.mRemindersReminderItems.add(remindersReminderItems);
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }
}
