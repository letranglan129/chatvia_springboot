package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class GetOnlineEvent {
    @SerializedName("command")
    private String command;
    @SerializedName("id")
    private Integer id;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
