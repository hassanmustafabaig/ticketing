package com.venturedive.app.ticketing.core.processor;

import com.venturedive.app.ticketing.common.enumerations.Priority;
import com.venturedive.app.ticketing.common.enumerations.StrategyName;
import com.venturedive.app.ticketing.common.enumerations.TicketStatus;
import com.venturedive.app.ticketing.common.helper.TaskHelper;
import com.venturedive.app.ticketing.config.AppConfig;
import com.venturedive.app.ticketing.entity.Delivery;
import com.venturedive.app.ticketing.entity.Ticket;
import com.venturedive.app.ticketing.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class PickedupOrderStrategy implements Strategy {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TaskHelper taskHelper;

    @Override
    public void process(List<Delivery> list) {
        if(list != null && !list.isEmpty()) {

            ExecutorService executorService = Executors.newFixedThreadPool(appConfig.getThreadPoolSize());

            list.stream().parallel().forEach(delivery -> {

                Ticket ticket = new Ticket(Priority.LOW, delivery, TicketStatus.PENDING);

                ZonedDateTime estimatedDeliveryTime = ZonedDateTime.now()
                        .plusMinutes(delivery.getTimeToReachDestination());

                if (ZonedDateTime.now().isAfter(delivery.getExpectedDeliveryTime())) {
                    ticket.setPriority(Priority.CRITICAL);
                    executorService.execute(new TicketCreationTask(ticket, ticketService));

                } else if (estimatedDeliveryTime.isAfter(delivery.getExpectedDeliveryTime())) {
                    ticket.setPriority(taskHelper.findPriorityByCustomerType(delivery.getCustomerType()));
                    executorService.execute(new TicketCreationTask(ticket, ticketService));
                }
            });

            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(appConfig.getExecutorServiceMaxWait(), TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }
    }

    @Override
    public StrategyName getStrategyName() {
        return StrategyName.PickedupOrderStrategy;
    }
}
