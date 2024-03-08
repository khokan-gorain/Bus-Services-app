package com.khokan_gorain_nsubusservices_app.ModelClass;

public class DatabaseResponseMsg {
    String message;

    public DatabaseResponseMsg() {
    }

    public DatabaseResponseMsg(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
