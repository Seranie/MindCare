package com.example.mind_care.home.fences.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.mind_care.R;
import com.example.mind_care.home.fences.worker.LocationCheckWorker;
import com.example.mind_care.home.fences.worker.ShareLocationWorker;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ShareLocationFragment extends Fragment {
    private final String SHARE_LOCATION_WORKER_TAG = "ShareLocation";
    private final String CHECK_LOCATION_WORKER_TAG = "CheckLocation";
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 2;
    private final int LOCATION_REQUEST_INTERVAL = 15000;

    MaterialSwitch shareLocationSwitch;
    MaterialSwitch checkLocationSwitch;

    CompoundButton.OnCheckedChangeListener shareLocationListener;
    CompoundButton.OnCheckedChangeListener checkLocationListener;
    PeriodicWorkRequest locationCheckRequest;
    OneTimeWorkRequest oneTimeLocationCheckRequest;

    private final ActivityResultLauncher<IntentSenderRequest> gpsSettingsLauncher = gpsPermissionLauncher();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.share_location_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shareLocationSwitch = view.findViewById(R.id.share_location_switch);
        checkLocationSwitch = view.findViewById(R.id.check_location_switch);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Handle permission request here if needed
            requestPermission();
            return;
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestBackgroundPermission();
        }

        //Changes switch check state based on worker state
        isLocationWorkerRunning();
        isCheckLocationWorkerRunning();

        shareLocationListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //check gps is enabled, Switch is checked
                    LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_REQUEST_INTERVAL).build();
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                    SettingsClient settingsClient = LocationServices.getSettingsClient(requireContext());
                    Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

                    task.addOnSuccessListener(locationSettingsResponse -> {
                        Log.i("INFO", "Location settings are enabled");
                        startLocationWorker();
                    }).addOnFailureListener(e -> {
                        Log.i("INFO", "Location settings are NOT enabled");
                        if (e instanceof ResolvableApiException) {
                            try {
                                Log.i("INFO", "Try get access to gps");
                                gpsSettingsLauncher.launch(new IntentSenderRequest.Builder(((ResolvableApiException) e).getResolution()).build());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                } else {
                    // Switch is unchecked
                    stopLocationWorker();
                }
            }
        };

        shareLocationSwitch.setOnCheckedChangeListener(shareLocationListener);

        checkLocationListener = new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                        requestNotificationPermission();
                    }else{
                        startCheckLocationWorker();
                    }
                } else {
                    stopCheckLocationWorker();
                }
            }};
        checkLocationSwitch.setOnCheckedChangeListener(checkLocationListener);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void requestBackgroundPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void startLocationWorker() {
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        // Start the location worker
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(ShareLocationWorker.class, 15, TimeUnit.MINUTES).setConstraints(constraints).build();
//        WorkManager.getInstance(requireActivity().getApplicationContext()).enqueueUniquePeriodicWork(SHARE_LOCATION_WORKER_TAG, ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
        WorkManager.getInstance(requireActivity().getApplicationContext()).enqueue(new OneTimeWorkRequest.Builder(ShareLocationWorker.class).build());
    }

    private void stopLocationWorker() {
        WorkManager.getInstance(requireActivity().getApplicationContext()).cancelUniqueWork(SHARE_LOCATION_WORKER_TAG);
    }

    private void isLocationWorkerRunning() {
        WorkManager workManager = WorkManager.getInstance(requireContext());
        workManager.getWorkInfosForUniqueWorkLiveData(SHARE_LOCATION_WORKER_TAG).observe(getViewLifecycleOwner(), workInfos -> {
            if (workInfos != null && !workInfos.isEmpty()) {
                WorkInfo workInfo = workInfos.get(0);
                shareLocationSwitch.setOnCheckedChangeListener(null);
                shareLocationSwitch.setChecked(workInfo.getState() == WorkInfo.State.RUNNING || workInfo.getState() == WorkInfo.State.ENQUEUED);
                shareLocationSwitch.setOnCheckedChangeListener(shareLocationListener);
            }
        });
    }

    private void startCheckLocationWorker() {
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        locationCheckRequest = new PeriodicWorkRequest.Builder(LocationCheckWorker.class, 15, TimeUnit.MINUTES).setConstraints(constraints).build();
//        oneTimeLocationCheckRequest = new OneTimeWorkRequest.Builder(LocationCheckWorker.class).build();

        observeForCheckLocationChange(locationCheckRequest.getId());

        // Enqueue the work request
        WorkManager.getInstance(requireActivity().getApplicationContext()).enqueueUniquePeriodicWork(CHECK_LOCATION_WORKER_TAG, ExistingPeriodicWorkPolicy.REPLACE, locationCheckRequest);

//        WorkManager.getInstance(requireActivity().getApplicationContext()).enqueue(oneTimeLocationCheckRequest);

    }

    private void stopCheckLocationWorker() {
        WorkManager.getInstance(requireActivity().getApplicationContext()).cancelUniqueWork(CHECK_LOCATION_WORKER_TAG);
    }

    private void isCheckLocationWorkerRunning() {
        WorkManager workManager = WorkManager.getInstance(requireContext());
        workManager.getWorkInfosForUniqueWorkLiveData(CHECK_LOCATION_WORKER_TAG).observe(getViewLifecycleOwner(), workInfos -> {
            if (workInfos != null && !workInfos.isEmpty()) {
                WorkInfo workInfo = workInfos.get(0);
                checkLocationSwitch.setOnCheckedChangeListener(null);
                checkLocationSwitch.setChecked(workInfo.getState() == WorkInfo.State.RUNNING || workInfo.getState() == WorkInfo.State.ENQUEUED);
                checkLocationSwitch.setOnCheckedChangeListener(checkLocationListener);
            }
        });
    }

    private ActivityResultLauncher<IntentSenderRequest> gpsPermissionLauncher() {
        return registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Log.i("INFO", "GPS is enabled");
                startLocationWorker();
            } else {
                Log.i("INFO", "GPS is NOT enabled");
                isLocationWorkerRunning();
            }
        });


    }

    private void observeForCheckLocationChange(UUID uid){
        WorkManager.getInstance(requireActivity().getApplicationContext()).getWorkInfoByIdLiveData(uid).observe(getViewLifecycleOwner(), workInfo -> {
            if(workInfo != null){
                if(workInfo.getState() == WorkInfo.State.FAILED){
                    String errorMessage = workInfo.getOutputData().getString("message");
                    if (errorMessage != null){
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void requestNotificationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }
}
