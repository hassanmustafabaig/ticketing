package com.venturedive.app.ticketing.core;

import com.venturedive.app.ticketing.config.AppConfig;
import com.venturedive.app.ticketing.core.processor.TicketScheduler;
import com.venturedive.app.ticketing.core.rule.TicketGenerationForPickedupOrderRule;
import com.venturedive.app.ticketing.core.rule.TicketGenerationForPreparingOrderRule;
import com.venturedive.app.ticketing.core.rule.TicketGenerationForReceivedOrderRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketSchedulerTest {

    @Mock
    private AppConfig mockAppConfig;
    @Mock
    private TicketGenerationForReceivedOrderRule mockTicketGenerationForReceivedOrderRule;
    @Mock
    private TicketGenerationForPreparingOrderRule mockTicketGenerationForPreparingOrderRule;
    @Mock
    private TicketGenerationForPickedupOrderRule mockTicketGenerationForPickedupOrderRule;

    @InjectMocks
    private TicketScheduler ticketSchedulerUnderTest;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    void shouldGetCronValue() {
        when(mockAppConfig.getScheduleCronExpression()).thenReturn("0 0/1 * * * *");
        String result = ticketSchedulerUnderTest.getCronValue();
        assertThat(result).isEqualTo("0 0/1 * * * *");
    }

    @Test
    public void shouldReturnValidExpressionForEvery1Minute() {
        @SuppressWarnings("deprecation")
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator("0 0/1 * * * *", TimeZone.getTimeZone(ZoneId.of("UTC")));
        ZonedDateTime date = LocalDateTime.of(2021, 12, 24, 1, 1, 0).atZone(ZoneId.of("UTC"));
        ZonedDateTime expected = LocalDateTime.of(2021, 12, 24, 1, 2, 0).atZone(ZoneId.of("UTC"));
        assertThat(cronSequenceGenerator.next(Date.from(date.toInstant()))).isEqualTo(Date.from(expected.toInstant()));

    }

    @Test
    public void shouldReturnValidExpressionForEvery5Minute() {
        @SuppressWarnings("deprecation")
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator("0 0/5 * * * *", TimeZone.getTimeZone(ZoneId.of("UTC")));
        ZonedDateTime date = LocalDateTime.of(2021, 12, 24, 1, 1, 0).atZone(ZoneId.of("UTC"));
        ZonedDateTime expected = LocalDateTime.of(2021, 12, 24, 1, 5, 0).atZone(ZoneId.of("UTC"));
        assertThat(cronSequenceGenerator.next(Date.from(date.toInstant()))).isEqualTo(Date.from(expected.toInstant()));

    }
}
