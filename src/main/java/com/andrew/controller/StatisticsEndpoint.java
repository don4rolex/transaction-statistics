package com.andrew.controller;

import com.andrew.dto.Statistics;
import com.andrew.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andrew
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsEndpoint {

  private final StatisticsService statisticsService;

  @Autowired
  public StatisticsEndpoint(StatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @GetMapping
  public ResponseEntity<Statistics> getStatistics() {

    return ResponseEntity.ok(statisticsService.getStatistics());
  }
}
