package com.heygongc.device.presentation.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeviceResponse {
    private String type;
    private String name;
    private String soundMode;
    private String sensitivity;
    private String soundActive;
    private String streamActive;
    private String frontCamera;

}
