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



    public DeviceResponse getDevice(Long deviceSeq, User user) {
        Device device = deviceRepository.findById(deviceSeq).orElseThrow(() -> new DeviceNotFoundException());

        if(device.getUser().getSeq().equals(user.getSeq())) {
            DeviceResponse deviceResponse = new DeviceResponse(
                    device.getUser().getSeq(),
                    device.getType(),
                    device.getName()
            );
            return deviceResponse;
        }
        throw new ForbiddenException();

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

    public DeviceResponse updateDevice(Long deviceSeq, String deviceName, User user) {
        Device device = deviceRepository.findById(deviceSeq)
                .orElseThrow(() -> new DeviceNotFoundException());

        if (deviceName != null && device.getUser().getSeq().equals(user.getSeq()) ) {
            device.setName(deviceName);


            DeviceResponse deviceResponse = new DeviceResponse(
                    device.getUser().getSeq(), // Assuming user has a getUserSeq() method
                    device.getType(),
                    device.getName()
            );

            return deviceResponse;
        }

        throw new ForbiddenException();
    }

    public void deleteDevice(Long deviceSeq, User user) {
        Device device = deviceRepository.findById(deviceSeq)
                .orElseThrow(() -> new DeviceNotFoundException());

        if(device.getUser().getSeq().equals(user.getSeq())) {
            deviceRepository.deleteById(deviceSeq);
        }

        throw new ForbiddenException();

    }

    @Transactional
    public void deleteAllDevices(Long userSeq) {
        deviceRepository.deleteAllByUserSeq(userSeq);
    }


}
