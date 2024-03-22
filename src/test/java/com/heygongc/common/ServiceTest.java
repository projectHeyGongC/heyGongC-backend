package com.heygongc.common;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
abstract public class ServiceTest {

    @Autowired
    private DatabaseCleaner cleaner;

    @AfterEach
    void tearDown() {
        cleaner.execute();
    }
}
