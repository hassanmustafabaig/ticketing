package com.venturedive.app.ticketing.dto;

import com.venturedive.app.ticketing.common.enumerations.CustomerType;
import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import lombok.*;
import org.keycloak.representations.AccessTokenResponse;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class DeliveryDto {

    private String currentDistanceFromDestination;
    private String customerType;
    private String deliveryStatus;
    private String expectedDeliveryTime;
    private String riderRating;
    private String timeToPrepareFood;
    private String timeToReachDestination;
}