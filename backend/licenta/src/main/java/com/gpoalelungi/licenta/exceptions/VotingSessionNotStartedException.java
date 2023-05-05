package com.gpoalelungi.licenta.exceptions;

public class VotingSessionNotStartedException extends RuntimeException{
  public VotingSessionNotStartedException(String message) {
    super(message);
  }
}
