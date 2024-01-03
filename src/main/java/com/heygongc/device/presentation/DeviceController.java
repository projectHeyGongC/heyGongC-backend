package com.heygongc.device.presentation;

import com.heygongc.device.application.DeviceService;
import com.heygongc.device.domain.Device;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevice(HttpServletRequest request){
        Long userSeq = Long.valueOf((String) request.getAttribute("userSeq"));
        List<Device> devices = deviceService.getAllDevices(userSeq);

        return ResponseEntity.ok().body(devices);
    }

    @PostMapping
    public ResponseEntity<Device> addDevice(HttpServletRequest request, String qrCode, String deviceName){
        Long userSeq = Long.valueOf((String) request.getAttribute("userSeq"));
        Device device = deviceService.addDevice(userSeq, qrCode, deviceName);

        return ResponseEntity.ok().body(device);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteAllDevice(HttpServletRequest request) {
        Long userSeq = Long.valueOf((String) request.getAttribute("userSeq"));
        Boolean result = deviceService.deleteAllDevices(userSeq);

        return ResponseEntity.ok().body(result);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Optional<Device>> getDevice(@PathVariable(name = "id") Long deviceSeq) {
        Optional<Device> device = deviceService.getDevice(deviceSeq);

        return ResponseEntity.ok().body(device);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable(name = "id") Long deviceSeq, String deviceName, HttpServletRequest request) {
        Long userSeq = Long.valueOf((String) request.getAttribute("userSeq"));
        Device device = deviceService.updateDevice(deviceSeq, userSeq, deviceName);

        return ResponseEntity.ok().body(device);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteDevice(@PathVariable(name = "id") Long deviceSeq, HttpServletRequest request ) {
        Long userSeq = Long.valueOf((String) request.getAttribute("userSeq"));
        Boolean result = deviceService.deleteDevice(deviceSeq, userSeq);

        return ResponseEntity.ok().body(result);
    }
}
