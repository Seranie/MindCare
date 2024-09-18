package com.example.mind_care.home.reminders.model;

import com.google.firebase.Timestamp;

import java.time.LocalDateTime;
import java.util.List;

public class RemindersReminderItem {
    private String mTitle;
    private String mNote;
    private Timestamp mSchedule;
    private List<LocalDateTime> mAlertItems;

    public String getNote() {
        return mNote;
    }

    public void setNote(String mNote) {
        this.mNote = mNote;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }


}
