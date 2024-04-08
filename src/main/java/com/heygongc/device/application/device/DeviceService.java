package com.heygongc.device.application.device;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.device.domain.type.CameraModeType;
import com.heygongc.device.domain.type.SensitivityType;
import com.heygongc.device.exception.DeviceNotFoundException;
import com.heygongc.device.presentation.request.device.DeviceInfoRequest;
import com.heygongc.global.utils.EnumUtils;
import com.heygongc.user.domain.entity.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceService{

    private final DeviceRepository deviceRepository;


    public DeviceService(DeviceRepository deviceRepository){
        this.deviceRepository = deviceRepository;

    }

    public Device getDevice(String deviceId, User user) {
        return deviceRepository.findMyDevice(deviceId, user)
                .orElseThrow(DeviceNotFoundException::new);
    }

    public List<Device> getAllDevices(Long userSeq) {
        return deviceRepository.findAllByUserSeq(userSeq);
    }

    @Transactional
    public void subscribeDevice(DeviceInfoRequest request, User user) {
        Device device = deviceRepository.findByDeviceId(request.deviceId())
                .orElseThrow(DeviceNotFoundException::new);
        device.changeDeviceName(request.deviceName());
        device.connectDevice();
        device.setDeviceOwner(user.getUserSeq());
    }

    @Transactional
    public void updateDevice(String deviceId, String deviceName, User user) {
        Device device = deviceRepository.findMyDevice(deviceId, user)
                .orElseThrow(DeviceNotFoundException::new);

        device.changeDeviceName(deviceName);

    }

    @Transactional
    public List<String> disconnectDevice(List<String> deviceIds, User user) {
        List<Device> devices = deviceRepository.findAllDevices(deviceIds, user);
        List<String> tokens = devices.stream()
                .map(Device::getFcmToken)
                .collect(Collectors.toList());

        devices.forEach(Device::disConnectDevice);

        return tokens;
    }

    @Transactional
    public void changeDeviceSetting(String deviceId, String sensitivity, String cameraMode, User user){
        Device device = deviceRepository.findMyDevice(deviceId, user)
                .orElseThrow(DeviceNotFoundException::new);

        device.changeDeviceSetting(EnumUtils.getEnumConstant(SensitivityType.class, sensitivity),
                EnumUtils.getEnumConstant(CameraModeType.class, cameraMode));
        
    }

    @Transactional
    public Device controlDevice(String deviceId, User user){

        return deviceRepository.findMyDevice(deviceId, user)
                .orElseThrow(DeviceNotFoundException::new);



    }

}
