package com.venturedive.app.ticketing.dto;

import com.venturedive.app.ticketing.common.enumerations.Priority;
import com.venturedive.app.ticketing.common.enumerations.TicketStatus;
import com.venturedive.app.ticketing.entity.Delivery;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class TicketDto {

    private long id;
    private long sequence;
    private Priority priority;
    private Delivery delivery;
    private TicketStatus ticketStatus;
}