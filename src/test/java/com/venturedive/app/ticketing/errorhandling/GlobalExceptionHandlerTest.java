package com.venturedive.app.ticketing.errorhandling;

import com.google.gson.JsonSyntaxException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandlerUnderTest;

    @BeforeEach
    void setUp() {
        globalExceptionHandlerUnderTest = new GlobalExceptionHandler();
    }

    @Test
    void shouldReturnMalformedJwtExceptionErrorMessageAndStatus() {
        MalformedJwtException error = new MalformedJwtException(ErrorTypes.INVALID_TOKEN.getMessage(), new Exception(ErrorTypes.INVALID_TOKEN.getMessage()));
        ResponseEntity<Object> result = globalExceptionHandlerUnderTest.exceptionHandler(error);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturnIOExceptionErrorMessageAndStatus() {
        IOException error = new IOException(ErrorTypes.INTERNAL_SERVER_ERROR.getMessage(), new Exception(ErrorTypes.INTERNAL_SERVER_ERROR.getMessage()));
        ResponseEntity<Object> result = globalExceptionHandlerUnderTest.exceptionHandler(error);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void shouldReturnIllegalStateExceptionErrorMessageAndStatus() {
        IllegalStateException error = new IllegalStateException(ErrorTypes.ILLEGAL_STATE.getMessage(), new Exception(ErrorTypes.ILLEGAL_STATE.getMessage()));
        ResponseEntity<Object> result = globalExceptionHandlerUnderTest.exceptionHandler(error);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void shouldReturnSignatureExceptionErrorMessageAndStatus() {
         SignatureException error = new SignatureException(ErrorTypes.AUTHENTICATION_FALIURE.getMessage(), new Exception(ErrorTypes.AUTHENTICATION_FALIURE.getMessage()));
         ResponseEntity<Object> result = globalExceptionHandlerUnderTest.exceptionHandler(error);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturnExpiredJwtExceptionErrorMessageAndStatus() {
        ExpiredJwtException error = new ExpiredJwtException(null, null, ErrorTypes.EXPIRED_TOKEN.getMessage());
        ResponseEntity<Object> result = globalExceptionHandlerUnderTest.exceptionHandler(error);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturnAccessDeniedExceptionErrorMessageAndStatus() {
        AccessDeniedException error = new AccessDeniedException(ErrorTypes.ACCESS_DENIED.getMessage(), new Exception(ErrorTypes.ACCESS_DENIED.getMessage()));
        ResponseEntity<Object> result = globalExceptionHandlerUnderTest.exceptionHandler(error);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturnJsonSyntaxExceptionErrorMessageAndStatus() {
        JsonSyntaxException error = new JsonSyntaxException(ErrorTypes.INVALID_INPUT_DATA.getMessage(), new Exception(ErrorTypes.INVALID_INPUT_DATA.getMessage()));
        ResponseEntity<Object> result = globalExceptionHandlerUnderTest.exceptionHandler(error);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturnRuntimeExceptionErrorMessageAndStatus() {
        ResponseEntity<Object> result = globalExceptionHandlerUnderTest.exceptionHandler(new RuntimeException(ErrorTypes.RUNTIME_ERROR.getMessage()));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void shouldReturnIllegalArgumentExceptionErrorMessageAndStatus() {
        IllegalArgumentException error = new IllegalArgumentException(ErrorTypes.INVALID_INPUT_DATA.getMessage(), new Exception(ErrorTypes.INVALID_INPUT_DATA.getMessage()));
        ResponseEntity<Object> result = globalExceptionHandlerUnderTest.exceptionHandler(error);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturnExceptionErrorMessageAndStatus() {
        ResponseEntity<Object> result = globalExceptionHandlerUnderTest.exceptionHandler(new Exception(ErrorTypes.INTERNAL_SERVER_ERROR.getMessage()));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
