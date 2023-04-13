package com.chatvia.chatapp.WS.Event;

import com.google.gson.annotations.SerializedName;

public class WSEventReceiver {

    @SerializedName("command")
    private String event;

    public WSEventReceiver(String event) {
        this.event = event;
    }

    public WSEventReceiver() {
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}