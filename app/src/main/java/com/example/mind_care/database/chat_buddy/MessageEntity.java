package com.example.mind_care.database.chat_buddy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class MessageEntity {
    @PrimaryKey(autoGenerate = true)
    private int messageId;
    private boolean isFromAi;
    private String message;

    public MessageEntity(boolean isFromAi, String message) {
        this.isFromAi = isFromAi;
        this.message = message;
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
