package com.example.mind_care.home.chat_buddy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.BaseTools;
import com.example.mind_care.home.chat_buddy.adapter.ChatBuddyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ChatBuddyFragment extends BaseTools {
    private TextInputLayout textInputLayout;
    private TextInputEditText editText;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_buddy_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        textInputLayout = view.findViewById(R.id.chat_buddy_text_input_layout);
        editText = view.findViewById(R.id.chat_buddy_edit_text);

        RecyclerView recyclerView = view.findViewById(R.id.chat_buddy_recyclerview);
        recyclerView.setAdapter(new ChatBuddyAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO set up recycler view animations for items as well


    }


    @Override
    public void setOnFabClickedDestination(FloatingActionButton fab, NavController navController) {
        ;
    }

    @Override
    public void setFabImage(FloatingActionButton fab) {
//        // Set the image for the FAB
//        fab.setImageResource(R.drawable.outline_send_10);
    }

    private void showKeyboard(View view) {
        //View is already created and ready
        textInputLayout.setVisibility(View.VISIBLE);
        editText.requestFocus();
        InputMethodManager imm = requireActivity().getSystemService(InputMethodManager.class);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        textInputLayout.setEndIconOnClickListener(v -> {

        });

    }



}
