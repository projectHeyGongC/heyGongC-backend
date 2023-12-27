package com.heygongc.device.presentation;

import com.heygongc.device.application.DeviceService;
import com.heygongc.device.domain.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDevice(@PathVariable(name = "id") Long id) {
        return deviceService.getDevice(id);
    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Device> updateDevice(@PathVariable(name = "id") Long id) {
//        Device device =
//    }

}
