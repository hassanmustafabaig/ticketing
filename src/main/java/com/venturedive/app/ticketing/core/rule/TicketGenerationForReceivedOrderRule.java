package com.venturedive.app.ticketing.core.rule;

import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import com.venturedive.app.ticketing.common.enumerations.StrategyName;
import com.venturedive.app.ticketing.core.processor.StrategyExecutor;
import com.venturedive.app.ticketing.service.DeliveryService;
import com.venturedive.app.ticketing.service.TicketService;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Rule(name = "Over-due deliveries with received_order status rule",
        description = "When orders are not delivered on expected delivery time and currently in received state")
public class TicketGenerationForReceivedOrderRule {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private StrategyExecutor strategyExecutor;

    @Condition
    public boolean doesOverdueDeliveriesExist(@Fact("overdueReceivedDeliveriesExist") boolean doesExist) {

        Integer count = deliveryService.getDeliveryByDeliveryStatusCount(DeliveryStatus.ORDER_RECEIVED);

        if(count > 0){
            return true;
        }
        else{
            return false;
        }
    }

    @Action
    public void createTicket() {
        strategyExecutor.process(deliveryService.geByDeliveryStatus(DeliveryStatus.ORDER_RECEIVED), StrategyName.ReceivedOrderStrategy);
    }
}
