package com.andrew.controller;

import com.andrew.dto.Transaction;
import com.andrew.exception.FutureTransactionDateException;
import com.andrew.exception.TransactionOutOfDateException;
import com.andrew.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andrew
 */
@RestController
@RequestMapping("/transactions")
public class TransactionEndpoint {

  @Value("${request.successful}")
  private int REQUEST_SUCCESSFUL;

  @Value("${transaction.out.of.date}")
  private int TRANSACTION_OUT_OF_DATE;

  @Value("${unparsable.field}")
  private int UNPARSABLE_FIELD;

  private final TransactionService transactionService;

  @Autowired
  public TransactionEndpoint(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping
  public ResponseEntity addTransaction(@RequestBody Transaction transaction) throws FutureTransactionDateException,
      TransactionOutOfDateException {

    transactionService.addTransaction(transaction);

    return ResponseEntity.status(REQUEST_SUCCESSFUL).build();
  }

  @DeleteMapping
  public ResponseEntity deleteTransactions() {
    transactionService.deleteTransaction();

    return ResponseEntity.status(TRANSACTION_OUT_OF_DATE).build();
  }
}
