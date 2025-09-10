package com.bank.loan.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Exposes a system Clock bean to make "today" testable via a fixed clock. */
@Configuration
public class ClockConfig {
    @Bean
    public Clock systemClock() {
        return Clock.systemDefaultZone();
    }
}
