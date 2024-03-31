package com.heygongc.video.application;

import com.heygongc.user.domain.entity.User;
import com.heygongc.video.domain.entity.Video;
import com.heygongc.video.domain.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public Optional<Video> getVideo(User user, String requestAt) throws ParseException {

        return videoRepository.findOneByUserSeqAndCreatedAt(user.getUserSeq(), requestAt);
    }
}
