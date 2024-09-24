package com.example.mind_care.home.reminders.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.mind_care.R;
import com.example.mind_care.home.reminders.viewModel.ReminderGroupViewModel;

import java.io.File;

public class CreateGroupFragment extends Fragment {
    private Uri imageUri;
    private ImageView createGroupImage;
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
        if (uri != null) {
            // Handle the URI of the selected media
            // Do something with the selected media URI
            getContext().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String filepath = getRealPathFromURI(uri);
            imageUri = filepath != null ? Uri.parse(filepath) : uri;
            Glide.with(this).load(new File(imageUri.toString())).error(R.drawable.outline_image_not_supported_24).into(createGroupImage);
        }
    });
    private Button chooseImageButton;
    private EditText groupName;
    private Button confirmButton;
    private Button cancelButton;

    private ReminderGroupViewModel groupViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupViewModel = new ViewModelProvider(requireActivity()).get(ReminderGroupViewModel.class);

        createGroupImage = view.findViewById(R.id.create_group_image);
        chooseImageButton = view.findViewById(R.id.create_group_choose_image_button);
        groupName = view.findViewById(R.id.create_group_text_field);
        confirmButton = view.findViewById(R.id.create_group_confirm);
        cancelButton = view.findViewById(R.id.create_group_cancel);

        if (imageUri == null){
            imageUri = Uri.parse("");
        }

        chooseImageButton.setOnClickListener((View v) -> {
            pickMediaLauncher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
        });

        confirmButton.setOnClickListener((View v) ->{
            groupViewModel.createGroup(imageUri, groupName.getText().toString());
            Navigation.findNavController(v).popBackStack();
        });
        cancelButton.setOnClickListener((View v) ->{
            Navigation.findNavController(v).popBackStack();
        });

    }

    private String getRealPathFromURI(Uri uri) {
        String filePath = null;
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }

        return filePath;
    }
}
