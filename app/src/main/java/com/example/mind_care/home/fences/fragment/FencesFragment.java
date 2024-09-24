package com.example.mind_care.home.fences.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.mind_care.R;
import com.example.mind_care.home.BaseTools;
import com.example.mind_care.home.fences.worker.LocationCheckWorker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.TimeUnit;

public class FencesFragment extends BaseTools implements OnMapReadyCallback {
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fences_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fences_map);
        mapFragment.getMapAsync(this);

        navController = Navigation.findNavController(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        PeriodicWorkRequest locationCheckRequest = new PeriodicWorkRequest.Builder(
                LocationCheckWorker.class, 15, TimeUnit.MINUTES)
                .build();

        // Enqueue the work request
        WorkManager.getInstance(requireActivity().getApplicationContext()).enqueue(locationCheckRequest);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
        // Example: Add a marker in Sydney and move the camera
        LatLng sg = new LatLng(1.290270, 103.851959);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sg));
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
        return true;
    }


    @Override
    public void setOnFabClickedDestination(FloatingActionButton fab, NavController navController) {
        navController.navigate(R.id.createFencesFragment);
    }

    @Override
    public void setFabImage(FloatingActionButton fab) {
        fab.setImageResource(R.drawable.baseline_add_10);
    }

}














