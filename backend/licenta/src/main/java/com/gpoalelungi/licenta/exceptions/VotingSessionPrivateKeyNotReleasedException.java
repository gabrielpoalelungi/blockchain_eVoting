package com.gpoalelungi.licenta.exceptions;

public class VotingSessionPrivateKeyNotReleasedException extends RuntimeException{
  public VotingSessionPrivateKeyNotReleasedException(String message) {
    super(message);
  }
}
