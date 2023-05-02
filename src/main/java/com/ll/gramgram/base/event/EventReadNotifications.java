package com.ll.gramgram.base.event;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class EventReadNotifications extends ApplicationEvent {
    private final Notification notification;

    public EventReadNotifications(Object source, Notification notification) {
        super(source);
        this.notification = notification;
    }
}
