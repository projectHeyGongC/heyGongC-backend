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

    public void hideQRCode(String fcmToken) throws Exception {
        FirebaseData data = FirebaseData.builder()
                .token(fcmToken)
                .body("QR 코드 숨기기")
                .action(MessageType.HIDEQR.toString())
                .build();
        firebaseCloudMessaging.sendMessage(data);
    }

    public void showQRCode(List<String> fcmTokens) throws Exception {
        FirebaseData data = FirebaseData.builder()
                .tokens(fcmTokens)
                .body("QR 코드 보이기")
                .action(MessageType.SHOWQR.toString())
                .build();
        firebaseCloudMessaging.sendMessage(data);
    }

    public void controlDevice(String controlType, String fcmToken) throws Exception {
        ControlType type = EnumUtils.getEnumConstant(ControlType.class, controlType);

        FirebaseData data;
        switch (type != null ? type : ControlType.NULL) {
            case SOUNDON:
                data = FirebaseData.builder()
                        .token(fcmToken)
                        .body("소리 감지 모드 ON")
                        .action(MessageType.SOUNDMODEON.toString())
                        .build();
                break;
            case SOUNDOFF:
                data = FirebaseData.builder()
                        .token(fcmToken)
                        .body("소리 감지 모드 OFF")
                        .action(MessageType.SOUNDMODEOFF.toString())
                        .build();
                break;
            case STREAMON:
                data = FirebaseData.builder()
                        .token(fcmToken)
                        .body("스트리밍 모드 ON")
                        .action(MessageType.STREAMON.toString())
                        .build();
                break;
            case STREAMOFF:
                data = FirebaseData.builder()
                        .token(fcmToken)
                        .body("스트리밍 모드 OFF")
                        .action(MessageType.STREAMOFF.toString())
                        .build();
                break;
            default:
                throw new IllegalArgumentException("Invalid control type: " + controlType);
        }

        firebaseCloudMessaging.sendMessage(data);
    }

    public void changeSensitivity(String sensitivity, String fcmToken) throws Exception {
        FirebaseData data = FirebaseData.builder()
                .token(fcmToken)
                .body("소리 민감도 변경하기")
                .action(sensitivity)
                .build();
        firebaseCloudMessaging.sendMessage(data);
    }

    public void changeCameraMode(String cameraMode, String fcmToken) throws Exception {
        FirebaseData data = FirebaseData.builder()
                .token(fcmToken)
                .body("카메라 전/후면 변경하기")
                .action(cameraMode)
                .build();
        firebaseCloudMessaging.sendMessage(data);
    }
}
