package com.example.tutorial_menu;

public class GuideShowcaseCard {
    // A class representing a card's data in the guide showcase tab
    private int mCardImage;
    private int mCardTitle;
    private int mCardSubtitle;
    private int mCardDescription;

    public GuideShowcaseCard(int cardImage, int cardTitle, int cardSubtitle, int cardDescription){
        this.mCardImage = cardImage;
        this.mCardTitle = cardTitle;
        this.mCardSubtitle = cardSubtitle;
        this.mCardDescription = cardDescription;
    }

    public int getCardImage() {
        return mCardImage;
    }

    public int getCardTitle() {
        return mCardTitle;
    }

    public int getCardSubtitle() {
        return mCardSubtitle;
    }

    public int getCardDescription() {
        return mCardDescription;
    }
}
