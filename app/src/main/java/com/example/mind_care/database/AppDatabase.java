package com.example.mind_care.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ContactEntity.class}, version = 0)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactDao contactDao();
}
