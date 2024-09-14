package com.example.mind_care.home;

import androidx.fragment.app.Fragment;

public abstract class BaseTools extends Fragment{
    public interface FabListener{
        void setOnFabClickedDestination(int resId);
        void setFabImage(int imageId);
    }
}