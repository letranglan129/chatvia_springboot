package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class CancelCallEvent {
    @SerializedName("groupId")
    private String groupId;
    @SerializedName("senderId")
    private String senderId;
    @SerializedName("command")
    private String command;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
