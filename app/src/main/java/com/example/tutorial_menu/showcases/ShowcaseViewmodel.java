package com.example.tutorial_menu.showcases;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tutorial_menu.placeholder_fragments.PlaceholderFragment;


// Viewmodel for handling changes of the current fragment in showcase
public class ShowcaseViewmodel extends ViewModel {
    private MutableLiveData<Fragment> mCurrentFragment = new MutableLiveData<>();

    public ShowcaseViewmodel(){
        mCurrentFragment.setValue(new PlaceholderFragment());
    }

    public void setCurrentFragment(Fragment fragment){
        mCurrentFragment.setValue(fragment);
    }

    public MutableLiveData<Fragment> getCurrentFragment(){
        return mCurrentFragment;
    }
}
