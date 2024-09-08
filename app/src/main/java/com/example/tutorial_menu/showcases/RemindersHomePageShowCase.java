package com.example.tutorial_menu.showcases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tutorial_menu.R;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class RemindersHomePageShowCase extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reminders_home_page_showcase, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), "reminders_home_page_showcase");
        sequence.setConfig(config);

        sequence.addSequenceItem(view.findViewById(R.id.reminders_home_page_showcase_options), "HI", "BYE");
        sequence.addSequenceItem(view.findViewById(R.id.reminders_home_page_showcase_fab), "HI", "BYE");

        MaterialShowcaseView.resetAll(getActivity());
        sequence.start();

    }
}
