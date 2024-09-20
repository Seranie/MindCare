package com.example.mind_care.home.contacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mind_care.R;
import com.example.mind_care.database.ContactEntity;
import com.example.mind_care.home.contacts.viewmodel.ContactsViewModel;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private List<ContactEntity> contactsList;
    private Context context;
    
    public ContactsAdapter(ContactsViewModel contactsViewModel, Context context) {
        this.context = context;
        contactsViewModel.getContactsLiveData().observe(this, (queryContactsList) -> {
            this.contactsList = queryContactsList;
        });
        contactsViewModel.getAllContacts();
    }

    @NonNull
    @Override
    public ContactsAdapter.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item_layout, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ContactsViewHolder holder, int position) {
        ContactEntity contact = contactsList.get(position);
        holder.contactName.setText(contact.contactName);
        Glide.with(context).load(contact.getImageUri()).error(R.drawable.outline_image_not_supported_24).into(holder.contactImage);
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        ImageButton contactImage;
        TextView contactName;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            contactImage = itemView.findViewById(R.id.contacts_item_image);
            contactName = itemView.findViewById(R.id.contacts_item_textview);
        }
    }
}
