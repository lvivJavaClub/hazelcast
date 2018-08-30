package com.lohika.jclub.hazelcast.jet;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JetConfig;

import org.springframework.beans.factory.annotation.Value;
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
    config.setProperty("hazelcast.rest.enabled", "true");

    JoinConfig joinConfig = config.getNetworkConfig().getJoin();

    joinConfig.getMulticastConfig().setEnabled(false);
    joinConfig.getTcpIpConfig().setEnabled(true).setMembers(singletonList("127.0.0.1"));

    config.setManagementCenterConfig(managementCenterConfig);
    config.setGroupConfig(groupConfig);
    return config;
  }

  @Bean
  public JetInstance jetInstance(Config config) {
    JetConfig jetConfig = new JetConfig();
    jetConfig.setHazelcastConfig(config);

    return Jet.newJetInstance(jetConfig);

  }
}
