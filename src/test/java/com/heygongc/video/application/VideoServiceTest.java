package com.heygongc.video.application;

import com.heygongc.common.ServiceTest;
import com.heygongc.user.domain.entity.User;
import com.heygongc.video.domain.entity.Video;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.heygongc.user.setup.UserSetup.saveGoogleUser;
import static com.heygongc.video.setup.VideoSetup.saveVideo;

@SuppressWarnings("NonAsciiCharacters")
public class VideoServiceTest extends ServiceTest {

    @Autowired
    private VideoService videoService;

    @Test
    @DisplayName("비디오를 조회한다")
    void getVideo() throws ParseException {
        // when
        User 구글테스트계정 = saveGoogleUser();
        Video 비디오 = saveVideo(구글테스트계정);

        // when
        LocalDateTime now = LocalDateTime.now();
        String today = now.getYear() +
                "-" + String.format("%02d", now.getMonthValue()) +
                "-" + now.getDayOfMonth();
        Optional<Video> video = videoService.getVideo(구글테스트계정, today);

        // then
        Assertions.assertThat(video).isNotNull();
        Assertions.assertThat(video.get().getVideo_seq()).isNotNull();

    }
}
