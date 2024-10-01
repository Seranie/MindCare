package com.example.mind_care.database.chat_buddy;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MessageDao {
    @Insert
    void insertMessage(MessageEntity messageEntity);

    @Query("DELETE FROM messages")
    void deleteMessages();

    @Query("DELETE FROM sqlite_sequence WHERE name = 'messages'")
    void deletePrimaryKeys();
}
