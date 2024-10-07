package com.example.mind_care.home.reminders.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mind_care.R;
import com.example.mind_care.home.reminders.fragment.CreateNewReminderFragment;
import com.example.mind_care.home.reminders.model.RemindersGroupItem;
import com.example.mind_care.home.reminders.viewModel.ReminderGroupViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Comparator;

public class CreateReminderGroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ReminderGroupViewModel groupViewModel;
    private final int VIEW_TYPE_ADD_GROUP = 0;
    private final int VIEW_TYPE_GROUP_ITEM = 1;
    private final int NUMBER_OF_EXTRA_ITEMS = 1;
    private String selectedGroupId;
    private GroupItemViewHolder selectedGroupViewHolder = null;
    private final Context context;
    private ArrayList<RemindersGroupItem> groupItems = new ArrayList<>();
    private CreateNewReminderFragment fragment;

    public CreateReminderGroupsAdapter(ReminderGroupViewModel groupViewModel, Context context, CreateNewReminderFragment fragment) {
        this.groupViewModel = groupViewModel;
        this.context = context;
        groupViewModel.getRemindersGroupLiveData().observe(fragment, remindersGroupItems -> {
            Log.i("INFO", String.valueOf(remindersGroupItems));
            groupItems = (ArrayList<RemindersGroupItem>) remindersGroupItems;
            groupItems.sort(Comparator.comparing(RemindersGroupItem::getName, String.CASE_INSENSITIVE_ORDER));
            notifyDataSetChanged();
        });
        this.fragment = fragment;
    }

    public String getSelectedGroupId() {
        return selectedGroupId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ADD_GROUP) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_add_group, parent, false);
            return new AddGroupItemViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminders_group_item, parent, false);
            return new GroupItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //TODO
        // bind user image
        // bind group name
        //get viewmodels group objects list, check if not empty, then initialize images and group names respectively
        if (holder.getItemViewType() == VIEW_TYPE_GROUP_ITEM) {
            GroupItemViewHolder groupItemViewHolder = (GroupItemViewHolder) holder;

            if(!groupItems.isEmpty()){
                Uri imageUri = groupItems.get(position).getImageSource();
                Glide.with(context).load(imageUri).error(R.drawable.outline_image_not_supported_24).into(groupItemViewHolder.groupImage);
                groupItemViewHolder.groupName.setText(groupItems.get(position).getName());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position +1 == getItemCount()) {
            return VIEW_TYPE_ADD_GROUP;
        } else {
            return VIEW_TYPE_GROUP_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return groupViewModel.getSize() + NUMBER_OF_EXTRA_ITEMS;
    }

    public class GroupItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        private final ImageView groupImage;
        private final TextView groupName;

        public GroupItemViewHolder(@NonNull View itemView) {
            super(itemView);
            groupImage = itemView.findViewById(R.id.group_item_image);
            groupName = itemView.findViewById(R.id.group_item_name);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.reminders_group_item_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                return true;
            });
        }

        @Override
        public void onClick(View view) {
            toggleSelect(view, getAdapterPosition());
        }

        private void toggleSelect(View v, int position) {
            if (selectedGroupViewHolder == this) {
                v.setBackground(null);
                selectedGroupId = null;
                selectedGroupViewHolder = null;
            }
            else if (selectedGroupViewHolder == null){
                selectedGroupViewHolder = this;
                selectedGroupId = groupItems.get(position).getGroupId();
                v.setBackground(getPrimaryColor(v));
            }
            else {
                selectedGroupViewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
                selectedGroupViewHolder = this;
                selectedGroupId = groupItems.get(position).getGroupId();
                v.setBackground(getPrimaryColor(v));
            }
        }

        private Drawable getPrimaryColor(View v) {
            return ContextCompat.getDrawable(v.getContext(), R.drawable.group_icon_drawable);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.delete_group_menu_item) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle(R.string.delete_group_dialog_title)
                        .setMessage(R.string.delete_group_dialog_message)
                        .setPositiveButton(context.getString(R.string.delete_group_dialog_yes), (dialogInterface, i) -> {
                            groupViewModel.deleteGroup(fragment.requireActivity(),groupItems.get(getBindingAdapterPosition()));
                        })
                        .setNegativeButton(context.getString(R.string.delete_group_dialog_no), (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .show();
                return true;
            }
            return false;
        }
    }

    public class AddGroupItemViewHolder extends RecyclerView.ViewHolder {

        public AddGroupItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Navigation.findNavController(view).navigate(R.id.createGroupFragment);
                }
            });
        }

    }
}
