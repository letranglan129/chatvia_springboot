package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SendCallEvent {
    @SerializedName("groupId")
    private String groupId;
    @SerializedName("senderId")
    private String senderId;
    @SerializedName("command")
    private String command;
    @SerializedName("type")
    private String type;
    @SerializedName("roomId")
    private String roomId;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
