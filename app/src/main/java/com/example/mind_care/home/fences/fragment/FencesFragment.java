package com.example.mind_care.home.fences.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mind_care.R;
import com.example.mind_care.home.BaseTools;
import com.example.mind_care.home.fences.FenceObjectModel;
import com.example.mind_care.home.fences.viewmodel.FencesViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.GeoPoint;


public class FencesFragment extends BaseTools implements OnMapReadyCallback {
    private NavController navController;
    private SupportMapFragment mapFragment;
    private final float ZOOM_LEVEL = 11;
    private FencesViewModel fencesViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fences_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        fencesViewModel = new ViewModelProvider(requireActivity()).get(FencesViewModel.class);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fences_map);
        mapFragment.getMapAsync(this);

        navController = NavHostFragment.findNavController(getParentFragment());
    }

    @Override
    public void onResume() {
        super.onResume();
        fencesViewModel.getAllFences();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng sg = new LatLng(1.290270, 103.851959);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sg, ZOOM_LEVEL));

        CircleOptions circleOptions = new CircleOptions()
                .strokeColor(Color.RED)      // Circle border color
                .fillColor(Color.argb(50, 255, 0, 0)) // Fill color with transparency
                .strokeWidth(5);

        fencesViewModel.getFencesLiveData().observe(getViewLifecycleOwner(), fences -> {
            for (FenceObjectModel fence : fences) {
                GeoPoint location = fence.getLocation();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                circleOptions.center(latLng).radius(fence.getRadius());
                googleMap.addCircle(circleOptions);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu resource
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fences_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.link_account_menu) {
            navController.navigate(R.id.linkAccountFragment);
        }
        else if(item.getItemId() == R.id.share_location_menu){
            navController.navigate(R.id.shareLocationFragment);
        }
        else if(item.getItemId() == R.id.manage_fences_menu){
            navController.navigate(R.id.manageFencesFragment);
        }
        return true;
    }


    @Override
    public void setOnFabClickedDestination(FloatingActionButton fab, NavController navController) {
        fab.setOnClickListener(v -> navController.navigate(R.id.createFencesFragment));
    }

    @Override
    public void setFabImage(FloatingActionButton fab) {
        fab.setImageResource(R.drawable.baseline_add_10);
    }

}














