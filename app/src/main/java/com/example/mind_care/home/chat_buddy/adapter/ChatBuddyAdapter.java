package com.example.mind_care.home.chat_buddy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;

public class ChatBuddyAdapter extends RecyclerView.Adapter<ChatBuddyAdapter.ChatBuddyViewHolder> {
    private final List<MessageObjectModel> messageList;
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_AI = 2;

    @NonNull
    @Override
    public ChatBuddyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_USER){
            return new ChatBuddyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_buddy_user_message_item_layout, parent, false));
        }
        else{
            return new ChatBuddyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_buddy_ai_message_item_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBuddyViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).isFromAi() ? VIEW_TYPE_AI : VIEW_TYPE_USER;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ChatBuddyViewHolder  extends RecyclerView.ViewHolder{
        public ChatBuddyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
