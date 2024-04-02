package com.heygongc.user.setup;

import com.heygongc.common.ApplicationContextProvider;
import com.heygongc.global.type.OsType;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
import com.heygongc.user.domain.type.SnsType;

public class UserSetup {

    private static final UserRepository userRepository = ApplicationContextProvider.getBean(UserRepository.class);

    public static User saveGoogleUser() {
        return userRepository.save(User.createUser()
                .deviceId("deviceId" + ((int) (Math.random() * 9999) + 1))
                .snsId("snsId" + ((int) (Math.random() * 9999) + 1))
                .snsType(SnsType.GOOGLE)
                .deviceOs(OsType.valueOf("AOS"))
                .email("test" + ((int) (Math.random() * 9999) + 1) + "@test.com")
                .alarm(true)
                .ads(true)
                .build());
    }

    public static User saveAppleUser() {
        return userRepository.save(User.createUser()
                .deviceId("deviceId" + ((int) (Math.random() * 9999) + 1))
                .snsId("snsId" + ((int) (Math.random() * 9999) + 1))
                .snsType(SnsType.APPLE)
                .deviceOs(OsType.valueOf("AOS"))
                .email("test" + ((int) (Math.random() * 9999) + 1) + "@test.com")
                .alarm(true)
                .ads(true)
                .build());
    }
}
