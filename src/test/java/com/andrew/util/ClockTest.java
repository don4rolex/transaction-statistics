package com.andrew.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author andrew
 */
public class ClockTest {

  @Before
  public void setup() {
    Clock.unfreeze();
  }

  @Test
  public void freeze() {
    Calendar yesterday = Calendar.getInstance();
    yesterday.set(Calendar.YEAR, 2017);
    yesterday.set(Calendar.MONTH, Calendar.AUGUST);
    yesterday.set(Calendar.DAY_OF_MONTH, Calendar.THURSDAY);

    Clock.freeze(yesterday.getTime());
    assertEquals(yesterday.getTime(), Clock.getTime());
  }

  @Test
  public void unfreeze() {
    assertFalse(Clock.isFrozen());
  }

  @Test
  public void isFrozen() {
    Clock.freeze();
    assertTrue(Clock.isFrozen());
  }

  @Test
  public void getTime() {
    Calendar yesterday = Calendar.getInstance();
    yesterday.set(Calendar.YEAR, 2017);
    yesterday.set(Calendar.MONTH, Calendar.AUGUST);
    yesterday.set(Calendar.DAY_OF_MONTH, Calendar.THURSDAY);

    Date frozenTime = Clock.freeze(yesterday.getTime());
    assertEquals(frozenTime, Clock.getTime());

    Clock.unfreeze();
    assertNotEquals(frozenTime, Clock.getTime());
  }
}