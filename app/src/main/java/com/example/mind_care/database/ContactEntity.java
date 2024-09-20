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
    public String imageUri;

    public ContactEntity(String contactName, String contactNumber, String imageUri) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.imageUri = imageUri.toString();
    }

    public Uri getImageUri(){
        return Uri.parse(imageUri);
    }

    public int getContactId() {
        return contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
