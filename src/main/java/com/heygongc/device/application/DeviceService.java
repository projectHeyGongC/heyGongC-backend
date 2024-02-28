package com.heygongc.device.application;

import com.heygongc.device.domain.Device;
import com.heygongc.device.domain.DeviceRepository;
import com.heygongc.device.exception.DeviceNotFoundException;
import com.heygongc.device.presentation.request.DeviceInfoRequest;
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
        Device device = deviceRepository.findMyDevice(deviceSeq, user)
                .orElseThrow(DeviceNotFoundException::new); // Optional을 사용한 처리
        return device;
    }

    public List<Device> getAllDevices(Long userSeq) {
        return deviceRepository.findAllByUserSeq(userSeq);
    }


    public Device addDevice(User user, DeviceInfoRequest request) {

        Device device = Device.builder()
                .uuid(request.uuid())
                .type(request.type())
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
        Device device = deviceRepository.findMyDevice(deviceSeq, user)
                .orElseThrow(DeviceNotFoundException::new); // Optional을 사용한 처리

            device.changeDeviceName(deviceName);
            return device;

    }


    @Transactional
    public void deleteDevice(Long deviceSeq, User user) {
        deviceRepository.findMyDevice(deviceSeq, user)
                .orElseThrow(DeviceNotFoundException::new); // Optional을 사용한 처리

        deviceRepository.deleteById(deviceSeq);

    }

    @Transactional
    public void deleteAllDevices(Long userSeq) {
        deviceRepository.deleteAllByUserSeq(userSeq);
    }


}
