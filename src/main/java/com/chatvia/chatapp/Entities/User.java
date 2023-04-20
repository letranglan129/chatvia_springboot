package com.chatvia.chatapp.Entities;

import com.google.gson.annotations.SerializedName;

public class User {
    private Integer id;
    private String email;
    private String password;
    private String fullname;
    private String phone;
    private Integer connectId;
    private String avatar;
    private String status;
    private String blockBy;
    private String blockedUserId;
    @SerializedName("friend_id")
    private String reqFriendId;
    @SerializedName("user_id")
    private String reqUserId;
    private String describe;
    private Boolean isOnline;

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getReqFriendId() {
        return reqFriendId;
    }

    public void setReqFriendId(String reqFriendId) {
        this.reqFriendId = reqFriendId;
    }

    public String getReqUserId() {
        return reqUserId;
    }

    public void setReqUserId(String reqUserId) {
        this.reqUserId = reqUserId;
    }

    public String getBlockBy() {
        return blockBy;
    }

    public void setBlockBy(String blockBy) {
        this.blockBy = blockBy;
    }

    public String getBlockedUserId() {
        return blockedUserId;
    }

    public void setBlockedUserId(String blockedUserId) {
        this.blockedUserId = blockedUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getConnectId() {
        return this.connectId;
    }

    public void setConnectId(Integer connectId) {
        this.connectId = connectId;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
