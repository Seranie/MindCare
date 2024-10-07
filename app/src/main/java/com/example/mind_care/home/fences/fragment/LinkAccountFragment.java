package com.example.mind_care.home.fences.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LinkAccountFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.link_account_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputLayout textInputLayout = view.findViewById(R.id.link_account_text_input_layout);
        TextInputEditText uidField = view.findViewById(R.id.link_account_edit_text);
        Button confirmButton = view.findViewById(R.id.link_account_confirm_button);
        Button cancelButton = view.findViewById(R.id.link_account_cancel_button);



        UserUidViewModel userUidViewModel = new ViewModelProvider(this).get(UserUidViewModel.class);

        confirmButton.setOnClickListener(v -> {
            String userInput = uidField.getText().toString();
            if(userInput.isEmpty()){
                textInputLayout.setError("UID cannot be empty");
            }else if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(userInput)){
                textInputLayout.setError("UID must be from another user");
            }
            else{
                userUidViewModel.validateUid(userInput);
                userUidViewModel.getIsValidLiveData().observe(getViewLifecycleOwner(), (isValid) -> {
                    if(isValid){
                        final String userInputForObserver = uidField.getText().toString();
                        userUidViewModel.setLinkedAccount(userInputForObserver);
                        Navigation.findNavController(v).popBackStack();
                    }
                    else{
                        textInputLayout.setError("Invalid UID");
                    }
                });
            }

        });

        cancelButton.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }
}
