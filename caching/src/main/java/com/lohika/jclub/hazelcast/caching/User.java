package com.lohika.jclub.hazelcast.caching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class User {
  private Long id;
  private String name;
}
