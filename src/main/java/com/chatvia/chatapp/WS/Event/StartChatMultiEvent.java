package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class StartChatMultiEvent {
    @SerializedName("command")
    private String command;
    @SerializedName("userId")
    private Integer userId;
    @SerializedName("groupId")
    private Integer groupId;

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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
