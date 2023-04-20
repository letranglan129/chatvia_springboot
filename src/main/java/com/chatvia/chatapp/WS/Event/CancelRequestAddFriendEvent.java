package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class CancelRequestAddFriendEvent {
    @SerializedName("command")
    private String command;
    @SerializedName("userId")
    private Integer userId;
    @SerializedName("friendId")
    private Integer friendId;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }
}
