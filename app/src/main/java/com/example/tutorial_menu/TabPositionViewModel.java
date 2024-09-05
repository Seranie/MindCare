package com.example.tutorial_menu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TabPositionViewModel extends ViewModel {
    private final MutableLiveData<Integer> selectedTab = new MutableLiveData<Integer>();

    public MutableLiveData<Integer> getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(int tabPosition){
        selectedTab.setValue(tabPosition);
    }
}
