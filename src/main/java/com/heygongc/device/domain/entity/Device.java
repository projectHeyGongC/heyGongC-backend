package com.heygongc.device.domain.entity;

import com.heygongc.device.application.DeviceSensitivityEnum;
import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.global.type.OsType;
import com.heygongc.notification.domain.Notification;
import com.heygongc.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access=PROTECTED) // No default constructor for entity 오류 해결을 위해 필요
@AllArgsConstructor
@DynamicUpdate
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
    private DeviceSensitivityEnum sensitivity;

    @Column(name = "sound_active", nullable = false)
    @ColumnDefault("false")
    private boolean soundActive;

    @Column(name = "stream_active", nullable = false)
    @ColumnDefault("false")
    private boolean streamActive;

    @Column(name = "front_camera", nullable = false)
    @ColumnDefault("false")
    private boolean frontCamera;

    @Column(name = "battery")
    private int battery;

    @Column(name = "temperature")
    private int temperature;

    @Builder(builderMethodName = "createDevice")
    public Device(Long userSeq, String deviceId, String modelName, String deviceName, OsType deviceOs){
        this.userSeq = userSeq;
        this.deviceId = deviceId;
        this.modelName = modelName;
        this.deviceName = deviceName;
        this.deviceOs = deviceOs;
        this.isPaired = false;
        this.soundMode = false;
        this.sensitivity = DeviceSensitivityEnum.MEDIUM;
        this.soundActive = false;
        this.streamActive = false;
        this.frontCamera = false;
        this.battery = 0;
        this.temperature = 0;
    }

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;
    public void changeDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public void pairDevice() { this.isPaired = true;}
    public void unpairDevice() {
        this.userSeq = null;
        this.isPaired = false;
        this.soundMode = false;
        this.sensitivity = DeviceSensitivityEnum.MEDIUM;
        this.soundActive = false;
        this.streamActive = false;
        this.frontCamera = false;
        this.battery = 0;
        this.temperature = 0;
    }

}
