package com.heygongc.video.domain.repository.video;

import com.heygongc.video.domain.entity.Video;

import java.text.ParseException;
import java.util.Optional;

public interface VideoRepository {
    Video save(Video video);
    Optional<Video> getVideo(Long userSeq, String requestAt) throws ParseException;
}
