package com.example.tutorial_menu;

public class TabPositionModel {
    //A class to store tab position data
    private int mTabPosition;

    public TabPositionModel(int tabPosition){
        this.mTabPosition = tabPosition;
    }

    public int getTabPosition() {
        return mTabPosition;
    }

    public void setTabPosition(int mTabPosition) {
        this.mTabPosition = mTabPosition;
    }
}
