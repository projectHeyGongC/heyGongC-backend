package com.heygongc.video.domain.repository;

import com.heygongc.video.domain.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long>, CustomVideoRepository{
}
