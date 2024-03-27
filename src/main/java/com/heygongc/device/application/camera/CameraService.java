package com.heygongc.device.application.camera;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.device.presentation.request.camera.CameraSubscribeRequest;
import com.heygongc.global.type.OsType;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.repository.NotificationRepository;
import com.heygongc.notification.domain.type.NotificationType;
import com.heygongc.user.application.JwtUtil;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
import com.heygongc.user.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CameraService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final JwtUtil jwtUtil;

    public CameraService(DeviceRepository deviceRepository, UserRepository userRepository, NotificationRepository notificationRepository, JwtUtil jwtUtil) {
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
                .orElseGet(() -> deviceRepository.save(
                        Device.createDevice()
                                .deviceId(request.deviceId())
                                .modelName(request.modelName())
                                .deviceOs(OsType.valueOf(request.deviceOs()))
                                .fcmToken(request.fcmToken())
                                .build()
        ));

        String accessToken = jwtUtil.generateCameraAccessToken(String.valueOf(device.getDeviceSeq()), device.getDeviceId());

        return accessToken;
    }

    @Transactional
    public void changeCameraDeviceStatus(Device device, int battery, int temperature) {
        device.changeCameraDeviceStatus(battery, temperature);
    }

    public boolean isConnected(Device device){
        return device.isConnected();
    }

    @Transactional
    public void alertSoundAlarm(Device device, User user) {
        notificationRepository.save(Notification.createNotification()
                .type(NotificationType.SOUND)
                .device(device)
                .user(user)
                .build());
    }
}
