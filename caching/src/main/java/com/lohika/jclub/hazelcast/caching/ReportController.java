package com.lohika.jclub.hazelcast.caching;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @GetMapping("/{id}")
  @ResponseBody
  public ResponseEntity<String> getSlowReport(@PathVariable Long id) {
    return new ResponseEntity<>(reportService.getSlowReport(id), HttpStatus.OK);
  }
}
