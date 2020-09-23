package com.yunmall.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
@Configuration
public class ActiveMqConfig {
	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	@Value("${spring.activemq.user}")
	private String username;

	@Value("${spring.activemq.password}")
	private String password;

	@Value("${spring.activemq.queue-name}")
	private String queueName;

	@Value("${spring.activemq.topic-name}")
	private String topicName;

	@Bean(name = "queue")
	public Queue queue() {
		return new ActiveMQQueue(queueName);
	}

	@Bean(name = "topic")
	public Topic topic() {
		return new ActiveMQTopic(topicName);
	}

	@Bean
	public ConnectionFactory connectionFactory(){
		return new ActiveMQConnectionFactory(username, password, brokerUrl);
	}

	@Bean
	public JmsMessagingTemplate jmsMessageTemplate(){
		return new JmsMessagingTemplate(connectionFactory());
	}

	// 在Queue模式中，对消息的监听需要对containerFactory进行配置
	@Bean("queueListener")
	public JmsListenerContainerFactory<?> queueJmsListenerContainerFactory(ConnectionFactory connectionFactory){
		SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPubSubDomain(false);
		return factory;
	}

	//在Topic模式中，对消息的监听需要对containerFactory进行配置
	@Bean("topicListener")
	public JmsListenerContainerFactory<?> topicJmsListenerContainerFactory(ConnectionFactory connectionFactory){
		SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPubSubDomain(true);
		return factory;
	}

	@Bean(name = "topicListenerFactory")
	public JmsListenerContainerFactory<DefaultMessageListenerContainer> topicListenerFactory(ConnectionFactory connectionFactory){
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

		factory.setSubscriptionDurable(true);// Set this to "true" to register a durable subscription,

		factory.setClientId("A");

		factory.setConnectionFactory(connectionFactory);
		return factory;

	}
	 
}
