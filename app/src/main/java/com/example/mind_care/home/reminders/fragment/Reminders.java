package com.example.mind_care.home.reminders.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.BaseTools;
import com.example.mind_care.home.reminders.adapter.RemindersGroupAdapter;
import com.example.mind_care.home.reminders.adapter.RemindersReminderAdapter;
import com.example.mind_care.home.reminders.model.RemindersGroupItem;
import com.example.mind_care.home.reminders.model.RemindersReminderItem;

import java.util.ArrayList;
import java.util.List;

public class Reminders extends BaseTools {
    RecyclerView reminderItemsRecyclerView;
    FabListener fabListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reminders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabListener = (FabListener) getParentFragment();

        //Get recycler views
        RecyclerView groupItemsRecyclerView = view.findViewById(R.id.reminders_group_recyclerview);
        RecyclerView reminderItemsRecyclerView = view.findViewById(R.id.reminders_items_recyclerview);

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

    }

    @Override
    public void onResume() {
        super.onResume();
        fabListener.setFabImage(R.drawable.reminders_icon);
        //on click navigate to some other destination
        fabListener.setOnFabClickedDestination(R.id.createNewReminderFragment);
    }
}
