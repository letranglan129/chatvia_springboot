package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReadNotifyEvent {
    @SerializedName("command")
    private String command;
    @SerializedName("notifyIds")
    private List<String> notifyIds;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<String> getNotifyIds() {
        return notifyIds;
    }

    public void setNotifyIds(List<String> notifyIds) {
        this.notifyIds = notifyIds;
    }
}
