package com.heygongc.video.setup;

import com.heygongc.common.ApplicationContextProvider;
import com.heygongc.user.domain.entity.User;
import com.heygongc.video.domain.entity.Video;
import com.heygongc.video.domain.repository.VideoRepository;

public class VideoSetup {

    private static final VideoRepository videoRepository = ApplicationContextProvider.getBean(VideoRepository.class);

    public static Video saveVideo(User user) {
        return videoRepository.save(Video.builder()
                .user(user)
                .name("videoName" + ((int) (Math.random() * 9999) + 1))
                .url("videoUrl" + ((int) (Math.random() * 9999) + 1))
                .build());
    }
}
