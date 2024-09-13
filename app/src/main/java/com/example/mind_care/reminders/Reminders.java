package com.example.mind_care.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.showcases.ShowcaseFragment;

import java.util.ArrayList;
import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class Reminders extends ShowcaseFragment {
    RecyclerView reminderItemsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reminders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get recycler views
        RecyclerView groupItemsRecyclerView = view.findViewById(R.id.reminders_group_recyclerview);
        RecyclerView reminderItemsRecyclerView = view.findViewById(R.id.reminders_reminder_recyclerview);

        //Create arrays to hold reminder groups and a temporary reminder list as well
        //TODO reminder list will change to be gotten from database instead in the future
        List<RemindersGroupItem> remindersGroupItems = new ArrayList<>();
        List<RemindersReminderItem> remindersReminderItems = new ArrayList<>();

        //Create a fake reminder item for temp use and add to list TODO remove in future.
        RemindersReminderItem remindersReminderItem = new RemindersReminderItem();
        remindersReminderItem.setTitle("Do something");
        remindersReminderItem.setNote("On someday");
        remindersReminderItems.add(remindersReminderItem);

        //Create and add a new group object into list TODO will be dynamically gotten from database in future.
        remindersGroupItems.add(new RemindersGroupItem(
                R.drawable.chat_buddy_icon,
                R.string.reminders_group_name
        ));


        groupItemsRecyclerView.setAdapter(new RemindersGroupAdapter(remindersGroupItems));
        groupItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        reminderItemsRecyclerView.setAdapter(new RemindersReminderAdapter(remindersReminderItems));
        reminderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        MenuHost menuHost = requireActivity();
//        menuHost.addMenuProvider(new MenuProvider() {
//            @Override
//            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
//                menuInflater.inflate(R.menu.reminders_options_menu, menu);
//            }
//
//            @Override
//            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
//                return false;
//            }
//        });

//        makeShowcase(getContext(), view.findViewById(R.id.reminders_group_recyclerview), "Groups", "Here you can choose the groups you want to see reminders for.");
//        makeShowcase(getContext(), view.findViewById(R.id.reminders_reminder_recyclerview), "Reminders", "Here you can either click on the reminders to edit them or click the circle to complete them");

//        TapTargetSequence sequence = new TapTargetSequence(getActivity())
//                .targets(
//                    TapTarget.forView(groupItemsRecyclerView, "Hi").cancelable(false),
//                    TapTarget.forView(reminderItemsRecyclerView, "BYE").cancelable(false)
//                )
//                .listener(new TapTargetSequence.Listener(){
//                    @Override
//                    public void onSequenceFinish() {
//
//                    }
//
//                    @Override
//                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
//
//                    }
//
//                    @Override
//                    public void onSequenceCanceled(TapTarget lastTarget) {
//                        ;
//                    }
//                });
//        sequence.start();


        ShowcaseConfig config = new ShowcaseConfig();

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this.getActivity(), "reminders_home_page_showcase");
        sequence.setConfig(config);

        sequence.addSequenceItem(groupItemsRecyclerView, "IS A BUTTON", "SOMTHING");
        sequence.addSequenceItem(reminderItemsRecyclerView, "HELLO", "BINGUS");

        sequence.start();
    }

    @Override
    public void startShowcase() {
        ;
    }

}
