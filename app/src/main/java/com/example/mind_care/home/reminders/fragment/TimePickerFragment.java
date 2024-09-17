package com.example.mind_care.home.reminders.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private OnTimeSetCallback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        try{
            callback.onTimeSet(timePicker, hourOfDay, minute);
        } catch (NullPointerException e){
            Log.i("TimePickerFragment", "callback is null");
        }
    }

    public interface OnTimeSetCallback {
        void onTimeSet(TimePicker timePicker, int hourOfDay, int minute);
    }

    public void setOnTimeSetListener(OnTimeSetCallback callback) {
        this.callback = callback;
    }
}
