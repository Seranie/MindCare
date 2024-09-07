package com.example.tutorial_menu.reminders;

import java.util.List;

public class RemindersGroupItem {
    //TODO incomplete class, carrys data of each group in the reminders tab
    private int mImageSource;
    private int mNameSource;
    private List<RemindersReminderItem> mRemindersReminderItems;

    public RemindersGroupItem(int imageSource, int nameSource){
        this.mImageSource = imageSource;
        this.mNameSource = nameSource;
    }


    public int getNameSource() {
        return mNameSource;
    }

    public void setNameSource(int mNameSource) {
        this.mNameSource = mNameSource;
    }

    public int getImageSource() {
        return mImageSource;
    }

    public void setImageSource(int mImageSource) {
        this.mImageSource = mImageSource;
    }

    public List<RemindersReminderItem> getRemindersReminderItems() {
        return mRemindersReminderItems;
    }

    public void setRemindersReminderItems(RemindersReminderItem remindersReminderItems) {
        this.mRemindersReminderItems.add(remindersReminderItems);
    }
}
