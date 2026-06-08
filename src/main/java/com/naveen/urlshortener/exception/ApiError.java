package com.naveen.urlshortener.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiError {

    private final boolean success;
    private final String message;
    private final LocalDateTime timeStamp;

    public ApiError(String message) {
        this.success = false;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }
}
