package com.cinnamonbay.backend.service;

public class InternalServerException extends RuntimeException{
    public InternalServerException(String message) {
        super(message);
    }
}
