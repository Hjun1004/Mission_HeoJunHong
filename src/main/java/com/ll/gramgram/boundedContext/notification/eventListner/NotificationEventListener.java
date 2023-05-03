package com.ll.gramgram.boundedContext.notification.eventListner;

import com.ll.gramgram.base.event.*;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;

    @EventListener
    public void listen(EventAfterLike event) {
        notificationService.whenAfterLike(event.getLikeablePerson());
    }


    @EventListener
    public void listen(EventReadNotifications event) {
        notificationService.readNotification(event.getNotification());
    }

    @EventListener
    @Transactional
    public void listen(EventAfterModifyAttractiveType event) {
        notificationService.whenAfterModifyAttractiveType(event.getLikeablePerson(), event.getOldAttractiveTypeCode());
    }

}
