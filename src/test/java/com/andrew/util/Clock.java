package com.andrew.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author andrew
 */
public class Clock {

  private static Date frozenTime;

  private static final LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

  public static Date freeze() {
    final Date time = Date.from(now.toInstant(ZoneOffset.UTC));
    return freeze(time);
  }

  public static Date freeze(Date time) {
    frozenTime = new Date(time.getTime());
    return time;
  }

  public static Date unfreeze() {
    final Date frozenTime = Clock.frozenTime;
    Clock.frozenTime = null;
    return frozenTime;
  }

  public static boolean isFrozen() {
    return frozenTime != null;
  }

  public static Date getTime() {
    return isFrozen() ? frozenTime : Date.from(now.toInstant(ZoneOffset.UTC));
  }
}
