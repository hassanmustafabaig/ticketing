package com.venturedive.app.ticketing.service;

import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import com.venturedive.app.ticketing.dto.DeliveryDto;
import com.venturedive.app.ticketing.entity.Delivery;

import java.util.List;

public interface DeliveryService {
    Delivery save(DeliveryDto deliveryDto);
    List<Delivery> getAll();
    List<Delivery> geByDeliveryStatus(DeliveryStatus deliveryStatus);
    Integer getDeliveryByDeliveryStatusCount(DeliveryStatus deliveryStatus);
    List<Delivery> search(int page, int batchSize, String sortDirection, String sortBy);
}
