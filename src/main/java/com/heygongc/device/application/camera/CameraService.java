package com.heygongc.device.application.camera;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.device.presentation.request.camera.CameraSubscribeRequest;
import com.heygongc.global.type.OsType;
import com.heygongc.user.application.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CameraService {

    private final DeviceRepository deviceRepository;

    private final JwtUtil jwtUtil;

    public CameraService(DeviceRepository deviceRepository, JwtUtil jwtUtil) {
        this.deviceRepository = deviceRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public String subscribeCamera(CameraSubscribeRequest request) {

        Device device = deviceRepository.findByDeviceId(request.deviceId())
                .orElseGet(() -> deviceRepository.save(
                        Device.createDevice()
                                .deviceId(request.deviceId())
                                .modelName(request.modelName())
                                .deviceOs(OsType.valueOf(request.deviceOs()))
                                .fcmToken(request.fcmToken())
                                .build()
        ));

        String accessToken = jwtUtil.generateLongAccessToken(String.valueOf(device.getDeviceSeq()), device.getDeviceId());

        return accessToken;
    }
}
