package com.example.mind_care;

import static androidx.core.app.AlarmManagerCompat.canScheduleExactAlarms;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.transition.Fade;
import android.util.Log;

import androidx.activity.EdgeToEdge;
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
import android.Manifest.permission;

import com.example.mind_care.showcases.ShowcaseChangeViewModel;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

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


        getWindow().setEnterTransition( new Fade());
        getWindow().setExitTransition( new Fade());

        // Find Navigation Components
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();
        DrawerLayout drawerLayout = findViewById(R.id.main);
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

    }


    @Override
    public boolean onSupportNavigateUp(){
        //Overrides default navigation button behavior and delegate to navController instead, else if navigateUp fails, use parent default method.
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    public void switchToShowcase(int fragmentId){
        Intent intent = new Intent(this, ShowcaseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("fragment_id", fragmentId);
        intent.putExtras(bundle);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @SuppressLint("InlinedApi")
    public void checkAndRequestNotificationPermission(){
        if (ContextCompat.checkSelfPermission(this, permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{permission.POST_NOTIFICATIONS},
                    1);
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
}