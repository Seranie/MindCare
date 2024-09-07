package com.example.tutorial_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;



//TODO change to appropriate tool instead of a generic home
public class Home extends Fragment{
    private List<RemindersGroupItem> remindersGroupItems;
    RecyclerView reminderItemsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView groupItemsRecyclerView = view.findViewById(R.id.reminders_group_recyclerview);
        RecyclerView reminderItemsRecyclerView = view.findViewById(R.id.reminders_reminder_recyclerview);

        //TODO will eventually change to using data dynamically called from user account's data in database
        remindersGroupItems = new ArrayList<>();
        RemindersReminderItem remindersReminderItem = new RemindersReminderItem();
        remindersReminderItem.setTitle("Do something");
        remindersReminderItem.setNote("On someday");
        //TODO groups will also have data dynamically called from database
        remindersGroupItems.add(new RemindersGroupItem(
                R.drawable.chat_buddy_icon,
                R.string.reminders_group_name
        ));

        List<RemindersReminderItem> remindersReminderItems = new ArrayList<>();
        remindersReminderItems.add(remindersReminderItem);

        groupItemsRecyclerView.setAdapter(new RemindersGroupAdapter(remindersGroupItems));
        groupItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        RemindersReminderAdapter remindersReminderAdapter = new RemindersReminderAdapter(remindersReminderItems);
        reminderItemsRecyclerView.setAdapter(remindersReminderAdapter);
        reminderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
