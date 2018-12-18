package com.andrew.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.andrew.util.BigDecimalSerializer;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author andrew
 */
@XmlRootElement
public class Statistics implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonSerialize(using = BigDecimalSerializer.class)
  private BigDecimal sum;

  @JsonSerialize(using = BigDecimalSerializer.class)
  private BigDecimal avg;

  @JsonSerialize(using = BigDecimalSerializer.class)
  private BigDecimal max;

  @JsonSerialize(using = BigDecimalSerializer.class)
  private BigDecimal min;

  private long count;

  public Statistics() {
  }

  public Statistics(long count, BigDecimal sum, BigDecimal avg, BigDecimal min, BigDecimal max) {
    this.count = count;
    this.sum = sum;
    this.avg = avg;
    this.min = min;
    this.max = max;
  }

  public BigDecimal getSum() {
    return sum;
  }

  public void setSum(BigDecimal sum) {
    this.sum = sum;
  }

  public BigDecimal getAvg() {
    return avg;
  }

  public void setAvg(BigDecimal avg) {
    this.avg = avg;
  }

  public BigDecimal getMax() {
    return max;
  }

  public void setMax(BigDecimal max) {
    this.max = max;
  }

  public BigDecimal getMin() {
    return min;
  }

  public void setMin(BigDecimal min) {
    this.min = min;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  @Override
  public String toString() {
    return "Statistics{" +
        "sum=" + sum +
        ", avg=" + avg +
        ", max=" + max +
        ", min=" + min +
        ", count=" + count +
        '}';
  }
}
