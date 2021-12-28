package com.venturedive.app.ticketing.service.impl;

import com.venturedive.app.ticketing.dto.TicketDto;
import com.venturedive.app.ticketing.entity.Ticket;
import com.venturedive.app.ticketing.repository.TicketRepository;
import com.venturedive.app.ticketing.service.TicketService;
import org.apache.james.mime4j.field.datetime.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service(value = "ticketService")
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> search(int page, int batchSize, String sortDirection, String sortBy) {

        Page<Ticket> list = ticketRepository.findAll(PageRequest.of(page, batchSize, sortDirection == "asc" ? ASC : DESC, sortBy));

        if (list.isEmpty()) {
            return new ArrayList<>();
        }

        return list.toList();
    }

    @Override
    public List<Ticket> getAllSortedByPriority()
    {
        return ticketRepository.getAllSortedByPriority();
    }
}


