package com.venturedive.app.ticketing.core.processor;

import com.venturedive.app.ticketing.entity.Ticket;
import com.venturedive.app.ticketing.service.TicketService;
import org.springframework.stereotype.Component;

@Component
public class TicketCreationTask implements Runnable {

    Ticket ticket;
    TicketService ticketService;

    public TicketCreationTask(Ticket ticket, TicketService ticketService) {
        this.ticket = ticket;
        this.ticketService = ticketService;
    }

    @Override
    public void run() {
        this.ticketService.save(ticket);
    }
}