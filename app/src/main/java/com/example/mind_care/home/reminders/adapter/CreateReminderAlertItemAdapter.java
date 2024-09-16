package com.example.mind_care.home.reminders.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.reminders.fragment.DatePickerFragment;
import com.example.mind_care.home.reminders.fragment.TimePickerFragment;
import com.example.mind_care.home.reminders.viewModel.ReminderAlertDateTimeViewModel;


import java.util.Calendar;

public class CreateReminderAlertItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ReminderAlertDateTimeViewModel alertDateTimeViewModel;
    FragmentManager fragmentManager;

    private final int ITEM_VIEW_TYPE_ADD_NEW = 0;
    private final int ITEM_VIEW_TYPE_REMINDER = 1;
    private final String DATE_PICKER_TAG = "datePicker";
    private final String TIME_PICKER_TAG = "timePicker";

    public CreateReminderAlertItemAdapter(ReminderAlertDateTimeViewModel alertDateTimeViewModel, FragmentManager fragmentManager) {
        this.alertDateTimeViewModel = alertDateTimeViewModel;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_VIEW_TYPE_ADD_NEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_create_reminder_add_new_alert_item, parent, false);
            return new ReminderAddItemViewHolder(view);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_create_reminder_alert_item, parent, false);
            return new ReminderItemViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == ITEM_VIEW_TYPE_REMINDER){
            ReminderItemViewHolder reminderItemViewHolder = (ReminderItemViewHolder) holder;
            alertDateTimeViewModel.getDateTimeObject(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount()){
            return ITEM_VIEW_TYPE_ADD_NEW;
        }
        else {return ITEM_VIEW_TYPE_REMINDER;}
    }


    @Override
    public int getItemCount() {
        return alertDateTimeViewModel.getSize();
    }

    public class ReminderItemViewHolder extends RecyclerView.ViewHolder{
        private Button reminderItem;
        private Button deleteButton;

        public ReminderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderItem = itemView.findViewById(R.id.alert_item_button);
            deleteButton = itemView.findViewById(R.id.alert_item_delete_button);

            int position = getAdapterPosition();
            reminderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Calendar calendar = Calendar.getInstance();

                    DatePickerFragment datePickerFragment = new DatePickerFragment() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        }
                    };
                    datePickerFragment.show(fragmentManager, DATE_PICKER_TAG);

                    TimePickerFragment timePickerFragment = new TimePickerFragment() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);

                            //If user OKs both date and time, edit the existing alert item.
                            alertDateTimeViewModel.replaceDateTimeObject(position, calendar);
                        }
                    };
                    timePickerFragment.show(fragmentManager, TIME_PICKER_TAG);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDateTimeViewModel.deleteDateTimeObject(position);
                }
            });
        }

        public void setReminderItem(Button reminderItem) {
            this.reminderItem = reminderItem;
        }

        public void setDeleteButton(Button deleteButton) {
            this.deleteButton = deleteButton;
        }
    }

    public class ReminderAddItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Button reminderItem;
        private Button addButton;

        public ReminderAddItemViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderItem = itemView.findViewById(R.id.alert_item_add_button2);
            addButton = itemView.findViewById(R.id.alert_item_add_button);

            addButton.setOnClickListener(this);
            reminderItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Add on click show date/time picker, then pass datetime to viewmodel
            final Calendar calendar = Calendar.getInstance();

            DatePickerFragment datePickerFragment = new DatePickerFragment() {
                @Override
                public void onDateSet(DatePicker datePicker, int yr, int mth, int date) {
                    calendar.set(Calendar.YEAR, yr);
                    calendar.set(Calendar.MONTH, mth);
                    calendar.set(Calendar.DAY_OF_MONTH, date);
                }
            };
            datePickerFragment.show(fragmentManager, DATE_PICKER_TAG);

            TimePickerFragment timePickerFragment = new TimePickerFragment() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                }
            };
            timePickerFragment.show(fragmentManager, TIME_PICKER_TAG);

            alertDateTimeViewModel.addDateTimeToArray(calendar);
        }
    }
}
