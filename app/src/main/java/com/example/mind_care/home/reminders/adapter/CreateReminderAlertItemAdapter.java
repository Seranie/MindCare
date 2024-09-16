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

import java.time.LocalDateTime;

public class CreateReminderAlertItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_VIEW_TYPE_ADD_NEW = 0;
    private final int ITEM_VIEW_TYPE_REMINDER = 1;
    private final String DATE_PICKER_TAG = "datePicker";
    private final String TIME_PICKER_TAG = "timePicker";
    private final int NUMBER_OF_EXTRA_ITEMS = 1;

    ReminderAlertDateTimeViewModel alertDateTimeViewModel;
    FragmentManager fragmentManager;

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
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_create_reminder_alert_item, parent, false);
            return new ReminderItemViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == ITEM_VIEW_TYPE_REMINDER) {
            if(alertDateTimeViewModel.getSize() != 0){
                ReminderItemViewHolder reminderItemViewHolder = (ReminderItemViewHolder) holder;
                String dateTimeString = alertDateTimeViewModel.getDateTimeString(position);
                reminderItemViewHolder.reminderItem.setText(dateTimeString);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position+1 == getItemCount()) {
            return ITEM_VIEW_TYPE_ADD_NEW;
        } else {
            return ITEM_VIEW_TYPE_REMINDER;
        }
    }

    @Override
    public int getItemCount() {
        return alertDateTimeViewModel.getSize() + NUMBER_OF_EXTRA_ITEMS;
    }

    public class ReminderItemViewHolder extends RecyclerView.ViewHolder {
        private Button reminderItem;
        private Button deleteButton;
        private LocalDateTime dateTime = LocalDateTime.now();

        public ReminderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderItem = itemView.findViewById(R.id.alert_item_button);
            deleteButton = itemView.findViewById(R.id.alert_item_delete_button);

            int position = getAdapterPosition();

            reminderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    DatePickerFragment datePickerFragment = new DatePickerFragment() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            dateTime = dateTime.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth);
                        }
                    };
                    datePickerFragment.show(fragmentManager, DATE_PICKER_TAG);

                    TimePickerFragment timePickerFragment = new TimePickerFragment() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            dateTime = dateTime.withHour(hourOfDay).withMinute(minute);

                            //If user OKs both date and time, edit the existing alert item.
                            alertDateTimeViewModel.replaceDateTimeObject(position, dateTime);
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

    public class ReminderAddItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Button reminderItem;
        private final Button addButton;
        private LocalDateTime dateTime;

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
            DatePickerFragment datePickerFragment = new DatePickerFragment() {
                @Override
                public void onDateSet(DatePicker datePicker, int yr, int mth, int date) {
                    dateTime = dateTime.withYear(yr).withMonth(mth).withDayOfMonth(date);
                }
            };
            datePickerFragment.show(fragmentManager, DATE_PICKER_TAG);

            TimePickerFragment timePickerFragment = new TimePickerFragment() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                    dateTime = dateTime.withHour(hourOfDay).withMinute(minute);

                    //If user OKs both date and time pickers, add this dateTime to viewmodel.
                    alertDateTimeViewModel.addDateTimeToArray(dateTime);
                }
            };
            timePickerFragment.show(fragmentManager, TIME_PICKER_TAG);
        }
    }
}
