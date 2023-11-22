package com.clientless.songmanagement.util;

import com.clientless.songmanagement.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class GenerateResponse {
    public static ResponseEntity<Response> success(String message, Object data) throws JsonProcessingException {

        return new ResponseEntity<>(new Response(message,data,200),HttpStatus.OK);

    }

    public static ResponseEntity<Response> error(String message, Object data) throws JsonProcessingException {

        return new ResponseEntity<>(new Response(message,data,500), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public static ResponseEntity<Response> created(String message, Object data) throws JsonProcessingException {

        return new ResponseEntity<>(new Response(message,data,201), HttpStatus.CREATED);

    }

/*    public static ResponseEntity<String> notFound(String message, Object data) throws JsonProcessingException {

        return new ResponseEntity<String>(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS).writeValueAsString(new Response(message, data, 404)), HttpStatus.NOT_FOUND);

    }

    public static ResponseEntity<String> badRequest(String message, Object data) throws JsonProcessingException {

        return new ResponseEntity<String>(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS).writeValueAsString(new Response(message, data, 400)), HttpStatus.BAD_REQUEST);

    }

    public static ResponseEntity<String> unauthorized(Object data) throws JsonProcessingException {

        return new ResponseEntity<String>(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS).writeValueAsString(new Response("unauthorized", data, 401)), HttpStatus.UNAUTHORIZED);

    }

    public static ResponseEntity<String> unauthenticated(Object data) throws JsonProcessingException {

        return new ResponseEntity<String>(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS).writeValueAsString(new Response("unauthenticated", data, 403)), HttpStatus.FORBIDDEN);

    }*/
}
