package com.venturedive.app.ticketing.service;

import com.venturedive.app.ticketing.dto.TicketDto;
import com.venturedive.app.ticketing.entity.Ticket;

import java.util.List;

public interface TicketService {
    Ticket save(Ticket ticket);
    List<Ticket> getAll();
    List<Ticket> search(int page, int batchSize, String sortDirection, String sortBy);
    List<Ticket> getAllSortedByPriority();
}
