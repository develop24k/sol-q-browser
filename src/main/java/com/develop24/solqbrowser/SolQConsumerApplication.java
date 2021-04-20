package com.develop24.solqbrowser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.text.SimpleDateFormat;
import java.util.Date;

//@SpringBootApplication
public class SolQConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SolQConsumerApplication.class, args);
    }

    @JmsListener(destination = "OneRisk/Apps/DAIS",containerFactory = "myFactory")
    public void handle(Message message) {

        Date receiveTime = new Date();

        if (message instanceof TextMessage) {
            TextMessage tm = (TextMessage) message;
            try {
                System.out.println(
                        "Message Received at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(receiveTime)
                                + " with message content of: " + tm.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(message.toString());
        }
    }
}

