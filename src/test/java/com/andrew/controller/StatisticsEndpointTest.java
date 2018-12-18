package com.andrew.controller;

import com.andrew.dto.Statistics;
import com.andrew.dto.Transaction;
import com.andrew.service.TransactionService;
import com.andrew.util.Clock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author andrew
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticsEndpointTest {

  MockMvc mockMvc;

  @Mock
  private StatisticsEndpoint statisticsEndpoint;

  @Autowired
  private TestRestTemplate template;

  @Autowired
  private TransactionService transactionService;

  @Value("${request.successful}")
  private int REQUEST_SUCCESSFUL;

  @Value("${transaction.out.of.date}")
  private int TRANSACTION_OUT_OF_DATE;

  @Value("${unparsable.field}")
  private int UNPARSABLE_FIELD_FUTURE_TRANSACTION;

  @Value("${invalid.json}")
  private int INVALID_JSON;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(statisticsEndpoint).build();
  }

  @After
  public void tearDown() {
    transactionService.deleteTransaction();
  }

  @Test
  public void getStatistics() throws Exception {
    createTransactions();

    final List<Transaction> transactions = getTransactions();
    transactions.sort(Comparator.comparing(Transaction::getAmount));

    final long count = transactions.size();

    final BigDecimal sum = transactions.stream()
        .map(Transaction::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .setScale(2, BigDecimal.ROUND_HALF_UP);

    final BigDecimal avg = sum.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP);
    final BigDecimal min = transactions.get(0).getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
    final BigDecimal max = transactions.get(transactions.size() - 1).getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);

    final ResponseEntity<Statistics> response = template.getForEntity("/statistics", Statistics.class);
    final Statistics statistics = response.getBody();

    Assert.assertEquals(sum, statistics.getSum());
    Assert.assertEquals(avg, statistics.getAvg());
    Assert.assertEquals(max, statistics.getMax());
    Assert.assertEquals(min, statistics.getMin());
    Assert.assertEquals(count, statistics.getCount());
  }

  @Test
  public void getStatistics_noTransactions() {
    final ResponseEntity<Statistics> response = template.getForEntity("/statistics", Statistics.class);
    final Statistics statistics = response.getBody();

    final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);

    Assert.assertEquals(ZERO, statistics.getSum());
    Assert.assertEquals(ZERO, statistics.getAvg());
    Assert.assertEquals(ZERO, statistics.getMax());
    Assert.assertEquals(ZERO, statistics.getMin());
    Assert.assertEquals(ZERO.longValue(), statistics.getCount());
  }

  @Test
  public void getStatistics_oldTransactions() throws Exception {
    final LocalDateTime timeBehind = LocalDateTime.now(ZoneId.of("UTC")).minusSeconds(57);
    final Date date = Date.from(timeBehind.toInstant(ZoneOffset.UTC));

    Clock.freeze(date);
    createTransactions();
    Clock.unfreeze();

    Thread.sleep(5000);

    final ResponseEntity<Statistics> response = template.getForEntity("/statistics", Statistics.class);
    final Statistics statistics = response.getBody();

    final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);

    Assert.assertEquals(ZERO, statistics.getSum());
    Assert.assertEquals(ZERO, statistics.getAvg());
    Assert.assertEquals(ZERO, statistics.getMax());
    Assert.assertEquals(ZERO, statistics.getMin());
    Assert.assertEquals(ZERO.longValue(), statistics.getCount());
  }

  @Test
  public void getStatistics_newTransaction() throws Exception {
    final LocalDateTime timeBehind = LocalDateTime.now(ZoneId.of("UTC")).minusSeconds(57);
    final Date date = Date.from(timeBehind.toInstant(ZoneOffset.UTC));

    Clock.freeze(date);
    createTransactions();
    Clock.unfreeze();

    Thread.sleep(5000);

    final BigDecimal TEN = BigDecimal.TEN.setScale(2, BigDecimal.ROUND_HALF_UP);

    transactionService.addTransaction(new Transaction(TEN, Clock.getTime()));

    final ResponseEntity<Statistics> response = template.getForEntity("/statistics", Statistics.class);
    final Statistics statistics = response.getBody();

    Assert.assertEquals(TEN, statistics.getSum());
    Assert.assertEquals(TEN, statistics.getAvg());
    Assert.assertEquals(TEN, statistics.getMax());
    Assert.assertEquals(TEN, statistics.getMin());
    Assert.assertEquals(1, statistics.getCount());
  }

  private List<Transaction> getTransactions() {
    final List<Transaction> transactions = Arrays.asList(
        new Transaction(new BigDecimal(100.00), Clock.getTime()),
        new Transaction(new BigDecimal(294.00), Clock.getTime()),
        new Transaction(new BigDecimal(124.00), Clock.getTime()),
        new Transaction(new BigDecimal(541.00), Clock.getTime()),
        new Transaction(new BigDecimal(100.98), Clock.getTime()),
        new Transaction(new BigDecimal(220.21), Clock.getTime()),
        new Transaction(new BigDecimal(10.00), Clock.getTime()),
        new Transaction(new BigDecimal(300.00), Clock.getTime()),
        new Transaction(new BigDecimal(140.00), Clock.getTime()),
        new Transaction(new BigDecimal(780.00), Clock.getTime()),
        new Transaction(new BigDecimal(100.70), Clock.getTime()),
        new Transaction(new BigDecimal(650.90), Clock.getTime()),
        new Transaction(new BigDecimal(100.00), Clock.getTime()),
        new Transaction(new BigDecimal(1434.00), Clock.getTime()),
        new Transaction(new BigDecimal(130.00), Clock.getTime()),
        new Transaction(new BigDecimal(14340.00), Clock.getTime()),
        new Transaction(new BigDecimal(103230.00), Clock.getTime()),
        new Transaction(new BigDecimal(3100.00), Clock.getTime()),
        new Transaction(new BigDecimal(400.00), Clock.getTime()),
        new Transaction(new BigDecimal(600.00), Clock.getTime()),
        new Transaction(new BigDecimal(8900.00), Clock.getTime()),
        new Transaction(new BigDecimal(10430.00), Clock.getTime()),
        new Transaction(new BigDecimal(3100.00), Clock.getTime()),
        new Transaction(new BigDecimal(800.00), Clock.getTime())
    );

    return transactions;
  }

  private void createTransactions() throws Exception {
    final List<Transaction> transactions = getTransactions();
    for (Transaction transaction : transactions) {
      transactionService.addTransaction(transaction);
    }
  }
}