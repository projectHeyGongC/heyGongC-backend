package com.heygongc.video.domain.repository;

import com.heygongc.video.domain.entity.Video;

import java.text.ParseException;
import java.util.Optional;

public interface CustomVideoRepository {
    Optional<Video> findOneByUserSeqAndCreatedAt(Long userSeq, String requestAt) throws ParseException;
}
