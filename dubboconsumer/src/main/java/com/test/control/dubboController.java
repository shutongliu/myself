package com.test.control;

import com.alibaba.dubbo.config.annotation.Reference;
import com.test.activemq.consumer.queue.QueueReceiver1;
import com.test.activemq.consumer.queue.QueueReceiver2;
import com.test.activemq.cunsumer.topic.TopicReceiver1;
import com.test.activemq.cunsumer.topic.TopicReceiver2;
import com.test.activemq.produce.queue.QueueSender;
import com.test.activemq.producer.topic.TopicSender;
import com.test.dubbo.IDubboDemoService;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

/**
 * Created by liust on 2017/2/20.
 */
@Controller
public class dubboController {
    @Autowired
    IDubboDemoService dubboDemoService;
    @Autowired
    private TopicSender topicSender;
    @Autowired
    private QueueSender queueSender;

    @RequestMapping("/home")
    @ResponseBody
    public String home() {

        topicSender.send("topic","12");
        queueSender.send("queue","12");
        return dubboDemoService.sayHello("122");
    }
}
