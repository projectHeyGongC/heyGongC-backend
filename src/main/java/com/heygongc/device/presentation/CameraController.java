package com.heygongc.device.presentation;

import com.heygongc.device.application.camera.CameraService;
import com.heygongc.device.presentation.request.camera.CameraSubscribeRequest;
import com.heygongc.device.presentation.response.camera.CameraSubscribeResponse;
import com.heygongc.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Camera API", description = "카메라앱 API")
@RestController
@RequestMapping("/v1/cameras")
public class CameraController {

    private final CameraService cameraService;

    public CameraController(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    @PostMapping("/subscribe")
    @Operation(
            summary = "카메라 등록",
            description = "등록된 경우 token 발급, 등록되지 않은 경우 등록 후 token 발급",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CameraSubscribeResponse.class)))
            }
    )
    public ResponseEntity<CameraSubscribeResponse> subscribeCamera(@Parameter(name = "CameraSubscribeRequest", description = "카메라 등록 요청 정보", required = true)
                                                                   @RequestBody CameraSubscribeRequest request) {
        request.validate();
        String accessToken = cameraService.subscribeCamera(request);
        return ResponseEntity.ok()
                .body(
                        new CameraSubscribeResponse(accessToken)
                );
    }

    @PutMapping("/status")
    @Operation(
            summary = "카메라 기기 상태정보 변경",
            description = "카메라 기기 상태정보 변경",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
            }
    )
    public ResponseEntity<CameraSubscribeResponse> setCameraStatus(@Parameter(hidden = true) User user) {
//        String accessToken = cameraService.subscribeCamera(request);
        return ResponseEntity.ok()
                .body(
                        null
                );
    }
}

