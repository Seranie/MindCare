package com.example.mind_care.home.fences.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.fences.adapter.ManageFencesAdapter;
import com.example.mind_care.home.fences.viewmodel.ManageFencesViewModel;

public class ManageFencesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manage_fences_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ManageFencesViewModel manageFencesViewModel = new ViewModelProvider(requireActivity()).get(ManageFencesViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.manage_fences_recyclerview);
        ManageFencesAdapter adapter = new ManageFencesAdapter(manageFencesViewModel, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}
