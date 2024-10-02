package com.example.mind_care.home.contacts.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ContactsViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public ContactsViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ContactsViewModel.class)) {
            return (T) new ContactsViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
