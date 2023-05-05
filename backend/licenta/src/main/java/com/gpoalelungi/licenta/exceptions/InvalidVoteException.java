package com.gpoalelungi.licenta.exceptions;

public class InvalidVoteException extends RuntimeException{
  public InvalidVoteException(String message) {
    super(message);
  }
}
