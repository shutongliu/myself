package com.test.activemq;

import com.test.activemq.consumer.queue.QueueReceiver1;
import com.test.activemq.consumer.queue.QueueReceiver2;
import com.test.activemq.cunsumer.topic.TopicReceiver1;
import com.test.activemq.cunsumer.topic.TopicReceiver2;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.AbstractMessageListenerContainer;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liust on 2017/2/22.
 */
@Configuration
public class ActiveMQConfig {
    @Value("${spring.activemq.broker-url}")
    private String brokerURL;
    @Value("{spring.activemq.user}")
    private String user;
    @Value("{spring.activemq.password}")
    private String password;
    @Bean
    public ConnectionFactory targetConnectionFactory(){
        ConnectionFactory targetConnectionFactory = new ActiveMQConnectionFactory(user,password,brokerURL);
        return targetConnectionFactory;
    }
    @Bean
    public CachingConnectionFactory connectionFactory(@Autowired ConnectionFactory targetConnectionFactory){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setSessionCacheSize(100);
        cachingConnectionFactory.setTargetConnectionFactory(targetConnectionFactory);
        return  cachingConnectionFactory;
    }
    @Bean
    public JmsTemplate jmsQueueTemplate(@Autowired CachingConnectionFactory connectionFactory){
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(false);
        return jmsTemplate;
    }
    @Bean
    public JmsTemplate jmsTopicTemplate(@Autowired CachingConnectionFactory connectionFactory){
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }
    @Autowired
    private QueueReceiver1 queueReceiver1;
    @Autowired
    private QueueReceiver2 queueReceiver2;
    @Autowired
    private TopicReceiver1 topicReceiver1;
    @Autowired
    private TopicReceiver2 topicReceiver2;
    @Bean
    public List<MessageConsumer> getAllConsumer(@Autowired CachingConnectionFactory connectionFactory){
        List<MessageConsumer> messageConsumerList = new ArrayList<MessageConsumer>();
        try {

            Connection connection =connectionFactory.createConnection();
            connection.start();
            Session session=connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE); // 创建Session
            Destination destination=session.createTopic("topic");
            MessageConsumer messageConsumer=session.createConsumer(destination); // 创建消息消费者
            messageConsumer.setMessageListener(topicReceiver1);
            messageConsumerList.add(messageConsumer);
            Connection connection1 =connectionFactory.createConnection();
            connection1.start();
            Session session1=connection1.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE); // 创建Session
            Destination destination1=session1.createTopic("topic");
            MessageConsumer messageConsumer1=session1.createConsumer(destination1); // 创建消息消费者
            messageConsumer1.setMessageListener(topicReceiver2);
            messageConsumerList.add(messageConsumer1);
            Connection connection2 =connectionFactory.createConnection();
            connection2.start();
            Session session2=connection2.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE); // 创建Session
            Destination destination2=session2.createQueue("queue");
            MessageConsumer messageConsumer2=session2.createConsumer(destination2); // 创建消息消费者
            messageConsumer2.setMessageListener(queueReceiver1);
            messageConsumerList.add(messageConsumer2);
        }catch(Exception e){

        }
        return messageConsumerList;
    }
}
