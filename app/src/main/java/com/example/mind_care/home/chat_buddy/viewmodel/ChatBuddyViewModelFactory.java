package com.example.mind_care.home.chat_buddy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatBuddyViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public ChatBuddyViewModelFactory(Application application) {
        this.application = application;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ChatBuddyViewModel.class)){
            return (T) new ChatBuddyViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
