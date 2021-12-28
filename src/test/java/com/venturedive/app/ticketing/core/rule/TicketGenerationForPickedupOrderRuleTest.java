package com.venturedive.app.ticketing.core.rule;

import com.venturedive.app.ticketing.common.enumerations.CustomerType;
import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import com.venturedive.app.ticketing.common.enumerations.StrategyName;
import com.venturedive.app.ticketing.entity.Delivery;
import com.venturedive.app.ticketing.core.processor.StrategyExecutor;
import com.venturedive.app.ticketing.service.DeliveryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketGenerationForPickedupOrderRuleTest {

    @Mock
    private DeliveryService mockDeliveryService;
    @Mock
    private StrategyExecutor mockStrategyExecutor;

    @InjectMocks
    private TicketGenerationForPickedupOrderRule ticketGenerationForPickedupOrderRuleUnderTest;

    @Test
    void shouldReturnFalseWhenOverdueDeliveriesDoesNotExist() {
        when(mockDeliveryService.getDeliveryByDeliveryStatusCount(DeliveryStatus.ORDER_PICKED_UP)).thenReturn(0);
        boolean result = ticketGenerationForPickedupOrderRuleUnderTest.doesOverdueDeliveriesExist(false);
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueWhenOverdueDeliveriesDoesExist() {
        when(mockDeliveryService.getDeliveryByDeliveryStatusCount(DeliveryStatus.ORDER_PICKED_UP)).thenReturn(1);
        boolean result = ticketGenerationForPickedupOrderRuleUnderTest.doesOverdueDeliveriesExist(true);
        assertThat(result).isTrue();
    }

    @Test
    void shouldProcessListOfDeliveriesForTicketCreation() {
        List<Delivery> deliveries = List.of(Delivery.builder().id(0L).customerType(CustomerType.VIP).deliveryStatus(DeliveryStatus.ORDER_PICKED_UP).expectedDeliveryTime(ZonedDateTime.now()).currentDistance(200).riderRating(5).timeToPrepareFood(15).timeToReachDestination(10).build());
        when(mockDeliveryService.geByDeliveryStatus(DeliveryStatus.ORDER_PICKED_UP)).thenReturn(deliveries);
        ticketGenerationForPickedupOrderRuleUnderTest.createTicket();
        verify(mockStrategyExecutor).process(deliveries,  StrategyName.PickedupOrderStrategy);
    }

    @Test
    void shouldSendEmptyListForProcessing() {
        when(mockDeliveryService.geByDeliveryStatus(DeliveryStatus.ORDER_PICKED_UP)).thenReturn(Collections.emptyList());
        ticketGenerationForPickedupOrderRuleUnderTest.createTicket();
        verify(mockStrategyExecutor).process(Collections.emptyList(), StrategyName.PickedupOrderStrategy);
    }
}
