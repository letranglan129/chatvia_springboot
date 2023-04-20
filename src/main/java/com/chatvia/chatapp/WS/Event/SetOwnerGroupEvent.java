package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class SetOwnerGroupEvent {
    @SerializedName("command")
    private String command;
    @SerializedName("groupId")
    private Integer groupId;
    @SerializedName("userId")
    private Integer userId;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
