package com.example.mind_care.home.reminders.model;

public class DateModel {
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;


    public DateModel(int dayOfMonth, int month, int year) {
        this.mDayOfMonth = dayOfMonth;
        this.mMonth = month;
        this.mYear = year;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int mYear) {
        this.mYear = mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public int getDayOfMonth() {
        return mDayOfMonth;
    }

    public void setDayOfMonth(int mDayOfMonth) {
        this.mDayOfMonth = mDayOfMonth;
    }
}
