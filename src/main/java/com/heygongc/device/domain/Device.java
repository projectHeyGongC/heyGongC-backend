package com.heygongc.device.domain;

import com.heygongc.device.application.DeviceSensitivityEnum;
import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.notification.domain.Notification;
import com.heygongc.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name="uuid", nullable = false)
    private String uuid;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sound_mode", nullable = false)
    private boolean soundMode;

    @Column(name = "sensitivity", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceSensitivityEnum sensitivity;

    @Column(name = "sound_active", nullable = false)
    private boolean soundActive;

    @Column(name = "stream_active", nullable = false)
    private boolean streamActive;

    @Column(name = "front_camera", nullable = false)
    private boolean frontCamera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<Notification> notifications = new ArrayList<>();
    private List<Notification> notifications;
    public void changeDeviceName(String deviceName) {
        this.name = deviceName;
    }

}
