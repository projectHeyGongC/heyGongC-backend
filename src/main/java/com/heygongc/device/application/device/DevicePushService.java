package com.heygongc.device.application.device;

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

    public void controlDevice(String controlType, String controlMode, String fcmToken) throws Exception {
        FcmActionType type = EnumUtils.getEnumConstant(FcmActionType.class, controlType);
        if (type == null) {
            throw new IllegalArgumentException("Invalid control type: " + controlType);
        }

        FirebaseData data = FirebaseData.builder()
                .token(fcmToken)
                .body(type.body())
                .action(type.name())
                .content(controlMode)
                .build();
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

    public void changeCameraOrientation(String cameraOrientation, String fcmToken) throws Exception {
        FirebaseData data = FirebaseData.builder()
                .token(fcmToken)
                .body(FcmActionType.CAMERA_ORIENTATION.body())
                .action(FcmActionType.CAMERA_ORIENTATION.name())
                .content(cameraOrientation)
                .build();
        firebaseCloudMessaging.sendMessage(data);
    }
}
