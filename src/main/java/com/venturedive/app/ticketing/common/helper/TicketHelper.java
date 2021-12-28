package com.venturedive.app.ticketing.common.helper;

import com.venturedive.app.ticketing.dto.TicketDto;
import com.venturedive.app.ticketing.entity.Ticket;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class TicketHelper {

    public static List<TicketDto> getTransformedList(List<Ticket> list) {
        List<TicketDto> tickets = Collections.emptyList();

        AtomicLong sequence = new AtomicLong();
        if (list != null && !list.isEmpty()) {

            tickets = list.stream().map(t -> {

                TicketDto ticket = TicketDto.builder()
                        .id(t.getId())
                        .sequence(sequence.incrementAndGet())
                        .priority(t.getPriority())
                        .ticketStatus(t.getTicketStatus()).build();
                return ticket;

            }).collect(Collectors.toList());
        }

        return tickets;
    }
}



