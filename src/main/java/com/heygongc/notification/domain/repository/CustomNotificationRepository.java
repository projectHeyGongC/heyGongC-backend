package com.heygongc.notification.domain.repository;

import com.heygongc.notification.domain.entity.Notification;

import java.text.ParseException;
import java.util.List;

public interface CustomNotificationRepository {

    List<Notification> findAllByUserSeq(Long userSeq);
    List<Notification> findAllByUserSeqAndCreatedAt(Long userSeq, String requestAt) throws ParseException;
}
