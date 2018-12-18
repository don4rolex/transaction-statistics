package com.andrew.service;

import com.andrew.dto.Statistics;
import com.andrew.dto.Transaction;
import com.andrew.exception.FutureTransactionDateException;
import com.andrew.exception.TransactionOutOfDateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author andrew
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

  private final TransactionService transactionService;

  private final TimeService timeService;

  @Autowired
  public StatisticsServiceImpl(TransactionService transactionService, TimeService timeService) {
    this.transactionService = transactionService;
    this.timeService = timeService;
  }

  @Override
  public Statistics getStatistics() {
    final List<Transaction> recentTransactions = getRecentTransactions();
    if (recentTransactions.isEmpty()) {
      return new Statistics(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    final long count = recentTransactions.size();
    final BigDecimal sum = recentTransactions.stream()
        .map(Transaction::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    final BigDecimal avg = sum.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP);
    final BigDecimal min = recentTransactions.get(0).getAmount();
    final BigDecimal max = recentTransactions.get(recentTransactions.size() - 1).getAmount();

    return new Statistics(count, sum, avg, min, max);
  }

  private List<Transaction> getRecentTransactions() {
    final List<Transaction> transactions = transactionService.getTransactions();
    synchronized (transactions) {
      return transactions.stream()
          .filter(this::filterTransactions)
          .sorted(Comparator.comparing(Transaction::getAmount))
          .collect(Collectors.toList());
    }
  }

  private boolean filterTransactions(Transaction transaction) {
    try {
      return timeService.isTimestampValid(transaction.getTimestamp());
    } catch (FutureTransactionDateException | TransactionOutOfDateException ex) {
      return false;
    }
  }
}
