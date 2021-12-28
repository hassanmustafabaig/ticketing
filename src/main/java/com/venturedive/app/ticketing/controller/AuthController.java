package com.venturedive.app.ticketing.controller;


import com.venturedive.app.ticketing.common.constant.GlobalConstant;
import com.venturedive.app.ticketing.dto.AuthDto;
import com.venturedive.app.ticketing.dto.StringResponseDto;
import com.venturedive.app.ticketing.dto.UserDto;
import com.venturedive.app.ticketing.service.KeycloakService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConstant.API_PREFIX_AUTH)
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private KeycloakService keycloakService;

    @PostMapping(path = GlobalConstant.API_AUTH_LOGIN)
    public ResponseEntity<?> login(@RequestBody  UserDto userDto) {
        log.info("userDto is = ", userDto);
        return ResponseEntity.ok(keycloakService.getAccessToken(userDto));
    }

    @PostMapping(path = GlobalConstant.API_AUTH_GET_REFRESH_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRefreshToken(@RequestBody AuthDto authDto) {
        log.info("refresh_token is = ", authDto.getRefreshToken());
        return ResponseEntity.ok(keycloakService.getAccessTokenByRefreshToken(authDto.getRefreshToken()));
    }

    @PostMapping(path = GlobalConstant.API_AUTH_LOGOUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(@RequestBody AuthDto authDto) {
        log.info("refresh_token is = ", authDto.getRefreshToken());
        keycloakService.logout(authDto.getRefreshToken());
        return ResponseEntity.ok(new StringResponseDto("Successfully logout"));
    }
}
