package com.example.mind_care.home;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class BaseTools extends Fragment{

    public abstract void setOnFabClickedDestination(FloatingActionButton fab, NavController navController);
    public abstract void setFabImage(FloatingActionButton fab);

}