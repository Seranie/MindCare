package com.example.mind_care.home.reminders.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mind_care.R;
import com.example.mind_care.home.reminders.fragment.Reminders;
import com.example.mind_care.home.reminders.model.RemindersGroupItem;
import com.example.mind_care.home.reminders.viewModel.ReminderGroupViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class RemindersGroupAdapter extends RecyclerView.Adapter<RemindersGroupAdapter.GroupItemViewHolder> implements DefaultLifecycleObserver {
    private final Context context;
    private final Reminders fragment;
    private List<RemindersGroupItem> remindersGroupItems = new ArrayList<>();
    private String selectedGroupId;
    private GroupItemViewHolder selectedGroupViewHolder = null;

    public RemindersGroupAdapter(ReminderGroupViewModel groupViewModel, Context context, Fragment fragment) {
        this.context = context;
        this.fragment = (Reminders) fragment;
        groupViewModel.getRemindersGroupLiveData().observe(fragment, groupItems -> {
            remindersGroupItems = groupItems;
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public GroupItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_group_item, parent, false);
        return new GroupItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupItemViewHolder holder, int position) {
        RemindersGroupItem groupItem = remindersGroupItems.get(position);
        try{
            Glide.with(context).load(groupItem.getImageSource()).error(R.drawable.outline_image_not_supported_24).into(holder.groupItemImage);

        }catch (Exception e){
            Log.e("ERROR", Objects.requireNonNull(e.getMessage()));
        }
        holder.groupItemName.setText(groupItem.getName());
    }

    @Override
    public int getItemCount() {
        return remindersGroupItems.size();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onPause(owner);
        if(selectedGroupViewHolder != null){
            selectedGroupViewHolder.itemView.performClick();
        }
    }

    public interface OnGroupItemClickListener {
        void onItemClick(String groupId);
    }

    class GroupItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView groupItemImage;
        TextView groupItemName;

        public GroupItemViewHolder(@NonNull View itemView) {
            super(itemView);
            groupItemImage = itemView.findViewById(R.id.group_item_image);
            groupItemName = itemView.findViewById(R.id.group_item_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Update UI state of selected group
            toggleSelect(view, getAdapterPosition());

            if(selectedGroupId != null){
                OnGroupItemClickListener listener = fragment;
                listener.onItemClick(selectedGroupId);
            }
        }

        private void toggleSelect(View v, int position) {
            if (selectedGroupViewHolder == this) {
                v.setBackground(null);
                selectedGroupId = "";
                selectedGroupViewHolder = null;
            } else if (selectedGroupViewHolder == null) {
                selectedGroupViewHolder = this;
                selectedGroupId = remindersGroupItems.get(position).getGroupId();
                v.setBackground(getPrimaryColor(v));
            } else {
                selectedGroupViewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
                selectedGroupViewHolder = this;
                selectedGroupId = remindersGroupItems.get(position).getGroupId();
                v.setBackground(getPrimaryColor(v));
            }
        }

        private Drawable getPrimaryColor(View v) {
            return ContextCompat.getDrawable(v.getContext(), R.drawable.group_icon_drawable);
        }
    }


}
