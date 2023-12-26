package com.heygongc.device.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeviceNameRequest {
    @NotBlank
    @Size(min=1, message = "이름은 한 글자 이상이어야 해요.")
    private String name;


}
