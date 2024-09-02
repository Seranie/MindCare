package com.example.tutorial_menu;

public class GuideDocsCard {
    private int mTitle;
    private int mDescription;

    public GuideDocsCard(int title, int description){
        this.mTitle = title;
        this.mDescription = description;
    }

    public int getTitle() {
        return mTitle;
    }

    public int getDescription() {
        return mDescription;
    }
}
