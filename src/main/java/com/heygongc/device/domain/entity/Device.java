package com.heygongc.device.domain.entity;

import com.heygongc.device.domain.type.CameraModeType;
import com.heygongc.device.domain.type.SensitivityType;
import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.global.type.OsType;
import com.heygongc.notification.domain.Notification;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@NoArgsConstructor(access=PROTECTED) // No default constructor for entity 오류 해결을 위해 필요
@DynamicUpdate //변경된 필드만 Update
@Table(name = "device")
public class Device extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_seq")
    private Long deviceSeq;

    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name="device_id", nullable = false)
    private String deviceId;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "device_os", nullable = false)
    @Enumerated(EnumType.STRING)
    private OsType deviceOs;

    @Column(name = "is_paired", nullable = false)
    @ColumnDefault("false")
    private boolean isPaired;

    @Column(name = "sound_mode", nullable = false)
    @ColumnDefault("false")
    private boolean soundMode;

    @Column(name = "sensitivity", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'MEDIUM'")
    private SensitivityType sensitivity;

    @Column(name = "sound_active", nullable = false)
    @ColumnDefault("false")
    private boolean soundActive;

    @Column(name = "stream_active", nullable = false)
    @ColumnDefault("false")
    private boolean streamActive;

    @Column(name = "camera_mode", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FRONT'")
    private CameraModeType cameraMode;

    @Column(name = "battery")
    private int battery;

    @Column(name = "temperature")
    private int temperature;

    @Column(name = "fcm_token")
    private String fcmToken;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    @Builder(builderMethodName = "createDevice")
    public Device(String deviceId, String modelName, OsType deviceOs){
        this.deviceId = deviceId;
        this.modelName = modelName;
        this.deviceOs = deviceOs;
        this.isPaired = false;
        this.soundMode = false;
        this.sensitivity = SensitivityType.MEDIUM;
        this.soundActive = false;
        this.streamActive = false;
        this.cameraMode = CameraModeType.FRONT;
        this.battery = 0;
        this.temperature = 0;
        this.fcmToken = fcmToken;
    }

    public void changeDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public void pairDevice() { this.isPaired = true;}
    public void unpairDevice() {
        this.userSeq = null;
        this.isPaired = false;
        this.soundMode = false;
        this.sensitivity = SensitivityType.MEDIUM;
        this.soundActive = false;
        this.streamActive = false;
        this.cameraMode = CameraModeType.FRONT;
        this.battery = 0;
        this.temperature = 0;
        this.fcmToken = null;
    }

    public void changeDeviceSetting(SensitivityType sensitivity, CameraModeType cameraMode){
        this.sensitivity = sensitivity;
        this.cameraMode = cameraMode;
    }

    public void soundModeOn(){
        this.soundMode = true;
    }

    public void soundModeOff(){
        this.soundMode = false;
    }

    public void startStreaming(){
        this.streamActive = true;
    }

    public void stopStreaming(){
        this.streamActive = false;
    }
}
