package com.lohika.jclub.hazelcast.main;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class MainApplication {

  public MainApplication() {
    Config config = new Config();

    config.getGroupConfig().setName("hz-app").setPassword("s3crEt");

    config.setProperty("hazelcast.rest.enabled", "true");

    config.getManagementCenterConfig().setEnabled(true);
    config.getManagementCenterConfig().setUrl("http://127.0.0.1:5700/hazelcast-mancenter");

    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
  }

  public static void main(String[] args) throws InterruptedException {
    new MainApplication();
  }

}

