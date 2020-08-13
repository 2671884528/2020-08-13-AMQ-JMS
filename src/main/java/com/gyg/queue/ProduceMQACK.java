package com.gyg.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @auther 郭永钢
 * @data 2020/8/12 17:27
 * @desc:
 */

public class ProduceMQACK {
    private final static String MQ_URL = "tcp://101.37.116.241:61616/";
    private final static String MQ_NAME = "queueG";

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
            connection.start();
            //3.获得session
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            //4.创建目的地
            Destination queue = session.createQueue(MQ_NAME);
            //5.创建生产者
            MessageProducer producer = session.createProducer(queue);
            //设置非持久化
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //设置持久化
//            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
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
