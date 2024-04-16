package com.heygongc.device.application.device;

import com.heygongc.device.domain.type.ControlType;
import com.heygongc.global.infra.FirebaseCloudMessaging;
import com.heygongc.global.infra.FirebaseData;
import com.heygongc.global.type.FcmActionType;
import com.heygongc.global.utils.EnumUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DevicePushService {

    private final FirebaseCloudMessaging firebaseCloudMessaging;

    public DevicePushService(FirebaseCloudMessaging firebaseCloudMessaging) {
        this.firebaseCloudMessaging = firebaseCloudMessaging;
    }

    public void showQRCode(List<String> fcmTokens) throws Exception {
        FirebaseData data = FirebaseData.builder()
                .tokens(fcmTokens)
                .body(FcmActionType.QR_CODE.body())
                .action(FcmActionType.QR_CODE.name())
                .content("ON")
                .build();
        firebaseCloudMessaging.sendMessage(data);
    }

    public void hideQRCode(String fcmToken) throws Exception {
        FirebaseData data = FirebaseData.builder()
                .token(fcmToken)
                .body(FcmActionType.QR_CODE.body())
                .action(FcmActionType.QR_CODE.name())
                .content("OFF")
                .build();
        firebaseCloudMessaging.sendMessage(data);
    }

    public void controlDevice(String controlType, String fcmToken) throws Exception {
        ControlType type = EnumUtils.getEnumConstant(ControlType.class, controlType);

        FirebaseData data = switch (type != null ? type : ControlType.NULL) {
            case SOUNDON -> FirebaseData.builder()
                    .token(fcmToken)
                    .body(FcmActionType.SOUND_SENSING.body())
                    .action(FcmActionType.SOUND_SENSING.name())
                    .content("ON")
                    .build();
            case SOUNDOFF -> FirebaseData.builder()
                    .token(fcmToken)
                    .body(FcmActionType.SOUND_SENSING.body())
                    .action(FcmActionType.SOUND_SENSING.name())
                    .content("OFF")
                    .build();
            case STREAMON -> FirebaseData.builder()
                    .token(fcmToken)
                    .body(FcmActionType.STREAM.body())
                    .action(FcmActionType.STREAM.name())
                    .content("ON")
                    .build();
            case STREAMOFF -> FirebaseData.builder()
                    .token(fcmToken)
                    .body(FcmActionType.STREAM.body())
                    .action(FcmActionType.STREAM.name())
                    .content("OFF")
                    .build();
            default -> throw new IllegalArgumentException("Invalid control type: " + controlType);
        };

        firebaseCloudMessaging.sendMessage(data);
    }

    public void changeSensitivity(String sensitivity, String fcmToken) throws Exception {
        FirebaseData data = FirebaseData.builder()
                .token(fcmToken)
                .body(FcmActionType.SENSITIVITY.body())
                .action(FcmActionType.SENSITIVITY.name())
                .content(sensitivity)
                .build();
        firebaseCloudMessaging.sendMessage(data);
    }

    public void changeCameraMode(String cameraMode, String fcmToken) throws Exception {
        FirebaseData data = FirebaseData.builder()
                .token(fcmToken)
                .body(FcmActionType.CAMERA_ORIENTATION.body())
                .action(FcmActionType.CAMERA_ORIENTATION.name())
                .content(cameraMode)
                .build();
        firebaseCloudMessaging.sendMessage(data);
    }
}
