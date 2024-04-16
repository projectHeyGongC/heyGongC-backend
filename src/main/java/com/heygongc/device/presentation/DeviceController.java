package com.heygongc.device.presentation;

import com.heygongc.device.application.device.DeviceService;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.presentation.request.device.*;
import com.heygongc.device.presentation.response.device.DeviceInfoResponse;
import com.heygongc.device.presentation.response.device.DeviceListResponse;
import com.heygongc.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Tag(name = "Device API", description = "기기 API")
@RestController
@RequestMapping("/v1/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    @Operation(
            summary = "기기 목록 조회",
            description = "[모니터링 > 메인] 유저의 모든 기기 목록을 나열합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DeviceListResponse.class))))
            }
    )
    public ResponseEntity<List<DeviceListResponse>> getAllDevices(@Parameter(hidden = true) User user) {
        List<Device> devices = deviceService.getDevices(user);

        List<DeviceListResponse> deviceResponses = devices.stream()
                .map(device -> new DeviceListResponse(
                        device.getDeviceId(),
                        device.getDeviceName(),
                        device.getBattery(),
                        device.getTemperature(),
                        device.getDeviceConnectStatus(),
                        device.getSoundStatus()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(deviceResponses);
    }

    @PostMapping("/subscribe")
    @Operation(
            summary = "메인 앱에서 카메라 기기 추가하기",
            description = "[모니터링 > 메인 > 기기 추가하기] 메인 앱에 카메라 기기를 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
            }
    )
    public ResponseEntity<Void> subscribeDevice(
            @Parameter(name = "DeviceSubscribeRequest", description = "기기 추가 요청 정보", required = true) @RequestBody DeviceSubscribeRequest request,
            @Parameter(hidden = true) User user) throws Exception {
        deviceService.subscribeDevice(request.deviceId(), request.deviceName(), user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{deviceId}")
    @Operation(
            summary = "기기 상세 정보 조회",
            description = "[모니터링 > 메인 > 기기 설정] 기기의 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceInfoResponse.class)))
            }
    )
    public ResponseEntity<DeviceInfoResponse> getDeviceInfo(
            @Parameter(description = "기기 아이디", required = true, in = ParameterIn.PATH) @PathVariable(name = "deviceId") String deviceId,
            @Parameter(hidden = true) User user) {
        Device device = deviceService.getDevice(deviceId, user);
        return ResponseEntity.ok()
                .body(
                    new DeviceInfoResponse(
                            device.getDeviceId(),
                            device.getDeviceName(),
                            device.getModelName(),
                            device.getSensitivity().name(),
                            device.getCameraOrientation().name(),
                            device.getSoundStatus()
                    )
        );
    }

    @PutMapping("/{deviceId}")
    @Operation(
            summary = "기기 정보 수정",
            description = "[모니터링 > 메인 > 기기 설정] 기기의 이름을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
            }
    )
    public ResponseEntity<Void> changeDeviceName(
            @Parameter(description = "기기 아이디", required = true, in = ParameterIn.PATH) @PathVariable(name = "deviceId") String deviceId,
            @Parameter(name = "DeviceNameRequest", description = "기기 정보 수정 요청 정보", required = true) @RequestBody DeviceNameRequest request,
            @Parameter(hidden = true) User user) {
        deviceService.changeDeviceName(deviceId, request.deviceName(), user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/disconnect")
    @Operation(
            summary = "기기 연동 해제",
            description = "[모니터링 > 메인 > 기기 설정] 메인 앱과 연결되어 있는 하나 또는 모든 카메라 기기와의 연동을 해제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
            }
    )
    public ResponseEntity<Void> disconnectDevice(
            @Parameter(name = "DeviceDisconnectRequest", description = "기기 연동 해제 요청 정보", required = true) @RequestBody DeviceDisconnectRequest request,
            @Parameter(hidden = true) User user) throws Exception {
        deviceService.disconnectDevices(request.deviceIds(), user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{deviceId}/settings")
    @Operation(
            summary = "기기 설정 변경하기",
            description = "[모니터링 > 메인 > 기기 설정] 소리 세기 민감도 조절 및 카메라 기기의 카메라 종류(전면 카메라 또는 후면 카메라) 를 바꿀 때 사용합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
            }
    )
    public ResponseEntity<Void> changeDeviceSetting(
            @Parameter(description = "기기 아이디", required = true, in = ParameterIn.PATH) @PathVariable(name = "deviceId") String deviceId,
            @Parameter(name = "DeviceSettingRequest", description = "기기 설정 변경 요청 정보", required = true) @RequestBody DeviceSettingRequest request,
            @Parameter(hidden = true) User user) throws Exception {
        deviceService.changeDeviceSetting(deviceId, request.sensitivity(), request.cameraOrientation(), user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{deviceId}/control")
    @Operation(
            summary = "기기 제어하기",
            description = "[모니터링 > 메인 > 스트리밍] 메인 앱에서 카메라 앱 기기를 제어합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
            }
    )
    public ResponseEntity<Void> controlDevice(
            @Parameter(description = "기기 아이디", required = true, in = ParameterIn.PATH) @PathVariable(name = "deviceId") String deviceId,
            @Parameter(name = "DeviceControlRequest", description = "기기 제어 요청 정보", required = true) @RequestBody DeviceControlRequest request,
            @Parameter(hidden = true) User user) throws Exception {
        deviceService.controlDevice(deviceId, user, request.controlType(), request.controlMode());
        return ResponseEntity.ok().build();
    }
}
