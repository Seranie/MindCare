package com.example.mind_care;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mind_care.showcases.ShowcaseChangeViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private final String image_file_name = "user_avatar_image.png";
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private ImageView navHeaderImage;
    private FirebaseUser user;
    private DrawerLayout drawerLayout;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
        if (uri != null) {
            saveImageToInternalStorage(uri);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Make navhostfragment the size of the coordinatorlayout - actionbarsize.
//        CoordinatorLayout coordinator = findViewById(R.id.main_activity_coordinatorlayout);
//        coordinator.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                coordinator.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                int coordinatorHeight = coordinator.getHeight();
//                Log.i("INFO", String.valueOf(coordinatorHeight));
//                int appbarHeight = findViewById(R.id.appbar).getHeight();
//                Log.i("INFO", String.valueOf(appbarHeight));
//                FrameLayout frameLayout = findViewById(R.id.main_activity_frame_layout);
//                ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
//                layoutParams.height = coordinatorHeight - appbarHeight;
//                frameLayout.setLayoutParams(layoutParams);
//                frameLayout.requestLayout();
//            }
//        });


        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());

        // Find Navigation Components
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();
        drawerLayout = findViewById(R.id.main);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        //Set toolbar as default actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set up NavigationUI
        //Sets up home page and guide page as top-level destinations
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.guideTabFragment, R.id.homeTabFragment).setOpenableLayout(drawerLayout).build();

        //Sets up toolbar navController such that title changes accordingly
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //Sets up navigationView with navController to listen to menu events
        //Menu item id's are linked to destination id's which serve as the basis for navigating click events in drawer
        NavigationUI.setupWithNavController(navigationView, navController);


        //Create viewmodel and pass to showcase tab's recyclerview items TODO
        ShowcaseChangeViewModel model = new ViewModelProvider(this).get(ShowcaseChangeViewModel.class);
        model.getShowcaseChangeId().observe(this, id -> {
            switchToShowcase(id);
        });

        //Request notification permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkAndRequestNotificationPermission();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkAndRequestExactAlarmPermission();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission.READ_EXTERNAL_STORAGE},
                    3);
        }

        //Set up nav_header with relevant information
        View headerView = navigationView.getHeaderView(0);
        navHeaderImage = headerView.findViewById(R.id.nav_header_image);
        navHeaderImage.setOnClickListener(v -> pickMediaLauncher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build()));
        loadImageStored();

        TextView username = headerView.findViewById(R.id.nav_header_username);
        user = FirebaseAuth.getInstance().getCurrentUser();
        try {
            String usernameString = getString(R.string.nav_header_username, user.getEmail());
            username.setText(usernameString);
        } catch (NullPointerException e) {
            String usernameString = getString(R.string.nav_header_username, user.getPhoneNumber());
            username.setText(usernameString);
        }

        TextView uidTextView = headerView.findViewById(R.id.nav_header_uid);
        setUidText(uidTextView);
    }

    private void setUidText(TextView textView){
        String uid = user.getUid();
        String uidText = getString(R.string.nav_header_uid, uid);

        SpannableString spannableString = new SpannableString(uidText);

        int uidStartIndex = uidText.indexOf(uid);
        int uidEndIndex = uidStartIndex + uid.length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Copy the UID to clipboard when clicked
                ClipboardManager clipboard = (ClipboardManager) widget.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("UID", uid);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(widget.getContext(), "UID copied!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
//                ds.setUnderlineText(false); // disable underline for the UID part
            }
        };

        spannableString.setSpan(clickableSpan, uidStartIndex, uidEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onSupportNavigateUp() {
        //Overrides default navigation button behavior and delegate to navController instead, else if navigateUp fails, use parent default method.
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    public void switchToShowcase(int fragmentId) {
        Intent intent = new Intent(this, ShowcaseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("fragment_id", fragmentId);
        intent.putExtras(bundle);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @SuppressLint("InlinedApi")
    public void checkAndRequestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                // The user checked "Don't ask again". You should handle this case accordingly.
                Toast.makeText(this, "Please enable notifications permission", Toast.LENGTH_SHORT).show();
            }

            // Request permission
            ActivityCompat.requestPermissions(this, new String[]{permission.POST_NOTIFICATIONS}, 1);
        }
    }

    @SuppressLint("NewApi")
    private void checkAndRequestExactAlarmPermission() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (!alarmManager.canScheduleExactAlarms()) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(intent);
        }
    }

    private void loadImageStored() {
        File directory = getFilesDir();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().equals(image_file_name)) {
                    String imagePath = new File(getFilesDir(), image_file_name).getAbsolutePath();
                    runOnUiThread(()->Glide.with(this).load(new File(imagePath)).error(R.drawable.empty_avatar_placeholder).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(navHeaderImage));
                    return;
                }
            }
        }
        runOnUiThread(()-> Glide.with(this).load(R.drawable.empty_avatar_placeholder).into(navHeaderImage));
    }

    private void saveImageToInternalStorage(Uri uri) {
        new Thread(() -> {
            try { //Write Uri image to app's internal storage
                InputStream inputStream = getContentResolver().openInputStream(uri);
                FileOutputStream fileOutputStream = openFileOutput(image_file_name, Context.MODE_PRIVATE);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
                fileOutputStream.close();
                inputStream.close();
                loadImageStored();
            } catch (FileNotFoundException e) {
                Log.e("Error", "File not found");
            } catch (IOException e) {
                Log.e("Error", "IOException");
            }
        }).start();
    }

    public DrawerLayout getDrawerLayout(){
        return drawerLayout;
    }

}