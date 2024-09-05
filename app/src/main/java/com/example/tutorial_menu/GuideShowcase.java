package com.example.tutorial_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GuideShowcase extends Fragment {
    private TabPositionViewModel viewModel;

    public GuideShowcase(TabPositionViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.guide_showcase, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        //Create a list of Card objects, each card will represent each tool available in the app
        List<GuideShowcaseCard> cardList = new ArrayList<>();
        //TODO change drawable to tool representations
        cardList.add(new GuideShowcaseCard(
                R.drawable.cat1,
                R.string.showcase_card1_title,
                R.string.showcase_card1_subtitle,
                R.string.showcase_card1_description
        ));
        cardList.add(new GuideShowcaseCard(
                R.drawable.doge,
                R.string.showcase_card2_title,
                R.string.showcase_card2_subtitle,
                R.string.showcase_card2_description
        ));
        cardList.add(new GuideShowcaseCard(
                R.drawable.deer,
                R.string.showcase_card3_title,
                R.string.showcase_card3_subtitle,
                R.string.showcase_card3_description
        ));
        cardList.add(new GuideShowcaseCard(
                R.drawable.seal,
                R.string.showcase_card4_title,
                R.string.showcase_card4_subtitle,
                R.string.showcase_card4_description
        ));

        //Set up recyclerview with the adapter and use linearlayout for scrolling
        //TODO add item animations
        RecyclerView recyclerView = view.findViewById(R.id.guide_showcase_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GuideShowcaseAdapter guideShowcaseAdapter = new GuideShowcaseAdapter(cardList, viewModel);
        recyclerView.setAdapter(guideShowcaseAdapter);
    }
}
