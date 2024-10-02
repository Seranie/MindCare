package com.example.mind_care.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mind_care.database.chat_buddy.MessageEntity;
import com.example.mind_care.database.contacts.ContactDao;
import com.example.mind_care.database.contacts.ContactEntity;

@Database(entities = {ContactEntity.class, MessageEntity.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactDao contactDao();
}
