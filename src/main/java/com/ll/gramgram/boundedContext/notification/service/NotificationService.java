package com.ll.gramgram.boundedContext.notification.service;

import com.ll.gramgram.base.event.EventAfterLike;
import com.ll.gramgram.base.event.EventReadNotifications;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Notification findById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public List<Notification> findByToInstaMember(InstaMember toInstaMember) {
        return notificationRepository.findByToInstaMember(toInstaMember);
    }

    public RsData<Notification> whenAfterLike(LikeablePerson likeablePerson) {
        InstaMember fromInstaMember = likeablePerson.getFromInstaMember();
        InstaMember toInstaMember = likeablePerson.getToInstaMember();

        Notification notification = Notification
                .builder()
                .toInstaMember(toInstaMember)
                .fromInstaMember(fromInstaMember)
                .typeCode("Like")
                .oldGender(null)
                .oldAttractiveTypeCode(0)
                .newGender(fromInstaMember.getGender())
                .newAttractiveTypeCode(likeablePerson.getAttractiveTypeCode())
                .build();

        notificationRepository.save(notification);

        return RsData.of("S-1", "알림 메세지가 생성되었습니다.", notification);
    }

    @Transactional
    public void read(Notification notification) {
        publisher.publishEvent(new EventReadNotifications(this, notification));
    }

    public void readNotification(Notification notification) {
        notification.setReadDate();
    }


    public void whenAfterModifyAttractiveType(LikeablePerson likeablePerson, int oldAttractiveTypeCode) {
        InstaMember fromInstaMember = likeablePerson.getFromInstaMember();
        InstaMember toInstaMember = likeablePerson.getToInstaMember();

        Notification notification = Notification
                .builder()
                .readDate(null)
                .toInstaMember(toInstaMember)
                .fromInstaMember(fromInstaMember)
                .typeCode("Modify")
                .oldGender(null)
                .oldAttractiveTypeCode(oldAttractiveTypeCode)
                .newGender(null)
                .newAttractiveTypeCode(likeablePerson.getAttractiveTypeCode())
                .build();

        notificationRepository.save(notification);
    }
}
