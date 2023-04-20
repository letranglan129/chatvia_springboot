package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class GetFriendEvent {
    @SerializedName("command")
    public String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
