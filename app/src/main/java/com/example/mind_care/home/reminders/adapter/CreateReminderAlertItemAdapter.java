package com.example.mind_care.home.reminders.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.reminders.fragment.DatePickerFragment;
import com.example.mind_care.home.reminders.fragment.TimePickerFragment;
import com.example.mind_care.home.reminders.viewModel.ReminderAlertDateTimeViewModel;
import com.google.android.material.chip.Chip;

import java.time.LocalDateTime;

public class CreateReminderAlertItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_VIEW_TYPE_ADD_NEW = 0;
    private final int ITEM_VIEW_TYPE_REMINDER = 1;
    private final String DATE_PICKER_TAG = "datePicker";
    private final String TIME_PICKER_TAG = "timePicker";
    private final int NUMBER_OF_EXTRA_ITEMS = 1;

    ReminderAlertDateTimeViewModel alertDateTimeViewModel;
    FragmentManager fragmentManager;

    public CreateReminderAlertItemAdapter(ReminderAlertDateTimeViewModel alertDateTimeViewModel, FragmentManager fragmentManager, Fragment fragment) {
        this.alertDateTimeViewModel = alertDateTimeViewModel;
        this.fragmentManager = fragmentManager;
        alertDateTimeViewModel.getMutableDateTimeLiveData().observe(fragment, arrayList -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_VIEW_TYPE_ADD_NEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_create_reminder_add_new_alert_item, parent, false);
            return new ReminderAddItemViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_create_reminder_alert_item, parent, false);
            return new ReminderItemViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == ITEM_VIEW_TYPE_REMINDER) {
            if (alertDateTimeViewModel.getSize() != 0) {
                ReminderItemViewHolder reminderItemViewHolder = (ReminderItemViewHolder) holder;
                String dateTimeString = alertDateTimeViewModel.getDateTimeString(position);
                reminderItemViewHolder.reminderItem.setText(dateTimeString);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return ITEM_VIEW_TYPE_ADD_NEW;
        } else {
            return ITEM_VIEW_TYPE_REMINDER;
        }
    }

    @Override
    public int getItemCount() {
        return alertDateTimeViewModel.getSize() + NUMBER_OF_EXTRA_ITEMS;
    }

    public class ReminderItemViewHolder extends RecyclerView.ViewHolder implements DatePickerFragment.OnDateSetCallback, TimePickerFragment.OnTimeSetCallback {
        private final Chip reminderItem;
        private LocalDateTime dateTime = LocalDateTime.now();

        public ReminderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderItem = itemView.findViewById(R.id.alert_item_chip);

            reminderItem.setOnClickListener(view -> {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setOnDateSetListener(ReminderItemViewHolder.this);
                datePickerFragment.show(fragmentManager, DATE_PICKER_TAG);
            });
            reminderItem.setOnCloseIconClickListener(view -> alertDateTimeViewModel.deleteDateTimeObject(getAdapterPosition()));
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            dateTime = dateTime.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth);
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(fragmentManager, TIME_PICKER_TAG);
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            dateTime = dateTime.withHour(hourOfDay).withMinute(minute);
            //If user OKs both date and time, edit the existing alert item.
            alertDateTimeViewModel.replaceDateTimeObject(getAdapterPosition(), dateTime);
        }
    }

    public class ReminderAddItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, DatePickerFragment.OnDateSetCallback, TimePickerFragment.OnTimeSetCallback {
        private final Chip reminderItem;
        private LocalDateTime dateTime = LocalDateTime.now();

        public ReminderAddItemViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderItem = itemView.findViewById(R.id.create_alert_item_chip);
            reminderItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Add on click show date/time picker, then pass datetime to viewmodel
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setOnDateSetListener(this);
            datePickerFragment.show(fragmentManager, DATE_PICKER_TAG);

        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            dateTime = dateTime.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth);
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(fragmentManager, TIME_PICKER_TAG);
        }

        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            dateTime = dateTime.withHour(hourOfDay).withMinute(minute);
            alertDateTimeViewModel.addDateTimeToArray(dateTime);
        }
    }
}
