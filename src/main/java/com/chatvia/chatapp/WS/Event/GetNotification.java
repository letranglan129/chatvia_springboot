package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class GetNotification {
    @SerializedName("command")
    private String command;
    @SerializedName("userId")
    private String userId;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
