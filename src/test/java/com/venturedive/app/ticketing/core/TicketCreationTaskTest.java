package com.venturedive.app.ticketing.core;

import com.venturedive.app.ticketing.common.enumerations.CustomerType;
import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import com.venturedive.app.ticketing.common.enumerations.Priority;
import com.venturedive.app.ticketing.common.enumerations.TicketStatus;
import com.venturedive.app.ticketing.entity.Delivery;
import com.venturedive.app.ticketing.entity.Ticket;
import com.venturedive.app.ticketing.core.processor.TicketCreationTask;
import com.venturedive.app.ticketing.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketCreationTaskTest {

    @Mock
    private Ticket mockTicket;
    @Mock
    private TicketService mockTicketService;

    private TicketCreationTask ticketCreationTaskUnderTest;

    @BeforeEach
    void setUp() {
        ticketCreationTaskUnderTest = new TicketCreationTask(mockTicket, mockTicketService);
    }

    @Test
    void shouldRunToCreateTicket() {
        Ticket ticket = Ticket.builder().id(1L).priority(Priority.CRITICAL).delivery(Delivery.builder().id(1L).customerType(CustomerType.VIP).deliveryStatus(DeliveryStatus.ORDER_PICKED_UP).expectedDeliveryTime(ZonedDateTime.now()).currentDistance(200).riderRating(5).timeToPrepareFood(15).timeToReachDestination(10).build()).ticketStatus(TicketStatus.PENDING).build();
        when(mockTicketService.save(mockTicket)).thenReturn(ticket);
        ticketCreationTaskUnderTest.run();
        verify(mockTicketService).save(mockTicket);
    }
}
