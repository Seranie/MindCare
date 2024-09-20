package com.example.mind_care;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mind_care.home.BaseTools;
import com.example.mind_care.home.ViewpagerNavigationMediator;
import com.example.mind_care.home.reminders.fragment.Reminders;
import com.example.mind_care.placeholder_fragments.PlaceholderFragment;
import com.example.mind_care.showcases.ToolsFragmentStateAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements BaseTools.FabListener {
    private NavController navController;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Fragment list to hold tool fragments
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        BaseTools remindersFragment = new Reminders();
        PlaceholderFragment contactsFragment = new PlaceholderFragment().setText("Contacts");
        PlaceholderFragment fencesFragment = new PlaceholderFragment().setText("Fences");
        PlaceholderFragment chatBuddyFragment = new PlaceholderFragment().setText("Chat Buddy");


        //Add fragments to array
        fragmentList.add(remindersFragment);
        fragmentList.add(contactsFragment);
        fragmentList.add(fencesFragment);
        fragmentList.add(chatBuddyFragment);

        //Get viewpager2 and set adapter
        ViewPager2 viewPager = view.findViewById(R.id.home_view_pager);
        ToolsFragmentStateAdapter toolsFragmentStateAdapter = new ToolsFragmentStateAdapter(this, fragmentList);
        viewPager.setAdapter(toolsFragmentStateAdapter);

        BottomNavigationView bottomNavigation = view.findViewById(R.id.home_bottom_navigation_view);

        // Set up viewpager2 swipe to change bottom nav selection
        new ViewpagerNavigationMediator(bottomNavigation, viewPager).attach();

        fab = view.findViewById(R.id.home_fab);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Fix appbar being collapsed even after returning from fragment.
        AppBarLayout layout = getActivity().findViewById(R.id.appbar);
        layout.setExpanded(true);
    }


    @Override
    public void setOnFabClickedDestination(int resId) {
        fab.setOnClickListener(view -> navController.navigate(resId));
    }

    public void setFabImage(int imageId) {
        fab.setImageResource(imageId);
    }
}