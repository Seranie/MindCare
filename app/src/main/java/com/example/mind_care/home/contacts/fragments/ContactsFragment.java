package com.example.mind_care.home.contacts.fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.BaseTools;
import com.example.mind_care.home.contacts.adapter.ContactsAdapter;
import com.example.mind_care.home.contacts.viewmodel.ContactsViewModel;
import com.example.mind_care.home.contacts.viewmodel.ContactsViewModelFactory;

public class ContactsFragment extends BaseTools {
    private final int GRID_SPAN = 2;
    ContactsViewModel contactsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FabListener fabListener = (FabListener) getParentFragment();
        fabListener.setFabImage(R.drawable.outline_person_add_alt_1_24);
        fabListener.setOnFabClickedDestination(R.id.createNewContactFragment);
        Context context = requireActivity();
        ContactsViewModelFactory factory = new ContactsViewModelFactory((Application) context.getApplicationContext());
        contactsViewModel = new ViewModelProvider((ViewModelStoreOwner) context, factory).get(ContactsViewModel.class);

        RecyclerView contactsRecyclerview = view.findViewById(R.id.contacts_recyclerview);
        contactsRecyclerview.setAdapter(new ContactsAdapter(contactsViewModel, getContext(), this));
        contactsRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), GRID_SPAN));
    }

    @Override
    public void onResume() {
        super.onResume();
        contactsViewModel.getAllContacts();
    }


}
