package com.heygongc.device.presentation;

import com.heygongc.device.application.camera.CameraPushService;
import com.heygongc.device.application.camera.CameraService;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.presentation.request.camera.CameraStatusRequest;
import com.heygongc.device.presentation.request.camera.CameraSubscribeRequest;
import com.heygongc.device.presentation.response.camera.CameraDeviceSettingResponse;
import com.heygongc.device.presentation.response.camera.CameraIsConnectedResponse;
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
    private final CameraPushService cameraPushService;

    public CameraController(CameraService cameraService, CameraPushService cameraPushService) {
        this.cameraService = cameraService;
        this.cameraPushService = cameraPushService;
    }

    @PostMapping("/subscribe")
    @Operation(
            summary = "카메라 등록",
            description = "등록된 경우 token 발급, 등록되지 않은 경우 등록 후 token 발급",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CameraSubscribeResponse.class)))
            }
    )
    public ResponseEntity<CameraSubscribeResponse> subscribeCamera(
            @Parameter(name = "CameraSubscribeRequest", description = "카메라 등록 요청 정보", required = true) @RequestBody CameraSubscribeRequest request) {
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
    public ResponseEntity<Void> setCameraStatus(
            @Parameter(name = "CameraStatusRequest", description = "카메라 등록 요청 정보", required = true) @RequestBody CameraStatusRequest request,
            @Parameter(hidden = true) Device device) {
        cameraService.changeCameraDeviceStatus(device, request.battery(), request.temperature());
        return ResponseEntity.ok().build();

    }

    @GetMapping("/status")
    @Operation(
            summary = "카메라 상태 체크 조회",
            description = "회원과 연결되지 않은 경우 QR 노출",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CameraIsConnectedResponse.class)))
            }
    )
    public ResponseEntity<CameraIsConnectedResponse> getCameraQRStatus(
            @Parameter(hidden = true) Device device) {
        boolean isConnected = cameraService.isConnected(device);
        return ResponseEntity.ok()
                .body(
                        new CameraIsConnectedResponse(isConnected)
                );
    }

    @GetMapping("/settings")
    @Operation(
            summary = "카메라 설정 정보 불러오기",
            description = "소리 민감도 및 카메라 모드 정보를 불러올 때 사용됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CameraDeviceSettingResponse.class)))
            }
    )
    public ResponseEntity<CameraDeviceSettingResponse> getCameraSettings(
            @Parameter(hidden = true) Device device) {
        return ResponseEntity.ok()
                .body(
                        new CameraDeviceSettingResponse(device.getSensitivity().toString(), device.getCameraMode().toString())
                );
    }

    @GetMapping("/sound/occur")
    @Operation(
            summary = "카메라 소리 감지 발생",
            description = "카메라 앱에서 소리 감지 시 해당 api 리퀘를 보내면 서버에서 메인앱으로 소리 감지 알림을 보냄",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
            }
    )
    public ResponseEntity<Void> alertSoundAlarm(
            @Parameter(hidden = true) Device device) {
        User user = cameraService.getUserByDevice(device);
        cameraService.alertSoundAlarm(device, user);

        String fcmToken = user.getFcmToken();
        cameraPushService.alertSoundAlarm(fcmToken);

        return ResponseEntity.ok().build();

    }


}

