package com.venturedive.app.ticketing.core.rule;

import com.venturedive.app.ticketing.common.enumerations.CustomerType;
import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import com.venturedive.app.ticketing.common.enumerations.StrategyName;
import com.venturedive.app.ticketing.entity.Delivery;
import com.venturedive.app.ticketing.core.processor.StrategyExecutor;
import com.venturedive.app.ticketing.service.DeliveryService;
import com.venturedive.app.ticketing.service.TicketService;
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
class TicketGenerationForReceivedOrderRuleTest {

    @Mock
    private DeliveryService mockDeliveryService;
    @Mock
    private TicketService mockTicketService;
    @Mock
    private StrategyExecutor mockStrategyExecutor;

    @InjectMocks
    private TicketGenerationForReceivedOrderRule ticketGenerationForReceivedOrderRuleUnderTest;

    @Test
    void shouldReturnFalseWhenOverdueDeliveriesDoesNotExist() {
        when(mockDeliveryService.getDeliveryByDeliveryStatusCount(DeliveryStatus.ORDER_RECEIVED)).thenReturn(0);
        boolean result = ticketGenerationForReceivedOrderRuleUnderTest.doesOverdueDeliveriesExist(false);
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueWhenOverdueDeliveriesDoesExist() {
        when(mockDeliveryService.getDeliveryByDeliveryStatusCount(DeliveryStatus.ORDER_RECEIVED)).thenReturn(1);
        boolean result = ticketGenerationForReceivedOrderRuleUnderTest.doesOverdueDeliveriesExist(true);
        assertThat(result).isTrue();
    }

    @Test
    void shouldProcessListOfDeliveriesForTicketCreation() {
        List<Delivery> deliveries = List.of(Delivery.builder().id(0L).customerType(CustomerType.VIP).deliveryStatus(DeliveryStatus.ORDER_RECEIVED).expectedDeliveryTime(ZonedDateTime.now()).currentDistance(200).riderRating(5).timeToPrepareFood(15).timeToReachDestination(10).build());
        when(mockDeliveryService.geByDeliveryStatus(DeliveryStatus.ORDER_RECEIVED)).thenReturn(deliveries);
        ticketGenerationForReceivedOrderRuleUnderTest.createTicket();
        verify(mockStrategyExecutor).process(deliveries, StrategyName.ReceivedOrderStrategy);
    }

    @Test
    void shouldSendEmptyListForProcessing() {
        when(mockDeliveryService.geByDeliveryStatus(DeliveryStatus.ORDER_RECEIVED)).thenReturn(Collections.emptyList());
        ticketGenerationForReceivedOrderRuleUnderTest.createTicket();
        verify(mockStrategyExecutor).process(Collections.emptyList(), StrategyName.ReceivedOrderStrategy);
    }
}
