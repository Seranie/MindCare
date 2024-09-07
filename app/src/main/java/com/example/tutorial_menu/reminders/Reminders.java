package com.example.tutorial_menu.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_menu.R;

import java.util.ArrayList;
import java.util.List;

public class Reminders extends Fragment {
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

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.reminders_options_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });


    }
}
