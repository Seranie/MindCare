package com.example.tutorial_menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GuideDocs extends Fragment {
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guide_docs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Create list of guide doc cards and add them to list to pass to adapter.
        List<GuideDocsCard> cardList = new ArrayList<>();
        cardList.add(new GuideDocsCard(1));
        cardList.add(new GuideDocsCard(2));
        cardList.add(new GuideDocsCard(3));
        cardList.add(new GuideDocsCard(4));

        //Create adapter
        //Set recyclerview layoutmanager and adapter.
        recyclerView = view.findViewById(R.id.guide_docs_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GuideDocsAdapter guideDocsAdapter = new GuideDocsAdapter(cardList);
        recyclerView.setAdapter(guideDocsAdapter);

        TabPositionViewModel tabPositionViewModel = new ViewModelProvider(getParentFragment()).get(TabPositionViewModel.class);
        //Set observe on TabPositionLiveData such that data can be used to determine which titleLayout to flash
        tabPositionViewModel.getTabPositionLiveData().observe(getViewLifecycleOwner(), tabPositionModel -> {
            int tabPosition = tabPositionModel.getTabPosition();

            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //Ensure recyclerview is laid out first
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    recyclerView.smoothScrollToPosition(tabPosition);
                    GuideDocsAdapter.GuideDocsViewHolder viewHolder = (GuideDocsAdapter.GuideDocsViewHolder) recyclerView.findViewHolderForAdapterPosition(tabPosition);

                    //Add delay to ensure button press prompt is visible
                    recyclerView.postDelayed(()->{
                        if (viewHolder != null) {
                            //Flashes the title layout to prompt user to click on it
                            LinearLayout titleLayout = viewHolder.getTitleLayout();
//                            titleLayout.setPressed(true);
//                            titleLayout.setPressed(false);
                            titleLayout.performClick();
                        };
                    }, 200);
                }
            });
        });
    }
}