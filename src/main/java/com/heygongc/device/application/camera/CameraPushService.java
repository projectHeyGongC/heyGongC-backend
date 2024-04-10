package com.heygongc.device.application.camera;

import com.heygongc.global.infra.FirebaseCloudMessaging;
import com.heygongc.global.infra.FirebaseData;
import com.heygongc.global.type.MessageType;
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
                .body("소리 알람 보내기")
                .action(MessageType.SOUNDALERT.toString())
                .build();
        firebaseCloudMessaging.sendMessage(data);
    }
}
