package com.example.tutorial_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class GuideTab extends Fragment {
    private View view;

    public GuideTab(){
        super(R.layout.guide_tab);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.guide_tab, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        //Create a list to house fragments that individually represent each tab of the guide.
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new GuideDocs());
        fragmentList.add(new GuideShowcase());

        // Creates a new adapter to link the viewpager2 in guide_tab
        GuideFragmentStateAdapter guideFragmentStateAdapter = new GuideFragmentStateAdapter(this, fragmentList);
        ViewPager2 viewPager2 = view.findViewById(R.id.guide_view_pager);
        viewPager2.setAdapter(guideFragmentStateAdapter);


        TabLayout tabLayout = view.findViewById(R.id.guide_tab_layout);

        //Set up titles for each tab
        ArrayList<String> tabTitles = new ArrayList<>();
        tabTitles.add(getString(R.string.guide_tab_introduction));
        tabTitles.add(getString(R.string.guide_tab_showcase));

        //Creates a tablayoutmediator to link the tabs with the VP2
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {tab.setText(tabTitles.get(position));}).attach();

    }
}
