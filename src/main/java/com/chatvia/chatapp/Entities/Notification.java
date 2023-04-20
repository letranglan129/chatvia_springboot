package com.chatvia.chatapp.Entities;

import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("id")
    private String id;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("type")
    private String type;
    @SerializedName("payload")
    private String payload;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("seen_at")
    private String seenAt;
    @SerializedName("from_id")
    private String fromId;
    @SerializedName("fullname")
    private String fullname;
    @SerializedName("avatar")
    private String avatar;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayload() {
        return this.payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSeenAt() {
        return this.seenAt;
    }

    public void setSeenAt(String seenAt) {
        this.seenAt = seenAt;
    }

    public String getFromId() {
        return this.fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
}
