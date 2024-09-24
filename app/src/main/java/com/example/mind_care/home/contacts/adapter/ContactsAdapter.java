package com.example.mind_care.home.contacts.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mind_care.R;
import com.example.mind_care.database.ContactEntity;
import com.example.mind_care.home.contacts.viewmodel.ContactsViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private List<ContactEntity> contactsList = new ArrayList<>();
    private Context context;
    private Fragment fragment;
    private final static int REQUEST_CALL_PERMISSION = 1;
    
    public ContactsAdapter(ContactsViewModel contactsViewModel, Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        contactsViewModel.getContactsLiveData().observe(fragment, (queryContactsList) -> {
            this.contactsList = queryContactsList;
            notifyDataSetChanged();
        });
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
        ShapeableImageView contactImage;
        TextView contactName;

        @SuppressLint("ClickableViewAccessibility")
        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            contactImage = itemView.findViewById(R.id.contacts_item_image);
            contactName = itemView.findViewById(R.id.contacts_item_textview);
            GestureDetector gesture = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDoubleTap(MotionEvent e){
                    Log.i("INFO","CALL");
                    makePhoneCall();
                    return super.onDoubleTap(e);
                }
            });

            itemView.setOnTouchListener((v, event) -> {
                Log.i("TouchEvent", "Event action: " + event.getAction());
                gesture.onTouchEvent(event);
                return true;
            });


        }

        public void makePhoneCall(){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + contactsList.get(getAdapterPosition()).contactNumber));

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // You can request permission here if needed
                Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            }
            else {context.startActivity(callIntent);}
        }
    }
}
