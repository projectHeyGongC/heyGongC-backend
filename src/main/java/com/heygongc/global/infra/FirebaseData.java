package com.heygongc.global.infra;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Builder
public class FirebaseData {

    @Getter
    private String token;
    @Getter
    private List<String> tokens;
    @Getter
    private String body; // 푸시 메시지
    @Getter
    private boolean isSilent;

    private String action;
    private String content;

    public boolean isSinglePush() {
        return getToken() != null;
    }

    public boolean isMultiPush() {
        return getTokens() != null && getTokens().isEmpty();
    }

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
