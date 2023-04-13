package com.chatvia.chatapp.Entities;

public class ViewedMessage {
    private ViewedMessageId viewedMessageId;
    private java.sql.Timestamp viewedAt;

    public ViewedMessageId getViewedMessageId() {
        return this.viewedMessageId;
    }

    public void setViewedMessageId(ViewedMessageId messageId) {
        this.viewedMessageId = messageId;
    }

    public java.sql.Timestamp getViewedAt() {
        return this.viewedAt;
    }

    public void setViewedAt(java.sql.Timestamp viewedAt) {
        this.viewedAt = viewedAt;
    }
}
