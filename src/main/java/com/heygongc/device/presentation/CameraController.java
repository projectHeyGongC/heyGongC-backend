package com.heygongc.device.presentation;

import com.heygongc.device.application.DeviceService;
import com.heygongc.device.presentation.request.CameraSubscribeRequest;
import com.heygongc.user.presentation.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Camera API", description = "카메라 기기 API")
@RestController
@RequestMapping("/v1/cameras")
public class CameraController {

    private final DeviceService deviceService;

    public CameraController(DeviceService deviceService) {this.deviceService = deviceService;}

//    @PostMapping("/subscribe")
//    public ResponseEntity<TokenResponse> subscribe(@Parameter(name = "CameraSubscribeRequest", description = "카메라 기기 요청 정보", required = true)
//            @RequestBody CameraSubscribeRequest request){
//        request.validate();
//
//    }


}

