package com.andrew.service;

import com.andrew.exception.FutureTransactionDateException;
import com.andrew.exception.TransactionOutOfDateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author andrew
 */
@Service
public class TimeServiceImpl implements TimeService {

  @Value("${max.time.duration}")
  private long maxDuration;

  @Override
  public boolean isTimestampValid(Date timestamp) throws FutureTransactionDateException, TransactionOutOfDateException {
    final LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
    final LocalDateTime transactionTime = LocalDateTime.from(timestamp.toInstant().atZone(ZoneId.of("UTC")));

    if (transactionTime.isAfter(now)) {
      throw new FutureTransactionDateException("Transaction time cannot be in the future");
    }

    final long timeDifference = transactionTime.until(now, ChronoUnit.SECONDS);

    if (timeDifference >= maxDuration) {
      throw new TransactionOutOfDateException("Transaction is out of date");
    }

    return true;
  }
}
