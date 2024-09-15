package com.example.mind_care.home.reminders.adapter;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.home.reminders.model.RemindersGroupItem;

import java.util.List;

public class CreateReminderGroupsAdapter extends RecyclerView.Adapter<CreateReminderGroupsAdapter.GroupItemViewHolder> {
    private int selectedGroupPosition = RecyclerView.NO_POSITION;
    private final List<RemindersGroupItem> remindersGroupItems;

    public CreateReminderGroupsAdapter(List<RemindersGroupItem> remindersGroupItems) {
        this.remindersGroupItems = remindersGroupItems;
    }

    public int getSelectedGroupPosition() {
        return selectedGroupPosition;
    }

    @NonNull
    @Override
    public GroupItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_group_item, parent, false);

        return new GroupItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupItemViewHolder holder, int position) {
        //TODO
        // bind user image
        // bind group name

        if (position == getItemCount()) {
            holder.groupImage.setImageResource(R.drawable.outline_add_circle_outline_24);
            holder.groupName.setText(R.string.reminders_create_reminder_create_group);
        }

        // Resets color based on binding position, eg if scrolled off then scrolled back in, then the selected view should still have color
        if (selectedGroupPosition == position){
            holder.itemView.setBackgroundColor(getPrimaryColor(holder.itemView));
        }
        else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }


    @Override
    public int getItemCount() {
        return remindersGroupItems.size();
    }

    public class GroupItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView groupImage;
        private TextView groupName;

        public GroupItemViewHolder(@NonNull View itemView) {
            super(itemView);
            groupImage = itemView.findViewById(R.id.group_item_image);
            groupName = itemView.findViewById(R.id.group_item_name);
        }

        @Override
        public void onClick(View view) {
            toggleSelect(view, getAdapterPosition());
        }
    }

    private void toggleSelect(View v, int position){
        if (selectedGroupPosition == position){
            selectedGroupPosition = RecyclerView.NO_POSITION;
            v.setBackgroundColor(Color.TRANSPARENT);
        }
        else {
            selectedGroupPosition = position;
            v.setBackgroundColor(getPrimaryColor(v));
        }
        notifyItemChanged(selectedGroupPosition);

    }

    private int getPrimaryColor(View v){
        Resources.Theme theme = v.getContext().getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

}
