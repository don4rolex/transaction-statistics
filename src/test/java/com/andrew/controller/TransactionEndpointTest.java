package com.andrew.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.andrew.dto.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author andrew
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionEndpointTest {

  MockMvc mockMvc;

  @Mock
  private TransactionEndpoint transactionEndpoint;

  @Autowired
  private TestRestTemplate template;

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
    mockMvc = MockMvcBuilders.standaloneSetup(transactionEndpoint).build();
  }

  @Test
  public void addTransaction() throws Exception {
    final ResponseEntity response = createTransaction(new BigDecimal(200), new Date());
    Assert.assertEquals(REQUEST_SUCCESSFUL, response.getStatusCode().value());
  }

  @Test
  public void deleteTransactions() throws Exception {
    createTransaction(new BigDecimal(100), new Date());

    final ResponseEntity response = template.exchange("/transactions", HttpMethod.DELETE, null, ResponseEntity.class);
    Assert.assertEquals(TRANSACTION_OUT_OF_DATE, response.getStatusCode().value());
  }

  @Test
  public void addTransaction_outOfDate() throws Exception {
    final LocalDateTime timeAhead = LocalDateTime.now(ZoneId.of("UTC")).minusSeconds(70);
    final Date date = Date.from(timeAhead.toInstant(ZoneOffset.UTC));

    final ResponseEntity response = createTransaction(new BigDecimal(100.99), date);
    Assert.assertEquals(TRANSACTION_OUT_OF_DATE, response.getStatusCode().value());
  }

  @Test
  public void addTransaction_futureTransaction() throws Exception {
    final LocalDateTime timeAhead = LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(2);
    final Date date = Date.from(timeAhead.toInstant(ZoneOffset.UTC));

    final ResponseEntity response = createTransaction(new BigDecimal(100.00), date);
    Assert.assertEquals(UNPARSABLE_FIELD_FUTURE_TRANSACTION, response.getStatusCode().value());
  }

  @Test
  public void addTransaction_unparsableAmount() {
    final Object request = getHttpEntity("{\"amount\":\"amount\",\"timestamp\":\"2018-12-15T08:09:27.016Z\"}");

    final ResponseEntity response = template.postForEntity("/transactions", request, ResponseEntity.class);
    Assert.assertEquals(UNPARSABLE_FIELD_FUTURE_TRANSACTION, response.getStatusCode().value());
  }

  @Test
  public void addTransaction_unparsableDate() {
    final Object request = getHttpEntity("{\"timestamp\":\"10/12/2018 9:10 PM\",\"amount\":\"262.01\"}");

    final ResponseEntity response = template.postForEntity("/transactions", request, ResponseEntity.class);
    Assert.assertEquals(UNPARSABLE_FIELD_FUTURE_TRANSACTION, response.getStatusCode().value());
  }

  @Test
  public void addTransaction_invalidJson() {
    final Object request = getHttpEntity("false!");

    final ResponseEntity response = template.postForEntity("/transactions", request, ResponseEntity.class);
    Assert.assertEquals(INVALID_JSON, response.getStatusCode().value());
  }

  private ResponseEntity createTransaction(BigDecimal amount, Date date) throws Exception {
    final ObjectMapper mapper = new ObjectMapper();
    final Transaction transaction = new Transaction(amount, date);
    final String json = mapper.writeValueAsString(transaction);

    final HttpEntity<Object> request = getHttpEntity(json);

    return template.postForEntity("/transactions", request, ResponseEntity.class);
  }

  private HttpEntity<Object> getHttpEntity(Object body) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return new HttpEntity<>(body, headers);
  }
}