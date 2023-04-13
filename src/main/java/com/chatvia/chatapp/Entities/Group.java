package com.chatvia.chatapp.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group {
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("last_message")
    private String lastMessage;
    @SerializedName("desc")
    private String desc;
    @SerializedName("owner")
    private String owner;
    @SerializedName("members")
    private List<User> members;

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public String getAvatar() {
        return this.avatar;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
