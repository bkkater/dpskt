package br.edu.iff.ccc.bsi.dpskt_api.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }
}
