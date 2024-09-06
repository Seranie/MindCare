package com.example.tutorial_menu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TabPositionViewModel extends ViewModel {
    //A viewmodel for determining tab position changes in GuideTab
    private final MutableLiveData<TabPositionModel> TabPositionLiveData = new MutableLiveData<>();


    public MutableLiveData<TabPositionModel> getTabPositionLiveData() {
        return TabPositionLiveData;
    }

    public void setTabPositionLiveData(int tabPosition){
        TabPositionLiveData.setValue(new TabPositionModel(tabPosition));
    }
}
