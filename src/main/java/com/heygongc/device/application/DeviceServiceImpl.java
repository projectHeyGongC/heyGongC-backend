package com.heygongc.device.application;

import com.heygongc.device.domain.Device;
import com.heygongc.device.domain.DeviceRepository;
import com.heygongc.device.presentation.request.DeviceNameRequest;
import com.heygongc.device.presentation.response.DeviceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService{

    @Autowired
    private DeviceRepository deviceRepository;
    @Override
    public Boolean deleteAllDevices() {
        Long userSeq = extractUserSeqFromSecurityContext();
        // Your logic to delete all devices for the user
        return true;
    }

    @Override
    public DeviceResponse addDevice(DeviceNameRequest deviceNameRequest) {
        return null;
    }

    @Override
    public Device getDevice(Long device_seq) {
        return null;
    }

    @Override
    public List<Device> getAllDevices() {
        Long userSeq = extractUserSeqFromSecurityContext();
        // Your logic to get all devices for the user
        return deviceRepository.findAllByUserSeq(userSeq);
    }

    @Override
    public Device updateDevice(DeviceNameRequest deviceNameRequest) {
        return null;
    }

    @Override
    public Boolean deleteDevice(Long device_seq) {
        return true;
    }

    private Long extractUserSeqFromSecurityContext() {
        Long userSeq = 12345L;
        return userSeq;
    }

}
