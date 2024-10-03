package com.example.mind_care.home.reminders.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.BaseTools;
import com.example.mind_care.home.reminders.adapter.RemindersGroupAdapter;
import com.example.mind_care.home.reminders.adapter.RemindersReminderAdapter;
import com.example.mind_care.home.reminders.model.RemindersGroupItem;
import com.example.mind_care.home.reminders.model.RemindersReminderItem;
import com.example.mind_care.home.reminders.viewModel.ReminderGroupViewModel;
import com.example.mind_care.home.reminders.viewModel.ReminderItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Reminders extends BaseTools implements RemindersGroupAdapter.OnGroupItemClickListener {
    RecyclerView reminderItemsRecyclerView;
    private ReminderGroupViewModel groupViewModel;
    private ReminderItemViewModel reminderItemViewModel;

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
        reminderItemsRecyclerView = view.findViewById(R.id.reminders_items_recyclerview);

        //get group and reminders viewmodel associated with this user
        groupViewModel = new ViewModelProvider(requireActivity()).get(ReminderGroupViewModel.class);
        reminderItemViewModel = new ViewModelProvider(requireActivity()).get(ReminderItemViewModel.class);

        RemindersGroupAdapter groupAdapter = new RemindersGroupAdapter(groupViewModel, getContext(), this);
        groupItemsRecyclerView.setAdapter(groupAdapter);
        groupItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //adapter observes if fragment pauses, causing all cureently selected group to be deselected
        getLifecycle().addObserver(groupAdapter);

        reminderItemsRecyclerView.setAdapter(new RemindersReminderAdapter(groupViewModel, this, requireActivity(), getContext()));
        reminderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        groupViewModel.getGroupListFromRepository();




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
    public void onItemClick(String groupId) {
        //Get reminders from the group the user clicked on and update UI state accordingly
        groupViewModel.getRemindersFromGroup(groupId);
    }

    @Override
    public void setOnFabClickedDestination(FloatingActionButton fab, NavController navController) {
        fab.setOnClickListener(v -> {navController.navigate(R.id.createNewReminderFragment);});
    }

    @Override
    public void setFabImage(FloatingActionButton fab) {
        fab.setImageResource(R.drawable.reminders_icon);
    }
}
