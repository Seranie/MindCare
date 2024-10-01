package com.example.mind_care.home.chat_buddy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.BaseTools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChatBuddyFragment extends BaseTools {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_buddy_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.chat_buddy_recyclerview);
        recyclerView.setAdapter(new ChatBuddyAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO set up recycler view animations for items as well

    }







    @Override
    public void setOnFabClickedDestination(FloatingActionButton fab, NavController navController) {
        // Open keyboard for user to type input.
    }

    @Override
    public void setFabImage(FloatingActionButton fab) {
        // Set the image for the FAB
        fab.setImageResource(R.drawable.outline_send_10);
    }
}
