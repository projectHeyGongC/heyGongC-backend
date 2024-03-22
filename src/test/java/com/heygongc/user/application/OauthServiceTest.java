package com.heygongc.user.application;

import com.heygongc.common.MockServiceTest;
import com.heygongc.common.ServiceTest;
import com.heygongc.user.application.oauth.OauthFactory;
import com.heygongc.user.application.oauth.OauthUser;
import com.heygongc.user.domain.type.SnsType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
class OauthServiceTest extends MockServiceTest {

    @InjectMocks
    private OauthService oauthService;

    @Mock
    private OauthFactory oauthFactory;

    @Test
    public void 구글API를_통한_계정정보를_전달한다() {
        // given
        var 구글에서_받아온_계정정보 = 구글에서_받아온_계정정보();
        given(oauthFactory.getOauthUser(any(), any()))
                .willReturn(구글에서_받아온_계정정보);

        // when
        OauthUser oauthUser = oauthService.getOAuthUser("GOOGLE", "token");

        // then
        Assertions.assertThat(oauthUser).isNotNull();
        Assertions.assertThat(oauthUser.snsType()).isEqualTo(구글에서_받아온_계정정보.snsType());
        Assertions.assertThat(oauthUser.id()).isEqualTo(구글에서_받아온_계정정보.id());
        Assertions.assertThat(oauthUser.email()).isEqualTo(구글에서_받아온_계정정보.email());
    }

    private OauthUser 구글에서_받아온_계정정보() {
        return new OauthUser(SnsType.GOOGLE, "0000000", "test@gmail.com");
    }
}