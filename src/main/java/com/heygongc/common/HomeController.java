package com.heygongc.common;

import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping
    public void home() {
        log.info("Hello, HeyGongC!!");
    }
}
