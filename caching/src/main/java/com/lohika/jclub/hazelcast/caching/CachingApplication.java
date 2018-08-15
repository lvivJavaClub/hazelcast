package com.lohika.jclub.hazelcast.caching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class CachingApplication {

  public static void main(String[] args) {
    SpringApplication.run(CachingApplication.class, args);
  }
}
