package com.andrew.service;

import com.andrew.dto.Transaction;
import com.andrew.exception.FutureTransactionDateException;
import com.andrew.exception.TransactionOutOfDateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author andrew
 */
@Service
public class TransactionServiceImpl implements TransactionService {

  private final List<Transaction> transactions = Collections.synchronizedList(new ArrayList<>());

  private final TimeService timeService;

  @Autowired
  public TransactionServiceImpl(TimeService timeService) {
    this.timeService = timeService;
  }

  @Override
  public void addTransaction(Transaction transaction) throws TransactionOutOfDateException, FutureTransactionDateException {
    timeService.isTimestampValid(transaction.getTimestamp());
    transactions.add(transaction);
  }

  @Override
  public void deleteTransaction() {
    transactions.clear();
  }

  @Override
  public List<Transaction> getTransactions() {
    return transactions;
  }

}
