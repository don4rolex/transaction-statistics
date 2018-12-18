package com.andrew.service;

import com.andrew.exception.FutureTransactionDateException;
import com.andrew.exception.TransactionOutOfDateException;

import java.util.Date;

/**
 * @author andrew
 */
public interface TimeService {

  boolean isTimestampValid(Date timestamp) throws FutureTransactionDateException, TransactionOutOfDateException;
}
