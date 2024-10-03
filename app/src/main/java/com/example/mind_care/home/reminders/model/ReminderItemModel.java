package com.example.mind_care.home.reminders.model;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

public class ReminderItemModel {
    private String reminderId;
    private String ownerGroup;
    private String title;
    private String note;
    private LocalDateTime schedule;
    private List<LocalDateTime> reminderAlertItemList;
    private Timestamp createdDate;

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

    public LocalDateTime getSchedule() {
        return schedule;
    }

    public String getNote() {
        return note;
    }

    public String getTitle() {
        return title;
    }

    public List<LocalDateTime> getReminderAlertItemList() {
        return reminderAlertItemList;
    }

    public String getId(){
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
