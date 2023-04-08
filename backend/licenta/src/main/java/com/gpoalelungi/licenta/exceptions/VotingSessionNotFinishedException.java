package com.gpoalelungi.licenta.exceptions;

public class VotingSessionNotFinishedException extends RuntimeException{
    public VotingSessionNotFinishedException(String message) {
      super(message);
    }
}
