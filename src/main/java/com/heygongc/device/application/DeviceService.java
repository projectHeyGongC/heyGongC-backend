package com.heygongc.device.application;

import com.heygongc.device.domain.Device;
import com.heygongc.device.domain.DeviceRepository;
import com.heygongc.device.exception.DeviceNotFoundException;
import com.heygongc.device.presentation.request.DeviceInfoRequest;
import com.heygongc.device.presentation.response.DeviceResponse;
import com.heygongc.global.error.exception.ForbiddenException;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
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



    public Device getDevice(Long deviceSeq, User user) {
        Device device = deviceRepository.findDeviceBySeqAndUser(deviceSeq, user);
        if (device == null) {
            throw new DeviceNotFoundException();
        }
        return device;
    }

    public List<Device> getAllDevices(Long userSeq) {
        return deviceRepository.findAllByUserSeq(userSeq);
    }


    public Device addDevice(User user, DeviceInfoRequest request) {

        String[] parts = request.deviceQR().split("_");
        Long parsedDeviceSeq = Long.parseLong(parts[0]);
        String type = parts[1];

        Device device = Device.builder()
                .deviceSeq(parsedDeviceSeq)
                .type(type)
                .name(request.name())
                .soundMode(false)
                .sensitivity(DeviceSensitivityEnum.MEDIUM)
                .soundActive(false)
                .streamActive(false)
                .frontCamera(false)
                .user(user)
                .build();
        deviceRepository.save(device);





        return device;
    }

    @Transactional
    public Device updateDevice(Long deviceSeq, String deviceName, User user) {
        Device device = deviceRepository.findDeviceBySeqAndUser(deviceSeq, user);
        if (device == null) {
            throw new DeviceNotFoundException();
        }

        if (deviceName != null) {
            device.setName(deviceName);
            // Changes to 'device' are automatically tracked and persisted by JPA at transaction completion
            return device;
        } else {
            throw new ForbiddenException();
        }
    }


    @Transactional
    public void deleteDevice(Long deviceSeq, User user) {
        Device device = deviceRepository.findDeviceBySeqAndUser(deviceSeq, user);

        if (device != null) {
            deviceRepository.deleteById(deviceSeq);
        } else {
            throw new ForbiddenException();
        }
    }

    @Transactional
    public void deleteAllDevices(Long userSeq) {
        deviceRepository.deleteAllByUserSeq(userSeq);
    }


}
