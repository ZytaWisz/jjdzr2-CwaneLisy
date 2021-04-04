package com.infoshare.service;

public enum EventType {
    ADD("ADD"),
    UPDATE("UPDATE");

    public String type;

    EventType(String type) {
        this.type = type;
    }

    public String getEventType() {
        return type;
    }
}
