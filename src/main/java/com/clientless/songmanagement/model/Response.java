package com.clientless.songmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response implements Serializable {

    public Response(String message, Object data, int code) {
        this.message = message;
        this.data = data;
        this.code = code;
    }

    private String message = null;
    private Object data = null;
    private int code = 500;

}
