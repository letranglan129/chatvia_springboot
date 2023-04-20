package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddMemberToGroupEvent {
    @SerializedName("command")
    private String command;
    @SerializedName("members")
    private List<String> members;
    @SerializedName("groupId")
    private String groupId;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
