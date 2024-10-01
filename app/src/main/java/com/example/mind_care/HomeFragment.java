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
import com.example.mind_care.home.chat_buddy.fragment.ChatBuddyFragment;
import com.example.mind_care.home.contacts.fragments.ContactsFragment;
import com.example.mind_care.home.fences.fragment.FencesFragment;
import com.example.mind_care.home.reminders.fragment.Reminders;
import com.example.mind_care.placeholder_fragments.PlaceholderFragment;
import com.example.mind_care.showcases.ToolsFragmentStateAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class HomeFragment extends Fragment{
    private NavController navController;
    private FloatingActionButton fab;
    private BottomAppBar bottomAppBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        //Fragment list to hold tool fragments
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        BaseTools remindersFragment = new Reminders();
        BaseTools contactsFragment = new ContactsFragment();
        BaseTools fencesFragment = new FencesFragment();
        ChatBuddyFragment chatBuddyFragment = new ChatBuddyFragment();


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
        bottomAppBar = view.findViewById(R.id.bottom_app_bar);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (fragmentList.get(position) instanceof BaseTools) {
                    BaseTools currentFragment = (BaseTools) fragmentList.get(position);
                    currentFragment.setFabImage(fab);
                    currentFragment.setOnFabClickedDestination(fab, navController);
                }
                if(fragmentList.get(position) instanceof ChatBuddyFragment){
                    //Disable bottom app bar in chatbuddy so that editext can show
                    bottomAppBar.setVisibility(View.GONE);
                }else{
                    bottomAppBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //Fix appbar being collapsed even after returning from fragment.
        AppBarLayout layout = getActivity().findViewById(R.id.appbar);
        layout.setExpanded(true);
    }

}