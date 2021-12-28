package com.venturedive.app.ticketing.config;

import com.venturedive.app.ticketing.common.enumerations.CustomerType;
import com.venturedive.app.ticketing.common.enumerations.DeliveryStatus;
import com.venturedive.app.ticketing.common.enumerations.Priority;
import com.venturedive.app.ticketing.common.enumerations.TicketStatus;
import com.venturedive.app.ticketing.entity.Delivery;
import com.venturedive.app.ticketing.entity.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfigurationSource;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WebSecurityConfigTest {

    private WebSecurityConfig webSecurityConfigUnderTest;

    @BeforeEach
    void setUp() {
        webSecurityConfigUnderTest = new WebSecurityConfig();
    }


    @Test
    void shouldReturnSessionAuthenticationStrategy() {
        SessionAuthenticationStrategy result = webSecurityConfigUnderTest.sessionAuthenticationStrategy();
        assertThat(result).isNotNull();
    }

    @Test
    void shouldReturnKeycloakConfigResolver() {
        KeycloakConfigResolver result = webSecurityConfigUnderTest.keycloakConfigResolver();
        assertThat(result).isNotNull();
    }

    @Test
    void shouldReturnCorsConfigurationSource() {
        CorsConfigurationSource result = webSecurityConfigUnderTest.corsConfigurationSource();
        assertThat(result).isNotNull();
    }

    @Test
    void shouldReturnTicket() {
        Ticket result = webSecurityConfigUnderTest.ticket();
        assertThat(result).isNotNull();
    }
}
