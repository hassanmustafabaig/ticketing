package com.venturedive.app.ticketing.core.rule;

import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import com.venturedive.app.ticketing.common.enumerations.StrategyName;
import com.venturedive.app.ticketing.core.processor.StrategyExecutor;
import com.venturedive.app.ticketing.service.DeliveryService;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Rule(name = "Over-due deliveries with pickedup_order status rule",
        description = "When orders are not delivered on expected delivery time and currently in pickedup state")
public class TicketGenerationForPickedupOrderRule {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private StrategyExecutor strategyExecutor;

    @Condition
    public boolean doesOverdueDeliveriesExist(@Fact("overduePickedupDeliveriesExist") boolean doesExist) {

        Integer count = deliveryService.getDeliveryByDeliveryStatusCount(DeliveryStatus.ORDER_PICKED_UP);

        if(count > 0){
            return true;
        }
        else{
            return false;
        }
    }

    @Action
    public void createTicket() {
        strategyExecutor.process(deliveryService.geByDeliveryStatus(DeliveryStatus.ORDER_PICKED_UP), StrategyName.PickedupOrderStrategy);
    }
}
