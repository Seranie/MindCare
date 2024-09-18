package com.example.mind_care.home.reminders.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.ScheduleNotification;
import com.example.mind_care.home.reminders.model.ReminderItemModel;
import com.example.mind_care.home.reminders.viewModel.ReminderGroupViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class RemindersReminderAdapter extends RecyclerView.Adapter<RemindersReminderAdapter.ReminderItemViewHolder>{
    private List<ReminderItemModel> remindersReminderItems = new ArrayList<>();
    private ReminderGroupViewModel groupViewModel;
    private Context activityContext;

    public RemindersReminderAdapter(ReminderGroupViewModel groupViewModel, Fragment fragment, Context activityContext){
        this.groupViewModel = groupViewModel;
        this.activityContext = activityContext;
        groupViewModel.getRemindersLiveData().observe(fragment, reminders -> {
            this.remindersReminderItems = reminders;
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public RemindersReminderAdapter.ReminderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_reminder_item, parent, false);
        return new ReminderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemindersReminderAdapter.ReminderItemViewHolder holder, int position) {
        ReminderItemModel remindersReminderItem = remindersReminderItems.get(position);
        holder.chip.setText(remindersReminderItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return remindersReminderItems.size();
    }

    class ReminderItemViewHolder extends RecyclerView.ViewHolder{
        private Chip chip;

        public ReminderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.reminders_chip);
            chip.setOnCloseIconClickListener(view -> {
                String groupId = remindersReminderItems.get(getAdapterPosition()).getGroupId();
                String reminderId = remindersReminderItems.get(getAdapterPosition()).getId();

                groupViewModel.deleteReminder(groupId, reminderId);
                List<String> alertItemIds = groupViewModel.getAllAlertItemIdsFromReminder(groupId, reminderId);
                ScheduleNotification.cancelNotification(activityContext, reminderId, alertItemIds);
            });
        }
    }
}
