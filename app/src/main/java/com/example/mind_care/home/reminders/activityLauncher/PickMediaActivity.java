//package com.example.mind_care.home.reminders.activityLauncher;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.PickVisualMediaRequest;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class PickMediaActivity extends AppCompatActivity {
//
//    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
//            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
//                // Callback is invoked after the user selects a media item or closes the
//                // photo picker.
//                if (uri != null) {
//                    Log.d("PhotoPicker", "Selected URI: " + uri);
//                    // Do something with the selected media
//
//                } else {
//                    Log.d("PhotoPicker", "No media selected");
//                }
//            });
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        pickMedia.launch(new PickVisualMediaRequest.Builder()
//                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
//                .build());
//    }
//}
