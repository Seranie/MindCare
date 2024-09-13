package com.example.mind_care;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ToolsFragmentStateAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> fragmentArray;

    public ToolsFragmentStateAdapter(Fragment fragment, ArrayList<Fragment> fragmentArray){
        super(fragment);
        this.fragmentArray = fragmentArray;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArray.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArray.size();
    }
}
