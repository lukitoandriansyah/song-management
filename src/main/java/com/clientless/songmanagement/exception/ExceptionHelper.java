package com.clientless.songmanagement.exception;

import com.clientless.songmanagement.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ExceptionHelper {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHelper.class);

//    @ExceptionHandler(value = { Exception.class })
//    public ResponseEntity<String> handleException(Exception ex) throws JsonProcessingException {
//        logger.error("Exception: ",ex.getMessage());
//        return new ResponseEntity<String>(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS).writeValueAsString(new Response(ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value())), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(value = { HttpClientErrorException.Unauthorized.class })
    public ResponseEntity<Response> handleUnauthorizedException(HttpClientErrorException.Unauthorized ex) throws JsonProcessingException {
        logger.error("Exception: ",ex.getMessage());
        return new ResponseEntity<Response>(new Response(ex.getMessage(), null, HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { HttpClientErrorException.Forbidden.class })
    public ResponseEntity<Response> handleForbiddenException(HttpClientErrorException.Unauthorized ex) throws JsonProcessingException {
        logger.error("Exception: ",ex.getMessage());
        return new ResponseEntity<Response>(new Response(ex.getMessage(), null, HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { NotFoundException.class })
    public ResponseEntity<Response> handleNotFoundException(NotFoundException ex) throws JsonProcessingException {
        logger.error("Not Found Exception: ",ex.getMessage());
        return new ResponseEntity<>(new Response(ex.getMessage(), null,HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = { NotFoundExceptionCausesDelete.class })
    public ResponseEntity<Response> handleNotFoundExceptionCausesDelete(NotFoundExceptionCausesDelete ex) throws JsonProcessingException {
        logger.error("Not Found Exception: ",ex.getMessage());
        return new ResponseEntity<>(new Response(ex.getMessage(), null,HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { MissingServletRequestParameterException.class })
    public ResponseEntity<Response> handleBadRequestException(MissingServletRequestParameterException ex) throws JsonProcessingException {
        logger.error("Bad Request Exception: ",ex.getMessage());
        return new ResponseEntity<Response>(new Response(ex.getMessage(), null, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

}
