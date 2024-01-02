package com.heygongc.global.config;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeignClientErrorDecoder implements ErrorDecoder {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Exception decode(String methodKey, Response response) {
        // 에러 응답을 분석하고 적절한 예외를 반환합니다.
        // 예를 들어, HTTP 상태 코드에 따라 다른 예외를 던질 수 있습니다.

        if (response.status() == 404) {
//            return new NotFoundException("Resource not found");
        } else if (response.status() == 500) {
//            return new InternalServerErrorException("Internal server error");
        }

        // 기본적으로는 FeignException을 사용합니다.
        return FeignException.errorStatus(methodKey, response);
    }
}