package com.company.person.bdd.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class PersonMessageProducer {

    private final String brokerUrl;
    private final String queueName;

    public PersonMessageProducer(String brokerUrl, String queueName) {
        this.brokerUrl = brokerUrl;
        this.queueName = queueName;
    }

    public void publish(String messageBody) throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {

            Queue queue = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage(messageBody);

            producer.send(message);
        }
    }
}
