package com.example.mind_care.database;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "contacts")
public class ContactEntity {
    @PrimaryKey(autoGenerate = true)
    private int contactId;

    public String contactName;
    public String contactNumber;
    private String imageUri;

    public ContactEntity(String contactName, String contactNumber, Uri contactImageSource) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.imageUri = contactImageSource.toString();
    }

    public Uri getImageUri(){
        return Uri.parse(imageUri);
    }
}
