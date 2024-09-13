package com.example.mind_care.showcases.reminders;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mind_care.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

public class RemindersCreateNewReminderShowcase extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reminders_create_reminder_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Resources resources = getResources();

        TapTargetSequence sequence = new TapTargetSequence(getActivity());
        sequence.targets(
                        TapTarget.forView(view.findViewById(R.id.reminders_new_reminder_new_group), getString(resources, R.string.reminders_new_reminder_new_group_title), getString(resources, R.string.reminders_new_reminder_new_group_description)).transparentTarget(true),
                        TapTarget.forView(view.findViewById(R.id.reminders_new_schedule), getString(resources, R.string.reminders_new_reminder_schedule_title), getString(resources, R.string.reminders_new_reminder_schedule_description)).transparentTarget(true),
                        TapTarget.forView(view.findViewById(R.id.reminders_new_reminder), getString(resources, R.string.reminders_new_reminder_reminder_title), getString(resources, R.string.reminders_new_reminder_reminder_description)).transparentTarget(true),
                        TapTarget.forView(view.findViewById(R.id.reminders_set_repeat), getString(resources, R.string.reminders_new_reminder_set_repeat_title), getString(resources, R.string.reminders_new_reminder_set_repeat_description)).transparentTarget(true),
                        TapTarget.forView(view.findViewById(R.id.reminders_set_ringtone), getString(resources, R.string.reminders_new_reminder_set_ringtone_title), getString(resources, R.string.reminders_new_reminder_set_ringtone_description)).transparentTarget(true)
                )
                .continueOnCancel(true)
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        getActivity().finishAfterTransition();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                });

        sequence.start();

    }

    private String getString(Resources resource, int id) {
        return resource.getString(id);
    }
}
