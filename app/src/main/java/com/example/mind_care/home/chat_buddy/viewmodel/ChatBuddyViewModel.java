package com.example.mind_care.home.chat_buddy.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mind_care.database.chat_buddy.MessageEntity;
import com.example.mind_care.home.chat_buddy.ChatBuddyRepository;

import java.util.ArrayList;
import java.util.List;

public class ChatBuddyViewModel extends ViewModel {
    private LiveData<List<MessageEntity>> messagesLiveData;
    private ChatBuddyRepository repo;


    public ChatBuddyViewModel(Application application){
        repo = new ChatBuddyRepository(application);
        messagesLiveData = repo.getAllMessages();
    }

    public LiveData<List<MessageEntity>> getMessagesLiveData(){
        return messagesLiveData;
    }

    public void insertMessage(MessageEntity message){
        repo.insertMessage(message);
    }

    public void resetChat(){
        repo.resetChat();
    }

    public void updateMessage(MessageEntity message){
        repo.updateMessage(message);
    }
}
