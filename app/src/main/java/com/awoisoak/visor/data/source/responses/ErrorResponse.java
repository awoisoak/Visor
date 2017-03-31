package com.awoisoak.visor.data.source.responses;

/**
 * Generic Error Response
 */

public class ErrorResponse extends WPResponse {

    String message;

    public ErrorResponse(String message) {
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

}
