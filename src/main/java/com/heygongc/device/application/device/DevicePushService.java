package com.heygongc.device.application.device;

import com.heygongc.device.domain.entity.Device;
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

    public void hideQRCode(String fcmToken){
        HashMap<String, String> data = new HashMap<>();
        data.put("action", String.valueOf(MessageType.HIDEQR));
        firebaseCloudMessaging.sendMessage(fcmToken, "QR 코드 숨기기", data);
    }

    public void showQRCode(List<String> fcmTokens){
        HashMap<String, String> data = new HashMap<>();
        data.put("action", String.valueOf(MessageType.SHOWQR));
        if(!fcmTokens.isEmpty()) {
            firebaseCloudMessaging.sendMessage(fcmTokens, "QR 코드 보이기", data);
        }
    }

    public void controlDevice(String controlType, Device device){
        ControlType type = EnumUtils.getEnumConstant(ControlType.class, controlType);

        HashMap<String, String> data = new HashMap<>();


        switch (type) {

            case SOUNDON:
                device.soundModeOn();
                data.put("action", String.valueOf(MessageType.SOUNDMODEON));
                firebaseCloudMessaging.sendMessage(device.getFcmToken(), "소리 감지 모드 ON", data);
                break;
            case SOUNDOFF:
                device.soundModeOff();
                data.put("action", String.valueOf(MessageType.SOUNDMODEOFF));
                firebaseCloudMessaging.sendMessage(device.getFcmToken(), "소리 감지 모드 OFF", data);
                break;
            case STREAMON:
                device.startStreaming();
                break;
            case STREAMOFF:
                device.stopStreaming();
                break;
            default:
                throw new IllegalArgumentException("Invalid control type: " + controlType);
        }
    }
}
