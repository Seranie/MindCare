package com.example.mind_care.home.fences.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.mind_care.R;
import com.example.mind_care.home.fences.FenceObjectModel;
import com.example.mind_care.home.fences.viewmodel.CreateFencesViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreateFencesFragment extends Fragment implements OnMapReadyCallback {
    private SeekBar radiusBar;
    private GoogleMap mMap;
    private LatLng circleCenter;
    private CircleOptions circleOptions;
    private double circleRadius = 500;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_fence_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.create_fences_map);
        mapFragment.getMapAsync(this);

        radiusBar = view.findViewById(R.id.create_fence_seekbar);
        Button confirmButton = view.findViewById(R.id.create_fences_confirm_button);
        Button cancelButton = view.findViewById(R.id.create_fences_cancel_button);
        TextInputEditText editText = view.findViewById(R.id.create_fence_edit_text);
        TextInputLayout editLayout = view.findViewById(R.id.create_fence_text_layout);
        String inputText = editText.getText().toString();

        CreateFencesViewModel createFencesViewModel = new ViewModelProvider(requireActivity()).get(CreateFencesViewModel.class);

        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                circleRadius = seekBar.getProgress();
                if(circleOptions != null){
                    circleOptions.radius(circleRadius);
                }
                updateCircle();
            }
        });

        confirmButton.setOnClickListener(v -> {
            if(inputText != null){
                if(inputText.isEmpty()){
                    editLayout.setError("Please enter a name");
                }else{
                    FenceObjectModel model = new FenceObjectModel(circleCenter, circleRadius, inputText);
                    createFencesViewModel.setFence(model);
                    Navigation.findNavController(v).popBackStack();
                }
            }
        });

        cancelButton.setOnClickListener(v -> {
           Navigation.findNavController(v).popBackStack();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng defaultLocation = new LatLng(1.290270, 103.851959);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));

        circleCenter = defaultLocation;

        circleOptions = new CircleOptions()
                .center(circleCenter)        // Set the initial center of the circle
                .radius(circleRadius)                 // Radius in meters
                .strokeColor(Color.RED)      // Circle border color
                .fillColor(Color.argb(50, 255, 0, 0)) // Fill color with transparency
                .strokeWidth(5);

        mMap.addCircle(circleOptions);

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                // Update the circle's center to the current camera target
                circleCenter = mMap.getCameraPosition().target;

                // Update the circle on the map
                updateCircle();
            }
        });
    }

    private void updateCircle(){
        mMap.clear();
        mMap.addCircle(circleOptions.center(circleCenter));
    }
}
