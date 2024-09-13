package com.example.mind_care.reminders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;

import java.util.List;

public class RemindersReminderAdapter extends RecyclerView.Adapter<RemindersReminderAdapter.ReminderItemViewHolder>{
    //TODO incomplete adapter
    private List<RemindersReminderItem> remindersReminderItems;

    public RemindersReminderAdapter(List<RemindersReminderItem> remindersReminderItems){
        //TODO will need to be changed to use data gotten group RemindersGroupItem instead
        this.remindersReminderItems = remindersReminderItems;
    }

    public void updateList(List<RemindersReminderItem> newReminders){
        //TODO incomplete method to use for updating list based on which group is chosen
        remindersReminderItems = newReminders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RemindersReminderAdapter.ReminderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_reminder_item, parent, false);
        return new ReminderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemindersReminderAdapter.ReminderItemViewHolder holder, int position) {
        RemindersReminderItem remindersReminderItem = remindersReminderItems.get(position);
        holder.mNote.setText(remindersReminderItem.getNote());
        holder.mTitle.setText(remindersReminderItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return remindersReminderItems.size();
    }

    class ReminderItemViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        TextView mNote;

        public ReminderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.reminders_reminder_title);
            mNote = itemView.findViewById(R.id.reminders_reminder_note);
        }
    }
}
