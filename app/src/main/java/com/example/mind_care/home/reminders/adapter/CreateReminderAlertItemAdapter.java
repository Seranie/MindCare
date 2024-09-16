package com.example.mind_care.home.reminders.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.reminders.fragment.DatePickerFragment;
import com.example.mind_care.home.reminders.viewModel.ReminderAlertDateViewModel;
import com.example.mind_care.home.reminders.viewModel.ReminderAlertTimeViewModel;

public class CreateReminderAlertItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ReminderAlertDateViewModel alertDateViewModel;
    ReminderAlertTimeViewModel alertTimeViewModel;

    private final int ITEM_VIEW_TYPE_ADD_NEW = 0;
    private final int ITEM_VIEW_TYPE_REMINDER = 1;

    public CreateReminderAlertItemAdapter(ReminderAlertDateViewModel alertDateViewModel, ReminderAlertTimeViewModel alertTimeViewModel) {
        this.alertDateViewModel = alertDateViewModel;
        this.alertTimeViewModel = alertTimeViewModel;
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
        //TODO
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
        return 0;
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
                    DatePickerFragment datePickerFragment = new DatePickerFragment() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            //TODO
                        }
                    };
                }
            });
        }
    }

    public class ReminderAddItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Button reminderItem;
        private Button addButton;

        public ReminderAddItemViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderItem = itemView.findViewById(R.id.alert_item_add_button2);
            addButton = itemView.findViewById(R.id.alert_item_add_button);

            int position = getAdapterPosition();
            addButton.setOnClickListener(this);
            reminderItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            DatePickerFragment datePickerFragment = new DatePickerFragment() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                }
            };
        }
    }
}
