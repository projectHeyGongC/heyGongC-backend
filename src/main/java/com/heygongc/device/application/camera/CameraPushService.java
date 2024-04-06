package com.heygongc.device.application.camera;

import com.heygongc.global.infra.FirebaseCloudMessaging;
import com.heygongc.global.type.MessageType;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CameraPushService {

    private final FirebaseCloudMessaging firebaseCloudMessaging;

    public CameraPushService(FirebaseCloudMessaging firebaseCloudMessaging) {
        this.firebaseCloudMessaging = firebaseCloudMessaging;
    }

    public void alertSoundAlarm(String fcmToken) {
        HashMap<String, String> data = new HashMap<>();
        data.put("action", String.valueOf(MessageType.SOUNDALERT));
        firebaseCloudMessaging.sendMessage(fcmToken, "소리 알람 보내기", data);
    }
}
