package com.heygongc.device.application;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.device.exception.DeviceNotFoundException;
import com.heygongc.device.presentation.request.DeviceInfoRequest;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
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


//    public Device addDevice(User user, DeviceInfoRequest request) {
//
//        Device device = Device.builder()
//                .deviceId(request.uuid())
//                .modelName(request.type())
//                .deviceName(request.name())
//                .soundMode(false)
//                .sensitivity(DeviceSensitivityEnum.MEDIUM)
//                .soundActive(false)
//                .streamActive(false)
//                .frontCamera(false)
//                .user(user)
//                .build();
//        deviceRepository.save(device);
//
//        return device;
//    }

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
    public void deleteDevice(List<String> deviceIds, User user) {
        deviceRepository.deleteCameraDevices(deviceIds, user);

    }

}
