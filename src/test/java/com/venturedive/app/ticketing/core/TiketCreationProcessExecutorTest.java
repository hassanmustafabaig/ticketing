package com.venturedive.app.ticketing.core;

import com.venturedive.app.ticketing.config.AppConfig;
import com.venturedive.app.ticketing.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
class TiketCreationProcessExecutorTest {

    @Mock
    private AppConfig mockAppConfig;
    @Mock
    private TicketService mockTicketService;


    @Test
    public void shouldExecuteWithConcurrency() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicReference<Integer> counter = new AtomicReference<>(0);
        for (int i = 0; i < numberOfThreads; i++) {
            service.execute(() -> {
                counter.getAndSet(counter.get() + 1);
                latch.countDown();
            });
        }
        latch.await();
        assertThat(numberOfThreads).isEqualTo(counter.get());
    }
}



