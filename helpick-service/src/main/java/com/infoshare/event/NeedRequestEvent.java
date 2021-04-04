package com.infoshare.event;

import com.infoshare.domain.NeedRequest;
import com.infoshare.service.EventType;

public class NeedRequestEvent {

    private final EventType eventType;
    private final NeedRequest needRequest;

    public NeedRequestEvent(EventType eventType, NeedRequest needRequest) {
        this.eventType = eventType;
        this.needRequest = needRequest;
    }

    public EventType getEventType() {
        return eventType;
    }

    public NeedRequest getNeedRequest() {
        return needRequest;
    }
}
