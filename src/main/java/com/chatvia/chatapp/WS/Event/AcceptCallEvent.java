package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class AcceptCallEvent {
    @SerializedName("groupId")
    private String groupId;
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
