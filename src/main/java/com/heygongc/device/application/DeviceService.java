package com.heygongc.device.application;

import com.heygongc.device.domain.Device;
import com.heygongc.device.domain.DeviceRepository;
import com.heygongc.device.exception.DeviceNotFoundException;
import com.heygongc.device.presentation.request.DeviceInfoRequest;
import com.heygongc.device.presentation.response.DeviceResponse;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService{

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository){
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }



    public DeviceResponse getDevice(Long deviceSeq) {

        Device device = deviceRepository.findById(deviceSeq).orElseThrow(() -> new DeviceNotFoundException());

        DeviceResponse deviceResponse = new DeviceResponse(
                device.getUser().getSeq(),
                device.getType(),
                device.getName()
        );
        return deviceResponse;
    }

    public List<Device> getAllDevices(Long userSeq) {
        return deviceRepository.findAllByUserSeq(userSeq);
    }


    public DeviceResponse addDevice(User user, DeviceInfoRequest request) {
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

        DeviceResponse deviceResponse = new DeviceResponse(
                user.getSeq(), // Assuming user has a getUserSeq() method
                device.getType(),
                device.getName()
        );



        return deviceResponse;
    }

    public Device updateDevice(Long deviceSeq, String deviceName) {
        Device device = deviceRepository.findById(deviceSeq)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        if (deviceName != null) {
            device.setName(deviceName);
        }

        return device;
    }

    public void deleteDevice(Long deviceSeq) {
        deviceRepository.deleteById(deviceSeq);
    }

    public void deleteAllDevices(Long userSeq) {
        deviceRepository.deleteAllByUserSeq(userSeq);
    }


}
