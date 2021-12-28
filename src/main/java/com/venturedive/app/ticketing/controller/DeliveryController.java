package com.venturedive.app.ticketing.controller;

import com.google.gson.Gson;
import com.venturedive.app.ticketing.common.constant.GlobalConstant;
import com.venturedive.app.ticketing.dto.AuthDto;
import com.venturedive.app.ticketing.dto.DeliveryDto;
import com.venturedive.app.ticketing.dto.StringResponseDto;
import com.venturedive.app.ticketing.entity.Delivery;
import com.venturedive.app.ticketing.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;


@RestController
@RequestMapping(GlobalConstant.API_PREFIX_DELIVERY)
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @RolesAllowed(GlobalConstant.ROLE_ORDER_MANAGER)
    @GetMapping(GlobalConstant.API_DELIVERY_SEARCH)
    public ResponseEntity<List<Delivery>> searchDeliveries(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "batchSize", defaultValue = "10", required = false) Integer batchSize,
            @RequestParam(name = "sortDirection", defaultValue = "desc", required = false) String sortDirection,
            @RequestParam(name = "sortBy", defaultValue = "id", required = false) String sortBy)
    {
        List<Delivery> deliveryList = deliveryService.search(page, batchSize, sortDirection, sortBy);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deliveryList);
    }

    @RolesAllowed(GlobalConstant.ROLE_ORDER_MANAGER)
    @GetMapping(GlobalConstant.API_DELIVERY_GET_ALL)
    public ResponseEntity<List<Delivery>> getAllDeliveries()
    {
        List<Delivery> deliveryList = deliveryService.getAll();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deliveryList);
    }

    @RolesAllowed(GlobalConstant.ROLE_ORDER_MANAGER)
    @PostMapping(value = GlobalConstant.API_DELIVERY_ADD, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addDelivery(@RequestBody DeliveryDto entity)
    {
        Delivery delivery = deliveryService.save(entity);
        if(delivery!= null){
            return new ResponseEntity<>(new StringResponseDto("Delivery added successfully"), HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(new StringResponseDto("Failed to add delivery"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
