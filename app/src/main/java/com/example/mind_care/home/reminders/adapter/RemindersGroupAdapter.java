package com.example.mind_care.home.reminders.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mind_care.R;
import com.example.mind_care.home.reminders.model.RemindersGroupItem;

import java.util.List;

public class RemindersGroupAdapter extends RecyclerView.Adapter<RemindersGroupAdapter.GroupItemViewHolder> {
    //TODO incomplete adapter, takes in a list of group items
    private List<RemindersGroupItem> remindersGroupItems;
    private Context context;

    public RemindersGroupAdapter(List<RemindersGroupItem> remindersGroupItems, Context context){
        this.remindersGroupItems = remindersGroupItems;
        this.context = context;
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
        Glide.with(context).load(groupItem.getImageSource()).error(R.drawable.outline_image_not_supported_24).into(holder.groupItemImage);
        holder.groupItemName.setText(groupItem.getName());
    }

    @Override
    public int getItemCount() {
        return remindersGroupItems.size();
    }

    class GroupItemViewHolder extends RecyclerView.ViewHolder{
        ImageView groupItemImage;
        TextView groupItemName;

        public GroupItemViewHolder(@NonNull View itemView) {
            super(itemView);
            groupItemImage = itemView.findViewById(R.id.group_item_image);
            groupItemName = itemView.findViewById(R.id.group_item_name);
        }
    }
}
