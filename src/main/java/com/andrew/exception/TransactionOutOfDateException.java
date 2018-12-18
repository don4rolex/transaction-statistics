package com.andrew.exception;

/**
 * @author andrew
 */
public class TransactionOutOfDateException extends Exception {

  public TransactionOutOfDateException(String message) {
    super(message);
  }
}
