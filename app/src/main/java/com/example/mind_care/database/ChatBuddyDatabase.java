package com.example.mind_care.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mind_care.database.chat_buddy.MessageDao;
import com.example.mind_care.database.chat_buddy.MessageEntity;

@Database(entities = {MessageEntity.class}, version = 1)
public abstract class ChatBuddyDatabase extends RoomDatabase {
    public abstract MessageDao messageDao();
}
