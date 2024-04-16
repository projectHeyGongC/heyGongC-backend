package com.heygongc.device.application.camera;

import com.heygongc.global.infra.FirebaseCloudMessaging;
import com.heygongc.global.infra.FirebaseData;
import com.heygongc.global.type.FcmActionType;
import org.springframework.stereotype.Component;

@Component
public class CameraPushService {

    private final FirebaseCloudMessaging firebaseCloudMessaging;

    public CameraPushService(FirebaseCloudMessaging firebaseCloudMessaging) {
        this.firebaseCloudMessaging = firebaseCloudMessaging;
    }

    public void alertSoundAlarm(String fcmToken) throws Exception {
        FirebaseData data = FirebaseData.builder()
                .token(fcmToken)
                .body(FcmActionType.SOUND_ALERT.body())
                .action(FcmActionType.SOUND_ALERT.name())
                .build();
        firebaseCloudMessaging.sendMessage(data);
    }
}
