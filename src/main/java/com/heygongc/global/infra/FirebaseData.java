package com.heygongc.global.infra;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;

@Builder
public class FirebaseData {

    @Getter
    private String body; // 푸시 메시지
    private String action;
    private String content;

    public HashMap<String, String> getData() {
        HashMap<String, String> data = new HashMap<>();

        if (action != null) {
            data.put("action", action);
        }
        if (content != null) {
            data.put("content", content);
        }

        return data;
    }
}
