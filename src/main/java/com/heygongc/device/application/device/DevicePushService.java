package com.heygongc.device.application.device;

import com.heygongc.global.infra.FirebaseCloudMessaging;
import com.heygongc.global.infra.FirebaseData;
import com.heygongc.global.type.FcmActionType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DevicePushService {

    private final FirebaseCloudMessaging firebaseCloudMessaging;

    public DevicePushService(FirebaseCloudMessaging firebaseCloudMessaging) {
        this.firebaseCloudMessaging = firebaseCloudMessaging;
    }

    public void controlDevices(FcmActionType type, String controlMode, List<String> fcmTokens) throws Exception {
        FirebaseData data = FirebaseData.builder()
                .tokens(fcmTokens)
                .body(type.body())
                .action(type.name())
                .content(controlMode)
                .build();
        firebaseCloudMessaging.sendMessage(data);
    }

    public void controlDevice(FcmActionType type, String controlMode, String fcmToken) throws Exception {
        FirebaseData data = FirebaseData.builder()
                .token(fcmToken)
                .body(type.body())
                .action(type.name())
                .content(controlMode)
                .build();
        firebaseCloudMessaging.sendMessage(data);
    }

    public void showQRCode(List<String> fcmTokens) throws Exception {
        controlDevices(FcmActionType.QR_CODE, "ON", fcmTokens);
    }

    public void hideQRCode(String fcmToken) throws Exception {
        controlDevice(FcmActionType.QR_CODE, "OFF", fcmToken);
    }

    public void changeSensitivity(String sensitivity, String fcmToken) throws Exception {
        controlDevice(FcmActionType.SENSITIVITY, sensitivity, fcmToken);
    }

    public void changeCameraOrientation(String cameraOrientation, String fcmToken) throws Exception {
        controlDevice(FcmActionType.CAMERA_ORIENTATION, cameraOrientation, fcmToken);
    }
}
