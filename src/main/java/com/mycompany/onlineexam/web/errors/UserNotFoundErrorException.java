package com.mycompany.onlineexam.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class UserNotFoundErrorException extends HttpClientErrorException {
    public UserNotFoundErrorException(String user, String code) {
        super(HttpStatus.NOT_FOUND, user + " with " + user + "-code : " + code + " Not Found");
    }
}
