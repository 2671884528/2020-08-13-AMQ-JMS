package com.gyg.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @auther 郭永钢
 * @data 2020/8/12 17:27
 * @desc:
 */

public class CustomerMQ {
    private final static String MQ_URL = "tcp://101.37.116.241:61616/";
    private final static String MQ_NAME = "queueG";

    public static void main(String[] args) {
        custom();
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
            Destination queue = session.createQueue(MQ_NAME);

            //5.生成消费者
            MessageConsumer consumer = session.createConsumer(queue);
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
            consumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    //和生产者，生产的保持一致
                    if (null != message && message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        try {
                            System.out.println(textMessage.getText());
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
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
