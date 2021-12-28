package com.venturedive.app.ticketing.dto;

import lombok.*;
import org.keycloak.representations.AccessTokenResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class AuthDto {

    private AccessTokenResponse accessTokenResponse;
    private List<String> roles;
    private String refreshToken;
}