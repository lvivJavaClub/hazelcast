package com.lohika.jclub.hazelcast.caching;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;

@Configuration
public class AppConfiguration {

  @Bean
  public ManagementCenterConfig managementCenterConfig(
      @Value("${hazelcast.managementCenter:}") String managementCenter) {
    ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig(managementCenter, 1);
    managementCenterConfig.setEnabled(true);
    return managementCenterConfig;
  }

  @Bean
  public GroupConfig groupConfig(
      @Value("${hazelcast.groupConfig.name:}") String groupConfigUser,
      @Value("${hazelcast.groupConfig.password:}") String groupConfigPassword) {
    return new GroupConfig(groupConfigUser, groupConfigPassword);
  }

  @Bean
  public Config config(ManagementCenterConfig managementCenterConfig, GroupConfig groupConfig) {
    Config config = new XmlConfigBuilder().build();

    JoinConfig joinConfig = config.getNetworkConfig().getJoin();

    joinConfig.getMulticastConfig().setEnabled(false);
    joinConfig.getTcpIpConfig().setEnabled(true).setMembers(singletonList("127.0.0.1"));

    config.setManagementCenterConfig(managementCenterConfig);
    config.setProperty("hazelcast.rest.enabled", "true");
    config.setGroupConfig(groupConfig);
    return config;
  }

  @Bean
  public HazelcastInstance hazelcastInstance(Config config) {
    return Hazelcast.newHazelcastInstance(config);
  }

  @Bean
  public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
    return new HazelcastCacheManager(hazelcastInstance);
  }
}
