package com.example.mind_care.home.reminders.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.reminders.adapter.CreateReminderAlertItemAdapter;
import com.example.mind_care.home.reminders.adapter.CreateReminderGroupsAdapter;
import com.example.mind_care.home.reminders.viewModel.ReminderAlertDateViewModel;
import com.example.mind_care.home.reminders.viewModel.ReminderAlertTimeViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class CreateNewReminderFragment extends Fragment {
    private RecyclerView groupsRecyclerView;
    private RecyclerView remindersRecyclerView;
    private TextInputEditText title;
    private TextInputEditText note;
    private TextInputEditText schedule;
    private TextInputEditText repeat;
    private TextInputEditText ringtone;
    private Button confirmButton;
    private Button cancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reminders_create_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupsRecyclerView = view.findViewById(R.id.create_reminder_group_recyclerview);
        remindersRecyclerView = view.findViewById(R.id.create_reminder_item_recyclerview);
        title = view.findViewById(R.id.create_reminder_title);
        note = view.findViewById(R.id.create_reminder_note);
        schedule = view.findViewById(R.id.create_reminder_schedule);
        repeat = view.findViewById(R.id.create_reminder_repeat);
        ringtone = view.findViewById(R.id.create_reminder_ringtone);
        confirmButton = view.findViewById(R.id.create_reminder_confirm_button);
        cancelButton = view.findViewById(R.id.create_reminder_cancel_button);

        //Viewmodels for holding alert item date/times
        ReminderAlertDateViewModel alertDateViewModel = new ViewModelProvider(this).get(ReminderAlertDateViewModel.class);
        ReminderAlertTimeViewModel alertTimeViewModel = new ViewModelProvider(this).get(ReminderAlertTimeViewModel.class);

        //TODO send group list in from database
        CreateReminderGroupsAdapter groupsAdapter = new CreateReminderGroupsAdapter();
        groupsRecyclerView.setAdapter(groupsAdapter);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //TODO send reminder list in from database
        remindersRecyclerView.setAdapter(new CreateReminderAlertItemAdapter(alertDateViewModel, alertTimeViewModel));
        remindersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO gather data from fields
                int currentGroupSelected = groupsAdapter.getSelectedGroupPosition();
                String reminderTitle = title.getText() != null ? title.getText().toString() : "";
                String reminderNote = note.getText() != null ? note.getText().toString() : "";
                String reminderSchedule = schedule.getText() != null ? schedule.getText().toString() : "";
                String reminderRepeat = repeat.getText() != null ? repeat.getText().toString() : "";
                String reminderRingtone = ringtone.getText() != null ? ringtone.getText().toString() : "";


                //send to database
            }
        });

    }
}
