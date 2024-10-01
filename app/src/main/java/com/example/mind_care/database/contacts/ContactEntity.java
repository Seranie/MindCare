package com.example.mind_care.database.contacts;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "contacts")
public class ContactEntity {
    public String contactName;
    public String contactNumber;
    public String imageUri;
    @PrimaryKey(autoGenerate = true)
    private int contactId;

    public ContactEntity(String contactName, String contactNumber, String imageUri) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return Uri.parse(imageUri);
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
