package com.venturedive.app.ticketing.repository;

import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import com.venturedive.app.ticketing.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT d FROM Delivery d " +
            "LEFT JOIN Ticket t ON d.id = t.delivery WHERE d.deliveryStatus = :deliveryStatus AND t.id is NULL")
    List<Delivery> getDeliveryByDeliveryStatus(@Param("deliveryStatus")DeliveryStatus deliveryStatus);

    @Query("SELECT COUNT(d) FROM Delivery d " +
            "LEFT JOIN Ticket t ON d.id = t.delivery WHERE d.deliveryStatus = :deliveryStatus AND t.id is NULL")
    Integer getDeliveryByDeliveryStatusCount(@Param("deliveryStatus")DeliveryStatus deliveryStatus);
}