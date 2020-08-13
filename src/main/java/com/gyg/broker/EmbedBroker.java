package com.gyg.broker;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerService;

/**
 * @auther 郭永钢
 * @data 2020/8/13 18:53
 * @desc: 内嵌的一个小型的MQ
 */

public class EmbedBroker {

    public static void main(String[] args) throws Exception {
        //AMQ支持嵌入式的Broker
        BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();
    }
}
