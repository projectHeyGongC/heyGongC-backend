package com.heygongc.device.application;

import com.heygongc.device.domain.Device;
import com.heygongc.device.domain.DeviceRepository;
import com.heygongc.device.presentation.request.DeviceNameRequest;
import com.heygongc.device.presentation.response.DeviceResponse;
import com.heygongc.global.common.response.ApiResponse;
import com.heygongc.global.error.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sound.midi.MidiDeviceReceiver;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService{

    @Autowired
    private DeviceRepository deviceRepository;
    @Override
    public ApiResponse deleteAllDevices() {
        Long userSeq = extractUserSeqFromSecurityContext();
        // Your logic to delete all devices for the user
        return new ApiResponse(Boolean.TRUE,"All devices deleted successfully.");
    }

    @Override
    public DeviceResponse addDevice(DeviceNameRequest deviceNameRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Device> getDevice(Long device_seq) {
        Device device = deviceRepository.findById(device_seq).orElseThrow(() -> new ResourceNotFoundException("Device", "device_seq", device_seq));
        return new ResponseEntity<>(device, HttpStatus.OK);
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
    public ApiResponse deleteDevice(Long device_seq) {
        return null;
    }

    private Long extractUserSeqFromSecurityContext() {
        Long userSeq = 12345L;
        return userSeq;
    }

}
