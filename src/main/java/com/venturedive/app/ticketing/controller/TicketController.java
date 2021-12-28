package com.venturedive.app.ticketing.controller;

import com.venturedive.app.ticketing.common.constant.GlobalConstant;
import com.venturedive.app.ticketing.common.helper.TicketHelper;
import com.venturedive.app.ticketing.dto.TicketDto;
import com.venturedive.app.ticketing.entity.Delivery;
import com.venturedive.app.ticketing.entity.Ticket;
import com.venturedive.app.ticketing.service.DeliveryService;
import com.venturedive.app.ticketing.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping(GlobalConstant.API_PREFIX_TICKET)
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @RolesAllowed(GlobalConstant.ROLE_TICKETING_AGENT)
    @GetMapping(GlobalConstant.API_TICKET_GET_ALL)
    public ResponseEntity<List<TicketDto>> getAllSortedByPriority()
    {
        List<Ticket> ticketList = ticketService.getAllSortedByPriority();

        List<TicketDto> list = TicketHelper.getTransformedList(ticketList);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(list);
    }
}
