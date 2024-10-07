package com.example.mind_care.home.contacts.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mind_care.R;
import com.example.mind_care.database.contacts.ContactEntity;
import com.example.mind_care.home.contacts.viewmodel.ContactsViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private final static int REQUEST_CALL_PERMISSION = 1;
    private final Context context;
    private final Fragment fragment;
    private List<ContactEntity> contactsList = new ArrayList<>();
    private final NavController navController;
    private final ContactsViewModel contactsViewModel;
    private SoundPool soundPool;
    private int soundId;

    private final int MAX_STREAMS = 5;
    private final float LEFT_VOLUME = 1.0f;
    private final float RIGHT_VOLUME = 1.0f;
    private final int PRIORITY = 0;
    private final int LOOP = 0;
    private final float RATE = 1.0f;

    public ContactsAdapter(ContactsViewModel contactsViewModel, Context context, Fragment fragment, NavController navController) {
        this.context = context;
        this.fragment = fragment;
        this.navController = navController;
        this.contactsViewModel = contactsViewModel;
        contactsViewModel.getContactsLiveData().observe(fragment, (queryContactsList) -> {
            this.contactsList = queryContactsList;
            notifyDataSetChanged();
        });
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                                .build()
                ).setMaxStreams(MAX_STREAMS)
                .build();
        soundId = soundPool.load(context, R.raw.dial_number2, 1);
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

    public class ContactsViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {
        ShapeableImageView contactImage;
        TextView contactName;
        final long MAKE_CALL_DELAY = 1000;

        @SuppressLint("ClickableViewAccessibility")
        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            contactImage = itemView.findViewById(R.id.contacts_item_image);
            contactName = itemView.findViewById(R.id.contacts_item_textview);
            GestureDetector gesture = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // You can request permission here if needed
                        Toast.makeText(context, context.getString(R.string.dial_permission_not_granted), Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                    }else{
                        soundPool.play(soundId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, LOOP, RATE);
                        new Handler().postDelayed(()->makePhoneCall(), MAKE_CALL_DELAY);
                    }
                    return super.onDoubleTap(e);
                }
            });

            itemView.setOnTouchListener((v, event) -> {
                gesture.onTouchEvent(event);
                return false;
            });
            itemView.setOnLongClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.contacts_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                return true;
            });
        }

        public void makePhoneCall() {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + contactsList.get(getAdapterPosition()).contactNumber));
            context.startActivity(callIntent);
        }


        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            ContactEntity entity = contactsList.get(getBindingAdapterPosition());
            if (itemId == R.id.editContactMenu) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isEdit", true);
                bundle.putString("contactName", entity.contactName);
                bundle.putString("contactNumber", entity.contactNumber);
                bundle.putString("contactImage", entity.imageUri);
                bundle.putInt("contactId", entity.getContactId());
                navController.navigate(R.id.createNewContactFragment, bundle);
                return true;
            } else if (itemId == R.id.deleteContactMenu) {
                new MaterialAlertDialogBuilder(context).setTitle(R.string.delete_contacts_dialog_title).setMessage(R.string.delete_contacts_dialog_message).setPositiveButton(R.string.delete_contacts_dialog_yes, (dialog, which) -> {
                    contactsViewModel.deleteContact(entity.getContactId());
                }).setNegativeButton(R.string.delete_contacts_dialog_no, (dialog, which) -> dialog.dismiss()).show();
                return true;
            }
            return false;


        }
    }
}

