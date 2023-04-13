package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForwardMessageEvent {
    @SerializedName("receiversPrivate")
    private List<String> receiversPrivate;
    @SerializedName("receiversGroup")
    private List<String> receiversGroup;
    @SerializedName("senderId")
    private int senderId;
    @SerializedName("messageId")
    private String messageId;
    @SerializedName("command")
    private String command;

    public List<String> getReceiversPrivate() {
        return receiversPrivate;
    }

    public void setReceiversPrivate(List<String> receiversPrivate) {
        this.receiversPrivate = receiversPrivate;
    }

    public List<String> getReceiversGroup() {
        return receiversGroup;
    }

    public void setReceiversGroup(List<String> receiversGroup) {
        this.receiversGroup = receiversGroup;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
