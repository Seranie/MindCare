package com.example.tutorial_menu;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public abstract class ShowcaseFragment extends Fragment {
    private Context context;

    private void makeShowcase(View view, String title, String description){
        new GuideView.Builder(context)
                .setTitle(title)
                .setContentText(description)
                .setGravity(Gravity.auto) //optional
                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                .setTargetView(view)
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .build()
                .show();
    }

    public abstract void startShowcase();
}
