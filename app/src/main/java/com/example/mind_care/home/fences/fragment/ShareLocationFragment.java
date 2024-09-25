package com.example.mind_care.home.fences.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.mind_care.R;
import com.example.mind_care.home.fences.worker.ShareLocationWorker;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.concurrent.TimeUnit;

public class ShareLocationFragment extends Fragment {
    private final String SHARE_LOCATION_WORKER_TAG = "ShareLocation";
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 2;

    MaterialSwitch shareLocationSwitch;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.share_location_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         shareLocationSwitch = view.findViewById(R.id.share_location_switch);
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Handle permission request here if needed
            requestPermission();
            return;
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestBackgroundPermission();
        }

         //Changes switch check state based on worker state
        isLocationWorkerRunning();

        shareLocationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Switch is checked
                startLocationWorker();
            } else {
                // Switch is unchecked
                stopLocationWorker();
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void requestBackgroundPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void startLocationWorker(){
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        // Start the location worker
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(ShareLocationWorker.class, 15, TimeUnit.MINUTES).setConstraints(constraints).build();
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(SHARE_LOCATION_WORKER_TAG, ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }

    private void stopLocationWorker(){
        WorkManager.getInstance(requireContext()).cancelUniqueWork(SHARE_LOCATION_WORKER_TAG);
    }

    private void isLocationWorkerRunning() {
        WorkManager workManager = WorkManager.getInstance(requireContext());
        workManager.getWorkInfosForUniqueWorkLiveData(SHARE_LOCATION_WORKER_TAG).observe(getViewLifecycleOwner(), workInfos -> {
            if (workInfos != null && !workInfos.isEmpty()) {
                WorkInfo workInfo = workInfos.get(0);
                shareLocationSwitch.setChecked(workInfo.getState() == WorkInfo.State.RUNNING);
            }
        });
    }
}
