package com.mycompany.onlineexam.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class NotFoundException extends HttpClientErrorException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message + " not found");
    }
}
