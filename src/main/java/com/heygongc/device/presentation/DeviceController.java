package com.heygongc.device.presentation;

import com.heygongc.device.application.device.DeviceService;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.presentation.request.camera.CameraDeviceSettingRequest;
import com.heygongc.device.presentation.request.camera.ControlTypeRequest;
import com.heygongc.device.presentation.request.device.DeviceIdsRequest;
import com.heygongc.device.presentation.request.device.DeviceInfoRequest;
import com.heygongc.device.presentation.response.device.DeviceResponse;
import com.heygongc.global.error.ErrorResponse;
import com.heygongc.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
            summary = "기기 목록",
            description = "해당 유저의 모든 기기 목록을 나열합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
            }
    )
    public ResponseEntity<List<DeviceResponse>> getAllDevices(@Parameter(hidden = true) User user){
        Long userSeq = user.getSeq();
        List<Device> devices = deviceService.getAllDevices(userSeq);

        List<DeviceResponse> deviceResponses = devices.stream()
                .map(device -> new DeviceResponse(
                        device.getDeviceId(),
                        device.getDeviceName(),
                        device.getBattery(),
                        device.getTemperature()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(deviceResponses);
    }

    @PostMapping("/subscribe")
    @Operation(
            summary = "메인 앱에서 카메라 기기 연동하기",
            description = "메인 앱에서 카메라 앱 QR코드를 스캔 후 기기 이름 까지 입력되면 두 파라매터를 서버로 넘겨줍니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponse.class))),

            }
    )
    public ResponseEntity<Void> subscribeDevice(
            @Parameter(name = "DeviceInfoRequest", description = "카메라 기기 정보", required = true) @RequestBody DeviceInfoRequest request,
            @Parameter(hidden = true) User user){
        deviceService.subscribeDevice(request, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{deviceId}")
    @Operation(
            summary = "기기 정보 수정",
            description = "해당 기기의 이름을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Unauthorized Exception", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<Void> updateDevice(
            @Parameter(description = "기기 아이디", required = true, in = ParameterIn.PATH) @PathVariable(name = "deviceId") String deviceId,
            @Parameter(name = "deviceName", description = "수정된 기기 이름", required = true) @RequestBody String deviceName,
            @Parameter(hidden = true) User user) {

        deviceService.updateDevice(deviceId, deviceName, user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/disconnect")
    @Operation(
            summary = "기기 연동 해제",
            description = "메인 앱과 연결되어 있는 하나 또는 모든 카메라 기기와의 연동을 해제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Unauthorized Exception", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<Void> disconnectDevice(
            @Parameter(name = "DeviceIdsRequest", description = "연동 해제할 카메라 기기 목록", required = true) @RequestBody DeviceIdsRequest request,
            @Parameter(hidden = true) User user) {
        request.validate();
        deviceService.disconnectDevice(request.deviceIds(), user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("{deviceId}/control")
    @Operation(
            summary = "기기 제어하기",
            description = "메인 앱에서 카메라 앱 기기를 제어합니다. 어떤 명령을 카메라 앱 기기에 내릴 건지 정합니다." +
                    "소리 감지를 키거나 끄거나 원격 스트리밍을 요청할 때 해당 api를 사용합니다.",

            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
            }
    )
    public ResponseEntity<Void> controlDevice(
            @Parameter(description = "기기 아이디", required = true, in = ParameterIn.PATH) @PathVariable(name = "deviceId") String deviceId,
            @Parameter(name = "ControlTypeRequest", description = "명령 내릴 컨트롤 타입", required = true) @RequestBody ControlTypeRequest request,
            @Parameter(hidden = true) User user){
        request.validate();
        deviceService.controlDevice(deviceId, request.controlType(), user);

        return ResponseEntity.ok().build();
    }

    @PutMapping("{deviceId}/settings")
    @Operation(
            summary = "기기 설정 변경하기",
            description = "소리 세기 민감도 조절 및 카메라 기기의 카메라 종류(전면 카메라 또는 후면 카메라) 를 바꿀 때 사용합니다.",

            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
            }
    )
    public ResponseEntity<Void> changeDeviceSetting(
            @Parameter(description = "기기 아이디", required = true, in = ParameterIn.PATH) @PathVariable(name = "deviceId") String deviceId,
            @Parameter(name = "CameraDeviceSettingRequest", description = "명령 내릴 컨트롤 타입", required = true) @RequestBody CameraDeviceSettingRequest request,
            @Parameter(hidden = true) User user){
        request.validate();
        deviceService.changeDeviceSetting(deviceId, request.sensitivity(), request.cameraMode(), user);

        return ResponseEntity.ok().build();
    }

}
