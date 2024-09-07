package com.example.tutorial_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class GuideTab extends Fragment {
    private View view;
    private TabPositionViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.guide_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        TabLayout tabLayout = view.findViewById(R.id.guide_tab_layout);


        //Create a list to house fragments that individually represent each tab of the guide.
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        GuideDocs guideDocsFragment = new GuideDocs();
        GuideShowcase guideShowcaseFragment = new GuideShowcase();
        fragmentList.add(guideDocsFragment);
        fragmentList.add(guideShowcaseFragment);

        // Creates a new adapter to link the viewpager2 in guide_tab
        ViewPager2 viewPager2 = view.findViewById(R.id.guide_view_pager);
        GuideFragmentStateAdapter guideFragmentStateAdapter = new GuideFragmentStateAdapter(this, fragmentList);
        viewPager2.setAdapter(guideFragmentStateAdapter);

        //Set up titles for each tab
        ArrayList<String> tabTitles = new ArrayList<>();
        tabTitles.add(getString(R.string.guide_tab_introduction));
        tabTitles.add(getString(R.string.guide_tab_showcase));

        //Creates a tablayoutmediator to link the tabs with the VP2
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {tab.setText(tabTitles.get(position));}).attach();

        //Create viewmodel for tab switching use in recyclerviews
        viewModel = new ViewModelProvider(this).get(TabPositionViewModel.class);

        //Set up livedata within viewmodel to observe for click events in showcase view button
        viewModel.getTabPositionLiveData().observe(getViewLifecycleOwner(), tabPosition ->{
            //If a new tab position object is observed, switch tab to GuideDocs tab
            viewPager2.setCurrentItem(fragmentList.indexOf(guideDocsFragment), true);
        });
    }

}
