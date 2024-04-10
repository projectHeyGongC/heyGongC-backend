package com.heygongc.global.infra;

import com.heygongc.global.type.OsType;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Builder
public class FirebaseData {

    private String token;
    private List<String> tokens;
    @Getter private String body; // 푸시 메시지
    @Getter private boolean isSilent;
    @Getter private OsType osType;
    private String action;
    private String content;

    public List<String> getTokens() {
        if (tokens != null && !tokens.isEmpty()) {
            return tokens;
        }
        return Arrays.asList(token);
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
