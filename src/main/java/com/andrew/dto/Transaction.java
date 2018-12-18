package com.andrew.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.andrew.util.DateSerializer;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author andrew
 */
@XmlRootElement
public class Transaction implements Serializable {

  private static final long serialVersionUID = 1L;

  private BigDecimal amount;

  @JsonSerialize(using = DateSerializer.class)
  private Date timestamp;

  public Transaction() {
  }

  public Transaction(BigDecimal amount, Date timestamp) {
    this.amount = amount;
    this.timestamp = timestamp;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }


  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "Transaction{" +
        "amount=" + amount +
        ", timestamp=" + timestamp +
        '}';
  }
}
