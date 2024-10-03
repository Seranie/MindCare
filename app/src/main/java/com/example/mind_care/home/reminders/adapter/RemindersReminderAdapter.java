package com.example.mind_care.home.reminders.adapter;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.notification.ScheduleNotification;
import com.example.mind_care.home.reminders.model.ReminderItemModel;
import com.example.mind_care.home.reminders.viewModel.ReminderGroupViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RemindersReminderAdapter extends RecyclerView.Adapter<RemindersReminderAdapter.ReminderItemViewHolder>{
    private List<ReminderItemModel> remindersReminderItems = new ArrayList<>();
    private ReminderGroupViewModel groupViewModel;
    private Context activityContext;
    private SoundPool soundPool;
    private int soundId;

    private final int MAX_STREAMS = 5;
    private final float LEFT_VOLUME = 1.0f;
    private final float RIGHT_VOLUME = 1.0f;
    private final int PRIORITY = 0;
    private final int LOOP = 0;
    private final float RATE = 1.0f;

    public RemindersReminderAdapter(ReminderGroupViewModel groupViewModel, Fragment fragment, Context activityContext, Context fragmentContext){
        this.groupViewModel = groupViewModel;
        this.activityContext = activityContext;
        groupViewModel.getRemindersLiveData().observe(fragment, reminders -> {
            if(!reminders.isEmpty()){
                reminders.sort(Comparator.comparing(ReminderItemModel::getCreatedDate).reversed());
            }
            this.remindersReminderItems = reminders;
            notifyDataSetChanged();
        });

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                                .build()
                ).setMaxStreams(MAX_STREAMS)
                .build();
        soundId = soundPool.load(fragmentContext, R.raw.complete_reminder, 1);
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
        String note = remindersReminderItem.getNote();
        if(note.isEmpty()){
            holder.note.setVisibility(View.GONE);
        }
        else{
            holder.note.setVisibility(View.VISIBLE);
            holder.note.setText(note);
        }
    }

    @Override
    public int getItemCount() {
        return remindersReminderItems.size();
    }

    class ReminderItemViewHolder extends RecyclerView.ViewHolder implements ReminderGroupViewModel.OnAlertItemIdQueryComplete {
        private Chip chip;
        private TextView note;

        public ReminderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.reminders_chip);
            note = itemView.findViewById(R.id.reminder_note_textview);
            chip.setOnCloseIconClickListener(view -> {
                soundPool.play(soundId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, LOOP, RATE);
                String groupId = remindersReminderItems.get(getAdapterPosition()).getGroupId();
                String reminderId = remindersReminderItems.get(getAdapterPosition()).getId();
                groupViewModel.getAllAlertItemIdsFromReminder(groupId, reminderId, this);
            });
        }

        @Override
        public void onComplete(List<String> alertItemIds) {
            String reminderId = remindersReminderItems.get(getAdapterPosition()).getId();
            String groupId = remindersReminderItems.get(getAdapterPosition()).getGroupId();
            ScheduleNotification.cancelNotification(activityContext, reminderId, alertItemIds);
            groupViewModel.deleteReminder(groupId, reminderId);
        }
    }
}
