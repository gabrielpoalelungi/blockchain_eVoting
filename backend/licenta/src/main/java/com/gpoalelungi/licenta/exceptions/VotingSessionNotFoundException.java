package com.gpoalelungi.licenta.exceptions;

public class VotingSessionNotFoundException extends RuntimeException{
  public VotingSessionNotFoundException(String message) {
    super(message);
  }
}
