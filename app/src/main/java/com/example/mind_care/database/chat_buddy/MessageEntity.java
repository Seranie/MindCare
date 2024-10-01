package com.example.mind_care.database.chat_buddy;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class MessageEntity {
    @PrimaryKey(autoGenerate = true)
    private int messageId;

    private boolean isFromAi;
    private String message;
    private String imageUri;
    public MessageEntity(boolean isFromAi, String message, String imageUri) {
        this.isFromAi = isFromAi;
        this.message = message;
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public Uri getImageInUri() {
        return Uri.parse(imageUri);
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri.toString();
    }

    public boolean isFromAi() {
        return isFromAi;
    }

    public void setFromAi(boolean fromAi) {
        isFromAi = fromAi;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
