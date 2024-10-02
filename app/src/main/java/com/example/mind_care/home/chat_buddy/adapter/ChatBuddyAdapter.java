package com.example.mind_care.home.chat_buddy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.R;
import com.example.mind_care.database.chat_buddy.MessageEntity;
import com.example.mind_care.home.chat_buddy.viewmodel.ChatBuddyViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatBuddyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MessageEntity> messageList = new ArrayList<>();
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_AI = 2;
    private ChatBuddyViewModel chatBuddyViewModel;


    public ChatBuddyAdapter(ChatBuddyViewModel chatBuddyViewModel, LifecycleOwner owner){
        this.chatBuddyViewModel = chatBuddyViewModel;
        chatBuddyViewModel.getMessagesLiveData().observe(owner, messageEntities -> {
            messageList = messageEntities;
            notifyDataSetChanged(); //TODO or call from fragment instead to use more specific methods
        });
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_USER){
            return new UserChatBuddyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_buddy_user_message_item_layout, parent, false));
        }
        else{
            return new AiChatBuddyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_buddy_ai_message_item_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageEntity message = messageList.get(position);
        if(message.isFromAi()){ //TODO set image also
            ((AiChatBuddyViewHolder) holder).message.setText(message.getMessage());
        }
        else {
            ((UserChatBuddyViewHolder) holder).message.setText(message.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).isFromAi() ? VIEW_TYPE_AI : VIEW_TYPE_USER;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class UserChatBuddyViewHolder extends RecyclerView.ViewHolder{
        TextView message;
        ImageView imageView;

        public UserChatBuddyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.user_message_textview);
            imageView = itemView.findViewById(R.id.user_message_imageview);
        }
    }

    public class AiChatBuddyViewHolder extends RecyclerView.ViewHolder{
        TextView message;
        ImageView imageView;

        public AiChatBuddyViewHolder(@NonNull View itemView){
            super(itemView);
            message = itemView.findViewById(R.id.ai_message_textview);
            imageView = itemView.findViewById(R.id.ai_message_imageview);
        }
    }
}
