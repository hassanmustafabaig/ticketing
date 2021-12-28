package com.venturedive.app.ticketing.service.impl;

import com.venturedive.app.ticketing.common.enumerations.CustomerType;
import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import com.venturedive.app.ticketing.common.enumerations.Priority;
import com.venturedive.app.ticketing.common.enumerations.TicketStatus;
import com.venturedive.app.ticketing.entity.Delivery;
import com.venturedive.app.ticketing.entity.Ticket;
import com.venturedive.app.ticketing.repository.DeliveryRepository;
import com.venturedive.app.ticketing.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository mockTicketRepository;

    @InjectMocks
    private TicketServiceImpl ticketServiceImplUnderTest;


    @Test
    void shouldAddTicketSuccessfully() {

        Ticket ticket = getTicketObjectToAdd();
        Ticket expectedResult = getExpectedCreatedTicketObject();

        when(mockTicketRepository.save(ticket)).thenReturn(expectedResult);

        Ticket result = ticketServiceImplUnderTest.save(ticket);

        assertThat(result).isEqualTo(expectedResult);
        verify(mockTicketRepository).save(any(Ticket.class));
    }

    @Test
    void shouldAddUniqueTicketSuccessfully() {

        Ticket ticket = getTicketObjectToAdd();
        Ticket expectedResult = getExpectedCreatedTicketObject();

        List<Delivery> lits = fetchAllDeliveriesForWhichTicketNotCreated();

        when(mockTicketRepository.save(ticket)).thenReturn(expectedResult);

        assertThat(lits.stream()
                .filter(d -> ticket.getDelivery().getId() == d.getId())
                .findFirst()).isEmpty();

        Ticket result = ticketServiceImplUnderTest.save(ticket);

        assertThat(result).isEqualTo(expectedResult);
        verify(mockTicketRepository).save(any(Ticket.class));
    }

    @Test
    void shouldReturnAllTickets() {

        List<Ticket> tickets = fetchUnsortedTicketList();
        List<Ticket> expectedResult = fetchUnsortedTicketList();

        when(mockTicketRepository.findAll()).thenReturn(tickets);
        List<Ticket> result = ticketServiceImplUnderTest.getAll();
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldReturnEmptyListForGetAll() {
        when(mockTicketRepository.findAll()).thenReturn(Collections.emptyList());
        List<Ticket> result = ticketServiceImplUnderTest.getAll();
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void shouldReturnAllTicketsSortedByPriority() {

        List<Ticket> tickets = fetchSortedTicketList();
        List<Ticket> expectedResult = fetchSortedTicketList();

        when(mockTicketRepository.getAllSortedByPriority()).thenReturn(tickets);
        List<Ticket> result = ticketServiceImplUnderTest.getAllSortedByPriority();
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldReturnEmptyListForSortedByPriority() {
        when(mockTicketRepository.findAll()).thenReturn(Collections.emptyList());
        List<Ticket> result = ticketServiceImplUnderTest.getAll();
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void shouldNotReturnAllTicketsUnSortedByPriority() {
        List<Ticket> tickets = fetchSortedTicketList();
        List<Ticket> expectedResult = fetchUnsortedTicketList();

        when(mockTicketRepository.getAllSortedByPriority()).thenReturn(tickets);
        List<Ticket> result = ticketServiceImplUnderTest.getAllSortedByPriority();
        assertThat(result).isNotEqualTo(expectedResult);
    }

    private Ticket getTicketObjectToAdd(){
        Ticket ticket = Ticket.builder()
        .id(1L)
        .priority(Priority.CRITICAL)
        .delivery(Delivery.builder().id(1L).customerType(CustomerType.VIP).deliveryStatus(DeliveryStatus.ORDER_RECEIVED).expectedDeliveryTime(ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneOffset.UTC)).currentDistance(500).riderRating(4).timeToPrepareFood(15).timeToReachDestination(15).build())
        .ticketStatus(TicketStatus.PENDING).build();

        return ticket;
    }

    private Ticket getExpectedCreatedTicketObject(){
        Ticket ticket = Ticket.builder()
                .id(1L)
                .priority(Priority.CRITICAL)
                .delivery(Delivery.builder().id(1L).customerType(CustomerType.VIP).deliveryStatus(DeliveryStatus.ORDER_RECEIVED).expectedDeliveryTime(ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneOffset.UTC)).currentDistance(500).riderRating(4).timeToPrepareFood(15).timeToReachDestination(15).build())
                .ticketStatus(TicketStatus.PENDING).build();

        return ticket;
    }


    private List<Delivery> fetchAllDeliveriesForWhichTicketNotCreated(){
        Delivery delivery1 = Delivery.builder()
                .id(2)
                .customerType(CustomerType.VIP)
                .deliveryStatus(DeliveryStatus.ORDER_RECEIVED)
                .expectedDeliveryTime(ZonedDateTime.now())
                .currentDistance(500)
                .riderRating(4)
                .timeToPrepareFood(20)
                .timeToReachDestination(15)
                .build();

        Delivery delivery2 = Delivery.builder()
                .id(3)
                .customerType(CustomerType.LOYAL)
                .deliveryStatus(DeliveryStatus.ORDER_PREPARING)
                .expectedDeliveryTime(ZonedDateTime.now())
                .currentDistance(300)
                .riderRating(4)
                .timeToPrepareFood(20)
                .timeToReachDestination(15)
                .build();

        Delivery delivery3 = Delivery.builder()
                .id(4)
                .customerType(CustomerType.NEW)
                .deliveryStatus(DeliveryStatus.ORDER_PICKED_UP)
                .expectedDeliveryTime(ZonedDateTime.now())
                .currentDistance(400)
                .riderRating(4)
                .timeToPrepareFood(20)
                .timeToReachDestination(15)
                .build();

        List<Delivery> list = new ArrayList<>();
        list.add(delivery1);
        list.add(delivery2);
        list.add(delivery3);
        return list;
    }

    private List<Ticket> fetchSortedTicketList(){
        Ticket ticket1 = Ticket.builder()
                .id(1)
                .priority(Priority.CRITICAL)
                .ticketStatus(TicketStatus.PENDING)
                .build();

        Ticket ticket2 = Ticket.builder()
                .id(2)
                .priority(Priority.HIGHEST)
                .ticketStatus(TicketStatus.PENDING)
                .build();

        Ticket ticket3 = Ticket.builder()
                .id(3)
                .priority(Priority.HIGH)
                .ticketStatus(TicketStatus.PENDING)
                .build();

        List<Ticket> list = new ArrayList<>();
        list.add(ticket1);
        list.add(ticket2);
        list.add(ticket3);
        return list;
    }

    private List<Ticket> fetchUnsortedTicketList(){
        Ticket ticket1 = Ticket.builder()
                .id(1)
                .priority(Priority.HIGH)
                .ticketStatus(TicketStatus.PENDING)
                .build();

        Ticket ticket2 = Ticket.builder()
                .id(2)
                .priority(Priority.HIGHEST)
                .ticketStatus(TicketStatus.PENDING)
                .build();

        Ticket ticket3 = Ticket.builder()
                .id(3)
                .priority(Priority.CRITICAL)
                .ticketStatus(TicketStatus.PENDING)
                .build();

        List<Ticket> list = new ArrayList<>();
        list.add(ticket1);
        list.add(ticket3);
        list.add(ticket2);
        return list;
    }

    private List<Ticket> searchedList(){
       return List.of(Ticket.builder()
               .id(1)
               .priority(Priority.CRITICAL)
               .ticketStatus(TicketStatus.PENDING)
               .build(),
               Ticket.builder()
                       .id(2)
                       .priority(Priority.CRITICAL)
                       .ticketStatus(TicketStatus.PENDING)
                       .build());
    }
}
