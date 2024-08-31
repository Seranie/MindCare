package com.example.tutorial_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class How_to_guide extends Fragment {
    public How_to_guide(){
        super(R.layout.how_to_guide);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.how_to_guide, container, false);
    }
}
