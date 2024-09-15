package com.example.mind_care.home.reminders.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.reminders.fragment.DatePickerFragment;
import com.example.mind_care.home.reminders.model.ReminderAlertItem;
import com.example.mind_care.home.reminders.viewModel.ReminderAlertDateViewModel;
import com.example.mind_care.home.reminders.viewModel.ReminderAlertTimeViewModel;

import java.util.List;

public class CreateReminderAlertItemAdapter extends RecyclerView.Adapter<CreateReminderAlertItemAdapter.ReminderItemViewHolder> {
    ReminderAlertDateViewModel alertDateViewModel;
    ReminderAlertTimeViewModel alertTimeViewModel;

    public CreateReminderAlertItemAdapter(ReminderAlertDateViewModel alertDateViewModel, ReminderAlertTimeViewModel alertTimeViewModel) {
        this.alertDateViewModel = alertDateViewModel;
        this.alertTimeViewModel = alertTimeViewModel;
    }

    @NonNull
    @Override
    public ReminderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_create_reminder_alert_item, parent, false);
        return new ReminderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderItemViewHolder holder, int position) {
        //TODO
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
            reminderItem = itemView.findViewById(R.id.create_reminder_item_button);
            deleteButton = itemView.findViewById(R.id.alert_item_delete_button);

            int position = getAdapterPosition();
            reminderItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerFragment datePickerFragment = new DatePickerFragment() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            alertDateViewModel.addDateToArray(dayOfMonth, month, year);
                        }
                    };
                    datePickerFragment.show(view.getContext().getSupportFragmentManager(), "date picker");
                }
            });
        }


    }
}
