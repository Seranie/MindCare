package com.example.tutorial_menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

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
        Log.i("INFO1", String.valueOf(model));
        model.getShowcaseChangeId().observe(this, id -> {
            switchToShowcase(id);
        });
        //Switch based on viewModel data


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
        startActivity(intent);
    }

}