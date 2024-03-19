package com.heygongc.device.presentation;

import com.heygongc.device.application.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Camera API", description = "카메라 기기 API")
@RestController
@RequestMapping("/v1/cameras")
public class CameraController {

    private final DeviceService deviceService;

    public CameraController(DeviceService deviceService) {this.deviceService = deviceService;}


}

