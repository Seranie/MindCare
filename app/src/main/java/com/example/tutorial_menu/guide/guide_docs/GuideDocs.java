package com.example.tutorial_menu.guide.guide_docs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorial_menu.R;
import com.example.tutorial_menu.guide.TabPositionViewModel;

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
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                    //If recycler item is already on screen, simply get the viewholder and perform click action
                    if (layoutManager.findFirstVisibleItemPosition() <= tabPosition && tabPosition <= layoutManager.findLastVisibleItemPosition()) {
                        GuideDocsAdapter.GuideDocsViewHolder viewHolder = (GuideDocsAdapter.GuideDocsViewHolder) recyclerView.findViewHolderForAdapterPosition(tabPosition);
                        if (viewHolder != null) {
                            recyclerView.postDelayed(() -> {
                                LinearLayout titleLayout = viewHolder.getTitleLayout();
                                titleLayout.setPressed(true);
                                titleLayout.setPressed(false);
//                                titleLayout.performClick();
                            }, 200);
                        }
                    } else {
                        //Else if item is not already on screen, scroll to it, and when scroll ends, get the viewholder and perform click action
                        recyclerView.smoothScrollToPosition(tabPosition);
                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                    //Remove to prevent scroll listener from constantly being called
                                    recyclerView.removeOnScrollListener(this);
                                    GuideDocsAdapter.GuideDocsViewHolder viewHolder = (GuideDocsAdapter.GuideDocsViewHolder) recyclerView.findViewHolderForAdapterPosition(tabPosition);
                                    if (viewHolder != null) {
                                        recyclerView.postDelayed(() -> {
                                            LinearLayout titleLayout = viewHolder.getTitleLayout();
                                            titleLayout.setPressed(true);
                                            titleLayout.setPressed(false);
//                                            titleLayout.performClick();
                                        }, 200);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        });
    }
}