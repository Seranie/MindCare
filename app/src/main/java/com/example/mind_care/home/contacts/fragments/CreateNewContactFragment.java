package com.example.mind_care.home.contacts.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.mind_care.R;
import com.example.mind_care.home.contacts.viewmodel.ContactsViewModel;
import com.example.mind_care.home.contacts.viewmodel.ContactsViewModelFactory;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

public class CreateNewContactFragment extends Fragment {
    private Uri imageUri;
    ShapeableImageView imageView;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
        if (uri != null) {
            // Handle the URI of the selected media
            // Do something with the selected media URI
            getContext().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imageUri = uri;
            Glide.with(this).load(imageUri).error(R.drawable.outline_image_not_supported_24).into(imageView);
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.create_contact_imageview);
        Button chooseImageButton = view.findViewById(R.id.create_contact_choose_image_button);
        TextInputEditText nameField = view.findViewById(R.id.create_contact_name_field);
        TextInputEditText numberField = view.findViewById(R.id.create_contact_number_field);
        Button confirmButton = view.findViewById(R.id.create_contact_confirm_button);
        Button cancelButton = view.findViewById(R.id.create_contact_cancel_button);


        chooseImageButton.setOnClickListener(v -> {
            pickMediaLauncher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
        });

        ContactsViewModelFactory factory = new ContactsViewModelFactory(requireActivity().getApplication());
        ContactsViewModel contactsViewModel = new ViewModelProvider(requireActivity(), factory).get(ContactsViewModel.class);

        confirmButton.setOnClickListener(v -> {
            if (nameField.getText().toString().isEmpty()){
                nameField.setError("Name field cannot be empty");
            }
            if (numberField.getText().toString().isEmpty()){
                numberField.setError("Number field cannot be empty");
            }
            if (imageUri == null){
               imageUri = Uri.parse("");
            }

            contactsViewModel.insertContact(nameField.getText().toString(), numberField.getText().toString(), imageUri.toString());
            Navigation.findNavController(v).popBackStack();
        });
        cancelButton.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }
}
