package com.heygongc.device.application.device;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.device.DeviceRepository;
import com.heygongc.device.domain.type.CameraOrientationType;
import com.heygongc.device.domain.type.SensitivityType;
import com.heygongc.device.exception.DeviceNotFoundException;
import com.heygongc.global.type.FcmActionType;
import com.heygongc.global.utils.EnumUtils;
import com.heygongc.user.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService{

    private final DeviceRepository deviceRepository;
    private final DevicePushService devicePushService;

    public DeviceService(DeviceRepository deviceRepository, DevicePushService devicePushService) {
        this.deviceRepository = deviceRepository;
        this.devicePushService = devicePushService;
    }

    public Device getDevice(String deviceId) {
        return deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(DeviceNotFoundException::new);
    }

    public Device getDevice(String deviceId, User user) {
        return deviceRepository.findMyDevice(deviceId, user.getUserSeq())
                .orElseThrow(DeviceNotFoundException::new);
    }

    public List<Device> getDevices(List<String> deviceIds, User user) {
        return deviceRepository.getDevices(deviceIds, user.getUserSeq());
    }

    public List<Device> getDevices(User user) {
        return deviceRepository.getDevices(user.getUserSeq());
    }

    @Transactional
    public void subscribeDevice(String deviceId, String deviceName, User user) throws Exception {
        Device device = getDevice(deviceId);
        device.changeDeviceName(deviceName);
        device.connectDevice();
        device.setDeviceOwner(user.getUserSeq());
        devicePushService.hideQRCode(device.getFcmToken());
    }

    @Transactional
    public void changeDeviceName(String deviceId, String deviceName, User user) {
        Device device = getDevice(deviceId, user);
        device.changeDeviceName(deviceName);
    }

    @Transactional
    public void disconnectDevices(List<String> deviceIds, User user) throws Exception {
        List<Device> devices = getDevices(deviceIds, user);
        List<String> tokens = devices.stream()
                .map(Device::getFcmToken)
                .toList();
        devices.forEach(Device::disConnectDevice);
        devicePushService.showQRCode(tokens);
    }

    @Transactional
    public void controlDevice(String deviceId, User user, String controlType, String controlMode) throws Exception {
        Device device = getDevice(deviceId, user);
        FcmActionType type = EnumUtils.getEnumConstant(FcmActionType.class, controlType);
        if (type == null) {
            throw new IllegalArgumentException("Invalid control type: " + controlType);
        }

        switch (type) {
            case SENSITIVITY:
                device.setSensitivity(EnumUtils.getEnumConstant(SensitivityType.class, controlMode));
                break;
            case CAMERA_ORIENTATION:
                device.setCameraOrientation(EnumUtils.getEnumConstant(CameraOrientationType.class, controlMode));
                break;
            case SOUND_SENSING:
                device.setSoundSensing(controlMode);
                break;
            case STREAM:
                device.setStreamingMode(controlMode);
                break;
        }

        devicePushService.controlDevice(type, controlMode, device.getFcmToken());
    }
}
