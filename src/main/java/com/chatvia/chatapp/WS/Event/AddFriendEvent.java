package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class AddFriendEvent {
    @SerializedName("senderId")
    private Integer senderId;
    @SerializedName("receiverId")
    private Integer receiverId;
    @SerializedName("command")
    private String command;
    @SerializedName("senderName")
    private String senderName;

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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
