package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class SendMessageEvent {
    @SerializedName("senderId")
    private int senderId;
    @SerializedName("groupId")
    private String groupId;
    @SerializedName("msg")
    private String msg;
    @SerializedName("command")
    private String command;

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
