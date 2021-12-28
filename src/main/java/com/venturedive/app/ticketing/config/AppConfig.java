package com.venturedive.app.ticketing.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class AppConfig {

    @Value("${schedule.cron.expression}")
    private String scheduleCronExpression;

    @Value("${thread.pool.size}")
    private int threadPoolSize;

    @Value("${executorservice.wait.max}")
    private Long executorServiceMaxWait;

    @Value("${application.server.timezone}")
    private String applicationTimeZone;

}
