package com.heygongc.video.domain.repository.video;

import com.heygongc.video.domain.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoJpaRepository extends JpaRepository<Video, Long> {
}
