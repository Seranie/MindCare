package com.example.tutorial_menu;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tutorial_menu.reminders.Reminders;
import com.google.android.material.navigation.NavigationView;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public class ShowcaseActivity extends AppCompatActivity {
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcase_activity);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.showcase_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.showcase_toolbar);
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.showcase_fragment_container);
        navController = navHostFragment.getNavController();
        DrawerLayout drawerLayout = findViewById(R.id.showcase_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(drawerLayout).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController((NavigationView) findViewById(R.id.showcase_navigation_view), navController);



        int fragmentId = getIntent().getExtras().getInt("fragment_id");
        switch (fragmentId){
            case 0:
                navController.navigate(R.id.reminders);
        }

    }

    @Override
    public boolean onSupportNavigateUp(){
        //Overrides default navigation button behavior and delegate to navController instead, else if navigateUp fails, use parent default method.
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }


//    public static void makeShowcase(Context context, View view){
//        new GuideView.Builder(context)
//                .setTitle("Guide Title Text")
//                .setContentText("Guide Description Text\n .....Guide Description Text\n .....Guide Description Text .....")
//                .setGravity(Gravity.auto) //optional
//                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
//                .setTargetView(view)
//                .setContentTextSize(12)//optional
//                .setTitleTextSize(14)//optional
//                .build()
//                .show();
//    }
//
//    private void startShowcaseOnFragment(int fragmentId){
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.showcase_fragment_container);
//        if (fragment != null){
//            Fragment showcaseFragment = fragment.getChildFragmentManager().findFragmentById(fragmentId);
//            if (showcaseFragment != null){
//                showcaseFragment.onShowcaseViewCreated();
//            }
//        }
//
//    }
}
