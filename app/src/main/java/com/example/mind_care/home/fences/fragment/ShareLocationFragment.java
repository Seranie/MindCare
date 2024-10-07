package com.example.mind_care.home.fences.fragment;

import static android.app.ProgressDialog.show;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startForegroundService;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.Network;
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
import androidx.annotation.RequiresApi;
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
import com.example.mind_care.home.fences.service.CheckLocationService;
import com.example.mind_care.home.fences.service.LocationService;
import com.example.mind_care.home.fences.repository.LocationCheckRepository;
import com.example.mind_care.home.fences.worker.LocationCheckWorker;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.concurrent.TimeUnit;

public class ShareLocationFragment extends Fragment {
//    private final String SHARE_LOCATION_WORKER_TAG = "ShareLocation";
//    private final String CHECK_LOCATION_WORKER_TAG = "CheckLocation";

    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 2;
    private final int LOCATION_REQUEST_INTERVAL = 15000;
    private final int MAX_STREAMS = 5;
    private final float LEFT_VOLUME = 1.0f;
    private final float RIGHT_VOLUME = 1.0f;
    private final int PRIORITY = 0;
    private final int LOOP = 0;
    private final float RATE = 1.0f;
    private SoundPool soundPool;
    private int soundId;

    MaterialSwitch shareLocationSwitch;
    MaterialSwitch checkLocationSwitch;
    CompoundButton.OnCheckedChangeListener shareLocationListener;
    CompoundButton.OnCheckedChangeListener checkLocationListener;

//    private ConnectivityManager connectivityManager;
//    private ConnectivityManager.NetworkCallback checkLocationNetworkCallback;
//    private ConnectivityManager.NetworkCallback shareLocationNetworkCallback;

    private final ActivityResultLauncher<IntentSenderRequest> gpsSettingsLauncher = gpsPermissionLauncher();
    private ActivityResultLauncher<String> foregroundServicePermissionLauncher;

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

        soundPool = new SoundPool.Builder().setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_MEDIA).build()).setMaxStreams(MAX_STREAMS).build();
        soundId = soundPool.load(requireContext(), R.raw.share_location_toggle, 1);


        foregroundServicePermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Permission granted, start the foreground service
                        startLocationService();
                    } else {
                        // Permission denied, show a message to the user
                        Toast.makeText(requireContext(), "Foreground service permission denied", Toast.LENGTH_SHORT).show();
                    }
                });


        //Changes switch check state based on worker state
        isLocationServiceRunning();
        isCheckLocationServiceRunning();


        shareLocationListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                soundPool.play(soundId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, LOOP, RATE);

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Handle permission request here if needed
                    Toast.makeText(requireContext(), R.string.please_grant_location_permission, Toast.LENGTH_SHORT).show();
                    requestFineLocationPermission();
                    isLocationServiceRunning();
                    return;
                }
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Toast.makeText(requireContext(), R.string.please_grant_background_location_permission, Toast.LENGTH_SHORT).show();
                        requestBackgroundPermission();
                        isLocationServiceRunning();
                        return;
                    }
                }

                if (isChecked) {
                    //check gps is enabled, Switch is checked
                    LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_REQUEST_INTERVAL).build();
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                    SettingsClient settingsClient = LocationServices.getSettingsClient(requireContext());
                    Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

                    task.addOnSuccessListener(locationSettingsResponse -> {
                        Log.i("INFO", "Location settings are enabled");
                        checkForegroundServicePermission();
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
                    stopLocationService();
                }
            }
        };

        shareLocationSwitch.setOnCheckedChangeListener(shareLocationListener);

        checkLocationListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                soundPool.play(soundId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, LOOP, RATE);
                if (isChecked) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(requireContext(), R.string.cannot_use_this_feature_without_notification_permission, Toast.LENGTH_SHORT).show();
                        requestNotificationPermission();
                    } else {
                        LocationCheckRepository locationCheckRepository = new LocationCheckRepository();
                        locationCheckRepository.checkIfLinkedToPatient().handle((isLinked, throwable) -> {
                            if(throwable != null){
                                Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }else {
                                if(isLinked){
                                    startCheckLocationService();
                                }else{
                                    Toast.makeText(requireContext(), R.string.no_patient_linked_yet, Toast.LENGTH_SHORT).show();
                                }
                            }
                            return null;
                        });

                    }
                } else {
                    stopCheckLocationService();
                }
            }
        };
        checkLocationSwitch.setOnCheckedChangeListener(checkLocationListener);

    }


//    private void registerShareLocationNetworkCallback(){
//        shareLocationNetworkCallback = new ConnectivityManager.NetworkCallback(){
//
//            @Override
//            public void onAvailable(@NonNull Network network) {
//                super.onAvailable(network);
//                startLocationService();
//            }
//
//            @Override
//            public void onLost(@NonNull Network network) {
//                super.onLost(network);
//                if(isAdded()){
//                    requireActivity().runOnUiThread(()-> Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show());
//                }
//                stopLocationService();
//            }
//        };
//        try{
//            connectivityManager.registerDefaultNetworkCallback(shareLocationNetworkCallback);
//        } catch(IllegalArgumentException e){
//            Log.i("INFO", "Network callback already registered");
//        }
//    }

//    private void unregisterShareLocationNetworkCallback(){
//        if(connectivityManager != null && shareLocationNetworkCallback != null){
//            connectivityManager.unregisterNetworkCallback(shareLocationNetworkCallback);
//        }
//    }

//    private void registerCheckLocationNetworkCallback(){
//        //On newer API's might need to change because notification swiping can uncheck the switch, meaning the previous network callback is still registered whilst setting a new one.??)
//        checkLocationNetworkCallback = new ConnectivityManager.NetworkCallback(){
//
//            @Override
//            public void onAvailable(@NonNull Network network) {
//                super.onAvailable(network);
//                    startCheckLocationService();
//            }
//
//            @Override
//            public void onLost(@NonNull Network network) {
//                super.onLost(network);
//                if(isAdded()){
//                    requireActivity().runOnUiThread(()->{
//                        Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
//                    });
//                }
//                stopCheckLocationService();
//            }
//        };
//        try{
//            connectivityManager.registerDefaultNetworkCallback(checkLocationNetworkCallback);
//        } catch (IllegalArgumentException e){
//            Log.i("INFO", "Network callback already registered");
//        }
//    }

//    private void unregisterCheckLocationNetworkCallback(){
//        if(connectivityManager != null && checkLocationNetworkCallback != null){
//            connectivityManager.unregisterNetworkCallback(checkLocationNetworkCallback);
//        }
//    }

    private void requestFineLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestBackgroundPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE);
    }

//    private void startLocationWorker() {
//        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
//
//        // Start the location worker
//        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(ShareLocationWorker.class, 15, TimeUnit.MINUTES).setConstraints(constraints).build();
//        WorkManager.getInstance(requireActivity().getApplicationContext()).enqueueUniquePeriodicWork(SHARE_LOCATION_WORKER_TAG, ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
////        WorkManager.getInstance(requireActivity().getApplicationContext()).enqueue(new OneTimeWorkRequest.Builder(ShareLocationWorker.class).build()); //TODO for debug
//    }

    private void startLocationService(){
        Intent serviceIntent = new Intent(requireContext(), LocationService.class);
        requireContext().startForegroundService(serviceIntent);
    }

    private void stopLocationService(){
        Intent serviceIntent = new Intent(requireContext(), LocationService.class);
        requireContext().stopService(serviceIntent);
    }

    private void isLocationServiceRunning(){
        ActivityManager manager = (ActivityManager) requireActivity().getSystemService(Context.ACTIVITY_SERVICE);
        shareLocationSwitch.setOnCheckedChangeListener(null);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    shareLocationSwitch.setChecked(true);
                    shareLocationSwitch.setOnCheckedChangeListener(shareLocationListener);
                    return;
                }
            }
        }
        shareLocationSwitch.setChecked(false);
        shareLocationSwitch.setOnCheckedChangeListener(shareLocationListener);
    }

//    private void stopLocationWorker() {
//        WorkManager.getInstance(requireActivity().getApplicationContext()).cancelUniqueWork(SHARE_LOCATION_WORKER_TAG);
//    }

//    private void isLocationWorkerRunning() {
//        WorkManager workManager = WorkManager.getInstance(requireContext());
//        workManager.getWorkInfosForUniqueWorkLiveData(SHARE_LOCATION_WORKER_TAG).observe(getViewLifecycleOwner(), workInfos -> {
//            if (workInfos != null && !workInfos.isEmpty()) {
//                WorkInfo workInfo = workInfos.get(0);
//                shareLocationSwitch.setOnCheckedChangeListener(null);
//                shareLocationSwitch.setChecked(workInfo.getState() == WorkInfo.State.RUNNING || workInfo.getState() == WorkInfo.State.ENQUEUED);
//                shareLocationSwitch.setOnCheckedChangeListener(shareLocationListener);
//            }
//        });
//    }

//    private void startCheckLocationWorker() {
//        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
//
//        locationCheckRequest = new PeriodicWorkRequest.Builder(LocationCheckWorker.class, 15, TimeUnit.MINUTES).setConstraints(constraints).build();
////        oneTimeLocationCheckRequest = new OneTimeWorkRequest.Builder(LocationCheckWorker.class).build();
//
////        observeForCheckLocationChange(locationCheckRequest.getId());
//
//        // Enqueue the work request
//        WorkManager.getInstance(requireActivity().getApplicationContext()).enqueueUniquePeriodicWork(CHECK_LOCATION_WORKER_TAG, ExistingPeriodicWorkPolicy.REPLACE, locationCheckRequest);
//
////        WorkManager.getInstance(requireActivity().getApplicationContext()).enqueue(oneTimeLocationCheckRequest); //TODO for debug
//
//    }

//    private void stopCheckLocationWorker() {
//        WorkManager.getInstance(requireActivity().getApplicationContext()).cancelUniqueWork(CHECK_LOCATION_WORKER_TAG);
//    }

//    private void isCheckLocationWorkerRunning() {
//        WorkManager workManager = WorkManager.getInstance(requireContext());
//        workManager.getWorkInfosForUniqueWorkLiveData(CHECK_LOCATION_WORKER_TAG).observe(getViewLifecycleOwner(), workInfos -> {
//            if (workInfos != null && !workInfos.isEmpty()) {
//                WorkInfo workInfo = workInfos.get(0);
//                checkLocationSwitch.setOnCheckedChangeListener(null);
//                checkLocationSwitch.setChecked(workInfo.getState() == WorkInfo.State.RUNNING || workInfo.getState() == WorkInfo.State.ENQUEUED);
//                checkLocationSwitch.setOnCheckedChangeListener(checkLocationListener);
//            }
//        });
//    }

    private ActivityResultLauncher<IntentSenderRequest> gpsPermissionLauncher() {
        return registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Log.i("INFO", "GPS is enabled");
                checkForegroundServicePermission();
            } else {
                Log.i("INFO", "GPS is NOT enabled");
                isLocationServiceRunning();
            }
        });


    }

//    private void observeForCheckLocationChange(UUID uid) {
//        WorkManager.getInstance(requireActivity().getApplicationContext()).getWorkInfoByIdLiveData(uid).observe(getViewLifecycleOwner(), workInfo -> {
//            if (workInfo != null) {
//                if (workInfo.getState() == WorkInfo.State.FAILED) {
//                    String errorMessage = workInfo.getOutputData().getString("message");
//                    if (errorMessage != null) {
//                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }

    private void checkForegroundServicePermission(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            foregroundServicePermissionLauncher.launch(Manifest.permission.FOREGROUND_SERVICE);
        } else {
            // Permission already granted
            startLocationService();
        }
    }

    private void startCheckLocationService() {
        Intent serviceIntent = new Intent(requireContext(), CheckLocationService.class);
        requireContext().startForegroundService(serviceIntent);
    }

    private void stopCheckLocationService(){
        Intent serviceIntent = new Intent(requireContext(), CheckLocationService.class);
        requireContext().stopService(serviceIntent);
    }

    private void isCheckLocationServiceRunning(){
        checkLocationSwitch.setOnCheckedChangeListener(null);
        ActivityManager manager = (ActivityManager) requireActivity().getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (CheckLocationService.class.getName().equals(service.service.getClassName())) {
                    checkLocationSwitch.setChecked(true);
                    checkLocationSwitch.setOnCheckedChangeListener(checkLocationListener);
                    return;
                }
            }
        }
        checkLocationSwitch.setChecked(false);
        checkLocationSwitch.setOnCheckedChangeListener(checkLocationListener);
    }
}
