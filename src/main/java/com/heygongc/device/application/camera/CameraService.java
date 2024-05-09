package com.heygongc.device.application.camera;

import com.heygongc.auth.application.JwtUtil;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.device.presentation.request.camera.CameraSubscribeRequest;
import com.heygongc.global.type.OsType;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.repository.NotificationRepository;
import com.heygongc.notification.domain.type.NotificationType;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
import com.heygongc.user.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CameraService {

    private final CameraPushService cameraPushService;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final JwtUtil jwtUtil;

    public CameraService(CameraPushService cameraPushService, DeviceRepository deviceRepository, UserRepository userRepository, NotificationRepository notificationRepository, JwtUtil jwtUtil) {
        this.cameraPushService = cameraPushService;
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.jwtUtil = jwtUtil;
    }

    public User getUserByDevice(Device device) {
        return userRepository.findByUserSeq(device.getUserSeq())
                .orElseThrow(() -> new UserNotFoundException("미가입 사용자입니다."));
    }

    @Transactional
    public String subscribeCamera(CameraSubscribeRequest request) {
        Device device = deviceRepository.findByDeviceId(request.deviceId())
                .map(existingDevice -> {
                    if (!Objects.equals(existingDevice.getFcmToken(), request.fcmToken())) {
                        existingDevice.changeFcmToken(request.fcmToken());
                        deviceRepository.save(existingDevice);
                    }
                    return existingDevice;
                })
                .orElseGet(() -> {
                    return deviceRepository.save(
                            Device.createDevice()
                                    .deviceId(request.deviceId())
                                    .modelName(request.modelName())
                                    .deviceOs(OsType.valueOf(request.deviceOs()))
                                    .fcmToken(request.fcmToken())
                                    .build()
                    );
                });

        return jwtUtil.generateCameraAccessToken(device.getDeviceId());
    }
    @Transactional
    public void changeCameraDeviceStatus(Device device, int battery, int temperature) {
        device.changeCameraDeviceStatus(battery, temperature);
    }

    public boolean isConnected(Device device) {
        return device.isConnected();
    }

    @Transactional
    public void alertSoundAlarm(Device device) throws Exception {
        User user = getUserByDevice(device);

        notificationRepository.save(Notification.createNotification()
                .type(NotificationType.SOUND)
                .device(device)
                .user(user)
                .build());

        // 이벤트 알람 수신 허용이면 fcm 발송
        if (user.getAlarm()) {
            String fcmToken = user.getFcmToken();
            cameraPushService.alertSoundAlarm(fcmToken);
        }
    }
}
