package com.gyg.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @auther 郭永钢
 * @data 2020/8/12 15:30
 * @desc:
 */

public class MyTopicPersistCustomer {

    private final static String MQ_URL = "tcp://101.37.116.241:61616/";
    private final static String MQ_NAME = "topicG";


    public static void main(String[] args) {

//      pro();

        new Thread(() -> {
            custom("AAA");
        }, "AAA").start();

        new Thread(() -> {
            custom("BBB");
        }, "BBB").start();

        new Thread(() -> {
            custom("CCC");
        }, "CCC").start();
    }

    /**
     * 消费者
     */
    public static void custom(String s) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", MQ_URL);
        //2.获得连接
        try {
            Connection connection = factory.createConnection();
            //2.1:持久化需要加一个clientId不然会报错
            //javax.jms.JMSException: You cannot create a durable subscriber without specifying a unique clientID on a Connection
            connection.setClientID(s);
            //3.获得session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //4.创建目的地
            Topic topic = session.createTopic(MQ_NAME);
            //5.生成消费者
            //持久化加一个如下 TopicSubscriber继承MessageConsumer
            TopicSubscriber consumer = session.createDurableSubscriber(topic, "gyg");
            connection.start();

            //运用lambda简写
            consumer.setMessageListener((message) -> {
                //和生产者，生产的保持一致
                if (null != message && message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println(textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
            //监听器，需要控制台一直亮
            System.in.read();
            consumer.close();
//            session.commit();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
