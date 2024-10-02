package com.example.mind_care.database.chat_buddy;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {
    @Insert
    void insertMessage(MessageEntity messageEntity);

    @Query("DELETE FROM messages")
    void deleteMessages();

    @Query("DELETE FROM sqlite_sequence WHERE name = 'messages'")
    void deletePrimaryKeys();

    @Query("SELECT * FROM messages")
    LiveData<List<MessageEntity>> getAllMessages();
}
