package com.heygongc.device.presentation;

import com.heygongc.device.application.DeviceService;
import com.heygongc.device.domain.Device;
import com.heygongc.device.presentation.request.DeviceInfoRequest;
import com.heygongc.device.presentation.response.DeviceResponse;
import com.heygongc.global.argumentresolver.LoginUser;
import com.heygongc.global.common.response.ListResponse;
import com.heygongc.global.error.ErrorResponse;
import com.heygongc.user.domain.User;
import com.heygongc.user.presentation.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Device API", description = "기기 API")
@RestController
@RequestMapping("/v1/device")
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
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListResponse.class))),
            }
    )
    public ResponseEntity<ListResponse<Device>> getAllDevice(@Parameter(hidden = true) @LoginUser User user){
        Long userSeq = user.getSeq();
        List<Device> devices = deviceService.getAllDevices(userSeq);
        ListResponse<Device> deviceList = new ListResponse<Device>(
                devices
        );

        return ResponseEntity.ok().body(deviceList);
    }

    @PostMapping
    @Operation(
            summary = "기기 추가",
            description = "QR코드를 스캔하여 기기를 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponse.class))),

            }
    )
    public ResponseEntity<DeviceResponse> addDevice(
            @Parameter(hidden = true) @LoginUser User user,
            @RequestBody(description = "기기 정보") DeviceInfoRequest request){
        DeviceResponse deviceResponse = deviceService.addDevice(user, request);

        return ResponseEntity.ok().body(deviceResponse);
    }

    @DeleteMapping
    @Operation(
            summary = "모든 기기 삭제",
            description = "해당 유저의 모든 기기를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
            }
    )
    public ResponseEntity<Void> deleteAllDevice(@Parameter(hidden = true) @LoginUser User user) {
        Long userSeq = user.getSeq();
        deviceService.deleteAllDevices(userSeq);

        return ResponseEntity.ok().build();
    }



    @GetMapping("/{id}")
    @Operation(
            summary = "기기 선택",
            description = "해당 기기의 정보를 가져옵니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Unauthorized Exception", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))


            }
    )
    public ResponseEntity<DeviceResponse> getDevice(
            @Parameter(description = "기기 아이디", required = true, in = ParameterIn.PATH) @PathVariable(name = "id") Long deviceSeq,
            @Parameter(hidden = true) @LoginUser User user) {
        DeviceResponse deviceResponse = deviceService.getDevice(deviceSeq, user);


        return ResponseEntity.ok().body(deviceResponse);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "기기 정보 수정",
            description = "해당 기기의 이름을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Unauthorized Exception", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))

            }
    )
    public ResponseEntity<DeviceResponse> updateDevice(
            @Parameter(description = "기기 아이디", required = true, in = ParameterIn.PATH) @PathVariable(name = "id") Long deviceSeq,
            @Parameter(description = "수정된 기기 이름", required = true) @RequestParam(name = "deviceName") String deviceName,
            @Parameter(hidden = true) @LoginUser User user) {
        DeviceResponse deviceResponse = deviceService.updateDevice(deviceSeq, deviceName, user);

        return ResponseEntity.ok().body(deviceResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "기기 삭제",
            description = "해당 기기를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Unauthorized Exception", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))

            }
    )
    public ResponseEntity<Void> deleteDevice(
            @Parameter(description = "기기 아이디", required = true, in = ParameterIn.PATH) @PathVariable(name = "id") Long deviceSeq,
            @Parameter(hidden = true) @LoginUser User user) {
        deviceService.deleteDevice(deviceSeq, user);

        return ResponseEntity.ok().build();
    }
}
