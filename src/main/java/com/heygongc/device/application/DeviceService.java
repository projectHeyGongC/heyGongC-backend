package com.heygongc.device.application;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.device.domain.type.CameraModeType;
import com.heygongc.device.domain.type.ControlType;
import com.heygongc.device.domain.type.SensitivityType;
import com.heygongc.device.exception.DeviceNotFoundException;
import com.heygongc.device.presentation.request.ControlTypeRequest;
import com.heygongc.device.presentation.request.DeviceInfoRequest;
import com.heygongc.global.utils.EnumUtils;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
import com.heygongc.user.domain.type.SnsType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService{

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository){
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }



    public Device getDevice(String deviceId, User user) {
        Device device = deviceRepository.findMyDevice(deviceId, user)
                .orElseThrow(DeviceNotFoundException::new); // Optional을 사용한 처리
        return device;
    }

    public List<Device> getAllDevices(Long userSeq) {
        return deviceRepository.findAllByUserSeq(userSeq);
    }

    public Device subscribeDevice(DeviceInfoRequest request) {
        Device device = deviceRepository.findByDeviceId(request.deviceId())
                .orElseThrow(DeviceNotFoundException::new);
        device.changeDeviceName(request.deviceName());
        device.pairDevice();

        return device;
    }

    @Transactional
    public Device updateDevice(String deviceId, String deviceName, User user) {
        Device device = deviceRepository.findMyDevice(deviceId, user)
                .orElseThrow(DeviceNotFoundException::new); // Optional을 사용한 처리

            device.changeDeviceName(deviceName);
            return device;

    }

    @Transactional
    public void disconnectDevice(List<String> deviceIds, User user) {
        List<Device> devices = deviceRepository.updateCameraDevices(deviceIds, user);
        // 각 디바이스 상태 업데이트
        devices.forEach(device -> {
            device.unpairDevice();
        });

    }

    @Transactional
    public void changeDeviceSetting(String deviceId, String sensitivity, String cameraMode, User user){
        Device device = deviceRepository.findMyDevice(deviceId, user)
                .orElseThrow(DeviceNotFoundException::new); // Optional을 사용한 처리

        device.changeDeviceSetting(EnumUtils.getEnumConstant(SensitivityType.class, sensitivity),
                EnumUtils.getEnumConstant(CameraModeType.class, cameraMode));
    }

    @Transactional
    public void controlDevice(String deviceId, String controlType, User user){

        Device device = deviceRepository.findMyDevice(deviceId, user)
                .orElseThrow(DeviceNotFoundException::new); // Optional을 사용한 처리

        ControlType type = EnumUtils.getEnumConstant(ControlType.class, controlType);

        switch (type) {
            case SOUNDON:
                device.soundModeOn();
                break;
            case SOUNDOFF:
                device.soundModeOff();
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
