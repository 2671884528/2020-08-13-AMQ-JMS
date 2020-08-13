package com.gyg.topic;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @auther 郭永钢
 * @data 2020/8/6 15:59
 * @desc:
 */

public class MyTopicPersist {

    private final static String MQ_URL = "tcp://101.37.116.241:61616/";
    private final static String MQ_NAME = "topicG";

    public static void main(String[] args) {
        pro();
    }

    /**
     * 生产者
     */
    public static void pro() {

        //1.采用默认密码root，就不需要传入，修改了则需要传入
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", MQ_URL);
        //2.获得连接
        try {
            Connection connection = factory.createConnection();

            //3.获得session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //4.创建目的地
            Topic topic = session.createTopic(MQ_NAME);
            //5.创建生产者
            MessageProducer producer = session.createProducer(topic);
            //注意：如果是持久化的那么，就会改变connection.start();的位置。
            //需要放到生产消息之前。生产的就是一个持久化消息
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            connection.start();
            //6.生产消息
            for (int i = 1; i <= 3; i++) {
                TextMessage message = session.createTextMessage("message" + i);
                producer.send(message);
            }
            producer.close();
            session.close();
            connection.close();
            System.out.println("完成");

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }


}
