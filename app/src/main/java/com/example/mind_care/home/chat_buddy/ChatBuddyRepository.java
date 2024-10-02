package com.example.mind_care.home.chat_buddy;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.mind_care.database.ChatBuddyDatabase;
import com.example.mind_care.database.chat_buddy.MessageDao;
import com.example.mind_care.database.chat_buddy.MessageEntity;

import java.util.List;

public class ChatBuddyRepository {
    private MessageDao messageDao;
    private ChatBuddyDatabase db;
    private LiveData<List<MessageEntity>> messagesLiveData;

    public ChatBuddyRepository(Application application){
        db = Room.databaseBuilder(application, ChatBuddyDatabase.class, "chat_buddy_database").build();
        messageDao = db.messageDao();
        messagesLiveData = messageDao.getAllMessages(); //Room will update this livedata on change?
    }

    public LiveData<List<MessageEntity>> getAllMessages(){
        return messagesLiveData;
    }

    public void insertMessage(MessageEntity message){
        new Thread(()->{
            messageDao.insertMessage(message);
        }).start();
    }

    public void resetChat(){
        new Thread(()->{
            messageDao.deleteMessages();
            messageDao.deletePrimaryKeys();
        }).start();
    }

}
