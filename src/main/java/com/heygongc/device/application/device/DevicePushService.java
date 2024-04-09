package com.heygongc.device.application.device;

import com.heygongc.device.domain.type.ControlType;
import com.heygongc.global.infra.FirebaseCloudMessaging;
import com.heygongc.global.infra.FirebaseData;
import com.heygongc.global.type.MessageType;
import com.heygongc.global.utils.EnumUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DevicePushService {

    private final FirebaseCloudMessaging firebaseCloudMessaging;

    public DevicePushService(FirebaseCloudMessaging firebaseCloudMessaging) {
        this.firebaseCloudMessaging = firebaseCloudMessaging;
    }

    public void hideQRCode(String fcmToken) {
        FirebaseData data = FirebaseData.builder()
                .body("QR 코드 숨기기")
                .action(MessageType.HIDEQR.toString())
                .build();
        firebaseCloudMessaging.sendMessage(fcmToken, data);
    }

    public void showQRCode(List<String> fcmTokens) {
        FirebaseData data = FirebaseData.builder()
                .body("QR 코드 보이기")
                .action(MessageType.SHOWQR.toString())
                .build();
        firebaseCloudMessaging.sendMessage(fcmTokens, data);
    }

    public void controlDevice(String controlType, String fcmToken) {
        ControlType type = EnumUtils.getEnumConstant(ControlType.class, controlType);

        FirebaseData data;
        switch (type != null ? type : ControlType.NULL) {
            case SOUNDON:
                data = FirebaseData.builder()
                        .body("소리 감지 모드 ON")
                        .action(MessageType.SOUNDMODEON.toString())
                        .build();
                break;
            case SOUNDOFF:
                data = FirebaseData.builder()
                        .body("소리 감지 모드 OFF")
                        .action(MessageType.SOUNDMODEOFF.toString())
                        .build();
                break;
            case STREAMON:
                data = FirebaseData.builder()
                        .body("스트리밍 모드 ON")
                        .action(MessageType.STREAMON.toString())
                        .build();
                break;
            case STREAMOFF:
                data = FirebaseData.builder()
                        .body("스트리밍 모드 OFF")
                        .action(MessageType.STREAMOFF.toString())
                        .build();
                break;
            default:
                throw new IllegalArgumentException("Invalid control type: " + controlType);
        }

        firebaseCloudMessaging.sendMessage(fcmToken, data);
    }
}
