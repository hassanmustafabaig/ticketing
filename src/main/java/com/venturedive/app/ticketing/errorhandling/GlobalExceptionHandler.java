package com.venturedive.app.ticketing.errorhandling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Component
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

    @ResponseBody
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> exceptionHandler(MalformedJwtException error) {
        logger.warn(ErrorTypes.INVALID_TOKEN.getMessage(), error);
        return new ResponseEntity<>(ErrorTypes.INVALID_TOKEN.getMessage(),HttpStatus.UNAUTHORIZED);
    }
	
	@ResponseBody
    @ExceptionHandler(IOException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> exceptionHandler(IOException error) {
        logger.debug(ErrorTypes.INTERNAL_SERVER_ERROR.getMessage(), error);
        return new ResponseEntity<>(ErrorTypes.INTERNAL_SERVER_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ResponseBody
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> exceptionHandler(IllegalStateException error) {
        logger.debug(ErrorTypes.ILLEGAL_STATE.getMessage(), error);
    	List<String> list = new ArrayList<>();
    	try {
    		Type t = new TypeToken<List<String>>(){}.getType();
    		list = GSON.fromJson(error.getMessage(), t);
    	}
    	catch (Exception e) {
    		list.add(error.getMessage());
    	}

        return new ResponseEntity<>(ErrorTypes.ILLEGAL_STATE.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> exceptionHandler(AuthenticationException error) {
        logger.error(ErrorTypes.AUTHENTICATION_FALIURE.getMessage(), error);
        return new ResponseEntity<>(ErrorTypes.AUTHENTICATION_FALIURE.getMessage(),HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> exceptionHandler(SignatureException error) {
        logger.error(ErrorTypes.AUTHENTICATION_FALIURE.getMessage(), error);
        return new ResponseEntity<>(ErrorTypes.AUTHENTICATION_FALIURE.getMessage(),HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> exceptionHandler(ExpiredJwtException error) {
        logger.error(ErrorTypes.EXPIRED_TOKEN.getMessage(), error);
        return new ResponseEntity<>(ErrorTypes.EXPIRED_TOKEN.getMessage(),HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> exceptionHandler(AccessDeniedException error) {
        logger.error(ErrorTypes.ACCESS_DENIED.getMessage(), error);
        return new ResponseEntity<>(ErrorTypes.ACCESS_DENIED.getMessage(),HttpStatus.UNAUTHORIZED);
    }
     
    @ResponseBody
    @ExceptionHandler(JsonSyntaxException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> exceptionHandler(JsonSyntaxException error) {
        logger.error(ErrorTypes.INVALID_INPUT_DATA.getMessage(), error);
        return new ResponseEntity<>(ErrorTypes.INVALID_INPUT_DATA.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> exceptionHandler(RuntimeException error) {
        logger.error(ErrorTypes.RUNTIME_ERROR.getMessage(), error);
        return new ResponseEntity<>(ErrorTypes.RUNTIME_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object>  exceptionHandler(IllegalArgumentException error) {
        logger.error(ErrorTypes.INVALID_INPUT_DATA.getMessage(), error);
        return new ResponseEntity<>(ErrorTypes.INVALID_INPUT_DATA.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> exceptionHandler(Exception error) {
        logger.error(ErrorTypes.INTERNAL_SERVER_ERROR.getMessage(), error);
        return new ResponseEntity<>(ErrorTypes.INTERNAL_SERVER_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
