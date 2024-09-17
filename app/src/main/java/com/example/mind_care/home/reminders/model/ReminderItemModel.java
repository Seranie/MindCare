package com.example.mind_care.home.reminders.model;

import java.time.LocalDateTime;
import java.util.List;

public class ReminderItemModel {
    private String ownerGroup;
    private String title;
    private String note;
    private LocalDateTime schedule;
    private List<LocalDateTime> reminderAlertItemList;

    public ReminderItemModel(String ownerGroup, String title, String note, LocalDateTime schedule, List<LocalDateTime> reminderAlertItemList) {
        this.ownerGroup = ownerGroup;
        this.title = title;
        this.note = note;
        this.schedule = schedule;
        this.reminderAlertItemList = reminderAlertItemList;
    }

    public String getGroupId(){
        return ownerGroup;
    }
}
