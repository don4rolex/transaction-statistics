package com.andrew.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.AbstractMap;

/**
 * @author andrew
 */
@ControllerAdvice
@Component
public class GlobalExceptionHandler {

  @Value("${unparsable.field}")
  private int UNPARSABLE_FIELD;

  @Value("${invalid.json}")
  private int INVALID_JSON;

  @Value("${transaction.out.of.date}")
  private int TRANSACTION_OUT_OF_DATE;

  private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(value = {FutureTransactionDateException.class, InvalidFormatException.class})
  public ResponseEntity<AbstractMap.SimpleEntry<String, String>> unparsable(Exception exception) {
    LOG.error("Exception: Unable to process request. ", exception);

    return ResponseEntity.status(UNPARSABLE_FIELD).build();
  }

  @ExceptionHandler(value = {MismatchedInputException.class})
  public ResponseEntity<AbstractMap.SimpleEntry<String, String>> invalidJson(Exception exception) {
    LOG.error("Exception: Invalid JSON. ", exception);

    return ResponseEntity.status(INVALID_JSON).build();
  }

  @ExceptionHandler(value = {TransactionOutOfDateException.class})
  public ResponseEntity<AbstractMap.SimpleEntry<String, String>> outOfDate(Exception exception) {
    LOG.error("Exception: Transaction is out of date. ", exception);

    return ResponseEntity.status(TRANSACTION_OUT_OF_DATE).build();
  }
}
