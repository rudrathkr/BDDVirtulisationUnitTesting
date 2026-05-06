package com.company.person.bdd.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class PersonMessageConsumer {

    private final String brokerUrl;
    private final String queueName;

    public PersonMessageConsumer(String brokerUrl, String queueName) {
        this.brokerUrl = brokerUrl;
        this.queueName = queueName;
    }

    public String consume(long timeoutMillis) throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {

            connection.start();

            Queue queue = session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(queue);

            TextMessage message = (TextMessage) consumer.receive(timeoutMillis);

            if (message == null) {
                return null;
            }

            return message.getText();
        }
    }
}
