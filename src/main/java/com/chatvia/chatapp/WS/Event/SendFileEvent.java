package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SendFileEvent {
    @SerializedName("groupId")
    private String groupId;
    @SerializedName("senderId")
    private String senderId;
    @SerializedName("command")
    private String command;
    @SerializedName("files")
    private List<FileReceiver> files;

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

    public List<FileReceiver> getFiles() {
        return files;
    }

    public void setFiles(List<FileReceiver> files) {
        this.files = files;
    }
}


