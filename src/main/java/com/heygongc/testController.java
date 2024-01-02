package com.heygongc;

import com.heygongc.global.interceptor.Auth;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test")
public class testController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Auth
    @PostMapping("/getUserAndDevice")
    public ResponseEntity<String> sendUser(HttpServletRequest request) {
        Long userSeq = (Long) request.getAttribute("userSeq");
        String deviceId = (String) request.getAttribute("deviceId");
        logger.debug("userSeq({}), deviceId({})", userSeq, deviceId);
        return ResponseEntity.ok().body(userSeq + ", " + deviceId);
    }
}
