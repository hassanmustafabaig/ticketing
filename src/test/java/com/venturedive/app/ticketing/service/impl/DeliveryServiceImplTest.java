package com.venturedive.app.ticketing.service.impl;

import com.venturedive.app.ticketing.common.enumerations.CustomerType;
import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import com.venturedive.app.ticketing.entity.Delivery;
import com.venturedive.app.ticketing.repository.DeliveryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceImplTest {

    @Mock
    private DeliveryRepository mockDeliveryRepository;

    @InjectMocks
    private DeliveryServiceImpl deliveryServiceImplUnderTest;

    @Test
    void shouldReturnAllDeliveries() {
        List<Delivery> deliveries = fetchAllDeliveries();
        when(mockDeliveryRepository.findAll()).thenReturn(deliveries);
        List<Delivery> result = deliveryServiceImplUnderTest.getAll();
        assertThat(result).isEqualTo(deliveries);
    }

    @Test
    void shouldReturnEmptyListForGetAll() {
        when(mockDeliveryRepository.findAll()).thenReturn(Collections.emptyList());
        List<Delivery> result = deliveryServiceImplUnderTest.getAll();
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void shouldReturnDeliveriesByDeliveryStatus() {
        List<Delivery> expectedResult = fetchAllReceivedDeliveries();
        List<Delivery> deliveries = fetchAllReceivedDeliveries();
        when(mockDeliveryRepository.getDeliveryByDeliveryStatus(DeliveryStatus.ORDER_RECEIVED)).thenReturn(deliveries);
        List<Delivery> result = deliveryServiceImplUnderTest.geByDeliveryStatus(DeliveryStatus.ORDER_RECEIVED);
        assertThat(result.stream().filter(d -> d.getDeliveryStatus() != DeliveryStatus.ORDER_RECEIVED).count()).isEqualTo(0);
    }

    @Test
    void shouldReturnEmptyListByDeliveryStatus() {
        when(mockDeliveryRepository.getDeliveryByDeliveryStatus(DeliveryStatus.ORDER_RECEIVED)).thenReturn(Collections.emptyList());
        List<Delivery> result = deliveryServiceImplUnderTest.geByDeliveryStatus(DeliveryStatus.ORDER_RECEIVED);
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void shouldReturnCountByDeliveryStatus() {
        when(mockDeliveryRepository.getDeliveryByDeliveryStatusCount(DeliveryStatus.ORDER_RECEIVED)).thenReturn(0);
        Integer result = deliveryServiceImplUnderTest.getDeliveryByDeliveryStatusCount(DeliveryStatus.ORDER_RECEIVED);
        assertThat(result).isEqualTo(0);
    }

    private List<Delivery> fetchAllDeliveries(){
        Delivery delivery1 = Delivery.builder()
                .id(1)
                .customerType(CustomerType.VIP)
                .deliveryStatus(DeliveryStatus.ORDER_RECEIVED)
                .expectedDeliveryTime(ZonedDateTime.now())
                .currentDistance(500)
                .riderRating(4)
                .timeToPrepareFood(20)
                .timeToReachDestination(15)
                .build();

        Delivery delivery2 = Delivery.builder()
                .id(2)
                .customerType(CustomerType.LOYAL)
                .deliveryStatus(DeliveryStatus.ORDER_PREPARING)
                .expectedDeliveryTime(ZonedDateTime.now())
                .currentDistance(300)
                .riderRating(4)
                .timeToPrepareFood(20)
                .timeToReachDestination(15)
                .build();

        Delivery delivery3 = Delivery.builder()
                .id(2)
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

    private List<Delivery> fetchAllReceivedDeliveries(){
        Delivery delivery1 = Delivery.builder()
                .id(1)
                .customerType(CustomerType.VIP)
                .deliveryStatus(DeliveryStatus.ORDER_RECEIVED)
                .expectedDeliveryTime(ZonedDateTime.now())
                .currentDistance(500)
                .riderRating(4)
                .timeToPrepareFood(20)
                .timeToReachDestination(15)
                .build();

        Delivery delivery2 = Delivery.builder()
                .id(2)
                .customerType(CustomerType.LOYAL)
                .deliveryStatus(DeliveryStatus.ORDER_RECEIVED)
                .expectedDeliveryTime(ZonedDateTime.now())
                .currentDistance(300)
                .riderRating(4)
                .timeToPrepareFood(20)
                .timeToReachDestination(15)
                .build();

        Delivery delivery3 = Delivery.builder()
                .id(2)
                .customerType(CustomerType.NEW)
                .deliveryStatus(DeliveryStatus.ORDER_RECEIVED)
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
}
