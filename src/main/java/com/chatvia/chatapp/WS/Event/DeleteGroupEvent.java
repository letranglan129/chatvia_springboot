package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class DeleteGroupEvent {
    @SerializedName("command")
     private String command;
    @SerializedName("groupId")
     private String groupId;
    @SerializedName("ownerId")
     private Integer ownerId;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}
