package com.gpoalelungi.licenta.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
      super(message);
    }
}
