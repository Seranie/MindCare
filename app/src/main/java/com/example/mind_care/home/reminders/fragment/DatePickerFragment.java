package com.example.mind_care.home.reminders.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mind_care.home.reminders.viewModel.ReminderAlertDateViewModel;
import com.example.mind_care.home.reminders.model.DateModel;

import java.util.Calendar;

public abstract class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(requireActivity(), this, year, month, day);
    }

    @Override
    public abstract void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        //TODO store date
        DateModel date = new DateModel();
        date.setYear(year);
        date.setMonth(month);
        date.setDayOfMonth(dayOfMonth);
        ReminderAlertDateViewModel viewModel = new ViewModelProvider(getParentFragment()).get(ReminderAlertDateViewModel.class);
        viewModel.setMutableDateArray(date);
    }
}
