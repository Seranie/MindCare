package com.example.mind_care.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert
    void insertContact(ContactEntity contactEntity);

    @Update
    void updateContact(ContactEntity contactEntity);

    @Delete
    void deleteContact(ContactEntity contactEntity);

    @Query("SELECT * FROM contacts")
    List<ContactEntity> getAllContacts();

    @Query("SELECT * FROM contacts WHERE contactId = :contactId")
    ContactEntity getContactById(int contactId);

}
