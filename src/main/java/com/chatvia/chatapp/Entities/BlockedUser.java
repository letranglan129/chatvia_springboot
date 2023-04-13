package com.chatvia.chatapp.Entities;

import com.google.gson.annotations.SerializedName;

public class BlockedUser {
    @SerializedName("userId")
    private String userId;
    @SerializedName("blockedUserId")
    private String blockedUserId;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBlockedUserId() {
        return this.blockedUserId;
    }

    public void setBlockedUserId(String blockedUserId) {
        this.blockedUserId = blockedUserId;
    }
}
