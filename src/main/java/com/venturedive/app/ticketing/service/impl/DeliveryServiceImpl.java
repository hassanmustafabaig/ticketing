package com.venturedive.app.ticketing.service.impl;

import com.venturedive.app.ticketing.common.enumerations.CustomerType;
import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import com.venturedive.app.ticketing.config.AppConfig;
import com.venturedive.app.ticketing.dto.DeliveryDto;
import com.venturedive.app.ticketing.entity.Delivery;
import com.venturedive.app.ticketing.repository.DeliveryRepository;
import com.venturedive.app.ticketing.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service(value = "deliveryService")
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public Delivery save(DeliveryDto deliveryDto) {

        Delivery delivery = Delivery.builder()
                .customerType(CustomerType.values()[Integer.parseInt(deliveryDto.getCustomerType())])
                .deliveryStatus(DeliveryStatus.values()[Integer.parseInt(deliveryDto.getDeliveryStatus())])
                .currentDistance(Integer.parseInt(deliveryDto.getCurrentDistanceFromDestination()))
                .riderRating(Integer.parseInt(deliveryDto.getRiderRating()))
                .timeToPrepareFood(Integer.parseInt(deliveryDto.getTimeToPrepareFood()))
                .timeToReachDestination(Integer.parseInt(deliveryDto.getTimeToReachDestination())).build();

        delivery.setExpectedDeliveryTime(LocalDateTime
                .parse(deliveryDto.getExpectedDeliveryTime() )
                .atZone( ZoneId.of( appConfig.getApplicationTimeZone() ) )); ;

        return deliveryRepository.save(delivery);
    }

    @Override
    public List<Delivery> getAll() {
        List<Delivery> list = deliveryRepository.findAll();
        return list;
    }

    @Override
    public List<Delivery> geByDeliveryStatus(DeliveryStatus deliveryStatus) {
       return deliveryRepository.getDeliveryByDeliveryStatus(deliveryStatus);
    }

    @Override
    public Integer getDeliveryByDeliveryStatusCount(DeliveryStatus deliveryStatus) {
        return deliveryRepository.getDeliveryByDeliveryStatusCount(deliveryStatus);
    }

    @Override
    public List<Delivery> search(int page, int batchSize, String sortDirection, String sortBy) {

        Page<Delivery> list = deliveryRepository.findAll(PageRequest.of(page,batchSize,
                sortDirection == "asc" ? ASC : DESC, sortBy));

        if (list.isEmpty()) {
            return new ArrayList<>();
        }

        return list.toList();
    }
}


