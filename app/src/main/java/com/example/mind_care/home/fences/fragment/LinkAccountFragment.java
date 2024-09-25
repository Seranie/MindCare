package com.example.mind_care.home.fences.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.mind_care.R;
import com.example.mind_care.home.fences.viewmodel.UserUidViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class LinkAccountFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.link_account_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText uidField = view.findViewById(R.id.link_account_edit_text);
        Button confirmButton = view.findViewById(R.id.link_account_confirm_button);
        Button cancelButton = view.findViewById(R.id.link_account_cancel_button);
        String userInput = uidField.getText().toString();

        UserUidViewModel userUidViewModel = new ViewModelProvider(this).get(UserUidViewModel.class);

        confirmButton.setOnClickListener(v -> {
            userUidViewModel.validateUid(userInput);
            userUidViewModel.getIsValidLiveData().observe(getViewLifecycleOwner(), (isValid) -> {
                if(isValid){
                    userUidViewModel.setLinkedAccount(userInput);
                    Navigation.findNavController(v).popBackStack();
                }
                else{
                    uidField.setError("Invalid UID");
                }
            });
        });

        cancelButton.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }
}
