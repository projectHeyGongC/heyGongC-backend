package com.heygongc.device.presentation;

import com.heygongc.device.application.DeviceService;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.presentation.request.DeviceIdsRequest;
import com.heygongc.device.presentation.request.DeviceInfoRequest;
import com.heygongc.device.presentation.response.DeviceResponse;
import com.heygongc.global.error.ErrorResponse;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.presentation.request.UserLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;


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
                .map(device -> new DeviceResponse(device.getModelName(), device.getDeviceName()))
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
    public ResponseEntity<DeviceResponse> subscribeDevice(
            @Parameter(name = "DeviceInfoRequest", description = "카메라 기기 정보", required = true) @RequestBody DeviceInfoRequest request){
        Device device = deviceService.subscribeDevice(request);
        DeviceResponse deviceResponse = new DeviceResponse(
                device.getModelName(),
                device.getDeviceName()
        );
        return ResponseEntity.ok().body(deviceResponse);
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
    public ResponseEntity<DeviceResponse> updateDevice(
            @Parameter(description = "기기 아이디", required = true, in = ParameterIn.PATH) @PathVariable(name = "deviceId") String deviceId,
            @Parameter(name = "deviceName", description = "수정된 기기 이름", required = true) @RequestBody String deviceName,
            @Parameter(hidden = true) User user) {

        Device device = deviceService.updateDevice(deviceId, deviceName, user);

        DeviceResponse deviceResponse = new DeviceResponse(
                device.getModelName(),
                device.getDeviceName()
        );

        return ResponseEntity.ok().body(deviceResponse);
    }

    @PostMapping("/delete")
    @Operation(
            summary = "기기 연동 해제",
            description = "메인 앱과 연결되어 있는 하나 또는 모든 카메라 기기와의 연동을 해제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Unauthorized Exception", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))

            }
    )
    public ResponseEntity<Void> deleteDevice(
            @Parameter(name = "DeviceIdsRequest", description = "연동 해제할 카메라 기기 목록", required = true) @RequestBody DeviceIdsRequest request,
            @Parameter(hidden = true) User user) {
        request.validate();
        deviceService.deleteDevice(request.deviceIds(), user);

        return ResponseEntity.ok().build();
    }
}
