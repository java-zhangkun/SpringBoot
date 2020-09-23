package com.yunmall.mq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
	/*
	 * //queue模式的消费者
	 * 
	 * @JmsListener(destination="${spring.activemq.queue-name}",
	 * containerFactory="queueListener") public void readActiveQueue(String message)
	 * { System.out.println("queue接受到：" + message); }
	 * 
	 * @JmsListener(destination="${spring.activemq.topic-name}",
	 * containerFactory="topicListener") public void readActiveTopic(String message)
	 * { System.out.println("topic接受到：" + message); }
	 * 
	 * @JmsListener(destination="${spring.activemq.topic-name}",
	 * containerFactory="topicListener") public void readActiveTopic1(String
	 * message) { System.out.println("topic1接受到：" + message); }
	 * 
	 * @JmsListener(destination="${spring.activemq.topic-name}",
	 * containerFactory="topicListenerFactory") public void
	 * readActiveDurableTopic(String message) { System.out.println("持久化订阅接受到：" +
	 * message); }
	 */
}