package com.venturedive.app.ticketing.controller;


import com.venturedive.app.ticketing.common.constant.GlobalConstant;
import com.venturedive.app.ticketing.dto.UserDto;
import com.venturedive.app.ticketing.service.KeycloakService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;

@RestController
@RequestMapping(GlobalConstant.API_PREFIX_USER)
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private KeycloakService keycloakService;

    @PostMapping(GlobalConstant.API_USER_CREATE_ORDER_MANAGER_USER)
    public ResponseEntity<?> createOrderManagerUser(@RequestBody  UserDto userDto) {
        Response response = keycloakService.createUser(userDto);

        userDto.setStatusCode(response.getStatus());
        userDto.setStatus(response.getStatusInfo().toString());

        if (response.getStatus() == 201) {

            String userId = keycloakService.getUserId(response);
            log.info("Created userId = ", userId);

            keycloakService.setPasswordCredentials(userDto, userId);
            log.info("Password credentials set successfully");

            keycloakService.assignRole(userDto,userId, GlobalConstant.ROLE_ORDER_MANAGER);
            log.info(String.format("Role {} assigned to userId {}", GlobalConstant.ROLE_ORDER_MANAGER,userId));
        }
        return ResponseEntity.ok(userDto);
    }

    @PostMapping(GlobalConstant.API_USER_CREATE_TICKET_AGENT_USER)
    public ResponseEntity<?> createTicketAgentUser(@RequestBody  UserDto userDto) {
        Response response = keycloakService.createUser(userDto);

        userDto.setStatusCode(response.getStatus());
        userDto.setStatus(response.getStatusInfo().toString());

        if (response.getStatus() == 201) {

            String userId = keycloakService.getUserId(response);
            log.info("Created userId = ", userId);

            keycloakService.setPasswordCredentials(userDto, userId);
            log.info("Password credentials set successfully");

            keycloakService.assignRole(userDto,userId, GlobalConstant.ROLE_ORDER_MANAGER);
            log.info(String.format("Role {} assigned to userId {}", GlobalConstant.ROLE_TICKETING_AGENT,userId));
        }
        return ResponseEntity.ok(userDto);
    }

}
