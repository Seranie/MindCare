package com.example.mind_care.guide.guide_showcase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.showcases.ShowcaseChangeViewModel;
import com.example.mind_care.guide.TabPositionViewModel;

import java.util.ArrayList;
import java.util.List;

public class GuideShowcase extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guide_showcase, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Create a list of Card objects, each card will represent each tool available in the app
        List<GuideShowcaseCard> cardList = new ArrayList<>();
        cardList.add(new GuideShowcaseCard(
                R.drawable.reminders_card_image,
                R.string.showcase_card1_title,
                R.string.showcase_card1_subtitle,
                R.string.showcase_card1_description
        ));
        cardList.add(new GuideShowcaseCard(
                R.drawable.contacts_card_image,
                R.string.showcase_card2_title,
                R.string.showcase_card2_subtitle,
                R.string.showcase_card2_description
        ));
        cardList.add(new GuideShowcaseCard(
                R.drawable.fences_card_image,
                R.string.showcase_card3_title,
                R.string.showcase_card3_subtitle,
                R.string.showcase_card3_description
        ));
        cardList.add(new GuideShowcaseCard(
                R.drawable.chatbuddy_card_image,
                R.string.showcase_card4_title,
                R.string.showcase_card4_subtitle,
                R.string.showcase_card4_description
        ));

        //Set up recyclerview with the adapter and use linearlayout for scrolling

        ShowcaseChangeViewModel showcaseViewModel = new ViewModelProvider(getParentFragment().getActivity()).get(ShowcaseChangeViewModel.class);
        TabPositionViewModel tabPositionViewModel = new ViewModelProvider(getParentFragment()).get(TabPositionViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.guide_showcase_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GuideShowcaseAdapter guideShowcaseAdapter = new GuideShowcaseAdapter(cardList, tabPositionViewModel, showcaseViewModel, getContext());
        recyclerView.setAdapter(guideShowcaseAdapter);
    }
}
