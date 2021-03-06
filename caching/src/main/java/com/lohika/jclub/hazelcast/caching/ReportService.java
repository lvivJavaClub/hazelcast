package com.lohika.jclub.hazelcast.caching;

import lombok.AllArgsConstructor;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

@CacheConfig(cacheNames = "report")
@Service
@AllArgsConstructor
public class ReportService {

  private final UserRepository userRepository;

  @PostConstruct
  public void create() {
    userRepository.save(User.builder().id(1L).name(UUID.randomUUID().toString()).build());
  }

  @Cacheable
  public String getSlowReport(Long id) {
    Optional<User> user = userRepository.findById(id);
    String report = user.map(User::getName)
        .orElseThrow(() -> new RuntimeException("UPS )"));

    try {
      Thread.sleep(5000);
    } catch (InterruptedException ignore) {
    }

    return report;
  }
}
