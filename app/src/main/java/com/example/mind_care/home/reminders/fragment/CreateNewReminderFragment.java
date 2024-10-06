package com.example.mind_care.home.reminders.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.reminders.adapter.CreateReminderAlertItemAdapter;
import com.example.mind_care.home.reminders.adapter.CreateReminderGroupsAdapter;
import com.example.mind_care.home.reminders.model.ReminderItemModel;
import com.example.mind_care.home.reminders.viewModel.ReminderAlertDateTimeViewModel;
import com.example.mind_care.home.reminders.viewModel.ReminderGroupViewModel;
import com.example.mind_care.home.reminders.viewModel.ReminderItemViewModel;
import com.example.mind_care.home.reminders.viewModel.ReminderScheduleDateTimeViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.util.List;

public class CreateNewReminderFragment extends Fragment implements DatePickerFragment.OnDateSetCallback, TimePickerFragment.OnTimeSetCallback {
    private RecyclerView groupsRecyclerView;
    private RecyclerView remindersRecyclerView;
    private TextInputEditText title;
    private TextInputEditText note;
    private TextInputEditText schedule;
//    private TextInputEditText repeat;
//    private TextInputEditText ringtone;
    private Button confirmButton;
    private Button cancelButton;

    private final String datePickerTag = "datePicker";
    private final String timePickerTag = "timePicker";

    ReminderGroupViewModel groupViewModel;
    ReminderAlertDateTimeViewModel alertDateTimeViewModel;
    ReminderScheduleDateTimeViewModel scheduleDateTimeViewModel;
    ReminderItemViewModel reminderItemViewModel;

    private LocalDateTime scheduleDateTime = LocalDateTime.now();


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
//        repeat = view.findViewById(R.id.create_reminder_repeat);
//        ringtone = view.findViewById(R.id.create_reminder_ringtone);
        confirmButton = view.findViewById(R.id.create_reminder_confirm_button);
        cancelButton = view.findViewById(R.id.create_reminder_cancel_button);
        TextInputLayout titleLayout = view.findViewById(R.id.create_reminder_title_layout);

        //Viewmodels for holding alert item date/times
        alertDateTimeViewModel = new ViewModelProvider(this).get(ReminderAlertDateTimeViewModel.class);
        groupViewModel = new ViewModelProvider(requireActivity()).get(ReminderGroupViewModel.class);
        scheduleDateTimeViewModel = new ViewModelProvider(this).get(ReminderScheduleDateTimeViewModel.class);
        reminderItemViewModel = new ViewModelProvider(requireActivity()).get(ReminderItemViewModel.class);

        CreateReminderGroupsAdapter groupsAdapter = new CreateReminderGroupsAdapter(groupViewModel, getContext(), this);
        groupsRecyclerView.setAdapter(groupsAdapter);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //Get any updates from database and update viewmodel and thus adapter
        groupViewModel.getGroupListFromRepository();

        //TODO send alert item list in associated with the current reminder if any.
        remindersRecyclerView.setAdapter(new CreateReminderAlertItemAdapter(alertDateTimeViewModel, getChildFragmentManager(), this));
        remindersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // call date time pickers when user clicks on this.
        schedule.setOnClickListener(v ->{
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setOnDateSetListener(this);
            datePickerFragment.show(getChildFragmentManager(), datePickerTag);
        });
        scheduleDateTimeViewModel.getDateTimeLiveData().observe(getViewLifecycleOwner(), dateTime -> {
            schedule.setText(scheduleDateTimeViewModel.getDateTimeString());
        });


        confirmButton.setOnClickListener(v -> {
            String currentGroupSelectedId = groupsAdapter.getSelectedGroupId() != null ? groupsAdapter.getSelectedGroupId() : "";
            String reminderTitle = title.getText() != null ? title.getText().toString() : "";
            String reminderNote = note.getText() != null ? note.getText().toString() : "";
            List<LocalDateTime> reminderAlertItemList = alertDateTimeViewModel.getDateTimeList();
            LocalDateTime reminderSchedule = scheduleDateTimeViewModel.getDateTime();
            //TODO repeat and ringtone, for now empty might remove?
//            String reminderRepeat = repeat.getText() != null ? repeat.getText().toString() : "";
//            String reminderRingtone = ringtone.getText() != null ? ringtone.getText().toString() : "";

            //Check if at least title is filled and groupID must be selected.
            if(reminderTitle.isEmpty()){
                titleLayout.setError(getString(R.string.please_enter_a_title));
                return;
            }else{ titleLayout.setError(null); }
            if (currentGroupSelectedId.isEmpty()){
                Toast.makeText(getContext(), "Please select a group", Toast.LENGTH_SHORT).show();
            }
            else {
                //send to database
                ReminderItemModel reminderItem = new ReminderItemModel(currentGroupSelectedId, reminderTitle, reminderNote, reminderSchedule, reminderAlertItemList);
                reminderItemViewModel.addReminderItem(reminderItem);
                groupViewModel.scheduleAllNotifications(requireActivity().getApplicationContext());

                Navigation.findNavController(v).popBackStack();
            }
        });

        cancelButton.setOnClickListener(v -> {Navigation.findNavController(v).popBackStack();});
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        scheduleDateTime = scheduleDateTime.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth);
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setOnTimeSetListener(this);
        timePickerFragment.show(getChildFragmentManager(), timePickerTag);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        scheduleDateTime = scheduleDateTime.withHour(hourOfDay).withMinute(minute);
        scheduleDateTimeViewModel.changeDateTime(scheduleDateTime);
    }
}
