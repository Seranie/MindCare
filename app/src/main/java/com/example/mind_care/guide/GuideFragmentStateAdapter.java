package com.example.mind_care.guide;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class GuideFragmentStateAdapter extends FragmentStateAdapter {
    // Adapter class to be used by the guide page for viewpager.
    private final ArrayList<Fragment> fragmentArray;

    public GuideFragmentStateAdapter(Fragment fragment, ArrayList<Fragment> fragmentArray){
        super(fragment);
        this.fragmentArray = fragmentArray;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        return fragmentArray.get(position);
    }

    @Override
    public int getItemCount(){
        return fragmentArray.size();
    }

}
