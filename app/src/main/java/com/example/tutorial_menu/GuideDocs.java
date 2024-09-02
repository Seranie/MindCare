package com.example.tutorial_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GuideDocs extends Fragment {
//    public GuideIntroduction(){
//        super(R.layout.guide_introduction);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guide_docs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        //Create list of guide doc cards and add them to list to pass to adapter.
        List<GuideDocsCard> cardList = new ArrayList<>();
        cardList.add(new GuideDocsCard(
                R.string.docs_card1_title,
                R.string.docs_card1_description
        ));
        cardList.add(new GuideDocsCard(
                R.string.docs_card2_title,
                R.string.docs_card2_description
        ));
        cardList.add(new GuideDocsCard(
                R.string.docs_card3_title,
                R.string.docs_card3_description
        ));
        cardList.add(new GuideDocsCard(
                R.string.docs_card4_title,
                R.string.docs_card4_description
        ));

        //Create adapter
        //Set recyclerview layoutmanager and adapter.
        RecyclerView recyclerView = view.findViewById(R.id.guide_docs_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GuideDocsAdapter guideDocsAdapter = new GuideDocsAdapter(cardList);
        recyclerView.setAdapter(guideDocsAdapter);
    }
}
