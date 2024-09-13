package com.example.mind_care;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mind_care.placeholder_fragments.PlaceholderFragment;

import java.util.ArrayList;


//TODO change to viewpager
public class Home extends Fragment{

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
        PlaceholderFragment remindersFragment = new PlaceholderFragment().setText("Reminders");
        PlaceholderFragment contactsFragment = new PlaceholderFragment().setText("Contacts");
        PlaceholderFragment fencesFragment = new PlaceholderFragment().setText("Fences");
        PlaceholderFragment chatBuddyFragment = new PlaceholderFragment().setText("Chat Buddy");


        //Add fragments to array
        fragmentList.add(remindersFragment);
        fragmentList.add(contactsFragment);
        fragmentList.add(fencesFragment);
        fragmentList.add(chatBuddyFragment);

        //Get viewpager2 and set adapter
        ViewPager2 viewPager = view.findViewById(R.id.tools_view_pager);
        ToolsFragmentStateAdapter toolsFragmentStateAdapter = new ToolsFragmentStateAdapter(this, fragmentList);
        viewPager.setAdapter(toolsFragmentStateAdapter);


    }

}
