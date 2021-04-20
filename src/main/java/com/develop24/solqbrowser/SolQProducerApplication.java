package com.develop24.solqbrowser;

import com.solacesystems.jms.SolJmsUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

//@SpringBootApplication
public class SolQProducerApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SolQProducerApplication.class, args);
    }

    @Autowired(required = true)
    private JmsTemplate jmsTemplate;

    @Value("DRE-DEV-00")
    private String queueName;


    @Override
    public void run(String... args) throws Exception {

        // Update the jmsTemplate's connection factory to cache the connection
        CachingConnectionFactory ccf = new CachingConnectionFactory();
        ccf.setTargetConnectionFactory(jmsTemplate.getConnectionFactory());

        jmsTemplate.setConnectionFactory(ccf);

        // By default Spring Integration uses Queues, but if you set this to true you
        // will send to a PubSub+ topic destination
        jmsTemplate.setPubSubDomain(false);

        jmsTemplate.browse(queueName, (session, queueBrowser) -> {

            QueueBrowser qb = session.createBrowser(queueBrowser.getQueue());

            Enumeration list = qb.getEnumeration();


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while(list.hasMoreElements())
            {
                Date receiveTime = new Date();

                TextMessage m = (TextMessage)list.nextElement();

                System.out.println(
                        "Message Received at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(receiveTime)
                                + " with message content of: " + m.getText());
                m.acknowledge();

            }

            qb.close();
            return null;
        });



    }
}
