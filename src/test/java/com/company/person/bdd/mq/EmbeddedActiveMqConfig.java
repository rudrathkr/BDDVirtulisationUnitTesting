package com.company.person.bdd.mq;

import org.apache.activemq.broker.BrokerService;

public class EmbeddedActiveMqConfig {

    public static final String BROKER_URL = "vm://bdd-broker?create=false";

    private BrokerService brokerService;

    public void start() throws Exception {
        brokerService = new BrokerService();
        brokerService.setBrokerName("bdd-broker");
        brokerService.setPersistent(false);
        brokerService.setUseJmx(false);
        brokerService.addConnector("vm://bdd-broker");
        brokerService.start();
        brokerService.waitUntilStarted();
    }

    public void stop() throws Exception {
        if (brokerService != null) {
            brokerService.stop();
            brokerService.waitUntilStopped();
        }
    }
}
