package com.heygongc.device.application;

import com.heygongc.device.domain.Device;
import com.heygongc.device.domain.DeviceRepository;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService{

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<Device> getDevice(Long deviceSeq) {

        return deviceRepository.findById(deviceSeq);
    }

    @Override
    public List<Device> getAllDevices(Long userSeq) {
        return deviceRepository.findAllByUserSeq(userSeq);
    }


    public Device addDevice(Long userSeq, String qrCode, String deviceName) {
        String[] parts = qrCode.split("_");
        Long parsedDeviceSeq = Long.parseLong(parts[0]);
        String type = parts[1];

        User user = userRepository.findById(userSeq).orElseThrow(() -> new RuntimeException("해당 유저 없음"));

        Device device = Device.builder()
                .deviceSeq(parsedDeviceSeq)
                .type(type)
                .name(deviceName)
                .soundMode(false)
                .sensitivity(DeviceSensitivityEnum.MEDIUM)
                .soundActive(false)
                .streamActive(false)
                .frontCamera(false)
                .user(user)
                .build();

        return deviceRepository.save(device);
    }

    @Override
    public Device updateDevice(Long deviceSeq, Long userSeq, String deviceName) {
        Device device = deviceRepository.findById(deviceSeq)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        if (deviceName != null) {
            device.setName(deviceName);
        }

        return device;
    }

    @Override
    public Boolean deleteDevice(Long deviceSeq, Long userSeq) {
        deviceRepository.deleteById(deviceSeq);
        return true;
    }

    @Override
    public Boolean deleteAllDevices(Long userSeq) {
        deviceRepository.deleteAllByUserSeq(userSeq);
        return true;
    }


}
