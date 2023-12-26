package com.heygongc.device.domain;

import com.heygongc.device.application.DeviceSensitivityEnum;
import com.heygongc.global.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device")
public class Device extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_seq")
    private Long seq;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean sound_mode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceSensitivityEnum sensitivity;

    @Column(nullable = false)
    private boolean sound_active;

    @Column(nullable = false)
    private boolean stream_active;

    @Column(nullable = false)
    private boolean front_camera;


}
