package com.heygongc.global.common.response;


import java.util.List;

public record ListResponse<T>(List<T> content) {
    public ListResponse {
        if (content == null) {
            content = List.of(); // Ensures content is never null
        }
    }

}
