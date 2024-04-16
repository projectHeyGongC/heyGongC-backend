package com.heygongc.device.domain.entity;

import com.heygongc.device.domain.type.CameraOrientationType;
import com.heygongc.device.domain.type.SensitivityType;
import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.global.type.OsType;
import com.heygongc.notification.domain.entity.Notification;
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

    @Column(name = "is_connected", nullable = false)
    @ColumnDefault("false")
    private boolean isConnected;

    @Column(name = "sound_sensing", nullable = false)
    @ColumnDefault("false")
    private boolean soundSensing;

    @Column(name = "sensitivity", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'MEDIUM'")
    private SensitivityType sensitivity;

    @Column(name = "stream_active", nullable = false)
    @ColumnDefault("false")
    private boolean streamActive;

    @Column(name = "camera_mode", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FRONT'")
    private CameraOrientationType cameraOrientation;

    @Column(name = "battery")
    private int battery;

    @Column(name = "temperature")
    private int temperature;

    @Column(name = "fcm_token")
    private String fcmToken;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    @Builder(builderMethodName = "createDevice")
    public Device(String deviceId, String modelName, OsType deviceOs, String fcmToken){
        this.deviceId = deviceId;
        this.modelName = modelName;
        this.deviceOs = deviceOs;
        this.isConnected = false;
        this.soundSensing = false;
        this.sensitivity = SensitivityType.MEDIUM;
        this.streamActive = false;
        this.cameraOrientation = CameraOrientationType.FRONT;
        this.battery = 0;
        this.temperature = 0;
        this.fcmToken = fcmToken;
    }

    public void changeDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public void connectDevice() { this.isConnected = true;}
    public void disConnectDevice() {
        this.userSeq = null;
        this.isConnected = false;
        this.soundSensing = false;
        this.sensitivity = SensitivityType.MEDIUM;
        this.streamActive = false;
        this.cameraOrientation = CameraOrientationType.FRONT;
        this.battery = 0;
        this.temperature = 0;
        this.fcmToken = null;
    }

    public void changeDeviceSetting(SensitivityType sensitivity, CameraOrientationType cameraOrientation){
        this.sensitivity = sensitivity;
        this.cameraOrientation = cameraOrientation;
    }

    public void setSoundSensing(String mode){
        if ("ON".equals(mode)) {
            soundSensingOn();
        } else if ("OFF".equals(mode)) {
            soundSensingOff();
        }
    }

    public void soundSensingOn(){
        this.soundSensing = true;
    }

    public void soundSensingOff(){
        this.soundSensing = false;
    }

    public void setStreamingMode(String mode){
        if ("ON".equals(mode)) {
            activeStreaming();
        } else if ("OFF".equals(mode)) {
            inactiveStreaming();
        }
    }

    public void activeStreaming(){
        this.streamActive = true;
    }

    public void inactiveStreaming(){
        this.streamActive = false;
    }

    public void setDeviceOwner(Long userSeq) { this.userSeq = userSeq; }

    public void changeCameraDeviceStatus(int battery, int temperature){
        this.battery = battery;
        this.temperature = temperature;
    }

    public void changeFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

    public String getDeviceConnectStatus() {
        return this.isConnected ? "CONNECTED" : "DISCONNECTED";
    }

    public String getSoundStatus() {
        return this.soundSensing ? "ON" : "OFF";
    }
}
