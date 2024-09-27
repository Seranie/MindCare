package com.example.mind_care.home.fences.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.fences.FenceObjectModel;
import com.example.mind_care.home.fences.viewmodel.ManageFencesViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class ManageFencesAdapter extends RecyclerView.Adapter<ManageFencesAdapter.FenceViewHolder> {
    private ManageFencesViewModel manageFencesViewModel;
    private ArrayList<FenceObjectModel> fenceList = new ArrayList<>();

    public ManageFencesAdapter(ManageFencesViewModel manageFencesViewModel, Fragment fragment) {
        this.manageFencesViewModel = manageFencesViewModel;
        manageFencesViewModel.getManageFenceLiveData().observe(fragment, fenceList -> {
            this.fenceList = fenceList;
            notifyDataSetChanged();
        });
        manageFencesViewModel.getAllFences();
    }

    @NonNull
    @Override
    public ManageFencesAdapter.FenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_fences_items_layout, parent, false);
        return new FenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageFencesAdapter.FenceViewHolder holder, int position) {
        FenceObjectModel temp = fenceList.get(position);
        holder.setChipText(temp.getFenceName());
    }

    @Override
    public int getItemCount() {
        return fenceList.size();
    }

    public class FenceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Chip chip;

        public FenceViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.manage_fences_chip);
            chip.setOnCloseIconClickListener(this);
        }

        public void setChipText(String text){
            chip.setText(text);
        }

        @Override
        public void onClick(View view) {
            //ask view model to delete fence
            manageFencesViewModel.deleteFence(fenceList.get(getBindingAdapterPosition()));
        }
    }
}
