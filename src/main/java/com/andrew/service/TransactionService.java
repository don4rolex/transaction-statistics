package com.andrew.service;

import com.andrew.dto.Transaction;
import com.andrew.exception.FutureTransactionDateException;
import com.andrew.exception.TransactionOutOfDateException;

import java.util.List;

/**
 * @author andrew
 */
public interface TransactionService {

  void addTransaction(Transaction transaction) throws TransactionOutOfDateException, FutureTransactionDateException;

  void deleteTransaction();

  List<Transaction> getTransactions();
}
