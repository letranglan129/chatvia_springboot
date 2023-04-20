package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class AcceptFriendEvent {
    @SerializedName("senderId")
    private Integer senderId;
    @SerializedName("receiverId")
    private Integer receiverId;
    @SerializedName("notifyId")
    private Integer notifyId;
    @SerializedName("command")
    private String command;

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

    public Integer getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(Integer notifyId) {
        this.notifyId = notifyId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
