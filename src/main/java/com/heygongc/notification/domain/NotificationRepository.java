package com.heygongc.notification.domain;

import com.heygongc.notification.application.NotificationTypeEnum;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByType(String type);

    List<Notification> findTop101ByTypeEnumOrderByCreatedAtDesc(NotificationTypeEnum typeEnum);

    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.created_at < :dateTime")
    void deleteOldNotifications(@Param("dateTime") LocalDateTime dateTime);


}
