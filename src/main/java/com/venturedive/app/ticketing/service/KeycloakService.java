package com.venturedive.app.ticketing.service;

import com.venturedive.app.ticketing.dto.AuthDto;
import com.venturedive.app.ticketing.dto.UserDto;
import org.keycloak.representations.AccessTokenResponse;

import javax.ws.rs.core.Response;

public interface KeycloakService {
    Response createUser(UserDto userDto);
    String getUserId(Response response);
    void setPasswordCredentials(UserDto userDto, String userId);
    void assignRole(UserDto userDto, String userId, String role);
    AuthDto getAccessToken(UserDto userDto);
    AccessTokenResponse getAccessTokenByRefreshToken(String refreshToken);
    void logout(String refreshToken);
}
