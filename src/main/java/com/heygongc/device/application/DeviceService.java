package com.heygongc.device.application;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.device.domain.type.CameraModeType;
import com.heygongc.device.domain.type.ControlType;
import com.heygongc.device.domain.type.SensitivityType;
import com.heygongc.device.exception.DeviceNotFoundException;
import com.heygongc.device.presentation.request.DeviceInfoRequest;
import com.heygongc.global.infra.FirebaseCloudMessaging;
import com.heygongc.global.utils.EnumUtils;
import com.heygongc.user.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class DeviceService{

    private final DeviceRepository deviceRepository;
    private final FirebaseCloudMessaging firebaseCloudMessaging;


    public DeviceService(DeviceRepository deviceRepository, FirebaseCloudMessaging firebaseCloudMessaging){
        this.deviceRepository = deviceRepository;
        this.firebaseCloudMessaging = firebaseCloudMessaging;

    }


    public List<Device> getAllDevices(Long userSeq) {
        return deviceRepository.findAllByUserSeq(userSeq);
    }

    public void subscribeDevice(DeviceInfoRequest request) {
        Device device = deviceRepository.findByDeviceId(request.deviceId())
                .orElseThrow(DeviceNotFoundException::new);
        device.changeDeviceName(request.deviceName());
        device.pairDevice();
        deviceRepository.save(device);

        HashMap<String, String> data = new HashMap<>();
        data.put("action", "1");
        firebaseCloudMessaging.sendMessage(device.getFcmToken(), "QR 코드 숨기기", data);
    }

    @Transactional
    public void updateDevice(String deviceId, String deviceName, User user) {
        Device device = deviceRepository.findMyDevice(deviceId, user)
                .orElseThrow(DeviceNotFoundException::new); // Optional을 사용한 처리

        device.changeDeviceName(deviceName);
        deviceRepository.save(device);

    }

    @Transactional
    public void disconnectDevice(List<String> deviceIds, User user) {
        List<Device> devices = deviceRepository.findAllDevices(deviceIds, user);

        HashMap<String, String> data = new HashMap<>();
        data.put("action", "2");

        // 각 디바이스 상태 업데이트
        devices.forEach(device -> {
            device.unpairDevice();
            deviceRepository.save(device);
            firebaseCloudMessaging.sendMessage(device.getFcmToken(), "QR 코드 보이기", data);

        });
    }

    @Transactional
    public void changeDeviceSetting(String deviceId, String sensitivity, String cameraMode, User user){
        Device device = deviceRepository.findMyDevice(deviceId, user)
                .orElseThrow(DeviceNotFoundException::new); // Optional을 사용한 처리

        device.changeDeviceSetting(EnumUtils.getEnumConstant(SensitivityType.class, sensitivity),
                EnumUtils.getEnumConstant(CameraModeType.class, cameraMode));

        deviceRepository.save(device);

    }

    @Transactional
    public void controlDevice(String deviceId, String controlType, User user){

        Device device = deviceRepository.findMyDevice(deviceId, user)
                .orElseThrow(DeviceNotFoundException::new); // Optional을 사용한 처리

        ControlType type = EnumUtils.getEnumConstant(ControlType.class, controlType);

        HashMap<String, String> data = new HashMap<>();


        switch (type) {

            case SOUNDON:
                device.soundModeOn();
                data.put("action", "3");
                firebaseCloudMessaging.sendMessage(device.getFcmToken(), "소리 감지 모드 ON", data);
                break;
            case SOUNDOFF:
                device.soundModeOff();
                data.put("action", "4");
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

        deviceRepository.save(device);
    }

}
