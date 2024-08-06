package com.shadoww.imageservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;

//@ExtendWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ConfigTest {

    @Value("${spring.cloud.config.enabled}")
    private boolean configEnabled;

    @Test
    public void testConfigDisabled() {
        System.out.println(configEnabled);
        assertFalse(configEnabled);
    }
}