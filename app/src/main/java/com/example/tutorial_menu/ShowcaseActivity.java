package com.example.tutorial_menu;

import android.os.Bundle;
import android.transition.Fade;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tutorial_menu.showcases.ShowcaseViewmodel;
import com.example.tutorial_menu.showcases.chatbuddy.ChatbuddyHomePageShowcase;
import com.example.tutorial_menu.showcases.contacts.ContactsHomePageShowcase;
import com.example.tutorial_menu.showcases.fences.FencesHomePageShowcase;
import com.example.tutorial_menu.showcases.reminders.RemindersHomePageShowcase;

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

        //set up navigationUI
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.showcase_fragment_container);
        navController = navHostFragment.getNavController();
        DrawerLayout drawerLayout = findViewById(R.id.showcase_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(drawerLayout).build();



        FragmentManager fragmentManager = getSupportFragmentManager();
        ShowcaseViewmodel viewModel = new ViewModelProvider(this).get(ShowcaseViewmodel.class);

        //Swap initial starting fragment based on button that triggered it
        int fragmentId = getIntent().getExtras().getInt("fragment_id");
        switch (fragmentId){
            case 0:
                viewModel.setCurrentFragment(new RemindersHomePageShowcase());
                break;
            case 1:
                viewModel.setCurrentFragment(new ContactsHomePageShowcase());
                break;
            case 2:
                viewModel.setCurrentFragment(new FencesHomePageShowcase());
                break;
            case 3:
                viewModel.setCurrentFragment(new ChatbuddyHomePageShowcase());
                break;
        }

        // Setup viewmodel to observe of fragment changes and swap container accordingly
        viewModel.getCurrentFragment().observe(this, fragment -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.showcase_fragment_container, fragment);
            fragmentTransaction.commit();
        });




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
