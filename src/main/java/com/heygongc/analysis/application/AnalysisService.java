package com.heygongc.analysis.application;

import com.heygongc.analysis.presentation.response.AnalysisDetailResponse;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.repository.NotificationRepository;
import com.heygongc.user.domain.entity.User;
import com.heygongc.video.domain.entity.Video;
import com.heygongc.video.domain.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class AnalysisService {

    private final NotificationRepository notificationRepository;
    private final VideoRepository videoRepository;

    public AnalysisService(NotificationRepository notificationRepository, VideoRepository videoRepository) {
        this.notificationRepository = notificationRepository;
        this.videoRepository = videoRepository;
    }

    public List<Notification> getNotifications(User user, String requestAt) throws ParseException {

        return notificationRepository.findAllByUserSeqAndCreatedAt(user.getUserSeq(), requestAt);
    }

    public List<Notification> getNotifications(User user, String deviceId, String requestAt) throws ParseException {

        return notificationRepository.findAllByUserSeqAndDeviceIdAndCreatedAt(user.getUserSeq(), deviceId, requestAt);
    }

    public Optional<Video> getVideo(User user, String requestAt) throws ParseException {

        return videoRepository.findOneByUserSeqAndCreatedAt(user.getUserSeq(), requestAt);
    }
}
