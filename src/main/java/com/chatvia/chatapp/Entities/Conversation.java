package com.chatvia.chatapp.Entities;

import com.google.gson.annotations.SerializedName;

public class Conversation {
    @SerializedName(value = "sent_at")
    public String sentAt;
    @SerializedName(value = "sender_id")
    public String senderId;
    @SerializedName(value = "sender")
    public String sender;
    @SerializedName(value = "receiverName")
    public String receiverName;
    @SerializedName(value = "receiverId")
    public String receiverId;
    @SerializedName(value = "message")
    public String message;
    @SerializedName(value = "id")
    public String id;
    @SerializedName(value = "group_id")
    public String groupId;
    @SerializedName(value = "groupId")
    public String groupMultiId;
    @SerializedName(value = "fullname")
    public String fullname;
    @SerializedName(value = "avatar")
    public String avatar;
    @SerializedName(value = "groupAvatar")
    public String groupAvatar;
    @SerializedName(value = "countMessage")
    public String countMessage;
    @SerializedName(value = "groupName")
    public String groupName;
    @SerializedName(value = "isBlocked")
    public String isBlocked;
    @SerializedName(value = "desc")
    public String desc;
    @SerializedName(value = "type")
    public String type;

    public String getGroupMultiId() {
        return groupMultiId;
    }

    public void setGroupMultiId(String groupMultiId) {
        this.groupMultiId = groupMultiId;
    }

    public String getGroupAvatar() {
        return groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(String isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

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

    public String getCountMessage() {
        return countMessage;
    }

    public void setCountMessage(String countMessage) {
        this.countMessage = countMessage;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
