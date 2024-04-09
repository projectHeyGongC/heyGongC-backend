package com.heygongc.device.application.device;

import com.heygongc.device.domain.type.ControlType;
import com.heygongc.global.infra.FirebaseCloudMessaging;
import com.heygongc.global.type.MessageType;
import com.heygongc.global.utils.EnumUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class DevicePushService {

    private final FirebaseCloudMessaging firebaseCloudMessaging;

    public DevicePushService(FirebaseCloudMessaging firebaseCloudMessaging) {
        this.firebaseCloudMessaging = firebaseCloudMessaging;
    }

    public void hideQRCode(String fcmToken) {
        HashMap<String, String> data = new HashMap<>();
        data.put("action", String.valueOf(MessageType.HIDEQR));
        firebaseCloudMessaging.sendMessage(fcmToken, "QR 코드 숨기기", data);
    }

    public void showQRCode(List<String> fcmTokens) {
        HashMap<String, String> data = new HashMap<>();
        data.put("action", String.valueOf(MessageType.SHOWQR));
        if(!fcmTokens.isEmpty()) {
            firebaseCloudMessaging.sendMessage(fcmTokens, "QR 코드 보이기", data);
        }
    }

    public void controlDevice(String controlType, String fcmToken) {
        ControlType type = EnumUtils.getEnumConstant(ControlType.class, controlType);
        if (type == null) {
            throw new IllegalArgumentException("Invalid control type: " + controlType);
        }

        HashMap<String, String> data = new HashMap<>();
        switch (type) {
            case SOUNDON:
                data.put("action", String.valueOf(MessageType.SOUNDMODEON));
                firebaseCloudMessaging.sendMessage(fcmToken, "소리 감지 모드 ON", data);
                break;
            case SOUNDOFF:
                data.put("action", String.valueOf(MessageType.SOUNDMODEOFF));
                firebaseCloudMessaging.sendMessage(fcmToken, "소리 감지 모드 OFF", data);
                break;
            case STREAMON:
                data.put("action", String.valueOf(MessageType.STREAMON));
                firebaseCloudMessaging.sendMessage(fcmToken, "스트리밍 모드 ON", data);
                break;
            case STREAMOFF:
                data.put("action", String.valueOf(MessageType.STREAMOFF));
                firebaseCloudMessaging.sendMessage(fcmToken, "스트리밍 모드 OFF", data);
                break;
            default:
                throw new IllegalArgumentException("Invalid control type: " + controlType);
        }
    }
}
