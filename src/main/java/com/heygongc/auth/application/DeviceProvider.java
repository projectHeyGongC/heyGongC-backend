package com.heygongc.auth.application;


import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.global.error.exception.ForbiddenException;
import com.heygongc.global.error.exception.InvalidTokenException;
import com.heygongc.global.error.exception.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Qualifier("deviceProvider")
@Component
public class DeviceProvider implements TokenProvider {

    private final JwtUtil jwtUtil;
    private final DeviceRepository deviceRepository;

    public DeviceProvider(JwtUtil jwtUtil, DeviceRepository deviceRepository) {
        this.jwtUtil = jwtUtil;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Object extract(String token) {
        String deviceId = jwtUtil.extractSubject(token);
        Device device = deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(UnauthenticatedException::new);

        if (!Objects.equals(device.getDeviceId(), deviceId)) {
            throw new ForbiddenException("서로 다른 기기입니다.");
        }

        return device;
    }
}
