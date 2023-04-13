package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class StartChatPrivateEvent {
    @SerializedName("command")
    private String command;
    @SerializedName("senderId")
    private Integer senderId;
    @SerializedName("receiverId")
    private Integer receiverId;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }
}
