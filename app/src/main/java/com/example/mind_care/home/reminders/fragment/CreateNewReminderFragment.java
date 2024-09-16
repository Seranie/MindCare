package com.example.mind_care.home.reminders.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.example.mind_care.home.reminders.viewModel.ReminderAlertDateTimeViewModel;
import com.example.mind_care.home.reminders.viewModel.ReminderGroupViewModel;
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

    ReminderGroupViewModel groupViewModel;
    ReminderAlertDateTimeViewModel alertDateTimeViewModel;


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
        alertDateTimeViewModel = new ViewModelProvider(requireActivity()).get(ReminderAlertDateTimeViewModel.class);
        groupViewModel = new ViewModelProvider(requireActivity()).get(ReminderGroupViewModel.class);

        CreateReminderGroupsAdapter groupsAdapter = new CreateReminderGroupsAdapter(groupViewModel, getContext(), this);
        groupsRecyclerView.setAdapter(groupsAdapter);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //Get any updates from database and update viewmodel and thus adapter
        groupViewModel.getGroupListFromRepository();

        //TODO send alert item list in associated with the current reminder if any.
        remindersRecyclerView.setAdapter(new CreateReminderAlertItemAdapter(alertDateTimeViewModel, getChildFragmentManager()));
        remindersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        confirmButton.setOnClickListener(v -> {
            //TODO gather data from fields
            String currentGroupSelectedId = groupsAdapter.getSelectedGroupId() != null ? groupsAdapter.getSelectedGroupId() : "";
            String reminderTitle = title.getText() != null ? title.getText().toString() : "";
            String reminderNote = note.getText() != null ? note.getText().toString() : "";
            String reminderSchedule = schedule.getText() != null ? schedule.getText().toString() : "";
            String reminderRepeat = repeat.getText() != null ? repeat.getText().toString() : "";
            String reminderRingtone = ringtone.getText() != null ? ringtone.getText().toString() : "";

            //send to database
            Log.i("INFO", "Group id: " + currentGroupSelectedId);
        });

    }

}
