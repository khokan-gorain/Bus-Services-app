package com.khokan_gorain_nsubusservices_app.ModelClass;

public class StudentMsgClass {

    String message;
    int messageId;

    public StudentMsgClass(String message, int messageId) {
        this.message = message;
        this.messageId = messageId;
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
