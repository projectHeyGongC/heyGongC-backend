package com.heygongc.device.presentation.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeviceResponse {
    private String type;
    private String name;
    private String sound_mode;
    private String sensitivity;
    private String sound_active;
    private String stream_active;
    private String front_camera;

}
