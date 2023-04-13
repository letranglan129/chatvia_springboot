package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class DeleteMessageEvent {
    @SerializedName("command")
    private String command;
    @SerializedName("type")
    private String type;
    @SerializedName("userId")
    private Integer userId;
    @SerializedName("groupId")
    private Integer groupId;
    @SerializedName("messageId")
    private Integer messageId;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }
}
