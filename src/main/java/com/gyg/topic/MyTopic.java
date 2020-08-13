package com.gyg.topic;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @auther 郭永钢
 * @data 2020/8/6 15:59
 * @desc:
 */

public class MyTopic {

    private final static String MQ_URL = "tcp://101.37.116.241:61616/";
    private final static String MQ_NAME = "topicG";

    public static void main(String[] args) {

//      pro();

        new Thread(() -> {
            custom();
        }, "AAA").start();

        new Thread(() -> {
            custom();
        }, "BBB").start();

        new Thread(() -> {
            custom();
        }, "CCC").start();
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
            connection.start();
            //3.获得session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //4.创建目的地
            Topic topic = session.createTopic(MQ_NAME);
            //5.创建生产者
            MessageProducer producer = session.createProducer(topic);
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

    /**
     * 消费者
     */
    public static void custom() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", MQ_URL);
        //2.获得连接
        try {
            Connection connection = factory.createConnection();
            connection.start();
            //3.获得session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //4.创建目的地
            Topic topic = session.createTopic(MQ_NAME);
            //5.生成消费者
            MessageConsumer consumer = session.createConsumer(topic);
            //6.开始消费
            //第一种，阻塞消费
//            while (true) {
//                //receive可以加时间，等待到时间就结束消费
//                TextMessage message = (TextMessage) consumer.receive();
//                if (message != null)
//                    System.out.println(message.getText());
//                else
//                    break;
//            }
            //第二种，监听器消费
//            consumer.setMessageListener(new MessageListener() {
//                public void onMessage(Message message) {
//                    //和生产者，生产的保持一致
//                    if (null != message && message instanceof TextMessage) {
//                        TextMessage textMessage = (TextMessage) message;
//                        try {
//                            System.out.println(textMessage.getText());
//                        } catch (JMSException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
            //运用lambda简写
            consumer.setMessageListener((message)->{
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
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
