package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateGroupEvent {
    @SerializedName("groupName")
    private String groupName;
    @SerializedName("groupDesc")
    private String groupDesc;
    @SerializedName("groupMember[]")
    private List<String> groupMembers;
    @SerializedName("command")
    private String command;
    @SerializedName("senderId")
    private String senderId;
    @SerializedName("fullname")
    private String fullname;
    @SerializedName("avatar")
    private FileReceiver avatar;

    public FileReceiver getAvatar() {
        return avatar;
    }

    public void setAvatar(FileReceiver avatar) {
        this.avatar = avatar;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public List<String> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<String> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
