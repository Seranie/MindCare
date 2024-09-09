package com.example.tutorial_menu;

import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tutorial_menu.reminders.Reminders;
import com.example.tutorial_menu.showcases.ChatbuddyHomePageShowcase;
import com.example.tutorial_menu.showcases.ContactsHomePageShowcase;
import com.example.tutorial_menu.showcases.FencesHomePageShowcase;
import com.example.tutorial_menu.showcases.RemindersHomePageShowCase;
import com.example.tutorial_menu.showcases.ShowcaseFragment;
import com.google.android.material.navigation.NavigationView;

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
        getWindow().setEnterTransition( new Fade());
        getWindow().setExitTransition( new Fade());

//        Toolbar toolbar = findViewById(R.id.showcase_toolbar);
//        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.showcase_fragment_container);
        navController = navHostFragment.getNavController();
        DrawerLayout drawerLayout = findViewById(R.id.showcase_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(drawerLayout).build();

//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController((NavigationView) findViewById(R.id.showcase_navigation_view), navController);


        FragmentManager fragmentManager = getSupportFragmentManager();
        int fragmentId = getIntent().getExtras().getInt("fragment_id");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment currentFragment = null;

        switch (fragmentId){
            case 0:
                currentFragment = new RemindersHomePageShowCase();
                break;
            case 1:
                currentFragment = new ContactsHomePageShowcase();
                break;
            case 2:
                currentFragment = new FencesHomePageShowcase();
                break;
            case 3:
                currentFragment = new ChatbuddyHomePageShowcase();
                break;
        }
        transaction.replace(R.id.showcase_fragment_container, currentFragment).commit();



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
//    private void startShowcaseOnFragment(NavHostFragment fragment, int fragmentId){
//        if (fragment != null){
//            ShowcaseFragment showcaseFragment = (ShowcaseFragment) fragment.getChildFragmentManager().getPrimaryNavigationFragment();
//            Log.i("INFO",String.valueOf(showcaseFragment));
//            if (showcaseFragment != null){
//                showcaseFragment.startShowcase();
//                Log.i("INFO","HI");
//            }
//        }
}
