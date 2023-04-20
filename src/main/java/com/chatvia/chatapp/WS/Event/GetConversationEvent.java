package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class GetConversationEvent {
    @SerializedName("command")
    private String command;
    @SerializedName("userId")
    private Integer userId;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
