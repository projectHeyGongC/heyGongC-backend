package com.heygongc.device.application.device;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.device.domain.type.CameraModeType;
import com.heygongc.device.domain.type.ControlType;
import com.heygongc.device.domain.type.SensitivityType;
import com.heygongc.device.exception.DeviceNotFoundException;
import com.heygongc.device.presentation.request.device.DeviceInfoRequest;
import com.heygongc.global.utils.EnumUtils;
import com.heygongc.user.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService{

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;

    }

    public Device getDevice(String deviceId) {
        return deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(DeviceNotFoundException::new);
    }

    public Device getDevice(String deviceId, User user) {
        return deviceRepository.findMyDevice(deviceId, user)
                .orElseThrow(DeviceNotFoundException::new);
    }

    public List<Device> getDevices(List<String> deviceIds, User user) {
        return deviceRepository.findAllDevices(deviceIds, user);
    }

    public List<Device> getDevices(User user) {
        return deviceRepository.findAllByUserSeq(user.getUserSeq());
    }

    @Transactional
    public void subscribeDevice(DeviceInfoRequest request, User user) {
        Device device = getDevice(request.deviceId());
        device.changeDeviceName(request.deviceName());
        device.connectDevice();
        device.setDeviceOwner(user.getUserSeq());
    }

    @Transactional
    public void updateDevice(String deviceId, String deviceName, User user) {
        Device device = getDevice(deviceId, user);

        device.changeDeviceName(deviceName);
    }

    @Transactional
    public void disconnectDevices(List<Device> devices) {
        devices.forEach(Device::disConnectDevice);
    }

    @Transactional
    public void changeDeviceSetting(String deviceId, String sensitivity, String cameraMode, User user) {
        Device device = getDevice(deviceId, user);

        device.changeDeviceSetting(EnumUtils.getEnumConstant(SensitivityType.class, sensitivity),
                EnumUtils.getEnumConstant(CameraModeType.class, cameraMode));
    }

    @Transactional
    public void controlDevice(String deviceId, User user, String controlType) {
        Device device = getDevice(deviceId, user);
        ControlType type = EnumUtils.getEnumConstant(ControlType.class, controlType);

        switch (type != null ? type : ControlType.NULL) {
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
