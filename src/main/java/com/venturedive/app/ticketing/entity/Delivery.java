package com.venturedive.app.ticketing.entity;

import com.venturedive.app.ticketing.common.enumerations.CustomerType;
import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "delivery_details")
@Getter
@Setter
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "customer_type")
    @Enumerated(EnumType.ORDINAL)
    private CustomerType customerType;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @Column(name = "expected_delivery_time")
    private ZonedDateTime expectedDeliveryTime;

    @Column(name = "current_distance_from_destination_in_meters")
    private int currentDistance;

    @Column(name = "rider_rating")
    private int riderRating;

    @Column(name = "time_to_prepare_food")
    private int timeToPrepareFood;

    @Column(name = "time_to_reach_destination")
    private int timeToReachDestination;

    public Delivery(CustomerType customerType, DeliveryStatus deliveryStatus, ZonedDateTime expectedDeliveryTime,
                    int currentDistance, int riderRating, int timeToPrepareFood, int timeToReachDestination) {
        this.customerType = customerType;
        this.deliveryStatus = deliveryStatus;
        this.expectedDeliveryTime = expectedDeliveryTime;
        this.currentDistance = currentDistance;
        this.riderRating = riderRating;
        this.timeToPrepareFood = timeToPrepareFood;
        this.timeToReachDestination = timeToReachDestination;
    }
}
